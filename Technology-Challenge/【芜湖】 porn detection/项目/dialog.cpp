#include "dialog.h"
#include "ui_dialog.h"
#include"ui_msgitem.h"


Dialog::Dialog(QWidget *parent) :
    QDialog(parent),
    ui(new Ui::Dialog)
{
    ui->setupUi(this);
}

Dialog::~Dialog()
{
    delete ui;
}


MsgItem::MsgItem(QWidget *parent) : QWidget(parent), ui(new Ui::MsgItem)
{
    ui->setupUi(this);
    setWindowFlags( Qt::FramelessWindowHint/* | Qt::WindowStaysOnTopHint| Qt::WindowSystemMenuHint*/  | Qt::SubWindow );
    setAttribute( Qt::WA_TranslucentBackground, true );

}

MsgItem::~MsgItem()
{
    delete ui;
}


QString MsgItem::header() const
{
    return m_header;
}

void MsgItem::setHeader(const QString& header)
{
    m_header = header;
    ui->widgetHead->setStyleSheet(QString("border-image: url(%1);").arg(header));
}

QString MsgItem::datetime() const
{
    return ui->lbTime->text();
}

void MsgItem::setDatetime(const QString& datetime)
{
    ui->lbTime->setText(datetime);
}

void MsgItem::setData(const QString& header, const QString& time )
{
    setHeader(header);

    setDatetime(time);
}


void Dialog::signal_addItem(std::string head,std::string time){

    auto pMsgItem = new MsgItem();
    QListWidgetItem *item = new QListWidgetItem;
    item->setSizeHint( QSize(360, 70));

    pMsgItem->setHeader(QString::fromStdString(head));
    pMsgItem->setDatetime(QString::fromStdString(time));

}


