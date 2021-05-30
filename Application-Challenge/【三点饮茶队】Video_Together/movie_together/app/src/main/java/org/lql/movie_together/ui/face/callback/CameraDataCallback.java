package org.lql.movie_together.ui.face.callback;

import android.hardware.Camera;

/**
 * Time: 2019/1/25
 * Author: v_chaixiaogang
 * Description: camera1数据结果回调
 */
public interface CameraDataCallback {
    /**
     * @param data   预览数据
     * @param camera 相机设备
     * @param width  预览宽
     * @param height 预览高
     */
    void onGetCameraData(byte[] data, Camera camera, int width, int height);
}
