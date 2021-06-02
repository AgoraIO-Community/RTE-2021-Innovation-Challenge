package mobi.yiyin.utils;

import android.content.Context;

import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @ProjectName: yiyin
 * @Package: mobi.yiyin.utils
 * @ClassName: Utils
 * @Description: todo java类作用描述
 * @Author: zhoujiangnan
 * @CreateDate: 12/8/20 1:22 AM
 * @UpdateUser: 更新者：
 * @UpdateDate: 12/8/20 1:22 AM
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */
public class Utils {



    public  static void  yyOpenWXMin(Context context, String username, String path){
        openWXMin(context,"wx4e65a7ee31fb58ab",username,path);
    }

    /**
     *
     * @Package:        mobi.yiyin.utils
     * @ClassName:      Utils
     * @Description:    手机应用打开的微信小程序某个界面
     * @Author:         zhoujiangnan
     * @CreateDate:     12/8/20 1:27 AM
     * @UpdateUser:     zhoujiangnan
     * @UpdateDate:     12/8/20 1:27 AM
     * @UpdateRemark:   更新描述:
     * @Version:        1.0
     */
  public static void openWXMin(Context context, String appId , String username, String path){
        IWXAPI api = WXAPIFactory.createWXAPI(context, appId);
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        // 填小程序原始id
        req.userName = username;
        ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.path = path ;
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);


    }

}
