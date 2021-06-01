#ifndef DEMO_INTEGRATEDCLASS_H
#define DEMO_INTEGRATEDCLASS_H

#include<QWidget>
#include<QLabel>
#include<QEvent>
#include<QStandardItemModel>

#include"include_/Agora/AgoraBase.h"
#include"include_/Agora/IAgoraRtcEngine.h"
#include"include_/Agora/IAgoraService.h"
#include"include_/Agora/IAgoraLog.h"

#include"ViolationObserver.h"


using namespace agora::rtc;

static const char* AppID = "ba085745c164455d92f72e9724783dc3";








class Demo_EventHandler:public agora::rtc::IRtcEngineEventHandler
{
public:
    Demo_EventHandler(){}
    ~Demo_EventHandler(){}

public:
    virtual void onJoinChannelSuccess(const char *channel, uid_t uid, int elapsed) override;
    
    virtual void onLeaveChannel(const RtcStats &stats) override;
    
    virtual void onUserJoined(uid_t uid, int elapsed) override;
    
    virtual void onUserOffline(uid_t uid, USER_OFFLINE_REASON_TYPE reason) override;

    void setRenderVideoHwd(HWND hwd){
        this->m_renderVideoHwnd = hwd;
    }
    void setRtcEngine(IRtcEngine *Engine){
        this->m_rtcEngine = Engine;
    }
private:
    HWND m_renderVideoHwnd;
    IRtcEngine *m_rtcEngine = nullptr;

};


class Demo_VidoDlag{
    
public:
    Demo_VidoDlag(){}
    virtual ~Demo_VidoDlag(){}
    
public:
    bool InitAgora();
    void UnInitAogra();
    void RenderLocalVideo();
    void ResumeStatus();
    
    bool RegisterVideoFrameObserver(bool bEnable,agora::media::IVideoFrameObserver * videoFrameObserver = NULL);
public:
    void setLocalVideoWnd(HWND mHd){
        this->m_localVideoWnd = mHd;
    }

    void setRenderVideoWnd(HWND mHd){
        this->m_renderVideoWnd = mHd;
    }
    void setChannelName(char* channelName){
        this->m_channelName = channelName;
    }

    Demo_EventHandler* getEventHandlerObj(){
        return this->m_eventHandler;
    }

    IRtcEngine* getMRtcEngine(){
        return m_rtcEngine;
    }

    bool JoinChannel(char* ccChannelName,unsigned int u_id,const char* ccToken);
    bool LeveaChannel();
    
private:
    bool m_joinChannel =false;
    bool m_initalize = false;
    bool m_setVidoProc = false;

    char * m_channelName;
    HWND m_localVideoWnd;
    HWND m_renderVideoWnd;
    
    IRtcEngine *m_rtcEngine = nullptr;
    Demo_EventHandler *m_eventHandler = new Demo_EventHandler();
public:
    ViolationObserver m_ViolationObserver;
        
};

class ViolationInfoList: public QWidget{
    Q_OBJECT
public:
    explicit ViolationInfoList(QWidget *parent=0);
    void initUi();
    QWidget *Image;
    QLabel *Time;
    QString ImagePath;
    bool eventFilter(QObject* obj,QEvent *event);



};


#endif // DEMO_INTEGRATEDCLASS_H
