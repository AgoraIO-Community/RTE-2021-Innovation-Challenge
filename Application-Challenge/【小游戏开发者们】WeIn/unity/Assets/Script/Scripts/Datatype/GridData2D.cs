using System;
using System.Runtime.InteropServices;

public class GridData2D<DType> : IDisposable
{
    public DType[] Data;
    public int Width { get; private set; }
    public int Height { get; private set; }

    private GCHandle pinnedHandle;
    public bool Disposed { get; private set; }
    public GridData2D(int w, int h)
    {
        Data = new DType[w * h];
        Width = w;
        Height = h;

        pinnedHandle = GCHandle.Alloc(Data);
    }
    public GridData2D(DType[] d, int w, int h)
    {
        Data = d;
        Width = w;
        Height = h;

        pinnedHandle = GCHandle.Alloc(Data);
    }
    public int GetIndex(int x, int y)
    {
        return y * Width + x;
    }
    public void SetDataAt(int x, int y, DType t)
    {
        Data[GetIndex(x, y)] = t;
    }
    public DType GetDataAt(int x, int y)
    {
        return Data[GetIndex(x, y)];
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