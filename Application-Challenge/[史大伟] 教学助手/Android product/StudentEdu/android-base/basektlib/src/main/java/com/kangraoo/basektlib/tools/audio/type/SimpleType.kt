package com.kangraoo.basektlib.tools.audio.type

import com.kangraoo.basektlib.tools.UTime
import com.kangraoo.basektlib.tools.audio.IAudioType
import com.kangraoo.basektlib.tools.store.file.AttachmentStore
import com.kangraoo.basektlib.tools.store.filestorage.StorageType
import com.kangraoo.basektlib.tools.store.filestorage.UStorage
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/09/10
 * desc :
 * version: 1.0
 */
class SimpleType : IAudioType {
    override fun toType(fileList: List<File>): File? {
        // 创建音频文件,合并的文件放这里
        if (fileList.size == 1) {
            return fileList[0]
        }
        var path = UStorage.getWritePath(UTime.currentTimeMillis().toString() + ".amr", StorageType.TYPE_AUDIO)
        if (path != null) {
            var res = AttachmentStore.create(path)
            return FileOutputStream(res).use {
                // list里面为暂停录音 所产生的 几段录音文件的名字，中间几段文件的减去前面的6个字节头文件
                for (i in fileList.indices) {
                    val file = fileList[i]

                    FileInputStream(file).use { fileit ->
                        val myByte = ByteArray(fileit.available())
                        // 文件长度
                        val length = myByte.size
                        // 头文件
                        if (i == 0) {
                            while (fileit.read(myByte) != -1) {
                                it.write(myByte, 0, length)
                            }
                        } else {
                            while (fileit.read(myByte) != -1) {
                                it.write(myByte, 6, length - 6)
                            }
                        }
                    }
                }

                for (i in fileList.indices) {
                    val file = fileList[i]
                    if (file.exists()) {
                        file.delete()
                    }
                }
                res
            }
        }
        return null
    }
}
