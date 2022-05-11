package my.socket.server;

import Test.TestUser;
import Test.Weather;
import my.socket.mysql.DBManager;
import my.socket.mysql.UserOpration;
import my.socket.utils.Log;
import my.socket.utils.Protocol;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.lang.ObjectUtils;

import javax.naming.spi.DirectoryManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;

public class DataProcess {
    public static String getReturnString(String msg)
    {
        JSONObject jsonObj =JSONObject.fromString(msg);
        return switch (jsonObj.getString(Protocol.REQUEST_CODE)) {
            case Protocol.LOGIN -> userLoginAction(jsonObj);
            case Protocol.REGISTER -> userRegisterAction(jsonObj);
            case Protocol.Get_Weather -> getWeatherAction();
            case Protocol.SEND_FILE -> sendFileAction(jsonObj);
            default -> Protocol.REGISTER_FAILED;
        };
    }

    public static String savePath = null;

    public static String sendFileAction(JSONObject jsonObject)
    {
        String username = jsonObject.getString(Protocol.USER_NAME);
        String diaryNumber = jsonObject.getString(Protocol.DIARY_NUM);
        String type = jsonObject.getString(Protocol.FILE_TYPE);
        if(type.equals(Protocol.TypeEnum.MainFile.toString()))
            savePath = String.format("./DiaryStore/%s/%s/", username, diaryNumber);
        else
            savePath = String.format("./DiaryStore/%s/%s/%s/", username, diaryNumber, type);
        File folder = new File(savePath);
        if(!folder.exists() || !folder.isDirectory())
            if (!folder.mkdirs())
                return Protocol.SEND_FAILED;
        try
        {
            DBManager db =new DBManager();
            String sql = String.format("select * from t_diary where user='%s' and count=%s ",username,diaryNumber);
            ResultSet rs = db.select(sql);
            if(rs.next())
            {
                return Protocol.SEND_FILE;
            }
            sql = String.format("insert into t_diary ( user , count) values('%s', %s) ",username,diaryNumber);
            if(db.insert(sql) == -1)
                return Protocol.SEND_FAILED;
        }
        catch (SQLException e)
        {
            Log.print(e.getMessage());
        }
        return Protocol.SEND_FILE;
    }


    public static String requestFileAction(JSONObject jsonObject)
    {
        return Protocol.REQUEST_FILE;
    }


    //登录
    public static String userLoginAction(JSONObject user_json)
    {
        return UserOpration.getUserOprator().longin(user_json.getString("username"), user_json.getString("password"));
    }

    public static String userRegisterAction(JSONObject user_json)
    {
        return UserOpration.getUserOprator().register(user_json.getString("username"), user_json.getString("password"));
    }



    public static String getWeatherAction()
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String [] date = df.format(new Date()).split(" ")[0].split("-");
        DBManager db = new DBManager();
        String sql = String.format("select * from t_weather where year = %d and month = %d and day = %d",
                Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        // String sql = String.format("select * from t_user where user = '%s' and pwd = '%s' ","1", "2");

        try
        {
            ResultSet rs = db.select(sql);
            if(rs.next())
            {
                Weather weather =new Weather();
                weather.setDay(rs.getInt("day"));
                weather.setMonth(rs.getInt("month"));
                weather.setYear(rs.getInt("year"));
                weather.setNotice(rs.getString("notice"));
                weather.setTemperature(rs.getString("temperature"));
                weather.setWeather(rs.getString("weather"));
                weather.setWeek(rs.getString("week"));
                return JSONObject.fromObject(weather).toString();
            }
            else
            {
                Weather weather = ProcessWeatherJson(getWeatherJson());
                if(weather != null)
                    weather.setRequestCode("weather");
                sql = String.format("insert into t_weather (year, day, weather, temperature, notice, month, week) " +
                        "values (%d, %d,'%s','%s','%s', %d,'%s')",weather.getYear(), weather.getDay(), weather.getWeather(),
                        weather.getTemperature(), weather.getNotice(), weather.getMonth(), weather.getWeek());
                db.insert(sql);
                return JSONObject.fromObject(weather).toString();
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        return "";

    }

    public static Weather ProcessWeatherJson(String My_weather)
    {
        try{
            if(My_weather!=null)
            {
                Weather weather = new  Weather();
                JSONObject obj = JSONObject.fromString(My_weather);
                int result = obj.getInt("status");
                if (result == 200) {
                    JSONObject cityInfo = obj.getJSONObject("cityInfo");
                    JSONObject data = obj.getJSONObject("data");
                    JSONArray forecastArray = data.getJSONArray("forecast");

                    String []  date = obj.getString("time").split(" ")[0].split("-");
                    String city;// = cityInfo.getString("city");
                    String temperature = data.getString("wendu");
                    String day;
                    String type;
                    String notice;
                    day = forecastArray.getJSONObject(0).getString("week");
                    type = forecastArray.getJSONObject(0).getString("type");
                    notice = forecastArray.getJSONObject(0).getString("notice");
                    weather.setWeek(day);
                    weather.setNotice(notice);
                    weather.setYear(Integer.parseInt(date[0]));
                    weather.setMonth(Integer.parseInt(date[1]));
                    weather.setDay(Integer.parseInt(date[2]));
                    weather.setWeather(type);
                    weather.setTemperature(temperature);
                    return weather;


                }

            }
        }catch(JSONException e){
           return null;
        }
        return null;

    }

    public static String getWeatherJson()
    {
        String stringUrl = "http://t.weather.itboy.net/api/weather/city/101110101";
        HttpURLConnection urlConnection = null;
        BufferedReader reader;

        try {
            URL url = new URL(stringUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            //The temperature
            return buffer.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
