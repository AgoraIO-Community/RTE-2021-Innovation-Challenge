using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using UnityEngine;

    public class YoloWrapper : IDisposable
    {
        [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
        public delegate void MyDelegate(string str);
    private const string YoloLibraryName = "yolo_cpp_dll.dll";
    private const string VOC_NAMES = @".\voc\yolov3-tiny.names";
    private const string YOLO_VOC_CFG = @".\voc\yolov3-tiny.cfg";
    private const string YOLO_VOC_WEIGHTS = @".\voc\yolov3-tiny.weights";
    private const int MaxObjects = 100;

        [DllImport(YoloLibraryName, EntryPoint = "init")]
        private static extern int InitializeYolo(string configurationFilename, string weightsFilename, string vocNaame,int gpu);

        [DllImport(YoloLibraryName, EntryPoint = "detect_image")]
        private static extern int DetectImage(string filename, ref BboxContainer container);

        [DllImport(YoloLibraryName, EntryPoint = "detect_mat")]
        private static extern int DetectImage(IntPtr pArray, int nSize,ref BboxContainer container, float thresh = 0.200000003F, bool use_mean = false);

        [DllImport(YoloLibraryName, EntryPoint = "dispose")]
        private static extern int DisposeYolo();
    [DllImport(YoloLibraryName, EntryPoint = "set_debug")]
    private static extern int SetDebug(IntPtr fp);
    [DllImport(YoloLibraryName, EntryPoint = "test")]
    private static extern int Test(string s);
    [DllImport(YoloLibraryName, EntryPoint = "detect_net")]
    private static extern bool DetectNet(string filename, string type, float thresh = 0.200000003F, bool use_mean = false);
    [DllImport(YoloLibraryName, EntryPoint = "update_tracking")]
    private static extern int UpdateVideoRawData(IntPtr data, ref BboxContainer container, ref int w,ref int h);
    [DllImport(YoloLibraryName, EntryPoint = "resize")]
    private static extern int ResizeImage(IntPtr src,int nSize , int w,  int h, IntPtr des);
    [DllImport(YoloLibraryName, EntryPoint = "set_show")]
    private static extern int SetShow(bool show);

    [StructLayout(LayoutKind.Sequential)]
        public struct bbox_t
        {
            public UInt32 x, y, w, h;    // (x,y) - top-left corner, (w, h) - width & height of bounded box
            public float prob;                 // confidence - probability that the object was found correctly
            public UInt32 obj_id;        // class of object - from range [0, classes-1]
            public UInt32 track_id;      // tracking id for video (0 - untracked, 1 - inf - tracked object)
            public UInt32 frames_counter;
            public override String ToString()
            {
                return "Bbox: obj_id: " + obj_id + ", prob: " + prob + ", track_id: " + track_id + ", x: " + x + ", y: " + y + ", w: " + w + ", h:" + h + "";
            }
        };

    [StructLayout(LayoutKind.Sequential)]
    public struct BboxContainer
    {
        [MarshalAs(UnmanagedType.ByValArray, SizeConst = MaxObjects)]
        public bbox_t[] candidates;
        public int size;
    }

        public YoloWrapper(string configurationFilename, string weightsFilename,string vocName ,int gpu)
        {
            InitializeYolo(configurationFilename, weightsFilename, vocName,gpu);
     
    }
    public YoloWrapper()
    {

           InitializeYolo(YOLO_VOC_CFG, YOLO_VOC_WEIGHTS, VOC_NAMES, 0);
        MyDelegate callback_delegate = new MyDelegate(CallBackFunction);
        // Convert callback_delegate into a function pointer that can be
        // used in unmanaged code.
        IntPtr intptr_delegate = Marshal.GetFunctionPointerForDelegate(callback_delegate);
        // Call the API passing along the function pointer.
        SetDebug(intptr_delegate);
        SetShow(false);
   //     Test("Hello World!");
    }
    static void CallBackFunction(string str)
    {
        Debug.Log("::Native DLL callback : " + str);
    }
    public void Dispose()
        {
            DisposeYolo();
        }
    public Texture2D Resize(byte[] imageData,int width,int height)
    {
        var size = Marshal.SizeOf(imageData[0]) * imageData.Length;
        var src = Marshal.AllocHGlobal(size);
        var des = Marshal.AllocHGlobal(width*height*3);
        try
        {
            // Copy the array to unmanaged memory.
            Marshal.Copy(imageData, 0, src, imageData.Length);
            var count = ResizeImage(src, imageData.Length, width,height, des);
            Debug.Log(count);
            if (count == -1)
            {
                throw new NotSupportedException($"{YoloLibraryName} has no OpenCV support");
            }
          Texture2D  nativeTexture = new Texture2D(width, height, TextureFormat.RGB24, false);
            nativeTexture.LoadRawTextureData(des, width * height * 3);
            nativeTexture.Apply();
            return nativeTexture;
        }
        catch (Exception e)
        {
            Debug.Log(e.Message);
            return null;
        }
        finally
        {
            // Free the unmanaged memory.
            Marshal.FreeHGlobal(src);
            Marshal.FreeHGlobal(des);
        }
    }
    public string getVOCName(uint id)
        {
            var lines = File.ReadAllLines(VOC_NAMES).Select(s => s.Split('\t')).ToArray();
            return lines.Length > 0 ? lines.ElementAt((int)id)[0] : "";
        }
    public void DetectOnNet(string filename, string type, float thresh = 0.200000003F, bool use_mean = false)
    {
        DetectNet( filename,  type,  thresh ,  use_mean);

    }
    public BboxContainer UpdateRawData(IntPtr data, ref int w,ref int h)
    {
        var container = new BboxContainer();
        int kk = UpdateVideoRawData( data, ref  container,ref w,ref h);
        return container;
    }
    public BboxContainer Detect(string filename)
    {
        var container = new BboxContainer();
        var count = DetectImage(filename, ref container);
        return container;
    }
    public BboxContainer Detect(byte[] imageData, float thresh = 0.400000003F, bool use_mean = false)
        {
            var container = new BboxContainer();
            var size = Marshal.SizeOf(imageData[0]) * imageData.Length;
            var pnt = Marshal.AllocHGlobal(size);
            try
            {
                // Copy the array to unmanaged memory.
                Marshal.Copy(imageData, 0, pnt, imageData.Length);
                var count = DetectImage(pnt, imageData.Length, ref container, thresh, use_mean);
         //   Debug.Log("count" + count);
            if (count == -1)
                {
                    throw new NotSupportedException($"{YoloLibraryName} has no OpenCV support");
                }
            }
            catch (Exception exception)
            {
                return new BboxContainer();
            }
            finally
            {
                // Free the unmanaged memory.
                Marshal.FreeHGlobal(pnt);
            }

            return container;
        }
    public BboxContainer Detect(IntPtr pnt,int size, float thresh = 0.200000003F, bool use_mean = false)
    {
        var container = new BboxContainer();
     //   var size = Marshal.SizeOf(imageData[0]) * imageData.Length;
     //   var pnt = Marshal.AllocHGlobal(size);
        try
        {
            // Copy the array to unmanaged memory.
       //     Marshal.Copy(imageData, 0, pnt, imageData.Length);
            var count = DetectImage(pnt, size, ref container, thresh, use_mean);
            //   Debug.Log("count" + count);
            if (count == -1)
            {
                throw new NotSupportedException($"{YoloLibraryName} has no OpenCV support");
            }
        }
        catch (Exception exception)
        {
            return new BboxContainer();
        }
        finally
        {
            // Free the unmanaged memory.
            Marshal.FreeHGlobal(pnt);
        }

        return container;
    }
    public BboxContainer Detect(byte[] imageData, Texture2D src, float thresh = 0.200000003F, bool use_mean = false)
    {
        var container = new BboxContainer();
        var size = Marshal.SizeOf(imageData[0]) * imageData.Length;
        var pnt = Marshal.AllocHGlobal(size);
        try
        {
            // Copy the array to unmanaged memory.
            Marshal.Copy(imageData, 0, pnt, imageData.Length);
            var count = DetectImage(pnt, imageData.Length, ref container, thresh, use_mean);
            Debug.Log("count" + count);
            src.LoadRawTextureData(pnt, imageData.Length);
            src.Apply();
            if (count == -1)
            {
                throw new NotSupportedException($"{YoloLibraryName} has no OpenCV support");
            }
        }
        catch (Exception exception)
        {
            return new BboxContainer();
        }
        finally
        {
            // Free the unmanaged memory.
            Marshal.FreeHGlobal(pnt);
        }

        return container;
    }
    public BboxContainer Detect(IntPtr pnt,int length)
    {
        var container = new BboxContainer();
        try
        {
            var count = DetectImage(pnt, length, ref container);
            Debug.Log("count" + count);
            if (count == -1)
            {
                throw new NotSupportedException($"{YoloLibraryName} has no OpenCV support");
            }
        }
        catch (Exception exception)
        {
            Debug.Log("exception" + exception.Message);
            return new BboxContainer();
        }
        finally
        {
            // Free the unmanaged memory.
            Marshal.FreeHGlobal(pnt);
        }

        return container;
    }


}
