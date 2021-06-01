#include <jni.h>
#include "common.h"
#include "resonance_audio_api.h"
#include "binaural_surround_renderer.h"


using namespace vraudio;

extern "C"
JNIEXPORT jlong JNICALL
Java_com_hustunique_resonance_1audio_AudioRender_nCreateRenderEngine(
        JNIEnv *env, jobject thiz,
        jint num_channels,
        jint frames_per_buffer,
        jint sample_rate_hz) {
    auto api = CreateResonanceAudioApi(
            num_channels, frames_per_buffer,
            sample_rate_hz);
    api->EnableRoomEffects(false);
    api->SetMasterVolume(1);
    return reinterpret_cast<jlong>(api);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hustunique_resonance_1audio_AudioRender_nReleaseRenderEngine(
        JNIEnv *env, jobject thiz,
        jlong handler) {

    delete reinterpret_cast<ResonanceAudioApi *>(handler);
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_hustunique_resonance_1audio_AudioRender_nCreateSoundsSource(
        JNIEnv *env, jobject thiz,
        jlong handler) {
    auto api = reinterpret_cast<ResonanceAudioApi *>(handler);
    auto sourceId = api->CreateSoundObjectSource(RenderingMode::kBinauralMediumQuality);
    api->SetSoundObjectListenerDirectivity(sourceId, 0.5, 2);
    api->SetSourceDistanceModel(sourceId, DistanceRolloffModel::kLogarithmic, 1, 50);
    return sourceId;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hustunique_resonance_1audio_AudioRender_nReleaseSoundsSource(
        JNIEnv *env, jobject thiz,
        jlong handler,
        jint source_id) {
    auto api = reinterpret_cast<ResonanceAudioApi *>(handler);
    api->DestroySource(source_id);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hustunique_resonance_1audio_AudioRender_nSetSourcePosition(
        JNIEnv *env, jobject thiz,
        jlong handler, jint source_id,
        jfloat x, jfloat y, jfloat z) {
    auto api = reinterpret_cast<ResonanceAudioApi *>(handler);
    api->SetSourcePosition(source_id, x, y, z);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hustunique_resonance_1audio_AudioRender_nSetSourceRotation(
        JNIEnv *env, jobject thiz,
        jlong handler, jint source_id,
        jfloat x, jfloat y, jfloat z,
        jfloat w) {
    auto api = reinterpret_cast<ResonanceAudioApi *>(handler);
    api->SetSourceRotation(source_id, x, y, z, w);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hustunique_resonance_1audio_AudioRender_nSetHeadPosition(
        JNIEnv *env, jobject thiz,
        jlong handler, jfloat x, jfloat y,
        jfloat z) {
    auto api = reinterpret_cast<ResonanceAudioApi *>(handler);
    api->SetHeadPosition(x, y, z);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hustunique_resonance_1audio_AudioRender_nSetHeadRotation(
        JNIEnv *env, jobject thiz,
        jlong handler, jfloat x, jfloat y,
        jfloat z, jfloat w) {
    auto api = reinterpret_cast<ResonanceAudioApi *>(handler);
    api->SetHeadRotation(x, y, z, w);

}

extern "C"
JNIEXPORT jboolean JNICALL
Java_com_hustunique_resonance_1audio_AudioRender_nGetOutputData(
        JNIEnv *env, jobject thiz,
        jlong handler, jobject buffer,
        jint num_channels,
        jint num_frames) {
    auto directBuffer = static_cast<int16 *>(env->GetDirectBufferAddress(buffer));
    return reinterpret_cast<ResonanceAudioApi *>(handler)->FillInterleavedOutputBuffer(
            num_channels,
            num_frames,
            directBuffer);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_hustunique_resonance_1audio_AudioRender_nSetInputData(
        JNIEnv *env, jobject thiz,
        jlong handler, jint source_id,
        jobject buffer, jint num_channels,
        jint num_frames) {
    auto directBuffer = static_cast<int16 *>(env->GetDirectBufferAddress(buffer));
    reinterpret_cast<ResonanceAudioApi *>(handler)->SetInterleavedBuffer(
            source_id, directBuffer,
            num_channels,
            num_frames);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hustunique_resonance_1audio_AudioRender_nSetInputDataArray(
        JNIEnv *env, jobject thiz,
        jlong handler, jint source_id,
        jbyteArray array,
        jint num_channels,
        jint num_frames) {
    auto data = reinterpret_cast<int16 *>(env->GetByteArrayElements(array, nullptr));
    reinterpret_cast<ResonanceAudioApi *>(handler)->SetInterleavedBuffer(
            source_id, data,
            num_channels,
            num_frames);
}