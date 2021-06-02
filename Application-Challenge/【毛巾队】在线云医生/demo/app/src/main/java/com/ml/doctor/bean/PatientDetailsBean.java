package com.ml.doctor.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by gzq on 2017/11/22.
 */

public class PatientDetailsBean implements Parcelable{
    private String zid;
    private String eq;
    private String time;
    private String temper_ature;
    private String high_pressure;
    private String low_pressure;
    private String heart_rate;
    private String blood_sugar;
    private String blood_oxygen;
    private String cholesterol;
    private String uric_acid;
    private String pulse;
    private String ecg;
    private String yz;
    private String diagnosis;
    private String sugar_time;
    private String eqid;
    private String bname;
    private String state;
    private String hl;
    private User user;

    public String getZid() {
        return zid;
    }

    public void setZid(String zid) {
        this.zid = zid;
    }

    public String getEq() {
        return eq;
    }

    public void setEq(String eq) {
        this.eq = eq;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemper_ature() {
        return temper_ature;
    }

    public void setTemper_ature(String temper_ature) {
        this.temper_ature = temper_ature;
    }

    public String getHigh_pressure() {
        return high_pressure;
    }

    public void setHigh_pressure(String high_pressure) {
        this.high_pressure = high_pressure;
    }

    public String getLow_pressure() {
        return low_pressure;
    }

    public void setLow_pressure(String low_pressure) {
        this.low_pressure = low_pressure;
    }

    public String getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(String heart_rate) {
        this.heart_rate = heart_rate;
    }

    public String getBlood_sugar() {
        return blood_sugar;
    }

    public void setBlood_sugar(String blood_sugar) {
        this.blood_sugar = blood_sugar;
    }

    public String getBlood_oxygen() {
        return blood_oxygen;
    }

    public void setBlood_oxygen(String blood_oxygen) {
        this.blood_oxygen = blood_oxygen;
    }

    public String getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getUric_acid() {
        return uric_acid;
    }

    public void setUric_acid(String uric_acid) {
        this.uric_acid = uric_acid;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getEcg() {
        return ecg;
    }

    public void setEcg(String ecg) {
        this.ecg = ecg;
    }

    public String getYz() {
        return yz;
    }

    public void setYz(String yz) {
        this.yz = yz;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getSugar_time() {
        return sugar_time;
    }

    public void setSugar_time(String sugar_time) {
        this.sugar_time = sugar_time;
    }

    public String getEqid() {
        return eqid;
    }

    public void setEqid(String eqid) {
        this.eqid = eqid;
    }

    public String getBname() {
        return bname;
    }

    public void setBname(String bname) {
        this.bname = bname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHl() {
        return hl;
    }

    public void setHl(String hl) {
        this.hl = hl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    protected PatientDetailsBean(Parcel in) {
        zid = in.readString();
        eq = in.readString();
        time = in.readString();
        temper_ature = in.readString();
        high_pressure = in.readString();
        low_pressure = in.readString();
        heart_rate = in.readString();
        blood_sugar = in.readString();
        blood_oxygen = in.readString();
        cholesterol = in.readString();
        uric_acid = in.readString();
        pulse = in.readString();
        ecg = in.readString();
        yz = in.readString();
        diagnosis = in.readString();
        sugar_time = in.readString();
        eqid = in.readString();
        bname = in.readString();
        state = in.readString();
        hl = in.readString();
        user = in.readParcelable(User.class.getClassLoader());
    }

    public static final Creator<PatientDetailsBean> CREATOR = new Creator<PatientDetailsBean>() {
        @Override
        public PatientDetailsBean createFromParcel(Parcel in) {
            return new PatientDetailsBean(in);
        }

        @Override
        public PatientDetailsBean[] newArray(int size) {
            return new PatientDetailsBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(zid);
        parcel.writeString(eq);
        parcel.writeString(time);
        parcel.writeString(temper_ature);
        parcel.writeString(high_pressure);
        parcel.writeString(low_pressure);
        parcel.writeString(heart_rate);
        parcel.writeString(blood_sugar);
        parcel.writeString(blood_oxygen);
        parcel.writeString(cholesterol);
        parcel.writeString(uric_acid);
        parcel.writeString(pulse);
        parcel.writeString(ecg);
        parcel.writeString(yz);
        parcel.writeString(diagnosis);
        parcel.writeString(sugar_time);
        parcel.writeString(eqid);
        parcel.writeString(bname);
        parcel.writeString(state);
        parcel.writeString(hl);
        parcel.writeParcelable(user, i);
    }

    static class  User implements Parcelable{
        private String bid;
        private String categoryid;
        private String doct;
        private String eq;
        private String bname;
        private String sex;
        private String dz;
        private String age;
        private String sfz;
        private String tel;
        private String mh;
        private String eqid;
        private String state;
        private String qyzt;
        private String height;
        private String weight;
        private String blood_type;
        private String eating_habits;
        private String smoke;
        private String drink;
        private String exercise_habits;
        private String user_photo;

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getCategoryid() {
            return categoryid;
        }

        public void setCategoryid(String categoryid) {
            this.categoryid = categoryid;
        }

        public String getDoct() {
            return doct;
        }

        public void setDoct(String doct) {
            this.doct = doct;
        }

        public String getEq() {
            return eq;
        }

        public void setEq(String eq) {
            this.eq = eq;
        }

        public String getBname() {
            return bname;
        }

        public void setBname(String bname) {
            this.bname = bname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getDz() {
            return dz;
        }

        public void setDz(String dz) {
            this.dz = dz;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getSfz() {
            return sfz;
        }

        public void setSfz(String sfz) {
            this.sfz = sfz;
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

        public String getEqid() {
            return eqid;
        }

        public void setEqid(String eqid) {
            this.eqid = eqid;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getQyzt() {
            return qyzt;
        }

        public void setQyzt(String qyzt) {
            this.qyzt = qyzt;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getBlood_type() {
            return blood_type;
        }

        public void setBlood_type(String blood_type) {
            this.blood_type = blood_type;
        }

        public String getEating_habits() {
            return eating_habits;
        }

        public void setEating_habits(String eating_habits) {
            this.eating_habits = eating_habits;
        }

        public String getSmoke() {
            return smoke;
        }

        public void setSmoke(String smoke) {
            this.smoke = smoke;
        }

        public String getDrink() {
            return drink;
        }

        public void setDrink(String drink) {
            this.drink = drink;
        }

        public String getExercise_habits() {
            return exercise_habits;
        }

        public void setExercise_habits(String exercise_habits) {
            this.exercise_habits = exercise_habits;
        }

        public String getUser_photo() {
            return user_photo;
        }

        public void setUser_photo(String user_photo) {
            this.user_photo = user_photo;
        }

        protected User(Parcel in) {
            bid = in.readString();
            categoryid = in.readString();
            doct = in.readString();
            eq = in.readString();
            bname = in.readString();
            sex = in.readString();
            dz = in.readString();
            age = in.readString();
            sfz = in.readString();
            tel = in.readString();
            mh = in.readString();
            eqid = in.readString();
            state = in.readString();
            qyzt = in.readString();
            height = in.readString();
            weight = in.readString();
            blood_type = in.readString();
            eating_habits = in.readString();
            smoke = in.readString();
            drink = in.readString();
            exercise_habits = in.readString();
            user_photo = in.readString();
        }

        public static final Creator<User> CREATOR = new Creator<User>() {
            @Override
            public User createFromParcel(Parcel in) {
                return new User(in);
            }

            @Override
            public User[] newArray(int size) {
                return new User[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(bid);
            parcel.writeString(categoryid);
            parcel.writeString(doct);
            parcel.writeString(eq);
            parcel.writeString(bname);
            parcel.writeString(sex);
            parcel.writeString(dz);
            parcel.writeString(age);
            parcel.writeString(sfz);
            parcel.writeString(tel);
            parcel.writeString(mh);
            parcel.writeString(eqid);
            parcel.writeString(state);
            parcel.writeString(qyzt);
            parcel.writeString(height);
            parcel.writeString(weight);
            parcel.writeString(blood_type);
            parcel.writeString(eating_habits);
            parcel.writeString(smoke);
            parcel.writeString(drink);
            parcel.writeString(exercise_habits);
            parcel.writeString(user_photo);
        }
    }
}
