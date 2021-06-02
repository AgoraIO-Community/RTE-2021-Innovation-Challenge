package com.kangraoo.basektlib.tools

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import java.io.StringReader
import java.io.StringWriter
import java.net.URL
import java.util.*
import java.util.regex.Pattern
import javax.xml.transform.OutputKeys
import javax.xml.transform.Source
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource
import org.json.JSONArray
import org.json.JSONObject

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/17
 * desc :String 扩展
 */
object HString {
    fun concatObject(splt: String?, vararg args: Any?): String {
        var sp = splt
        if (splt == null) {
            sp = ""
        }
        if (args.isEmpty()) {
            return ""
        }
        val ret = StringBuilder()

        args.forEach {
            if (it == null) {
                ret.append("null").append(sp)
            } else {
                ret.append(it.toString()).append(sp)
            }
        }

        var result = ret.toString()
        return result.substring(0, result.length - sp!!.length)
    }

    fun getStringValue(any: Any?): String {
        return any.toString()
    }

    fun to32UUID(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    fun to36UUID(): String {
        return UUID.randomUUID().toString()
    }

    /**
     * 数字变为百分比
     */
    fun percentString(percent: Float): String {
        return String.format(Locale.US, "%d%%", (percent * 100).toInt())
    }

    /**
     * 删除字符串中的空白符,换行符,tab
     */
    fun removeBlanks(content: String?): String? {
        if (content == null)
            return null
        else {
            val buff = StringBuilder(content)
            for ((i, chars) in content.reversed().withIndex()) {
                if (' ' == chars || ('\n' == chars) || ('\t' == chars) ||
                    ('\r' == chars)) {
                    buff.deleteCharAt(i)
                }
            }
            return buff.toString()
        }
    }

    /**
     * counter ASCII character as one, otherwise two
     *
     * @param str
     * @return count
     */
    fun counterChars(str: String): Int {
        // return
        if (TextUtils.isEmpty(str)) {
            return 0
        }
        var count = 0
        for (i in 0 until str.length) {
            val tmp = str[i].toInt()
            count += if (tmp > 0 && tmp < 127) {
                1
            } else {
                2
            }
        }
        return count
    }

    /**
     * 获取get请求网络字符串
     */
    fun getNetString(map: Map<String, String?>): String? {
        val param = StringBuilder("")
        map.forEach {
            if (it.value == null) {
                return@forEach
            }
            param.append(it.key).append('=').append(it.value).append('&')
        }
        return if (param.isNotEmpty()) param.substring(0, param.toString().length - 1) else null
    }

    /**
     * json 格式化
     *
     * @param json
     * @return
     */
    fun jsonFormat(json: String): String? {
        var json = json
        if (TextUtils.isEmpty(json)) {
            return null
        }
        json = json.trim { it <= ' ' }
        return when {
            json.startsWith("{") -> {
                val jsonObject = JSONObject(json)
                jsonObject.toString(4)
            }
            json.startsWith("[") -> {
                val jsonArray = JSONArray(json)
                jsonArray.toString(4)
            }
            else -> {
                json
            }
        }
    }

    /**
     * xml 格式化
     *
     * @param xml
     * @return
     */
    fun xmlFormat(xml: String): String? {
        if (TextUtils.isEmpty(xml)) {
            return null
        }
        val xmlInput: Source =
            StreamSource(StringReader(xml))
        val xmlOutput =
            StreamResult(StringWriter())
        val transformer =
            TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
        transformer.transform(xmlInput, xmlOutput)
        return xmlOutput.writer.toString().replaceFirst(">".toRegex(), ">\n")
    }

    fun isInvalid(text: String?, defaultText: String?): String? {
        return if (TextUtils.isEmpty(text)) defaultText else text
    }
    fun getChineseCharacter(str: String): String {
        return str.replace("\\s|\t|\\u00a0".toRegex(), "").replace("\\p{P}|\\p{S}".toRegex(), "")
    }

    fun isNet(uri: String): Boolean {
        if (uri.startsWith("http://") || uri.startsWith("https://") || uri.startsWith("ftp://")) {
            return true
        }
        return false
    }

    /**
     * 去除url指定参数
     * @param url
     * @param name
     * @return
     */
    fun removeUrlParam(url: String, vararg name: String): String {
        var url = url
        for (s in name) {
            // 使用replaceAll正则替换,replace不支持正则
            url = url.replace("&?" + s + "=[^&]*".toRegex(), "")
        }
        return url
    }

    fun removeUrlParam(url: String): String {
        val index = url.indexOf("?")
        if (index != -1) {
            return url.substring(0, index)
        }
        return url
    }

    /**
     * 获取地址host
     */
    fun host(url: String) = URL(url).host

    /**
     * 设置文字高亮.
     *
     * @param textView 需要设置的TextView.
     * @param highlightText 高亮文字.
     */
    fun setTextHighlight(
        textView: TextView,
        color: Int,
        highlightText: String?,
        highlightTextClick: ClickableSpan?
    ) {
        val text = textView.text.toString()
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(highlightText)) return
        val spannableString = SpannableString(text)
        val pattern = Pattern.compile(highlightText)
        val matcher = pattern.matcher(spannableString)
        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()
            // 设置高亮.
            spannableString.setSpan(
                ForegroundColorSpan(color),
                start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // 设置点击事件.
            if (highlightTextClick != null) spannableString.setSpan(
                highlightTextClick,
                start,
                end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = spannableString
    }

    /**
     * 设置文字高亮.
     *
     * @param textView 需要设置的TextView.
     * @param highlightText 高亮文字.
     */
    fun setTextHighlight(
        textView: TextView,
        color: Int,
        highlightText: List<String?>,
        highlightTextClick: List<ClickableSpan?>
    ) {
        val text = textView.text.toString()
        if (TextUtils.isEmpty(text) || highlightText.isEmpty()) return
        if (highlightText.size != highlightTextClick.size) return
        var highlight: String?
        val spannableString = SpannableString(text)
        for (textIndex in highlightText.indices) {
            highlight = highlightText[textIndex]
            val pattern = Pattern.compile(highlight)
            val matcher = pattern.matcher(spannableString)
            while (matcher.find()) {
                val start = matcher.start()
                val end = matcher.end()
                // 设置高亮.
                spannableString.setSpan(
                    ForegroundColorSpan(color),
                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                // 设置点击事件.
                spannableString.setSpan(
                    highlightTextClick[textIndex],
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.text = spannableString
    }
}
