/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package org.lql.movie_together.ui.face.callback;


import org.lql.movie_together.ui.face.model.LivenessModel;

/**
 * 人脸检测回调接口。
 *
 * @Time: 2019/1/25
 * @Author: v_chaixiaogang
 */
public interface FaceDetectCallBack {
    public void onFaceDetectCallback(LivenessModel livenessModel);

    public void onTip(int code, String msg);

    void onFaceDetectDarwCallback(LivenessModel livenessModel);
}
