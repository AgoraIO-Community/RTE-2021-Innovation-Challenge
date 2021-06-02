package io.agora.openlive.utils;

import android.app.ActivityManager;
import android.content.Context;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

/**
 * json数据解析类
 *
 * @author JoeAllen_Li
 */
public class JsonParsingUtil {
    private static JsonParsingUtil jsonParsingUtil;

    private static JsonParsingUtil getInstance() {
        if (jsonParsingUtil == null) {
            synchronized (JsonParsingUtil.class) {
                if (jsonParsingUtil == null) {
                    jsonParsingUtil = new JsonParsingUtil();
                }
            }
        }
        return jsonParsingUtil;
    }

    public static JsonParsingUtil x() {
        return getInstance();
    }


    /**
     * 判断服务是否开启
     *
     * @param context     context
     * @param serviceName 服务的完整路径(例:com.example.service)
     * @return boolean
     */
//    public boolean isServiceRunning(Context context, String serviceName) {
//        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(100);
//        for (int i = 0; i < runningService.size(); i++) {
//            if (runningService.get(i).service.getClassName().equals(serviceName)) {
//                return true;
//            }
//        }
//        return false;
//    }




    /**
     * 判断是否是json数据
     *
     * @param jsonStr 目标数据
     * @return boolean
     */
    public boolean isJson(String jsonStr) {
        try {
            JSONObject.parseObject(jsonStr);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 判断是否是JsonArray数据
     *
     * @param jsonStr 目标数据
     * @return boolean
     */
//    public boolean isJsonArray(String jsonStr) {
//        try {
//            JSONArray.parseArray(jsonStr);
//            return false;
//        } catch (Exception e) {
//            return true;
//        }
//    }


//    /**
//     * 查询JSONObject中是否存在keyList
//     *
//     * @param jsonObject 目标
//     * @param keyList    Key集合
//     * @return 有一个Key不存在则返回false
//     */
//    public boolean containsKeyList(JSONObject jsonObject, List<String> keyList) {
//        for (String key : keyList) {
//            if (!jsonObject.containsKey(key)) {
//                return false;
//            }
//        }
//        return true;
//    }

//    /**
//     * 查询JSONObject中是否存在keyList
//     *
//     * @param jsonObject 目标
//     * @param keyList    Key集合
//     * @return 有一个Key不存在则返回false
//     */
//    public boolean containsKeyList(JSONObject jsonObject, String... keyList) {
//        for (String key : keyList) {
//            if (!jsonObject.containsKey(key)) {
//                return true;
//            }
//        }
//        return false;
//    }

//    public boolean containsKeyList(String jsonStr, List<String> keyList) {
//        try {
//            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
//            for (String key : keyList) {
//                if (!jsonObject.containsKey(key)) {
//                    return false;
//                }
//            }
//        } catch (Exception e) {
//            return false;
//        }
//        return true;
//    }

    public boolean containsKeyList(String jsonStr, String... keyList) {
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            for (String key : keyList) {
                if (!jsonObject.containsKey(key)) {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 最强json解析方法{[(没有之一)]}
     *
     * @param jsonStr 解析对象
     * @param keyList    键值集合不限制个数
     * @return 获取指定需要的数据
     */
    public Map<String, Object> getJsonObject(String jsonStr, String... keyList) {
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        Map<String, Object> map = new HashMap<>();
        for (String key : keyList) {
            map.put(key, jsonObject.get(key));
        }
        return map;
    }


//    public List<String> removeListDuplicate1(List<String> list) {
//        LinkedHashSet<String> set = new LinkedHashSet<>(list.size());
//        set.addAll(list);
//        list.clear();
//        list.addAll(set);
//        return list;
//    }
}