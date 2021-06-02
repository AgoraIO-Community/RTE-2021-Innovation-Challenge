/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package io.agora.rtc.ss.aidl;
// Declare any non-default types here with import statements

public interface IScreenSharing extends android.os.IInterface
{
  /** Default implementation for IScreenSharing. */
  public static class Default implements io.agora.rtc.ss.aidl.IScreenSharing
  {
    @Override public void registerCallback(io.agora.rtc.ss.aidl.INotification callback) throws android.os.RemoteException
    {
    }
    @Override public void unregisterCallback(io.agora.rtc.ss.aidl.INotification callback) throws android.os.RemoteException
    {
    }
    @Override public void startShare() throws android.os.RemoteException
    {
    }
    @Override public void stopShare() throws android.os.RemoteException
    {
    }
    @Override public void renewToken(java.lang.String token) throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements io.agora.rtc.ss.aidl.IScreenSharing
  {
    private static final java.lang.String DESCRIPTOR = "io.agora.rtc.ss.aidl.IScreenSharing";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an io.agora.rtc.ss.aidl.IScreenSharing interface,
     * generating a proxy if needed.
     */
    public static io.agora.rtc.ss.aidl.IScreenSharing asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof io.agora.rtc.ss.aidl.IScreenSharing))) {
        return ((io.agora.rtc.ss.aidl.IScreenSharing)iin);
      }
      return new io.agora.rtc.ss.aidl.IScreenSharing.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_registerCallback:
        {
          data.enforceInterface(descriptor);
          io.agora.rtc.ss.aidl.INotification _arg0;
          _arg0 = io.agora.rtc.ss.aidl.INotification.Stub.asInterface(data.readStrongBinder());
          this.registerCallback(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_unregisterCallback:
        {
          data.enforceInterface(descriptor);
          io.agora.rtc.ss.aidl.INotification _arg0;
          _arg0 = io.agora.rtc.ss.aidl.INotification.Stub.asInterface(data.readStrongBinder());
          this.unregisterCallback(_arg0);
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_startShare:
        {
          data.enforceInterface(descriptor);
          this.startShare();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_stopShare:
        {
          data.enforceInterface(descriptor);
          this.stopShare();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_renewToken:
        {
          data.enforceInterface(descriptor);
          java.lang.String _arg0;
          _arg0 = data.readString();
          this.renewToken(_arg0);
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements io.agora.rtc.ss.aidl.IScreenSharing
    {
      private android.os.IBinder mRemote;
      Proxy(android.os.IBinder remote)
      {
        mRemote = remote;
      }
      @Override public android.os.IBinder asBinder()
      {
        return mRemote;
      }
      public java.lang.String getInterfaceDescriptor()
      {
        return DESCRIPTOR;
      }
      @Override public void registerCallback(io.agora.rtc.ss.aidl.INotification callback) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().registerCallback(callback);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void unregisterCallback(io.agora.rtc.ss.aidl.INotification callback) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
          boolean _status = mRemote.transact(Stub.TRANSACTION_unregisterCallback, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().unregisterCallback(callback);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void startShare() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_startShare, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().startShare();
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void stopShare() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_stopShare, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().stopShare();
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void renewToken(java.lang.String token) throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          _data.writeString(token);
          boolean _status = mRemote.transact(Stub.TRANSACTION_renewToken, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().renewToken(token);
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static io.agora.rtc.ss.aidl.IScreenSharing sDefaultImpl;
    }
    static final int TRANSACTION_registerCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_unregisterCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_startShare = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    static final int TRANSACTION_stopShare = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
    static final int TRANSACTION_renewToken = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
    public static boolean setDefaultImpl(io.agora.rtc.ss.aidl.IScreenSharing impl) {
      if (Stub.Proxy.sDefaultImpl == null && impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static io.agora.rtc.ss.aidl.IScreenSharing getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  public void registerCallback(io.agora.rtc.ss.aidl.INotification callback) throws android.os.RemoteException;
  public void unregisterCallback(io.agora.rtc.ss.aidl.INotification callback) throws android.os.RemoteException;
  public void startShare() throws android.os.RemoteException;
  public void stopShare() throws android.os.RemoteException;
  public void renewToken(java.lang.String token) throws android.os.RemoteException;
}
