package Test;


import com.mysql.cj.xdevapi.JsonArray;
import com.mysql.cj.xdevapi.JsonParser;
import my.socket.mysql.DBManager;
import my.socket.mysql.UserOpration;
import my.socket.server.DataProcess;
import my.socket.utils.Protocol;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
    public  static  void main(String [] args)
    {
//        String userString = " {\"pwd\" :1,\"user\":\"xiaoming\"} ";
//        JSONObject userJson = JSONObject.fromString(userString);
//        TestUser user = (TestUser) JSONObject.toBean(JSONObject.fromObject(userString), TestUser.class);
//        System.out.println(user.getUser());
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//
//        //System.out.println(DataProcess.getWeatherJson());
//        String wea = DataProcess.getWeatherJson();
//        JSONObject aa = JSONObject.fromString(wea);
//        System.out.println(aa.getJSONObject("cityInfo").getString("city"));
//        Date d = new Date();
//        System.out.println(d.toString());

//        DBManager db = new DBManager();
//        ResultSet ret = db.select(String.format("select * form t_user where user='%s' and pwd='%s' ","AA", "SS"));
//        if(ret != null)
//        {
//            System.out.println("AA");
//        }
//        String json = DataProcess.getWeatherAction();
        //Weather w= DataProcess.ProcessWeatherJson(DataProcess.getWeatherJson());
//        System.out.println(Protocol.TypeEnum.valueOf("Image") == Protocol.TypeEnum.Image);
        //System.out.println(UserOpration.getUserOprator().register("1001","2"));



    }


}
