package com.kangraoo.basektlib.tools.proxy

import android.text.TextUtils
import com.danikula.videocache.ProxyCacheUtils
import com.danikula.videocache.file.FileNameGenerator
import com.kangraoo.basektlib.tools.HString

const val MAX_EXTENSION_LENGTH = 4

class Md5VideoNameGenerator : FileNameGenerator {
    override fun generate(url: String?): String {
        val extension = getExtension(url)
        val name = ProxyCacheUtils.computeMD5(HString.removeUrlParam(url!!))
        return if (TextUtils.isEmpty(extension)) name else "$name.$extension"
    }

    private fun getExtension(url: String?): String {
        return if (url != null) {
            val dotIndex = url.lastIndexOf('.')
            val slashIndex = url.lastIndexOf('/')
            if (dotIndex != -1 && dotIndex > slashIndex && dotIndex + 2 + MAX_EXTENSION_LENGTH > url.length) url.substring(
                dotIndex + 1,
                url.length
            ) else ""
        } else {
            ""
        }
    }
}
