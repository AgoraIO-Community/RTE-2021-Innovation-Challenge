package com.qdedu.baselibcommon.arouter

import com.qdedu.baselibcommon.arouter.service.VIDEO

object BaseRouterHub {

    private const val READER = "/reader"
    private const val READING = "/reading"
    private const val KEFU = "/kefu"
    private const val SHARE = "/share"
    private const val BUGLY = "/bugly"
    private const val ALI = "/ali"
    private const val VIDEO = "/video"
    private const val TEST = "/test"
    private const val BASE_COMMON = "/baseCommon"
    private const val YOUDAO="/youdao"
    private const val HOMEWORK="/homework"
    private const val IM="/im"

    const val ROUTE_DATA_CENTER = "/data/dataCenter"

    const val ROUTE_SERVICE_READER = "${READER}/service"

    const val ROUTE_SERVICE_INIT_READER = "${READER}/initservice"

    const val ROUTE_SERVICE_INIT_READING = "${READING}/initservice"

    const val ROTE_READING_SUPERIOR_BOOK ="${READING}/book"

    const val ROUTE_READING_HOME = "${READING}/home"

    const val ROUTE_READING_WORKS = "${READING}/works"

    const val ROUTE_SERVICE_READING = "${READING}/service"

    const val ROUTE_KEFU_SERVICE = "${KEFU}/service"

    const val ROUTE_KEFU_INIT = "${KEFU}/init"

    const val ROUTE_SHARE_SERVICE = "${SHARE}/service"

    const val ROUTE_SHARE_INIT = "${SHARE}/init"

    const val ROUTE_BUGLY_INIT = "${BUGLY}/init"

    const val ROUTE_BUGLY_SERVICE = "${BUGLY}/service"

    const val ROUTE_ALI_INIT = "${ALI}/init"

    const val ROUTE_ALI_SERVICE = "${ALI}/service"

    const val ROUTE_VIDEO_INIT = "${VIDEO}/init"

    const val ROUTE_VIDEO_SERVICE = "${VIDEO}/service"

    const val ROUTE_VIDEO_FRAGMENT = "${VIDEO}/fragment"

    const val ROUTE_SERVICE_TO_READING = "${BASE_COMMON}${READER}/service"

    const val ROUTE_UPDATE_SERVICE = "/update/service"

    const val ROUTE_TEST_INIT = "${TEST}/init"

    const val ROUTE_YOUDAO_INIT = "${YOUDAO}/init"

    const val ROUTE_HOMEWORK_SERVICE = "${HOMEWORK}/service"

    const val ROUTE_IM_INIT = "${IM}/init"

    const val ROUTE_IM_SERVICE = "${IM}/service"

}