#include <jni.h>
#include <oboe/oboe.h>
#include "common.h"

#include <cmath>

using namespace oboe;

class AudioPlayer : public AudioStreamDataCallback, public AudioStreamErrorCallback {

public:
    ~AudioPlayer() override = default;

    AudioPlayer(int channelCount, int samplesPreFrames, int sampleRate);

    int32_t writeData(void *buffer, int32_t numFrames);

    void startAudio();

    void stopAudio();

private:
    std::mutex mLock;
    std::shared_ptr<oboe::AudioStream> mStream;

    int channelCount;
    int samplesPerFrames;
    int sampleRate;

    bool onError(AudioStream *stream, Result res) override;

    void onErrorBeforeClose(AudioStream *audioStream, Result error) override;

    DataCallbackResult
    onAudioReady(AudioStream *audioStream, void *audioData, int32_t numFrames) override;
};

AudioPlayer::AudioPlayer(int channelCount, int samplesPreFrames, int sampleRate) :
        channelCount(channelCount),
        sampleRate(sampleRate),
        samplesPerFrames(samplesPreFrames) {}

DataCallbackResult AudioPlayer::onAudioReady(
        AudioStream *audioStream,
        void *audioData,
        int32_t numFrames) {
    LOGI("%d", numFrames);
    return DataCallbackResult::Continue;
}

void AudioPlayer::startAudio() {
    std::lock_guard<std::mutex> lock(mLock);

    AudioStreamBuilder builder;

    auto res = builder.setSharingMode(SharingMode::Shared)
            ->setDirection(Direction::Output)
            ->setPerformanceMode(PerformanceMode::None)
            ->setChannelCount(channelCount)
            ->setSampleRate(sampleRate)
            ->setSampleRateConversionQuality(SampleRateConversionQuality::Medium)
            ->setFormat(AudioFormat::I16)
            ->setErrorCallback(this)
//            ->setDataCallback(this)
            ->openStream(mStream);
    if (res != Result::OK) {
        LOGE("create audio stream error");
        return;
    }
    mStream->setBufferSizeInFrames(samplesPerFrames);
    res = mStream->requestStart();
    if (res != Result::OK) {
        LOGE("start audio stream error");
    } else {
        LOGI("start audio successful");
    }
}

void AudioPlayer::stopAudio() {
    std::lock_guard<std::mutex> lock(mLock);
    LOGI("stop stream");

    if (mStream) {
        mStream->stop();
        mStream->close();
        mStream.reset();
    }

}

int32_t AudioPlayer::writeData(void *buffer, int32_t numFrames) {
    std::lock_guard<std::mutex> lock(mLock);
    if (mStream) {
        auto res = mStream->write(buffer, numFrames, 0);
        if (res == Result::OK) {
            return res.value();
        } else {
            LOGE("oboe error %s", convertToText(res.error()));
        }
    }
    return 0;
}

bool AudioPlayer::onError(AudioStream *stream, Result res) {
    LOGE("oboe error %s", convertToText(res));
    return AudioStreamErrorCallback::onError(stream, res);
}

void AudioPlayer::onErrorBeforeClose(AudioStream *audioStream, Result error) {
    LOGE("oboe error %s", convertToText(error));
    AudioStreamErrorCallback::onErrorBeforeClose(audioStream, error);
}

extern "C"
JNIEXPORT jlong JNICALL
Java_com_hustunique_resonance_1audio_AudioPlayer_nCreateEngine(
        JNIEnv *env, jobject thiz,
        jint channels,
        jint samples_pre_frames,
        jint sample_rate) {
    return reinterpret_cast<jlong>(new AudioPlayer(channels, samples_pre_frames, sample_rate));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hustunique_resonance_1audio_AudioPlayer_nReleaseEngine(
        JNIEnv *env, jobject thiz,
        jlong handler) {
    delete reinterpret_cast<AudioPlayer *>(handler);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hustunique_resonance_1audio_AudioPlayer_nStartAudio(
        JNIEnv *env, jobject thiz,
        jlong handler) {
    reinterpret_cast<AudioPlayer *>(handler)->startAudio();
}

extern "C"
JNIEXPORT void JNICALL
Java_com_hustunique_resonance_1audio_AudioPlayer_nStopAudio(
        JNIEnv *env, jobject thiz,
        jlong handler) {
    reinterpret_cast<AudioPlayer *>(handler)->stopAudio();
}

extern "C"
JNIEXPORT jint JNICALL
Java_com_hustunique_resonance_1audio_AudioPlayer_nWriteData(
        JNIEnv *env, jobject thiz,
        jlong handler, jobject buffer,
        jint num_frames) {
    auto directBuffer = static_cast<int16_t *>(env->GetDirectBufferAddress(buffer));
    return reinterpret_cast<AudioPlayer *>(handler)->writeData(directBuffer, num_frames);
}
