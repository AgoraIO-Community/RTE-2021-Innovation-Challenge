package org.lql.movie_together.ui.face.utils;

import android.os.Environment;
import android.util.Log;


import org.json.JSONObject;
import org.lql.movie_together.ui.face.model.SingleBaseConfig;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * author : shangrong
 * date : 2019/5/23 11:46 AM
 * description :对配置文件进行读取和修改
 */
public class AttributeConfigUtils {

    public static final String folder = Environment.getExternalStorageDirectory() + File.separator + "Settings";

    // 配置文件路径
    public static final String filePath = folder + "/" + "attributeFaceConfig.txt";


    public static boolean isConfigExit() {
        File file1 = new File(folder);
        if (!file1.exists()) {
            file1.mkdirs();
        }
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        } else {
            try {
                file.createNewFile();
                modityJson();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }


    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 读取配置文件内容
     *
     * @return
     */
    public static Boolean initConfig() {
        String configMessage = FileUtils.txt2String(filePath);
        if (configMessage.equals("")) {
            Log.e("facesdk", "文件不存在");
            return false;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(configMessage);
            if (!identify(jsonObject)) {
                return false;
            }
//            SingleBaseConfig.getBaseConfig().setdPass(jsonObject.getString("dPass"));
            SingleBaseConfig.getBaseConfig().setDisplay(jsonObject.getBoolean("display"));
            SingleBaseConfig.getBaseConfig().setNirOrDepth(jsonObject.getBoolean("isNirOrDepth"));
            SingleBaseConfig.getBaseConfig().setDebug(jsonObject.getBoolean("debug"));
            SingleBaseConfig.getBaseConfig().setVideoDirection(jsonObject.getInt("videoDirection"));
            SingleBaseConfig.getBaseConfig().setDetectFrame(jsonObject.getString("detectFrame"));
//            SingleBaseConfig.getBaseConfig().setRadius(jsonObject.getInt("radius"));
            SingleBaseConfig.getBaseConfig().setDetectDirection(jsonObject.getInt("detectDirection"));
            SingleBaseConfig.getBaseConfig().setTrackType(jsonObject.getString("trackType"));
            SingleBaseConfig.getBaseConfig().setMinimumFace(jsonObject.getInt("minimumFace"));
            SingleBaseConfig.getBaseConfig().setBlur(Float.valueOf(jsonObject.get("blur") + ""));
            SingleBaseConfig.getBaseConfig().setIllumination(jsonObject.getInt("illumination"));
            SingleBaseConfig.getBaseConfig().setGesture(Float.valueOf(jsonObject.get("gesture") + ""));
            SingleBaseConfig.getBaseConfig().setPitch(Float.valueOf(jsonObject.get("pitch") + ""));
            SingleBaseConfig.getBaseConfig().setRoll(Float.valueOf(jsonObject.get("roll") + ""));
            SingleBaseConfig.getBaseConfig().setYaw(Float.valueOf(jsonObject.get("yaw") + ""));
            SingleBaseConfig.getBaseConfig().setOcclusion(Float.valueOf(jsonObject.get("occlusion") + ""));
            SingleBaseConfig.getBaseConfig().setLeftEye(Float.valueOf(jsonObject.get("leftEye") + ""));
            SingleBaseConfig.getBaseConfig().setRightEye(Float.valueOf(jsonObject.get("rightEye") + ""));
            SingleBaseConfig.getBaseConfig().setNose(Float.valueOf(jsonObject.get("nose") + ""));
            SingleBaseConfig.getBaseConfig().setMouth(Float.valueOf(jsonObject.get("mouth") + ""));
            SingleBaseConfig.getBaseConfig().setLeftCheek(Float.valueOf(jsonObject.get("leftCheek") + ""));
            SingleBaseConfig.getBaseConfig().setRightCheek(Float.valueOf(jsonObject.get("rightCheek") + ""));
            SingleBaseConfig.getBaseConfig().setChinContour(Float.valueOf(jsonObject.get("chinContour") + ""));
            SingleBaseConfig.getBaseConfig().setCompleteness(Float.valueOf(jsonObject.get("completeness") + ""));
//            SingleBaseConfig.getBaseConfig().setThreshold(jsonObject.getInt("threshold"));
            SingleBaseConfig.getBaseConfig().setLiveThreshold(jsonObject.getInt("liveThreshold"));
            SingleBaseConfig.getBaseConfig().setIdThreshold(jsonObject.getInt("IdThreshold"));
            SingleBaseConfig.getBaseConfig().setRgbAndNirThreshold(jsonObject.getInt("rgbAndNirThreshold"));
            SingleBaseConfig.getBaseConfig().setCamera_lightThreshold(jsonObject.getInt("cameraLightThreshold"));
            SingleBaseConfig.getBaseConfig().setActiveModel(jsonObject.getInt("activeModel"));
            SingleBaseConfig.getBaseConfig().setTimeLapse(jsonObject.getInt("timeLapse"));
            SingleBaseConfig.getBaseConfig().setType(jsonObject.getInt("type"));
            SingleBaseConfig.getBaseConfig().setQualityControl(jsonObject.getBoolean("qualityControl"));
            SingleBaseConfig.getBaseConfig().setLivingControl(jsonObject.getBoolean("livingControl"));
            SingleBaseConfig.getBaseConfig().setRgbLiveScore(Float.valueOf(jsonObject.get("rgbLiveScore") + ""));
            SingleBaseConfig.getBaseConfig().setNirLiveScore(Float.valueOf(jsonObject.get("nirLiveScore") + ""));
            SingleBaseConfig.getBaseConfig().setDepthLiveScore(Float.valueOf(jsonObject.get("depthLiveScore") + ""));
            SingleBaseConfig.getBaseConfig().setFramesThreshold(jsonObject.getInt("framesThreshold"));
            SingleBaseConfig.getBaseConfig().setCameraType(jsonObject.getInt("cameraType"));
            SingleBaseConfig.getBaseConfig().setMirrorRGB(jsonObject.getInt("mirrorRGB"));
            SingleBaseConfig.getBaseConfig().setMirrorNIR(jsonObject.getInt("mirrorNIR"));
            SingleBaseConfig.getBaseConfig().setRgbRevert(jsonObject.getBoolean("RGBRevert"));
            SingleBaseConfig.getBaseConfig().setAttribute(jsonObject.getBoolean("attribute"));
            SingleBaseConfig.getBaseConfig().setRgbAndNirWidth(jsonObject.getInt("rgbAndNirWidth"));
            SingleBaseConfig.getBaseConfig().setRgbAndNirHeight(jsonObject.getInt("rgbAndNirHeight"));
            SingleBaseConfig.getBaseConfig().setDepthWidth(jsonObject.getInt("depthWidth"));
            SingleBaseConfig.getBaseConfig().setDepthHeight(jsonObject.getInt("depthHeight"));
            SingleBaseConfig.getBaseConfig().setUsingBestImage(jsonObject.getBoolean("usingBestImage"));
            SingleBaseConfig.getBaseConfig().setBestImageScore(jsonObject.getInt("bestImageScore"));

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("facesdk", "文件内容异常,请检测是否规范");
            return false;
        }
    }

    // 校验sdcard里的txt文件内容是否正常
    public static boolean identify(JSONObject jsonObject) {
        try {
            Boolean display = (Boolean) jsonObject.get("display");
            Boolean isNirOrDepth = (Boolean) jsonObject.get("isNirOrDepth");

            Boolean debug = (Boolean) jsonObject.get("debug");
            int videoDirection = Integer.parseInt(jsonObject.getString("videoDirection"));
            if (!(videoDirection == 0 || videoDirection == 90 || videoDirection == 180 || videoDirection == 270)) {
                return false;
            }
            String detectFrame = (String) jsonObject.get("detectFrame");
            if (!(detectFrame.equals("wireframe") || detectFrame.equals("fixedarea"))) {
                return false;
            }

//            int radius = (int) jsonObject.get("radius");

            int detectDirection = (int) jsonObject.get("detectDirection");
            if (!(detectDirection == 0 || detectDirection == 90 || detectDirection == 180
                    || detectDirection == 270)) {
                return false;
            }

            String trackType = (String) jsonObject.get("trackType");
            if (!(trackType.equals("max") || trackType.equals("first") || trackType.equals("none"))) {
                return false;
            }

            int minimumFace = (int) jsonObject.get("minimumFace");
            if (minimumFace < 30) {
                return false;
            }

            float blur = Float.valueOf(jsonObject.get("blur") + "");
            if (blur > 1 || blur < 0) {
                return false;
            }

            int illumination = (int) jsonObject.get("illumination");
            if (illumination < 0 || illumination > 255) {
                return false;
            }

            float gesture = Float.valueOf(jsonObject.get("gesture") + "");

            float pitch = Float.valueOf(jsonObject.get("pitch") + "");
            if (pitch < -90 || pitch > 90) {
                return false;
            }

            float roll = Float.valueOf(jsonObject.get("roll") + "");
            if (roll < -90 || roll > 90) {
                return false;
            }

            float yaw = Float.valueOf(jsonObject.get("yaw") + "");
            if (yaw < -90 || yaw > 90) {
                return false;
            }

            float occlusion = Float.valueOf(jsonObject.get("occlusion") + "");
            if (occlusion < 0 || occlusion > 1) {
                return false;
            }


            float leftEye = Float.valueOf(jsonObject.get("leftEye") + "");
            if (leftEye < 0 || leftEye > 1) {
                return false;
            }

            float rightEye = Float.valueOf(jsonObject.get("rightEye") + "");
            if (rightEye < 0 || rightEye > 1) {
                return false;
            }

            float nose = Float.valueOf(jsonObject.get("nose") + "");
            if (nose < 0 || nose > 1) {
                return false;
            }

            float mouth = Float.valueOf(jsonObject.get("mouth") + "");
            if (mouth < 0 || mouth > 1) {
                return false;
            }

            float leftCheek = Float.valueOf(jsonObject.get("leftCheek") + "");
            if (leftCheek < 0 || leftCheek > 1) {
                return false;
            }

            float rightCheek = Float.valueOf(jsonObject.get("rightCheek") + "");
            if (rightCheek < 0 || rightCheek > 1) {
                return false;
            }

            float chinContour = Float.valueOf(jsonObject.get("chinContour") + "");
            if (chinContour < 0 || chinContour > 1) {
                return false;
            }

            float completeness = Float.valueOf(jsonObject.get("completeness") + "");
            if (completeness < 0 || completeness > 1) {
                return false;
            }
            int rgbAndNirThreshold = Integer.valueOf(jsonObject.get("rgbAndNirThreshold") + "");
            if (rgbAndNirThreshold < 0 || rgbAndNirThreshold > 100) {
                return false;
            }

            int cameraLightThreshold = Integer.valueOf(jsonObject.get("cameraLightThreshold") + "");
            if (cameraLightThreshold < 0 || cameraLightThreshold > 100) {
                return false;
            }

            int liveThreshold = Integer.valueOf(jsonObject.get("liveThreshold") + "");
            if (liveThreshold < 0 || liveThreshold > 100) {
                return false;
            }

            int idThreshold = Integer.valueOf(jsonObject.get("IdThreshold") + "");
            if (idThreshold < 0 || idThreshold > 100) {
                return false;
            }

            int activeModel = Integer.valueOf(jsonObject.get("activeModel") + "");
            if (!(activeModel == 1 || activeModel == 2 || activeModel == 3)) {
                return false;
            }

            int timeLapse = Integer.valueOf(jsonObject.get("timeLapse") + "");

            int type = Integer.valueOf(jsonObject.get("type") + "");
            if (!(type == 0 || type == 1 || type == 2 || type == 3 || type == 4)) {
                return false;
            }

            float rgbLiveScore = Float.valueOf(jsonObject.get("rgbLiveScore") + "");
            if (rgbLiveScore < 0 || rgbLiveScore > 1) {
                return false;
            }

            float nirLiveScore = Float.valueOf(jsonObject.get("nirLiveScore") + "");
            if (nirLiveScore < 0 || nirLiveScore > 1) {
                return false;
            }

            float depthLiveScore = Float.valueOf(jsonObject.get("depthLiveScore") + "");
            if (depthLiveScore < 0 || depthLiveScore > 1) {
                return false;
            }
            int cameraType = jsonObject.getInt("cameraType");
            if (!(cameraType == 0 || cameraType == 1 || cameraType == 2 || cameraType == 3 ||
                    cameraType == 4 || cameraType == 5 || cameraType == 6)) {
                return false;
            }

            int mirrorRGB = jsonObject.getInt("mirrorRGB");
            if (!(mirrorRGB == 0 || mirrorRGB == 1)) {
                return false;
            }

            int mirrorNIR = jsonObject.getInt("mirrorNIR");
            if (!(mirrorNIR == 0 || mirrorNIR == 1)) {
                return false;
            }

            int getBestImageScore = jsonObject.getInt("bestImageScore");
            if (getBestImageScore < 0 || getBestImageScore > 100) {
                return false;
            }

            int rgbAndNirWidth = jsonObject.getInt("rgbAndNirWidth");
            int rgbAndNirHeight = jsonObject.getInt("rgbAndNirHeight");
            int depthWidth = jsonObject.getInt("depthWidth");
            int depthHeight = jsonObject.getInt("depthHeight");

        } catch (Exception e) {
            String errorMessage = getErrorInfoFromException(e);
            e.printStackTrace();
            Log.e("facesdk", "文件内容格式异常,请检测是否规范");
            return false;
        }

        return true;
    }

    /**
     * 修改配置文件内容并重新读取配置
     */
    public static boolean modityJson() {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("display", SingleBaseConfig.getBaseConfig().getDisplay());
            jsonObject.put("isNirOrDepth", SingleBaseConfig.getBaseConfig().getNirOrDepth());
            jsonObject.put("debug", SingleBaseConfig.getBaseConfig().isDebug());
            jsonObject.put("videoDirection", SingleBaseConfig.getBaseConfig().getVideoDirection());
            jsonObject.put("detectFrame", SingleBaseConfig.getBaseConfig().getDetectFrame());
//            jsonObject.put("radius", SingleBaseConfig.getBaseConfig().getRadius());
            jsonObject.put("detectDirection", SingleBaseConfig.getBaseConfig().getDetectDirection());
            jsonObject.put("trackType", SingleBaseConfig.getBaseConfig().getTrackType());
            jsonObject.put("minimumFace", SingleBaseConfig.getBaseConfig().getMinimumFace());
            jsonObject.put("blur", String.valueOf(SingleBaseConfig.getBaseConfig().getBlur()));
            jsonObject.put("illumination", SingleBaseConfig.getBaseConfig().getIllumination());
            jsonObject.put("gesture", SingleBaseConfig.getBaseConfig().getGesture());
            jsonObject.put("pitch", SingleBaseConfig.getBaseConfig().getPitch());
            jsonObject.put("roll", SingleBaseConfig.getBaseConfig().getRoll());
            jsonObject.put("yaw", SingleBaseConfig.getBaseConfig().getYaw());
            jsonObject.put("occlusion", String.valueOf(SingleBaseConfig.getBaseConfig().getOcclusion()));
            jsonObject.put("leftEye", String.valueOf(SingleBaseConfig.getBaseConfig().getLeftEye()));
            jsonObject.put("rightEye", String.valueOf(SingleBaseConfig.getBaseConfig().getRightEye()));
            jsonObject.put("nose", String.valueOf(SingleBaseConfig.getBaseConfig().getNose()));
            jsonObject.put("mouth", String.valueOf(SingleBaseConfig.getBaseConfig().getMouth()));
            jsonObject.put("leftCheek", String.valueOf(SingleBaseConfig.getBaseConfig().getLeftCheek()));
            jsonObject.put("rightCheek", String.valueOf(SingleBaseConfig.getBaseConfig().getRightCheek()));
            jsonObject.put("chinContour", String.valueOf(SingleBaseConfig.getBaseConfig().getChinContour()));
            jsonObject.put("completeness", String.valueOf(SingleBaseConfig.getBaseConfig().getCompleteness()));
//            jsonObject.put("threshold", SingleBaseConfig.getBaseConfig().getThreshold());
            jsonObject.put("liveThreshold", SingleBaseConfig.getBaseConfig().getLiveThreshold());
            jsonObject.put("IdThreshold", SingleBaseConfig.getBaseConfig().getIdThreshold());
            jsonObject.put("rgbAndNirThreshold", SingleBaseConfig.getBaseConfig().getRgbAndNirThreshold());
            jsonObject.put("cameraLightThreshold", SingleBaseConfig.getBaseConfig().getCamera_lightThreshold());
            jsonObject.put("activeModel", SingleBaseConfig.getBaseConfig().getActiveModel());
            jsonObject.put("timeLapse", SingleBaseConfig.getBaseConfig().getTimeLapse());
            jsonObject.put("type", SingleBaseConfig.getBaseConfig().getType());
            jsonObject.put("dPass", SingleBaseConfig.getBaseConfig().getdPass());
            jsonObject.put("qualityControl", SingleBaseConfig.getBaseConfig().isQualityControl());
            jsonObject.put("livingControl", SingleBaseConfig.getBaseConfig().isLivingControl());
            jsonObject.put("rgbLiveScore", SingleBaseConfig.getBaseConfig().getRgbLiveScore());
            jsonObject.put("nirLiveScore", SingleBaseConfig.getBaseConfig().getNirLiveScore());
            jsonObject.put("depthLiveScore", SingleBaseConfig.getBaseConfig().getDepthLiveScore());
            jsonObject.put("framesThreshold", SingleBaseConfig.getBaseConfig().getFramesThreshold());
            jsonObject.put("cameraType", SingleBaseConfig.getBaseConfig().getCameraType());
            jsonObject.put("mirrorRGB", SingleBaseConfig.getBaseConfig().getMirrorRGB());
            jsonObject.put("mirrorNIR", SingleBaseConfig.getBaseConfig().getMirrorNIR());
            jsonObject.put("RGBRevert", SingleBaseConfig.getBaseConfig().getRgbRevert());
            jsonObject.put("attribute", SingleBaseConfig.getBaseConfig().isAttribute());
            jsonObject.put("rgbAndNirWidth", SingleBaseConfig.getBaseConfig().getRgbAndNirWidth());
            jsonObject.put("rgbAndNirHeight", SingleBaseConfig.getBaseConfig().getRgbAndNirHeight());
            jsonObject.put("depthWidth", SingleBaseConfig.getBaseConfig().getDepthWidth());
            jsonObject.put("depthHeight", SingleBaseConfig.getBaseConfig().getDepthHeight());
            jsonObject.put("usingBestImage", SingleBaseConfig.getBaseConfig().isUsingBestImage());
            jsonObject.put("bestImageScore", SingleBaseConfig.getBaseConfig().getBestImageScore());

            // 修改内容写入配置文件
            FileUtils.writeTxtFile(jsonObject.toString(), filePath);
            // 重新读取配置文件内容
            initConfig();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getErrorInfoFromException(Exception e) {
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            return "\r\n" + sw.toString() + "\r\n";
        } catch (Exception e2) {
            return "bad getErrorInfoFromException";
        }
    }

    /**
     * 判断数字正则表达式
     *
     * @param str
     * @return
     */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;

    }

    /**
     * 判断字符正则表达式
     *
     * @param str
     * @return
     */
    public boolean isString(String str) {

        return str.matches("[a-zA-Z]+");

    }


    // 对象属性赋值
    public static <T> T gotObjectByObject(Object object, Class<T> clazz) throws Exception {
        T t = null;
        if (clazz != null && object != null) {
            t = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String key = field.getName();
                try {
                    Field field1 = object.getClass().getDeclaredField(key);
                    field1.setAccessible(true);
                    Object val = field1.get(object);
                    field.set(t, val);
                } catch (Exception e) {
                    t = null;
                    System.out.println(object.getClass().getName() + "没有该属性: " + key);
                }
            }
        }
        return t;
    }


}
