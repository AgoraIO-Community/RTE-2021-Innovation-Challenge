package top.joeallen.closedbook.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

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
     * 判断是否是json数据
     *
     * @param jsonStr 目标数据
     * @return boolean
     */
    public boolean isJson(String jsonStr) {
        try {
            JSONObject.parseObject(jsonStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断是否是JsonArray数据
     *
     * @param jsonStr 目标数据
     * @return boolean
     */
    public boolean isJsonArray(String jsonStr) {
        try {
            JSONArray.parseArray(jsonStr);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 判断是否是json数据
     *
     * @param jsonStr
     * @return
     */
    public JSONObject string2Json(String jsonStr) {
        try {
            return JSONObject.parseObject(jsonStr);
        } catch (Exception e) {
            return new JSONObject();
        }
    }


    /**
     * 查询JSONObject中是否存在keyList
     *
     * @param jsonObject 目标
     * @param keyList    Key集合
     * @return 有一个Key不存在则返回false
     */
    public boolean containsKeyList(JSONObject jsonObject, List<String> keyList) {
        for (String key : keyList) {
            if (!jsonObject.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 查询JSONObject中是否存在keyList
     *
     * @param jsonObject 目标
     * @param keyList    Key集合
     * @return 有一个Key不存在则返回false
     */
    public boolean containsKeyList(JSONObject jsonObject, String... keyList) {
        for (String key : keyList) {
            if (!jsonObject.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsKeyList(String jsonStr, List<String> keyList) {
        return contains(jsonStr, strListToArray(keyList));
    }

    public boolean containsKeyList(String jsonStr, String... keyList) {
        return contains(jsonStr, keyList);
    }

    private boolean contains(String jsonStr, String[] keyList) {
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
     * @param jsonObject 解析对象
     * @param keyList    键值集合不限制个数
     * @return 获取指定需要的数据
     */
    public Map<String, Object> getJsonObject(JSONObject jsonObject, String... keyList) {
        Map<String, Object> map = new HashMap<>();
        for (String key : keyList) {
            map.put(key, jsonObject.get(key));
        }
        return map;
    }


    public List<String> removeListDuplicate1(List<String> list) {
        LinkedHashSet<String> set = new LinkedHashSet<>(list.size());
        set.addAll(list);
        list.clear();
        list.addAll(set);
        return list;
    }

    /**
     * 集合转数组
     *
     * @param objectList 集合
     * @return 数组
     */
    public Object[] listToArray(List<Object> objectList) {
        return objectList.toArray();
    }

    /**
     * 集合转数组
     *
     * @param objectList 集合
     * @return 数组
     */
    public String[] strListToArray(List<String> objectList) {
        String[] strings = new String[objectList.size()];
        for (int i = 0; i < objectList.size(); i++) {
            strings[i] = objectList.get(i);
        }
        return strings;
    }

    /**
     * 数组转集合
     *
     * @param objects 数组
     * @return 集合
     */
    public List<Object> arrayToList(Object[] objects) {
        return Arrays.asList(objects);
    }
}