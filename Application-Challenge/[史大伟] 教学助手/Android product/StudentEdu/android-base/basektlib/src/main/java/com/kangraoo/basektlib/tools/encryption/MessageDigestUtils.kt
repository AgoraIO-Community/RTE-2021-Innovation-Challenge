package com.kangraoo.basektlib.tools.encryption

import java.io.BufferedInputStream
import java.io.FileInputStream
import java.security.MessageDigest

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/20
 * desc :
 */
object MessageDigestUtils {

    @JvmStatic
    fun md5(value: String, charSetName: String): String {
        return md5(value.toByteArray(charset(charSetName)))
    }

    @JvmStatic
    fun md5(source: ByteArray): String {
        return toDigest("MD5", source)
    }

    fun getStreamMD5(filePath: String): String {
        BufferedInputStream(FileInputStream(filePath)).use {

            val md5 = MessageDigest.getInstance("MD5")
            val buffer = ByteArray(4096)
            var numRead = 0
            while (it.read(buffer).also { k -> numRead = k } > 0) {
                md5.update(buffer, 0, numRead)
            }
            return toHex(md5.digest())
        }
    }

    /**
     * md5加密字符串
     * md5使用后转成16进制变成32个字节
     */
    @JvmStatic
    fun md5(str: String): String {
        return toDigest("MD5", str.toByteArray())
    }

    @JvmStatic
    fun sha1(str: String): String {
        return toDigest("SHA-1", str.toByteArray())
    }

    @JvmStatic
    fun sha256(str: String): String {
        return toDigest("SHA-256", str.toByteArray())
    }

    @JvmStatic
    fun sha512(str: String): String {
        return toDigest("SHA-512", str.toByteArray())
    }

    @JvmStatic
    fun sha384(str: String): String {
        return toDigest("SHA-384", str.toByteArray())
    }

    private fun toDigest(algorithm: String, source: ByteArray): String {
        val byte = MessageDigest.getInstance(algorithm).digest(source)
        return toHex(byte)
    }
    fun toHex(byte: ByteArray): String {
        return StringBuilder().also {
            byte.forEach { inIt ->
                val hex = inIt.toInt() and (0xFF)
                val hexStr = Integer.toHexString(hex)
                if (hexStr.length == 1) {
                    it.append("0").append(hexStr)
                } else {
                    it.append(hexStr)
                }
            }
        }.toString()
    }
}
