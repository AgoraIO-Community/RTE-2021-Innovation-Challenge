package com.kangraoo.basektlib.tools.debug;//package com.kangraoo.basektlib.tools.debug;
//
//import com.kangaroo.lib.app.SApplication;
//import com.kangaroo.lib.tools.HString;
//import com.kangaroo.lib.tools.UClipboard;
//import com.kangaroo.lib.tools.UTime;
//import com.kangaroo.lib.tools.encryption.HAes;
//import com.kangaroo.lib.tools.encryption.HBase64;
//import com.kangaroo.lib.tools.encryption.HEncryption;
//import com.kangaroo.lib.tools.encryption.HSha;
//import com.kangaroo.lib.tools.tip.ErrorTipToast;
//import com.kangaroo.lib.tools.tip.TipToast;
//
//import static com.kangaroo.lib.tools.encryption.HSha.SHA512;
//
///**
// * author : sdw
// * e-mail : shidawei@xiaohe.com
// * time : 2019/09/02
// * desc :
// * version: 1.0
// */
//public class UDebug {
//
//    public static final String debugTest = "%#debug#tip debug safe model#%";
//
//    public static boolean checkStatus(){
//        if(SApplication.getInstance().sConfiger.isDebugStatic()||!SApplication.getInstance().sConfiger.isOnlineDebug()){
//            return false;
//        }
//
//        String text = UClipboard.clipboardText(SApplication.getAppContext());
//        if(text!=null){
//            if(text.equals(debugTest)){
//                UClipboard.clipboardCopyText(SApplication.getAppContext(),null);
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static boolean checkToDebug(){
//        if(SApplication.getInstance().sConfiger.isDebugStatic()||!SApplication.getInstance().sConfiger.isOnlineDebug()){
//            return false;
//        }
//
//        String text = UClipboard.clipboardText(SApplication.getAppContext());
//        if(text!=null){
//            if(text.startsWith("%#debug_time#")){
//                UClipboard.clipboardCopyText(SApplication.getAppContext(),null);
//                String d = text.substring(13);
//                String safeCode =  getSafeCode();
//                String m = "f48b1449580560819f0935b27a82e047ea92da648ae493105301f8fe72c4b842da0a086b6a9da417f21f0a33ff94407f4354b51ae5a49db792dde40e93948bce";
//                String mz = HString.makeMd5(safeCode+m);
//                int size = 128/8;
//                byte[] result = mz.getBytes();
//                byte[] keyResult = new byte[size];
//                for(int i = 0;i<size;i++){
//                    keyResult[i] = result[i%result.length];
//                }
//                String t= null;
//                try{
//                    t= new String(HAes.getInstance().decrypt(HBase64.decode(d),keyResult));
//                }catch (Exception e){
//                    TipToast.getInstance().tip(ErrorTipToast.getInstance(),"code is error");
//                    return false;
//                }
//                if(UTime.isSameDay(Long.valueOf(t), Long.valueOf(UTime.currentTimeMillis()))){
//                    return true;
//                }else {
//                    TipToast.getInstance().tip(ErrorTipToast.getInstance(),"codetime is error");
//                }
//            }
//        }
//        return false;
//
//    }
//
//    public static String cTos(String safeCode, String p){
//        String m = HString.makeMd5(safeCode+HSha.getStringSha(p,SHA512));
//        String time = String.valueOf(UTime.currentTimeMillis());
//        int size = 128/8;
//        byte[] result = m.getBytes();
//        byte[] keyResult = new byte[size];
//        for(int i = 0;i<size;i++){
//            keyResult[i] = result[i%result.length];
//        }
//        String d = HBase64.encode(HAes.getInstance().encrypt(time.getBytes(),keyResult));
//        String s =  HString.concatObject("","%#debug_time#",d);
//        return s;
//    }
//
//    public static void safeCodeToClipboard(){
//        String safeCode =  getSafeCode();
//        UClipboard.clipboardCopyText(SApplication.getAppContext(),safeCode);
//    }
//
//    private static String getSafeCode(){
//        return HString.makeMd5(HSha.getStringSha(HEncryption.generateLibKeyString()+UTime.getNowDateTime(UTime.YMD),SHA512));
//    }
//
//}
