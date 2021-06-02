#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include<QStandardItemModel>
#include <QWidget>
#include <QMouseEvent>

#include"Demo_IntegratedClass.h"
#include"CAgoraOriginalVideoDlg.h"

#include"dialog.h"

static int oldImageNum =0;

QT_BEGIN_NAMESPACE
namespace Ui { class MainWindow; }
QT_END_NAMESPACE

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    MainWindow(QWidget *parent = nullptr);
    ~MainWindow();

public:
    int showFolderImage(QString filepath);

private slots:
    void on_UI_ConnectChannel_clicked();

private:
    Ui::MainWindow *ui;
    Demo_VidoDlag *mDemo = new Demo_VidoDlag();
    MsgItem *mMsgItem;
    Dialog *dialog;
};




#endif // MAINWINDOW_H
