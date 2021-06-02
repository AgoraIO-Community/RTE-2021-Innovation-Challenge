package com.qdedu.baselibcommon.exception

class TokenExceedException(val code: Int = 0,val msg: String) : Exception("code:${code} ${msg}")

class UserAccountException(val msg: String) : Exception(msg)