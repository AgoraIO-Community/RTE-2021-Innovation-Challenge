package com.ml.doctor.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**患者列表
 * Created by gzq on 2017/11/21.
 */

public class PatientListBean implements Parcelable{
    private String exercise_habits,sfz,sex,eqid,smoke,doid,weight,
            drink,xfid,bname,dz,blood_type,tel,mh,state,eating_habits,bid,user_photo,
            age,categoryid,height;

    protected PatientListBean(Parcel in) {
        exercise_habits = in.readString();
        sfz = in.readString();
        sex = in.readString();
        eqid = in.readString();
        smoke = in.readString();
        doid = in.readString();
        weight = in.readString();
        drink = in.readString();
        xfid = in.readString();
        bname = in.readString();
        dz = in.readString();
        blood_type = in.readString();
        tel = in.readString();
        mh = in.readString();
        state = in.readString();
        eating_habits = in.readString();
        bid = in.readString();
        user_photo = in.readString();
        age = in.readString();
        categoryid = in.readString();
        height = in.readString();
    }

    public static final Creator<PatientListBean> CREATOR = new Creator<PatientListBean>() {
        @Override
        public PatientListBean createFromParcel(Parcel in) {
            return new PatientListBean(in);
        }

        @Override
        public PatientListBean[] newArray(int size) {
            return new PatientListBean[size];
        }
    };

    public String getExercise_habits() {
        return exercise_habits;
    }

    public void setExercise_habits(String exercise_habits) {
        this.exercise_habits = exercise_habits;
    }

    public String getSfz() {
        return sfz;
    }

    public void setSfz(String sfz) {
        this.sfz = sfz;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEqid() {
        return eqid;
    }

    public void setEqid(String eqid) {
        this.eqid = eqid;
    }

    public String getSmoke() {
        return smoke;
    }

    public void setSmoke(String smoke) {
        this.smoke = smoke;
    }

    public String getDoid() {
        return doid;
    }

    public void setDoid(String doid) {
        this.doid = doid;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDrink() {
        return drink;
    }

    public void setDrink(String drink) {
        this.drink = drink;
    }

    public String getXfid() {
        return xfid;
    }

    public void setXfid(String xfid) {
        this.xfid = xfid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public String getBlood_type() {
        return blood_type;
    }

    public void setBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMh() {
        return mh;
    }

    public void setMh(String mh) {
        this.mh = mh;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEating_habits() {
        return eating_habits;
    }

    public void setEating_habits(String eating_habits) {
        this.eating_habits = eating_habits;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "PatientListBean{" +
                "exercise_habits='" + exercise_habits + '\'' +
                ", sfz='" + sfz + '\'' +
                ", sex='" + sex + '\'' +
                ", eqid='" + eqid + '\'' +
                ", smoke='" + smoke + '\'' +
                ", doid='" + doid + '\'' +
                ", weight='" + weight + '\'' +
                ", drink='" + drink + '\'' +
                ", xfid='" + xfid + '\'' +
                ", bname='" + bname + '\'' +
                ", dz='" + dz + '\'' +
                ", blood_type='" + blood_type + '\'' +
                ", tel='" + tel + '\'' +
                ", mh='" + mh + '\'' +
                ", state='" + state + '\'' +
                ", eating_habits='" + eating_habits + '\'' +
                ", bid='" + bid + '\'' +
                ", user_photo='" + user_photo + '\'' +
                ", age='" + age + '\'' +
                ", categoryid='" + categoryid + '\'' +
                ", height='" + height + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(exercise_habits);
        dest.writeString(sfz);
        dest.writeString(sex);
        dest.writeString(eqid);
        dest.writeString(smoke);
        dest.writeString(doid);
        dest.writeString(weight);
        dest.writeString(drink);
        dest.writeString(xfid);
        dest.writeString(bname);
        dest.writeString(dz);
        dest.writeString(blood_type);
        dest.writeString(tel);
        dest.writeString(mh);
        dest.writeString(state);
        dest.writeString(eating_habits);
        dest.writeString(bid);
        dest.writeString(user_photo);
        dest.writeString(age);
        dest.writeString(categoryid);
        dest.writeString(height);
    }
}
