/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package org.lql.movie_together.ui.face.model;


import com.baidu.idl.main.facesdk.FaceInfo;
import com.baidu.idl.main.facesdk.model.BDFaceImageInstance;

public class LivenessModel {

    private int faceDetectCode;
    private FaceInfo faceInfo;
    private long rgbDetectDuration;
    private long irDetectDuration;
    private long rgbLivenessDuration;
    private float irLivenessScore;
    private long irLivenessDuration;
    private long depthtLivenessDuration;
    private float rgbLivenessScore;
    private float depthLivenessScore;
    private int liveType;
    private float[] landmarks;
    private byte[] feature;
    private int trackStatus;

    public int getTrackStatus() {
        return trackStatus;
    }

    public void setTrackStatus(int trackStatus) {
        this.trackStatus = trackStatus;
    }

    private float featureScore;
    private float featureCode;
    private long featureDuration;
    private long checkDuration;
    private BDFaceImageInstance bdFaceImageInstance;
    private BDFaceImageInstance bdFaceImageInstanceCrop;

    private User user;

    private int[] shape;

    private FaceInfo[] trackFaceInfo;
    private long allDetectDuration;

    private float maskScore;
    private long maskScoreDuration;

    public long getMaskScoreDuration() {
        return maskScoreDuration;
    }

    public void setMaskScoreDuration(long maskScoreDuration) {
        this.maskScoreDuration = maskScoreDuration;
    }

    public float getMaskScore() {
        return maskScore;
    }

    public void setMaskScore(float maskScore) {
        this.maskScore = maskScore;
    }

    public BDFaceImageInstance getBdFaceImageInstance() {
        return bdFaceImageInstance;
    }

    public void setBdFaceImageInstance(BDFaceImageInstance bdFaceImageInstance) {
        this.bdFaceImageInstance = bdFaceImageInstance;
    }

    public BDFaceImageInstance getBdFaceImageInstanceCrop() {
        return bdFaceImageInstanceCrop;
    }

    public void setBdFaceImageInstanceCrop(BDFaceImageInstance bdFaceImageInstance) {
        this.bdFaceImageInstanceCrop = bdFaceImageInstance;
    }

    public long getAllDetectDuration() {
        return allDetectDuration;
    }

    public void setAllDetectDuration(long allDetectDuration) {
        this.allDetectDuration = allDetectDuration;
    }

    public byte[] getFeature() {
        return feature;
    }

    public void setFeature(byte[] feature) {
        this.feature = feature;
    }

    public float[] getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(float[] landmarks) {
        this.landmarks = landmarks;
    }

    public int[] getShape() {
        return shape;
    }

    public void setShape(int[] shape) {
        this.shape = shape;
    }


    public FaceInfo[] getTrackFaceInfo() {
        return trackFaceInfo;
    }

    public void setTrackFaceInfo(FaceInfo[] trackFaceInfo) {
        this.trackFaceInfo = trackFaceInfo;
    }


    public int getFaceDetectCode() {
        return faceDetectCode;
    }

    public void setFaceDetectCode(int faceDetectCode) {
        this.faceDetectCode = faceDetectCode;
    }

    public FaceInfo getFaceInfo() {
        return faceInfo;
    }

    public void setFaceInfo(FaceInfo faceInfo) {
        this.faceInfo = faceInfo;
    }

    public long getRgbDetectDuration() {
        return rgbDetectDuration;
    }

    public void setRgbDetectDuration(long rgbDetectDuration) {
        this.rgbDetectDuration = rgbDetectDuration;
    }

    public long getIrDetectDuration() {
        return irDetectDuration;
    }

    public void setIrDetectDuration(long irDetectDuration) {
        this.irDetectDuration = irDetectDuration;
    }

    public long getRgbLivenessDuration() {
        return rgbLivenessDuration;
    }

    public void setRgbLivenessDuration(long rgbLivenessDuration) {
        this.rgbLivenessDuration = rgbLivenessDuration;
    }

    public float getIrLivenessScore() {
        return irLivenessScore;
    }

    public void setIrLivenessScore(float irLivenessScore) {
        this.irLivenessScore = irLivenessScore;
    }

    public long getIrLivenessDuration() {
        return irLivenessDuration;
    }

    public void setIrLivenessDuration(long irLivenessDuration) {
        this.irLivenessDuration = irLivenessDuration;
    }

    public long getDepthtLivenessDuration() {
        return depthtLivenessDuration;
    }

    public void setDepthtLivenessDuration(long depthtLivenessDuration) {
        this.depthtLivenessDuration = depthtLivenessDuration;
    }

    public float getRgbLivenessScore() {
        return rgbLivenessScore;
    }

    public void setRgbLivenessScore(float rgbLivenessScore) {
        this.rgbLivenessScore = rgbLivenessScore;
    }

    public float getDepthLivenessScore() {
        return depthLivenessScore;
    }

    public void setDepthLivenessScore(float depthLivenessScore) {
        this.depthLivenessScore = depthLivenessScore;
    }

    public int getLiveType() {
        return liveType;
    }

    public void setLiveType(int liveType) {
        this.liveType = liveType;
    }

    public float getFeatureScore() {
        return featureScore;
    }

    public void setFeatureScore(float featureScore) {
        this.featureScore = featureScore;
    }

    public long getFeatureDuration() {
        return featureDuration;
    }

    public void setFeatureDuration(long featureDuration) {
        this.featureDuration = featureDuration;
    }

    public long getCheckDuration() {
        return checkDuration;
    }

    public void setCheckDuration(long checkDuration) {
        this.checkDuration = checkDuration;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public float getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(float featureCode) {
        this.featureCode = featureCode;
    }
}


