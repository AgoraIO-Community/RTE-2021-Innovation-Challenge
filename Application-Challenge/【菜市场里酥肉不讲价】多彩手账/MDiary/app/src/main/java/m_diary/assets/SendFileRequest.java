package m_diary.assets;

public class SendFileRequest {
    public String requestCode = "";
    public String userName = "";
    public String diaryNumber = "";
    public String fileType = "";
    public SendFileRequest(String requestCode, String userName, String diaryNumber, String fileType){
        this.requestCode = requestCode;
        this.userName = userName;
        this.diaryNumber = diaryNumber;
        this.fileType = fileType;
    }
}
