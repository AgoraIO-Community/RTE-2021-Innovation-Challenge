package io.agora.education.api.record.data

enum class RecordState(val value: Int) {
    RECORDING(1),
    FINISHED(2),
    WAIT_DOWNLOAD(3),
    WAIT_CONVERT(4),
    WAIT_UPLOAD(5)
}

data class EduRecordInfo(
        var appId: String,
        var roomUuid: String,
        var recordId: String,
        var recordState: RecordState,
        var recordingTime: Long
)
