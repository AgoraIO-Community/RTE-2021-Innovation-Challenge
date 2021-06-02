package com.ml.doctor.network;

import com.google.gson.reflect.TypeToken;
import com.ml.doctor.bean.LoginBean;
import com.ml.doctor.bean.PatientDetailsBean;
import com.ml.doctor.bean.PatientListBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkApi {

//    public static final String BasicUrl = "http://192.168.200.103:8080/ZZB/";
    public static final String BasicUrl = "http://118.31.238.207:8080/ZZB/";
//    public static final String BasicUrl="http://192.168.200.113:8080/ZZB/";//文博本地
    /**
     * 用户登录
     */
    public static final String Login = BasicUrl + "login/docter_login";
    /**
     * 患者列表
     */
    public static final String PatientList=BasicUrl+"docter/docters_usersss";
    /**
     * 患者详情页
     */
    public static final String PatientDetails=BasicUrl+"br/docter_oneuser";
    /**
     * 改变医生的在线状态
     */
    public static final String ChangeDocterOnlineStatus=BasicUrl+"docter/update_online_status";
    /**
     * 用户登录
     */
    public static void login(String username,String password,NetworkManager.SuccessCallback<LoginBean> listener,NetworkManager.FailedCallback failedCallback){
        Map<String,String> map=new HashMap<>();
        map.put("username",username);
        map.put("password",password);
        NetworkManager.getInstance().postResultClass(Login,map, LoginBean.class,listener,failedCallback);
    }

    /**
     * 患者列表
     * @param docterid
     * @param bname
     * @param start
     * @param limit
     * @param listener
     * @param failedCallback
     */
    public static void patientList(int docterid, String bname, int start, int limit, NetworkManager.SuccessCallback<List<PatientListBean>> listener, NetworkManager.FailedCallback failedCallback){

        Map<String,String> map=new HashMap<>();
        map.put("docterid",docterid+"");
        if(null!=bname){
            map.put("bname",bname);
        }
        map.put("start",start+"");
        map.put("limit",limit+"");
        NetworkManager.getInstance().getResultClass(PatientList,map,new TypeToken<List<PatientListBean>>() {
        }.getType(),listener,failedCallback);
    }

    /**
     * 患者详情页
     * @param start
     * @param limit
     * @param listener
     * @param failedCallback
     */
    public static void patientDetails(String bid, int start, int limit, NetworkManager.SuccessCallback<List<PatientDetailsBean>> listener, NetworkManager.FailedCallback failedCallback){

        Map<String,String> map=new HashMap<>();
        map.put("bid",bid);
        map.put("start",start+"");
        map.put("limit",limit+"");
        NetworkManager.getInstance().getResultClass(PatientDetails,map,new TypeToken<List<PatientDetailsBean>>() {
        }.getType(),listener,failedCallback);
    }

    /**
     * 更改医生的在线状态
     * @param docterid
     * @param online_status
     * @param listener
     * @param failedCallback
     */
    public static void changeDoctorStatus(String docterid,String online_status, NetworkManager.SuccessCallback<Object> listener, NetworkManager.FailedCallback failedCallback){

        Map<String,String> map=new HashMap<>();
        map.put("docterid",docterid);
        map.put("online_status",online_status);
        NetworkManager.getInstance().getResultClass(ChangeDocterOnlineStatus,map,Object.class,listener,failedCallback);
    }
//    public static void loginWithOpenId(String openId, String type, String nickName, String imageUrl,
//                                       NetworkManager.SuccessCallback<LoginInfoBean> listener, NetworkManager.FailedCallback failedCallback){
//        Map<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("nick", nickName);
//        paramsMap.put("portrait", imageUrl);
//        paramsMap.put("sign", Utils.md5HexDigest("#2017" + openId + "dD78hy2!" + nickName + "uK23lp"));
//        paramsMap.put("type", type);
//        paramsMap.put("userName", openId);
//        NetworkManager.getInstance().postResultClass(LoginWithOpenIdUrl, paramsMap, LoginInfoBean.class, listener, failedCallback);
//    }
//
//    public static void getHomePage(NetworkManager.SuccessCallback<HomePageBean> listener, NetworkManager.FailedCallback failedCallback){
//        NetworkManager.getInstance().getResultClass(HomePageUrl, HomePageBean.class, listener);
//    }
//
//    public static void getUserInfo(NetworkManager.SuccessCallback<UserPageBean> listener){
//        NetworkManager.getInstance().getResultClass(UserInfoUrl, UserPageBean.class, listener);
//    }
//
//    public static void favoriteOperation(long imageId, boolean isLike){
//        Map<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("targetId", String.valueOf(imageId));
//        NetworkManager.getInstance().postResultString(isLike ? FavoriteSaveUrl : FavoriteDeleteUrl, paramsMap, null);
//    }
//
//    public static void getFavoriteList(NetworkManager.SuccessCallback<FavoriteListBean> listener){
//        Map<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("pageSize", "1000");
//        NetworkManager.getInstance().getResultClass(GetFavoriteListUrl, paramsMap, FavoriteListBean.class, listener);
//    }
//
//    public static void getTraceList(NetworkManager.SuccessCallback<TraceListBean> listener){
//        Map<String, String> paramsMap = new HashMap<>();
//        paramsMap.put("pageSize", "1000");
//        NetworkManager.getInstance().getResultClass(GetTraceListUrl, paramsMap, TraceListBean.class, listener);
//    }
}
