package com.kangraoo.basektlib.tools.check

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/20
 * desc :
 */
object UPhoneCheck {

    /**
     * 大陆号码或香港号码均可
     */
    fun isPhoneLegal(str: String): Boolean {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str)
    }
    /**
     * 判断字符串是否符合手机号码格式
     * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
     * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
     * 电信号段: 133,149,153,170,173,177,180,181,189
     */

    fun isChinaPhoneLegal(str: String): Boolean {
        // "[1]"代表下一位为数字可以是几，"[0-9]"代表可以为0-9中的一个，"[5,7,9]"表示可以是5,7,9中的任意一位,[^4]表示除4以外的任何一个,\\d{9}"代表后面是可以是0～9的数字，有9位。
        return str matches (Regex("^((13[0-9])|(14[0,1,4-8])|(15[^4])|(16[2,5,6,7])|(17[^9])|(18[0-9])|(19[^4]))\\d{8}\$"))
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    fun isHKPhoneLegal(str: String): Boolean {
        return str matches (Regex("^(5|6|8|9)\\d{7}$"))
    }
}
