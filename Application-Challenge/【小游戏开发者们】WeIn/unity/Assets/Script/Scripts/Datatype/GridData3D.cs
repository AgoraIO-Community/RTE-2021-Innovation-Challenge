using System;
using System.Runtime.InteropServices;

/// <summary>
/// A generic data type with a 3D map where 
/// x:width y:height z:length are the dimentions
/// with a disposable utility
/// </summary>
/// <typeparam name="DType">Generic type</typeparam>
public class GridData3D<DType> : IDisposable
{
    public DType[] Data;
    /// <summary>
    /// X dimention size
    /// </summary>
    public int Width { get; private set; }
    /// <summary>
    /// Y dimention size
    /// </summary>
    public int Height { get; private set; }
    /// <summary>
    /// Z dimention size
    /// </summary>
    public int Length { get; private set; }

    private GCHandle pinnedHandle;
    public bool Disposed { get; private set; }
    public GridData3D(int w, int h, int l)
    {
        Data = new DType[w * h * l];
        Width = w;
        Height = h;
        Length = l;

        pinnedHandle = GCHandle.Alloc(Data);
    }
    public GridData3D(DType[] d, int w, int h, int l)
    {
        Data = d;
        Width = w;
        Height = h;
        Length = l;
        pinnedHandle = GCHandle.Alloc(Data);
    }
    public int GetIndex(int x, int y, int z)
    {
        return z * Width * Height + y * Width + x;
    }
    public void SetDataAt(int x, int y, int z, DType t)
    {
        Data[GetIndex(x, y, z)] = t;
    }
    public DType GetDataAt(int x, int y, int z)
    {
        return Data[GetIndex(x, y, z)];
    }

    public void Dispose()
    {
        Dispose(true);
    }
    protected virtual void Dispose(bool disposing)
    {
        if (disposing)
        {
            pinnedHandle.Free();
            GC.SuppressFinalize(this);
        }
        Disposed = true;
    }
}
