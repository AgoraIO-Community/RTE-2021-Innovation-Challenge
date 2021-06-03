package io.agora.education.api.record.listener

import io.agora.education.api.record.data.EduRecordInfo

interface EduRecordEventListener {
    fun onRecordStarted(record: EduRecordInfo)

    fun onRecordEnded(record: EduRecordInfo)
}
