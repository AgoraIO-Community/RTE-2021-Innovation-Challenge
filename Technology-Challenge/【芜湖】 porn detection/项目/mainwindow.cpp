#include "mainwindow.h"
#include "ui_mainwindow.h"
#include "ui_msgitem.h"
#include"ui_dialog.h"
#include<io.h>
#include<QListWidgetItem>


MainWindow::MainWindow(QWidget *parent)
    : QMainWindow(parent)
    , ui(new Ui::MainWindow)
{
    ui->setupUi(this);



    //connect(this,&MainWindow::slot_add,mMsgItem,&MsgItem::signal_add);
}

MainWindow::~MainWindow()
{
    delete ui;

}

void MainWindow::on_UI_ConnectChannel_clicked()
{
        dialog = new Dialog(this);
       dialog->setModal(false);
        dialog->show();

        //CAgoraOriginalVideoDlg *mAgora = new CAgoraOriginalVideoDlg();
        //mAgora->InitAgora();
        //mAgora->RenderLocalVideo((HWND)(ui->widget->winId()));
        //mAgora->RegisterVideoFrameObserver(true,&mAgora->m_averageFilterVideoFrameObserver);
        //mAgora->RegisterVideoFrameObserver(true,&mAgora->m_ViolationObserver);

        //会因为没有实体对象报错的方式
        mDemo->setLocalVideoWnd((HWND)(dialog->ui->UI_LocalVideo->winId()));
        //mDemo->getEventHandlerObj()->setRenderVideoHwd((HWND)(dialog->ui->UI_RenderVideo->winId()));
        //mDemo->setLocalVideoWnd((HWND)(ui->UI_LocalVideo->winId()));
        //mDemo->getEventHandlerObj()->setRenderVideoHwd((HWND)(ui->UI_RenderVideo->winId()));
        mDemo->InitAgora();
        mDemo->getEventHandlerObj()->setRtcEngine(mDemo->getMRtcEngine());
        mDemo->RenderLocalVideo();
        mDemo->RegisterVideoFrameObserver(true,&mDemo->m_ViolationObserver);

        QString tempChannelName = ui->UI_ChannelName->text();
        if(mDemo->JoinChannel((char*)tempChannelName.data(),0,AppID)){
            ui->UI_ChannelAddr->hide();
            ui->UI_ChannelName->hide();
            ui->UI_ConnectChannel->hide();
           // ui->UI_LocalVideo->show();
            //ui->UI_RenderVideo->show();

            //ui->UI_ViolationFont->show();
           // ui->UI_listWidget->show();
           // ui->UI_ViolationFont->show();
        }

}


