using System;
using System.Collections;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using UnityEngine;

// Define the functions which can be called from the .dll.
public  class OpenCVInterop
{
    [UnmanagedFunctionPointer(CallingConvention.Cdecl)]
    public delegate void MyDelegate(string str);

    private const string VOC_NAMES = @"data\obj.names";
    private const string YOLO_CPP_DLL = @"yolo_cpp_dll.dll";
    private const string YOLO_VOC_CFG = @"data\yolo-obj.cfg";
    private const string YOLO_VOC_WEIGHTS = @"data\yolo-obj_900.weights";

    [DllImport(YOLO_CPP_DLL, ExactSpelling = true, CallingConvention = CallingConvention.Cdecl)]
    internal static extern void Test(string s);


    [DllImport(YOLO_CPP_DLL, ExactSpelling = true, CallingConvention = CallingConvention.Cdecl)]
    internal static extern void SetDebugFunction(IntPtr fp);
   /* [DllImport(YOLO_CPP_DLL, ExactSpelling = true, CallingConvention = CallingConvention.Cdecl)]
    internal static extern void Init(string cfg, string weights, string names, string file);

    [DllImport(YOLO_CPP_DLL, ExactSpelling = true, CallingConvention = CallingConvention.Cdecl)]
    internal static extern bool Detect([In, Out] byte[] data, string type, float thresh, bool useMean);

    [DllImport(YOLO_CPP_DLL, ExactSpelling = true, CallingConvention = CallingConvention.Cdecl)]
    internal static unsafe extern bool DetectImage(string filename, out bbox_t* itemsFound, out int count,float thresh, bool useMean);

    [DllImport(YOLO_CPP_DLL, ExactSpelling = true, CallingConvention = CallingConvention.Cdecl)]
    internal static extern bool TestArray([In, Out] int[] array);*/

    public   OpenCVInterop()
    {
        try
        {
            MyDelegate callback_delegate = new MyDelegate(CallBackFunction);
        // Convert callback_delegate into a function pointer that can be
        // used in unmanaged code.
        IntPtr intptr_delegate = Marshal.GetFunctionPointerForDelegate(callback_delegate);
        // Call the API passing along the function pointer.
        SetDebugFunction(intptr_delegate);
          Test("Hello World!");
      //    Init(YOLO_VOC_CFG, YOLO_VOC_WEIGHTS, VOC_NAMES, ""); // ("yolo-voc.cfg", "yolo-voc.weights", "data/voc.names", "");

        }
        catch (Exception exception)
        {

            Console.WriteLine(exception.ToString());
            throw exception;
        }
    }
  
    static void CallBackFunction(string str)
    {
        Debug.Log("::Native DLL callback : " + str);
    }


  /*  public unsafe   bbox_t[] detectQimage(string filename)
    {
        bbox_t* itemsFound;
        int itemsCount=0;
        DetectImage(filename, out itemsFound,out itemsCount,0.8f, false);
        Debug.Log("itemsCount"+ itemsCount);
        bbox_t[] resArray = new bbox_t[itemsCount];
        for (int i = 0; i < resArray.Length; ++i)
        {
            Debug.Log(itemsFound[i].ToString());
            resArray[i] = itemsFound[i];
        }
        
        return resArray;
    }*/



}

public struct bbox_t
{
    uint x, y, w, h;    // (x,y) - top-left corner, (w, h) - width & height of bounded box
    float prob;                 // confidence - probability that the object was found correctly
    uint obj_id;        // class of object - from range [0, classes-1]
    uint track_id;      // tracking id for video (0 - untracked, 1 - inf - tracked object)

    public uint X
    {
        get
        {
            return x;
        }

        set
        {
            x = value;
        }
    }

    public uint Y
    {
        get
        {
            return y;
        }

        set
        {
            y = value;
        }
    }

    public uint W
    {
        get
        {
            return w;
        }

        set
        {
            w = value;
        }
    }

    public uint H
    {
        get
        {
            return h;
        }

        set
        {
            h = value;
        }
    }

    public float Prob
    {
        get
        {
            return prob;
        }

        set
        {
            prob = value;
        }
    }

    public uint Obj_id
    {
        get
        {
            return obj_id;
        }

        set
        {
            obj_id = value;
        }
    }

    public uint Track_id
    {
        get
        {
            return track_id;
        }

        set
        {
            track_id = value;
        }
    }

    public override String ToString()
    {
        return "Bbox: obj_id: " + Obj_id + ", prob: " + Prob + ", track_id: " + Track_id + ", x: " + X + ", y: " + Y + ", w: " + W + ", h:" + H + "";
    }
};

