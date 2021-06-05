package m_diary.utils;

public class Protocol {
    /****************************本地交互协议***************************/
    //存储及参数传递名称协议
    public static final String IMG_PATH = "imgPath";
    public static final String VIDEO_PATH = "videoPath";
    public static final String AUDIO_PATH = "audioPath";
    public static final String VIDEO_URI = "videoUri";
    public static final String AUDIO_URI = "audioUri";
    public static final String CHANGED = "changed";
    public static final String NEW_DIARY = "newDiary";
    public static final String AUDIO_NAME = "audioName";
    public static final String OPEN_TEXT_TYPE = "openTextType";
    public static final String STICKER_BITMAP = "stickerBitmap";
    public static final String IS_LOGIN = "isLogin";
    public static final String USER_NAME = "userName";
    public static final String CONTENT = "content";
    public static final String COLOR = "color";
    public static final String STYLE = "style";
    public static final String DIARY_PATH = "diaryPath";
    //返回协议
    public static final int ADD_TEXT = 8;
    public static final int ADD_STICKER = 9;
    public static final int ADD_PICTURE = 10;
    public static final int ADD_VIDEO = 11;
    public static final int ADD_AUDIO = 12;
    public static final int CHOOSE_AUDIO = 13;
    public static final int TAKE_PHOTO = 14;
    public static final int CHOOSE_PHOTO = 15;
    public static final int CHOOSE_VIDEO = 16;
    public static final int SHOOT_VIDEO = 17;
    public static final int OPEN_NEW_DIARY  = 18;
    public static final int NEW_TEXT  = 19;
    public static final int EDIT_TEXT  = 20;

    //权限协议
    public static final int RECORD_PERMISSION = 100;
    public static final int CAMERA_PERMISSION = 101;
    public static final int STORAGE_PERMISSION = 102;
    public static final int READE_PERMISSION = 103;
    public static final int OPEN_PERMISSION = 104;
    /****************************本地交互协议***************************/
    /****************************服务器交互协议***************************/
    //登录注册协议
    public static final String REQUEST_CODE = "requestCode";
    public static final String LOGIN_NULL = "loginNull";
    public static final String LOGIN = "login";
    public static final String REGISTER_NULL = "registerNull";
    public static final String REGISTER = "register";
    public static final String LOGOUT = "logout";
    public static final String LOGIN_SUCCESS = "loginSuccess";			//登录成功消息
    public static final String LOGIN_FAILED = "loginFailed";				//登录失败消息
    public static final String REGISTER_SUCCESS = "registerSuccess";		//注册成功消息
    public static final String REGISTER_FAILED = "registerFailed";		//注册失败消息
    public static final String REGISTER_EXIST = "registerExist";			//注册用户已存在消息
    public static final String LOGOUT_SUCCESS = "logoutSuccess";			//注销成功
    public static final String LOGOUT_FAILED = "logoutFailed";			//注销失败
    //天气协议
    public static final String Get_Weather = "getWeather";
    public static final String DAY = "day";
    public static final String MONTH = "month";
    public static final String YEAR = "year";
    public static final String WEEK = "week";
    public static final String WEATHER = "weather";
    public static final String TEMPERATURE = "temperature";
    public static final String NOTICE = "notice";
    //文件传输
    public static final String SEND_FILE = "sendFile";          //发送文件
    public static final String SEND_SUCCESS = "sendSuccess";
    public static final String SEND_FAILED = "sendFailed";
    public static final String DIARY_NUM = "diaryNumber";
    public static final String FILE_TYPE = "fileType";
    public  enum TypeEnum
    {
        Picture,
        MainFile,
        Video,
        Audio
    }
    /****************************服务器交互协议***************************/
}
