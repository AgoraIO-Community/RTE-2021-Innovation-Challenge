#include"Demo_IntegratedClass.h"

#include"ui_dialog.h"
#include"ui_msgitem.h"

#include<string>

#include<QPainter>
void Demo_EventHandler::onJoinChannelSuccess(const char *channel, uid_t uid, int elapsed){
    

    return ;
}

void Demo_EventHandler::onLeaveChannel(const RtcStats &stats){


}
void Demo_EventHandler::onUserJoined(uid_t uid, int elapsed){

    VideoCanvas renderVc;
    renderVc.uid = uid;
    renderVc.renderMode = RENDER_MODE_FIT;
    renderVc.view = m_renderVideoHwnd;

}
void Demo_EventHandler::onUserOffline(uid_t uid, USER_OFFLINE_REASON_TYPE reason){

}




bool Demo_VidoDlag::InitAgora(){
    
    m_rtcEngine =(IRtcEngine*)createAgoraRtcEngine();
    if(!m_rtcEngine)
    {
        perror("Create rtcEngine failed!\n");
        return false;
    }
    
    RtcEngineContext context;
    context.appId = AppID;
    context.eventHandler = m_eventHandler;
    
    int ret = m_rtcEngine->initialize(context);
    if(ret != 0){
        m_initalize = false;
        perror("initalize failed:");
        perror(std::to_string(ret).c_str());
    }else{
        m_initalize = true;
        m_rtcEngine->enableVideo();
    }
}

void Demo_VidoDlag::UnInitAogra(){
    if(m_rtcEngine){
        if(m_joinChannel)
            m_joinChannel = !m_rtcEngine->leaveChannel();
    }
    
    m_rtcEngine->stopPreview();
    m_rtcEngine->disableVideo();
    RegisterVideoFrameObserver(false);
    m_rtcEngine->release(true);
    m_rtcEngine = nullptr;

}

void Demo_VidoDlag::RenderLocalVideo(){
    if(m_rtcEngine){

        m_rtcEngine->startPreview();
        VideoCanvas canvas;
        canvas.uid = 0;
        canvas.renderMode = RENDER_MODE_FIT;
        canvas.view = m_localVideoWnd;
        m_rtcEngine->setupLocalVideo(canvas);     
    }
}

bool Demo_VidoDlag::RegisterVideoFrameObserver(bool bEnable, agora::media::IVideoFrameObserver *videoFrameObserver){
    agora::util::AutoPtr<agora::media::IMediaEngine> mediaEngine;
    mediaEngine.queryInterface(m_rtcEngine,agora::AGORA_IID_MEDIA_ENGINE);
    int nRet = 0;
    if(mediaEngine.get() == NULL)
        return false;
    if(bEnable){
        nRet = mediaEngine->registerVideoFrameObserver(videoFrameObserver);
    }else{
        nRet = mediaEngine->registerVideoFrameObserver(NULL);
    }
    return nRet ==0?true:false;
}

bool Demo_VidoDlag::JoinChannel( char *ccChannelName, unsigned int u_id, const char *ccToken){

    int nRet =0;
    if(strlen(ccToken) == 0){
        nRet = m_rtcEngine->joinChannel(nullptr,ccChannelName,nullptr,u_id);
    }else{
        nRet = m_rtcEngine->joinChannel(ccToken,ccChannelName,nullptr,u_id);
    }
    if(nRet == 0){
        m_channelName = ccChannelName;
    }
    if(!RegisterVideoFrameObserver(true,&m_ViolationObserver))
        return false;
    return true;
}

bool Demo_VidoDlag::LeveaChannel(){
    m_rtcEngine->stopPreview();
    int nRet = m_rtcEngine->leaveChannel();
    return nRet ==0?true:false;
}


//敏感信息的显示模块
void ViolationInfoList::initUi(){
    Image = new QWidget(this);
    Time = new QLabel(this);

    Image->setFixedSize(20,20);
    QPalette color;
    color.setColor(QPalette::Text,Qt::gray);
    Image->move(7,7);
    Time->move(54,27);
    Image->installEventFilter(this);
}

bool ViolationInfoList::eventFilter(QObject *obj, QEvent *event){

    if(obj == Image){
        if(event->type() == QEvent::Paint){
            QPainter painter(Image);
            painter.drawPixmap(Image->rect(),QPixmap(ImagePath));
        }
    }

    return QWidget::eventFilter(obj,event);
}




