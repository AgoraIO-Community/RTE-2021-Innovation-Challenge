package com.kangraoo.basektlib.tools

import java.util.*
import java.util.regex.Pattern

/******************** 正则相关常量 ********************/
/******************** 正则相关常量  */
/**
 * 正则：手机号（简单）
 */
const val REGEX_MOBILE_SIMPLE = "^[1]\\d{10}$"

/**
 * 正则：手机号（精确）
 *
 * 移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188
 *
 * 联通：130、131、132、145、155、156、175、176、185、186
 *
 * 电信：133、153、173、177、180、181、189
 *
 * 全球星：1349
 *
 * 虚拟运营商：170
 */
const val REGEX_MOBILE_EXACT =
    "^((13[0-9])|(14[0,1,4-8])|(15[^4])|(16[2,5,6,7])|(17[^9])|(18[0-9])|(19[^4]))\\d{8}$"

/**
 * 正则：电话号码
 */
const val REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}"

/**
 * 正则：身份证号码15位
 */
const val REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$"

/**
 * 正则：身份证号码18位
 */
const val REGEX_ID_CARD18 =
    "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$"

/**
 * 正则：邮箱
 */
const val REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"

/**
 * 正则：URL
 */
const val REGEX_URL = "[a-zA-z]+://[^\\s]*"

/**
 * 正则：汉字
 */
const val REGEX_ZH = "^[\\u4e00-\\u9fa5]+$"

/**
 * 正则：用户名，取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
 */
const val REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{1,8}(?<!_)$"

/**
 * 正则: 密码
 */
// public static final String REGEX_PWD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$";
const val REGEX_PWD =
    "(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{8,20}$"

/**
 * 正则: 支付宝账户
 */
const val REGES_ALI_ACCOUNT = "/^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+|\\d{9,11}$/"

/**
 * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
 */
const val REGEX_DATE =
    "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$"

/**
 * 正则：IP地址
 */
const val REGEX_IP =
    "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)"

/************** 以下摘自http://tool.oschina.net/regex **************/
/************** 以下摘自http://tool.oschina.net/regex  */
/**
 * 正则：双字节字符(包括汉字在内)
 */
const val REGEX_DOUBLE_BYTE_CHAR = "[^\\x00-\\xff]"

/**
 * 正则：空白行
 */
const val REGEX_BLANK_LINE = "\\n\\s*\\r"

/**
 * 正则：QQ号
 */
const val REGEX_TENCENT_NUM = "[1-9][0-9]{4,}"

/**
 * 正则：中国邮政编码
 */
const val REGEX_ZIP_CODE = "[1-9]\\d{5}(?!\\d)"

/**
 * 正则：正整数
 */
const val REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$"

/**
 * 正则：负整数
 */
const val REGEX_NEGATIVE_INTEGER = "^-[1-9]\\d*$"

/**
 * 正则：整数
 */
const val REGEX_INTEGER = "^-?[1-9]\\d*$"

/**
 * 正则：非负整数(正整数 + 0)
 */
const val REGEX_NOT_NEGATIVE_INTEGER = "^[1-9]\\d*|0$"

/**
 * 正则：非正整数（负整数 + 0）
 */
const val REGEX_NOT_POSITIVE_INTEGER = "^-[1-9]\\d*|0$"

/**
 * 正则：正浮点数
 */
const val REGEX_POSITIVE_FLOAT = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$"

/**
 * 正则：负浮点数
 */
const val REGEX_NEGATIVE_FLOAT = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$"

/**
 * 定义HTML标签的正则表达式
 */
const val REGEX_HTML = "<[^>]+>"

object URegex {

    /**
     * 验证身份证号码15位
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isIDCard15(input: CharSequence?): Boolean {
        return isMatch(REGEX_ID_CARD15, input)
    }

    /**
     * 验证身份证号码18位
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isIDCard18(input: CharSequence?): Boolean {
        return isMatch(REGEX_ID_CARD18, input)
    }

    /**
     * 验证邮箱
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isEmail(input: CharSequence?): Boolean {
        return isMatch(REGEX_EMAIL, input)
    }

    /**
     * 验证URL
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isURL(input: CharSequence?): Boolean {
        return isMatch(REGEX_URL, input)
    }

    /**
     * 验证汉字
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isZh(input: CharSequence?): Boolean {
        return isMatch(REGEX_ZH, input)
    }

    /**
     * 验证密码格式 密码必须包含：数字、字母、符号等，且长度不得小于8位数
     * "((?=.*\\d)(?=.*\\D)|(?=.*[a-zA-Z])(?=.*[^a-zA-Z]))^.{8,16}$";//8-16位的数字、字母、字符至少包含两种
     *
     *
     * "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";//6-20位的数字、字母
     */
    fun isPwd(password: String?): Boolean {
        return isMatch(REGEX_PWD, password)
    }

    /**
     * 验证IP地址
     *
     * @param input 待验证文本
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isIP(input: CharSequence?): Boolean {
        return isMatch(REGEX_IP, input)
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isMatch(regex: String?, input: CharSequence?): Boolean {
        return input != null && input.length > 0 && Pattern.matches(
            regex,
            input
        )
    }

    /**
     * 获取正则匹配的部分
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return 正则匹配的部分
     */
    fun getMatches(
        regex: String?,
        input: CharSequence?
    ): List<String>? {
        if (input == null) return null
        val matches: MutableList<String> =
            ArrayList()
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(input)
        while (matcher.find()) {
            matches.add(matcher.group())
        }
        return matches
    }

    /**
     * 获取正则匹配分组
     *
     * @param input 要分组的字符串
     * @param regex 正则表达式
     * @return 正则匹配分组
     */
    fun getSplits(input: String?, regex: String): Array<String?>? {
        return input?.split(regex.toRegex())?.toTypedArray()
    }

    /**
     * 替换正则匹配的第一部分
     *
     * @param input 要替换的字符串
     * @param regex 正则表达式
     * @param replacement 代替者
     * @return 替换正则匹配的第一部分
     */
    fun getReplaceFirst(
        input: String?,
        regex: String?,
        replacement: String?
    ): String? {
        return if (input == null) null else Pattern.compile(regex).matcher(input)
            .replaceFirst(replacement)
    }

    /**
     * 替换所有正则匹配的部分
     *
     * @param input 要替换的字符串
     * @param regex 正则表达式
     * @param replacement 代替者
     * @return 替换所有正则匹配的部分
     */
    fun getReplaceAll(
        input: String?,
        regex: String?,
        replacement: String?
    ): String? {
        return if (input == null) null else Pattern.compile(regex).matcher(input)
            .replaceAll(replacement)
    }

    /**
     * 身份证中间8位隐藏
     * 隐藏出生年月
     *
     * @param idCard 身份证号
     */
    fun hideIDCard(idCard: String): String? {
        return idCard.replace("(\\d{6})\\d{8}(\\w{4})".toRegex(), "$1*****$2")
    }

    /**
     * 手机号中间四位隐藏
     *
     * @param phone 手机号
     */
    fun hidePhoneNum(phone: String): String? {
        return phone.replace("(\\d{3})\\d{4}(\\d{4})".toRegex(), "$1****$2")
    }
}
