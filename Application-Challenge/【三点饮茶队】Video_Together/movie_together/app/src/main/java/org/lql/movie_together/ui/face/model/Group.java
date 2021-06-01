/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package org.lql.movie_together.ui.face.model;

/**
 * 组实体类
 */
@Deprecated
public class Group {
    private String groupId = "";
    private String desc = "";
    private long ctime;

    private boolean isChecked;  // 用于多选框

    public Group() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
