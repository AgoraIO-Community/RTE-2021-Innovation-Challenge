package io.agora.openlive.entitymodel;

public class Has {
    long time;
    double dis;

    public Has(long time, double dis) {
        this.time = time;
        this.dis = dis;
    }

    public long getTime() {
        return time;
    }

    public double getDis() {
        return dis;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setDis(double dis) {
        this.dis = dis;
    }
}
