/*
 * Copyright (C) 2019 Baidu, Inc. All Rights Reserved.
 */
package org.lql.movie_together.ui.face.callback;


/**
 * 人脸特征抽取回调接口。
 *
 * @Time: 2019/5/30
 * @Author: v_zhangxiaoqing01
 */
public interface FaceFeatureCallBack {

    public void onFaceFeatureCallBack(float featureSize, byte[] feature, long time);

}
