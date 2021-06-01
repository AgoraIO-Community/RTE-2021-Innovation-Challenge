#pragma once

#include <iostream>
#include<ctime>
#include<functional>
#include<direct.h>
#include<io.h>
#include<sys/stat.h>
#include<thread>
#include<atomic>
#include<memory>
#include<mutex>
#include<chrono>
#include<condition_variable>
#include"include_/rapidjson/document.h"
#include"include_/rapidjson/stringbuffer.h"
#include"include_/rapidjson/writer.h"
#include"include_/Curl/curl.h"
#include"include_/JpegTurbo/jpeglib.h"
#include"include_/freeImage/FreeImage.h"


#include"ViolationObserver.h"
#include"CAgoraOriginalVideoDlg.h"
#include"dialog.h"

#define MAX_BUFFER_SIZE 65535

static std::string TempDir = "..\\TmpImage";
static std::string ResizeDir = "..\\ResizeImage";
static std::string violationDir = "..\\ViolationImage";
static int time_out =0;
static time_t oldTime = NULL;

#pragma region BASE64

static const std::string base64_chars =
"ABCDEFGHIJKLMNOPQRSTUVWXYZ"
"abcdefghijklmnopqrstuvwxyz"
"0123456789+/";
static inline bool is_base64(const char c)
{
	return (isalnum(c) || (c == '+') || (c == '/'));
}

std::string base64_encode(const char* bytes_to_encode, unsigned int in_len)
{
	std::string ret;
	int i = 0;
	int j = 0;
	unsigned char char_array_3[3];
	unsigned char char_array_4[4];

	while (in_len--)
	{
		char_array_3[i++] = *(bytes_to_encode++);
		if (i == 3)
		{
			char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
			char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
			char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
			char_array_4[3] = char_array_3[2] & 0x3f;
			for (i = 0; (i < 4); i++)
			{
				ret += base64_chars[char_array_4[i]];
			}
			i = 0;
		}
	}
	if (i)
	{
		for (j = i; j < 3; j++)
		{
			char_array_3[j] = '\0';
		}

		char_array_4[0] = (char_array_3[0] & 0xfc) >> 2;
		char_array_4[1] = ((char_array_3[0] & 0x03) << 4) + ((char_array_3[1] & 0xf0) >> 4);
		char_array_4[2] = ((char_array_3[1] & 0x0f) << 2) + ((char_array_3[2] & 0xc0) >> 6);
		char_array_4[3] = char_array_3[2] & 0x3f;

		for (j = 0; (j < i + 1); j++)
		{
			ret += base64_chars[char_array_4[j]];
		}

		while ((i++ < 3))
		{
			ret += '=';
		}

	}
	return ret;
}

void ClearImage(std::string dir) {

	//在目录后面加上"\\*.*"进行第一次搜索
	std::string newDir = dir + "\\*.*";
	//用于查找的句柄
	intptr_t handle;
	struct _finddata_t fileinfo;
	//第一次查找
	handle = _findfirst(newDir.c_str(), &fileinfo);
	if (handle == -1) {
		return;
	}
	do
	{
		if (fileinfo.attrib & _A_SUBDIR) {//如果为文件夹，加上文件夹路径，再次遍历
			if (strcmp(fileinfo.name, ".") == 0 || strcmp(fileinfo.name, "..") == 0)
				continue;
			// 在目录后面加上"\\"和搜索到的目录名进行下一次搜索
			newDir = dir + "\\" + fileinfo.name;
			ClearImage(newDir.c_str());//先遍历删除文件夹下的文件，再删除空的文件夹			
			_rmdir(newDir.c_str());//删除空文件夹
		}
		else {
			std::string file_path = dir + "\\" + fileinfo.name;
			remove(file_path.c_str()); 
		}
	} while (!_findnext(handle, &fileinfo));

	_findclose(handle);
	return;
}

bool MoveImageToDest(const std::string& source, const std::string& target) {

	char buffer[1024];

	FILE* oStream = fopen(target.c_str(), "wb");
	FILE *iStream = fopen(source.c_str(), "rb");
	
	if (iStream == NULL) {		
		return false;
	}
	/*if (oStream == NULL)
		return false;*/

	int iFileSize = 0;
	std::fseek(iStream, 0, SEEK_END);
	iFileSize = std::ftell(iStream);
	std::fseek(iStream, 0, SEEK_SET);

	if (iFileSize <= 0) {
		perror("FileSize is less than zero\n ");
		return false;
	}
	int resSize = 0;
	while (resSize < iFileSize) {
		int res = fread(buffer, 1, iFileSize > 1024 ? 1024 : iFileSize, iStream);
		fwrite(buffer, 1, res, oStream);
		resSize += res;
		memset(buffer, 0, 1024);
	}

	fclose(iStream);
	fclose(oStream);
	return true;
}

#pragma endregion

#pragma region BuildJson

std::string writeJsonImage(const char* fileName) {

	rapidjson::Document mJson;
	mJson.SetObject();
	char* buffer = (char*)malloc(MAX_BUFFER_SIZE * sizeof(char));
	memset(buffer, 0, MAX_BUFFER_SIZE);
	int resSize = 0;
	long FileSize = 0;

	rapidjson::Document::AllocatorType& allocator = mJson.GetAllocator();
	rapidjson::Value Img;
	std::string Json_header = "data:image/jpeg;base64,";

	FILE* fstream = fopen(fileName, "rb");
	if (fstream == nullptr) {
		perror("Open Imagefile error \n");
		return NULL;
	}

	std::fseek(fstream, 0, SEEK_END);
	FileSize = std::ftell(fstream);
	std::fseek(fstream, 0, SEEK_SET);

	if (FileSize <= 0) {
		perror("FileSize is less than zero\n ");
		return NULL;
	}

	std::string jpegString;
	while (resSize < FileSize) {
		int res = fread(buffer, 1, FileSize > MAX_BUFFER_SIZE ? MAX_BUFFER_SIZE : FileSize, fstream);

		jpegString += base64_encode(buffer, res);
		resSize += res;
		memset(buffer, 0, MAX_BUFFER_SIZE);
	}

	Img.SetString((jpegString).c_str(), (jpegString).length(), allocator);

	mJson.AddMember("img", Img, allocator);

	//转为String并返回
	rapidjson::StringBuffer Json2Stringbuff;
	rapidjson::Writer<rapidjson::StringBuffer> jWriter(Json2Stringbuff);
	mJson.Accept(jWriter);
	std::string resultStr(Json2Stringbuff.GetString());

	fclose(fstream);
	free(buffer);
	return resultStr;
}


#pragma endregion

#pragma region HttpPost

class CurlClient {
private:
    bool m_bDebug;

private:
    void SetDebug(bool bDebug);
    void SetParam(std::string respondJson);
public:
    std::string neutral;
    std::string metamorphosis;
    std::string sex_addiction;
    std::string patinting;
    std::string pornography;
public:
    CurlClient() :m_bDebug(false) {
        neutral = "";
        metamorphosis = "";
        sex_addiction = "";
        patinting = "";
        pornography = "";
    }
    ~CurlClient() {}
    int Http_post(const std::string& desUrl, const std::string& param);

};

void CurlClient::SetDebug(bool bDebug) {
    m_bDebug = bDebug;
}

static int OnDebug(CURL*, curl_infotype itype, char* pData, size_t size, void*) {

    if (itype == CURLINFO_TEXT)
        printf("[TEXT]%s\n", pData);
    else if (itype == CURLINFO_HEADER_IN)
        printf("[HEADER_IN]%s\n", pData);
    else if (itype == CURLINFO_HEADER_OUT)
        printf("[HEADER_OUT]%s\n", pData);
    else if (itype == CURLINFO_DATA_IN)
        printf("[DATA_IN]%s\n", pData);
    else if (itype == CURLINFO_DATA_OUT)
        printf("[DATA_OUT]%s\n", pData);

    return 0;
}

static size_t OnWriteData(void* buffer, size_t size, size_t nmemb, void* lpvoid) {
    std::string* str = dynamic_cast<std::string*>((std::string*)lpvoid);
    if (str == NULL || buffer == nullptr)
        return -1;

    char* pData = (char*)buffer;
    str->append(pData, size * nmemb);

    return nmemb;
}


//一秒传一次，考虑建立长链接的形式来搞定整体的事情
int CurlClient::Http_post(const std::string& desUrl, const std::string& param) {

    CURLcode respond;

    struct curl_slist* header = nullptr;
    header = curl_slist_append(header, "token:fcdf896fa0583cc207dfedf55b614f40");
    header = curl_slist_append(header, "Content-Type:application/json");

    CURL* curl = curl_easy_init();
    if (curl == nullptr)
        return CURLE_FAILED_INIT;
    if (m_bDebug) {
        curl_easy_setopt(curl, CURLOPT_VERBOSE, 1L);
        curl_easy_setopt(curl, CURLOPT_DEBUGFUNCTION,OnDebug);
    }
    std::string strRespond;

    curl_easy_setopt(curl, CURLOPT_URL, desUrl.c_str());
    curl_easy_setopt(curl, CURLOPT_HTTPHEADER, header);
    curl_easy_setopt(curl, CURLOPT_POST, 1);
    curl_easy_setopt(curl, CURLOPT_POSTFIELDS, param.c_str());
    curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, OnWriteData,this);
    curl_easy_setopt(curl, CURLOPT_WRITEDATA, (void*)&strRespond);
    curl_easy_setopt(curl, CURLOPT_NOSIGNAL, 1);

    curl_easy_setopt(curl, CURLOPT_CONNECTTIMEOUT, 3);
    curl_easy_setopt(curl, CURLOPT_TIMEOUT, 3);
    respond = curl_easy_perform(curl);
    SetParam(strRespond);

    curl_slist_free_all(header);
    curl_easy_cleanup(curl);
    curl_global_cleanup();

    return respond;
}

void CurlClient::SetParam(std::string respondJson) {

    int iFirstParam = respondJson.find("\\u4e2d\\u6027");
    int iSecondParam = respondJson.find("\\u53d8\\u6001");
    int iThirdParam = respondJson.find("\\u6027\\u611f");
    int iFourthParam = respondJson.find("\\u753b\\u4f5c");
    int iFifthParam = respondJson.find("\\u8272\\u60c5");
    neutral = respondJson.substr(iFirstParam + 14, 7);
    metamorphosis = respondJson.substr(iSecondParam + 14, 7);
    sex_addiction = respondJson.substr(iThirdParam + 14, 7);
    patinting = respondJson.substr(iFourthParam + 14, 7);
    pornography = respondJson.substr(iFifthParam + 14, 7);
}

#pragma endregion

#pragma region VilationObserver implement


double ViolationObserver::getMetamorphosis() {
    return this->metamorphosis;
}

double ViolationObserver::getNeutral() {
    return this->neutral;
}

double ViolationObserver::getPatinting() {
    return this->patinting;
}

double ViolationObserver::getSexAddiction() {
    return this->sex_addiction;
}

double ViolationObserver::getPornograph() {
    return this->pornography;
}

void ViolationObserver::OnObserverViolatingContent(m_VideoFrame VideoFrame) {

    time_t nowTime = time(NULL);
    //秒为单位
    if((nowTime -oldTime) >= 5){
        this->interceptJpegAndUpload(VideoFrame);
        oldTime = nowTime;
    }
    return ;
}

void ViolationObserver::OffObserverViolatingContent(){

    ClearImage(TempDir);
    ClearImage(ResizeDir);
}


void  ViolationObserver::interceptJpegAndUpload(m_VideoFrame& videoFrame) {

    struct tm* localtime;
    time_t now_time = time(NULL);
    char timeStr[80];
    localtime = std::localtime(&now_time);
    strftime(timeStr, 100, "%b_%d_%H_%M_%S", localtime);

    std::string fileName = TempDir+"\\" + std::string(timeStr) + ".jpeg";
    std::string dstFileName = ResizeDir+"\\" + std::string(timeStr) + ".jpeg";

    struct _stat fileStat = {0};
    if(_stat(TempDir.c_str(),&fileStat) != 0){
        _mkdir(TempDir.c_str());
    }

    if(_stat(ResizeDir.c_str(),&fileStat) != 0){
        _mkdir(ResizeDir.c_str());
    }

    if(_stat(violationDir.c_str(),&fileStat) != 0){
        _mkdir(violationDir.c_str());
    }

    this->yuv420p_to_jpeg(fileName.c_str(), (char*)videoFrame.yBuffer, (char*)videoFrame.uBuffer, (char*)videoFrame.vBuffer, videoFrame.width, videoFrame.height);
    this->ChangeImageSize(fileName.c_str(), dstFileName.c_str());

    std::string destUrl = "https://api.xabc.site/detect";

    CurlClient client;
    int res = client.Http_post(destUrl, writeJsonImage(dstFileName.c_str()));
    if (res != 0) {
        printf("%s detected error because Http_post failed", std::string((char*)(&now_time)).c_str());
        Violation_Info tempInfo;
        tempInfo.neutral =0;
        tempInfo.patinting =0;
        tempInfo.pornography =0;
        tempInfo.metamorphosis =0;
        tempInfo.sex_addiction =0;

        tempInfo.ImagePath ="";
        tempInfo.time_Info = "";
        tempInfo.DetectResult = DetectType::INTERRUPT;
        //promiseObj.set_value(tempInfo);
        return ;
    }

    this->neutral =atof(client.neutral.c_str());
    this->metamorphosis =atof( client.metamorphosis.c_str());
    this->sex_addiction = atof(client.sex_addiction.c_str());
    this->patinting = atof(client.patinting.c_str());
    this->pornography = atof(client.pornography.c_str());
	
    if (metamorphosis > 0.8 || pornography > 0.8) {
        MoveImageToDest(fileName, violationDir + std::string(timeStr) + ".jpeg");
        Violation_Info tempInfo;
        tempInfo.neutral =this->neutral;
        tempInfo.patinting =this->patinting;
        tempInfo.pornography =this->pornography;
        tempInfo.metamorphosis =this->metamorphosis;
        tempInfo.sex_addiction =this->sex_addiction;

        tempInfo.ImagePath =fileName;
        tempInfo.time_Info = std::string(timeStr);
        tempInfo.DetectResult = DetectType::Violation;

        return ;
    }

    Violation_Info tempInfo;
    tempInfo.neutral =this->neutral;
    tempInfo.patinting =this->patinting;
    tempInfo.pornography =this->pornography;
    tempInfo.metamorphosis =this->metamorphosis;
    tempInfo.sex_addiction =this->sex_addiction;

    tempInfo.ImagePath ="";
    tempInfo.time_Info = "";
    tempInfo.DetectResult = DetectType::Normal;

    return ;

}


int ViolationObserver::yuv420p_to_jpeg(const char* filename, char* Ybuffer, char* Ubuffer, char* Vbuffer, long image_width, long image_height, int quality)
{
    //图像转黑白处理
    int nSize = image_width * image_height;
    //set UV to 128 to mask color information
    memset(Ubuffer, 128, nSize / 4);
    memset(Vbuffer, 128, nSize / 4);

    struct jpeg_compress_struct cinfo;
    struct jpeg_error_mgr jerr;
    cinfo.err = jpeg_std_error(&jerr);
    jpeg_create_compress(&cinfo);

    FILE* outfile;    // target file
    if ((outfile = fopen(filename, "wb")) == NULL) {
        fprintf(stderr, "can't open %s\n", filename);
        exit(1);
    }
    jpeg_stdio_dest(&cinfo, outfile);

    cinfo.image_width = image_width;  //image_width image width and height, in pixels
    cinfo.image_height = image_height;
    cinfo.input_components = 3;    // # of color components per pixel
    cinfo.in_color_space = JCS_YCbCr;  //colorspace of input image
    jpeg_set_defaults(&cinfo);
    jpeg_set_quality(&cinfo, quality, TRUE);

    //  cinfo.raw_data_in = TRUE;
    cinfo.jpeg_color_space = JCS_YCbCr;
    cinfo.comp_info[0].h_samp_factor = 2;
    cinfo.comp_info[0].v_samp_factor = 2;
    jpeg_start_compress(&cinfo, TRUE);

    JSAMPROW row_pointer[1];

    unsigned char* yuvbuf = (unsigned char*)malloc(image_width * 3);;

    if (yuvbuf != nullptr)
        memset(yuvbuf, 0, image_width * 3);
    else
    {
        printf("malloc yuvbuff space occure error");
        return -1;
    }

    char* ybase, * ubase, * vbase;
    ybase = Ybuffer;
    ubase = Ubuffer;
    vbase = Vbuffer;

    int j = 0;
    while (cinfo.next_scanline < cinfo.image_height)
    {
        int idx = 0;
        for (int i = 0; i < image_width; i++)
        {
            yuvbuf[idx++] = ybase[i + j * image_width];
            yuvbuf[idx++] = ubase[j / 2 * image_width / 2 + (i / 2)];
            yuvbuf[idx++] = vbase[j / 2 * image_width / 2 + (i / 2)];
        }
        row_pointer[0] = yuvbuf;
        jpeg_write_scanlines(&cinfo, row_pointer, 1);
        j++;
    }
    jpeg_finish_compress(&cinfo);
    jpeg_destroy_compress(&cinfo);
    free(yuvbuf);
    fclose(outfile);
    return 0;
}
void ViolationObserver::ChangeImageSize(const char* oriFilePath, const char* dstFilePath, const int nWidth, const int nHeight) {

    FreeImage_Initialise(TRUE);
    FIBITMAP* jpeg = FreeImage_Load(FIF_JPEG, oriFilePath, JP2_DEFAULT);
    FIBITMAP* dstImage = FreeImage_Rescale(jpeg, nWidth, nHeight);
    FreeImage_Save(FIF_JPEG, dstImage, dstFilePath);
    FreeImage_Unload(jpeg);
    FreeImage_Unload(dstImage);
    FreeImage_DeInitialise();
}

bool ViolationObserver::onCaptureVideoFrame(VideoFrame &videoFrame){

    ViolationObserver::m_VideoFrame mVideoFrame;
    mVideoFrame.width = videoFrame.width;
    mVideoFrame.height = videoFrame.height;
    mVideoFrame.yBuffer = videoFrame.yBuffer;
    mVideoFrame.uBuffer = videoFrame.uBuffer;
    mVideoFrame.vBuffer = videoFrame.vBuffer;

    OnObserverViolatingContent(mVideoFrame);
    return true;
}

bool ViolationObserver::onRenderVideoFrame(unsigned int uid, VideoFrame &videoFrame){

    return true;
}

#pragma endregion

