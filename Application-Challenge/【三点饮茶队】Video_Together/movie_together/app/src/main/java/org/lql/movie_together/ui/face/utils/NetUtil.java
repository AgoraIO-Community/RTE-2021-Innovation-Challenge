package org.lql.movie_together.ui.face.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
//    /* 网络状态 */
//    public static boolean isNet = true;
//
//    public static enum netType {
//
//        wifi, CMNET, CMWAP, noneNet
//
//    }

    /**
     * @param context
     * @return
     * @方法说明:判断WIFI网络是否可用
     * @方法名称:isWifiConnected
     * @返回值:boolean
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * @param context
     * @return
     * @方法说明:判断MOBILE网络是否可用
     * @方法名称:isMobileConnected
     * @返回值:boolean
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * @param context
     * @return
     * @方法说明:获取当前网络连接的类型信息
     * @方法名称:getConnectedType
     * @返回值:int
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

//    /**
//     * @param context
//     * @return
//     * @方法说明:获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap 网络3：net网络
//     * @方法名称:getAPNType
//     * @返回值:netType
//     */
//    public static netType getAPNType(Context context) {
//        ConnectivityManager connMgr = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        if (networkInfo == null) {
//            return netType.noneNet;
//        }
//        int nType = networkInfo.getType();
//
//        if (nType == ConnectivityManager.TYPE_MOBILE) {
//            if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
//                return netType.CMNET;
//            } else {
//                return netType.CMWAP;
//            }
//        } else if (nType == ConnectivityManager.TYPE_WIFI) {
//            return netType.wifi;
//        }
//        return netType.noneNet;
//
//    }

    /**
     * @param context
     * @return
     * @方法说明:判断是否有网络连接
     * @方法名称:isNetworkConnected
     * @返回值:boolean
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * @param context
     * @return
     * @方法说明:网络是否可用
     * @方法名称:isNetworkAvailable
     * @返回值:boolean
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param context
     * @return
     * @方法说明:判断是否是手机网络
     * @方法名称:is3GNet
     * @返回值:boolean
     */
    public static boolean is3GNet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }
}