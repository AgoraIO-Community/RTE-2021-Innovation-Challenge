#ifndef DIALOG_H
#define DIALOG_H

#include <QDialog>
#include<QListWidgetItem>


namespace Ui {
class MsgItem;
}

class MsgItem : public QWidget
{
    Q_OBJECT
     //可按照下面的方法自己添加需要的功能

     Q_PROPERTY(QString header READ header WRITE setHeader DESIGNABLE true)
     Q_PROPERTY(QString datetime READ datetime WRITE setDatetime DESIGNABLE true)


public:
     explicit MsgItem(QWidget *parent = 0);
     ~MsgItem();

     QString header() const;
     void setHeader(const QString& header);

     QString datetime() const;
     void setDatetime(const QString& datetime);
     void setData(const QString& header,const QString& time );


private slots:
          //void on_btnDelete();

signals:
          void signal_add(std::string header,std::string datatime);
          void signal_delete(QWidget*);

private:
          Ui::MsgItem *ui;
          QString m_header{""};
          QString m_uuid{""};
};

namespace Ui {
class Dialog;
}

class Dialog : public QDialog
{
    Q_OBJECT
public:
    explicit Dialog(QWidget *parent = nullptr);
    ~Dialog();

public:
   // void UpdataItem(std::string header,std::string time);

    void signal_addItem(const std::string header,const std::string );
    void signal_delItem();

public:
    Ui::Dialog *ui;
};

#endif // DIALOG_H
