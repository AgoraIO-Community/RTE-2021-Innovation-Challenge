package com.game.tingshuo.util

object MyUtils {

    //必须同时包含大小写字母及数字
    fun checkPassword(str: String): Boolean {
        var isDigit = false //定义一个boolean值，用来表示是否包含数字
        var isLowerCase = false //定义一个boolean值，用来表示是否包含字母
        var isUpperCase = false
        for (i in 0 until str.length) {
            if (Character.isDigit(str[i])) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true
            } else if (Character.isLowerCase(str[i])) {  //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true
            } else if (Character.isUpperCase(str[i])) {
                isUpperCase = true
            }
        }
        val regex = "^[a-zA-Z0-9]+$"
        return isDigit && isLowerCase && isUpperCase && str.matches(Regex(regex)) && (str.length <=16) && (str.length >=6)
    }

}