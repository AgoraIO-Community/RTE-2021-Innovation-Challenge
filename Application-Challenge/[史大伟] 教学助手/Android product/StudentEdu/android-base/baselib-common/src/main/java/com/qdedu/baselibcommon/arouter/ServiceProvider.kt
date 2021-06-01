package com.qdedu.baselibcommon.arouter

import com.kangraoo.basektlib.tools.URouter
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_ALI_INIT
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_ALI_SERVICE
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_BUGLY_INIT
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_BUGLY_SERVICE
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_DATA_CENTER
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_HOMEWORK_SERVICE
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_IM_SERVICE
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_KEFU_INIT
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_KEFU_SERVICE
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_SERVICE_INIT_READER
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_SERVICE_INIT_READING
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_SERVICE_READER
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_SERVICE_READING
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_SERVICE_TO_READING
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_SHARE_INIT
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_SHARE_SERVICE
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_TEST_INIT
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_UPDATE_SERVICE
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_VIDEO_INIT
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_VIDEO_SERVICE
import com.qdedu.baselibcommon.arouter.BaseRouterHub.ROUTE_YOUDAO_INIT
import com.qdedu.baselibcommon.arouter.service.*
import com.qdedu.baselibcommon.arouter.service.init.*

object ServiceProvider {

    val iDataCenterService: IDataCenterService? by lazy {
        URouter.provideCallBack(IDataCenterService::class.java, ROUTE_DATA_CENTER)
    }

    val readerService: IReaderService? by lazy {
        URouter.provideCallBack(IReaderService::class.java, ROUTE_SERVICE_READER)
    }

    val readerInit: IReaderInit? by lazy {
        URouter.provideCallBack(IReaderInit::class.java, ROUTE_SERVICE_INIT_READER)
    }

    val readingInit: IReadingInit? by lazy {
        URouter.provideCallBack(IReadingInit::class.java, ROUTE_SERVICE_INIT_READING)
    }

    val kefuService: IKefuService? by lazy {
        URouter.provideCallBack(IKefuService::class.java, ROUTE_KEFU_SERVICE)
    }

    val kefuInit: IKefuInit? by lazy {
        URouter.provideCallBack(IKefuInit::class.java, ROUTE_KEFU_INIT)
    }

    val toReadingService:IToReadingService? by lazy {
        URouter.provideCallBack(IToReadingService::class.java, ROUTE_SERVICE_TO_READING)
    }

    val readingService:IReadingService? by lazy {
        URouter.provideCallBack(IReadingService::class.java, ROUTE_SERVICE_READING)
    }

    val iShareService:IShareService? by lazy {
        URouter.provideCallBack(IShareService::class.java, ROUTE_SHARE_SERVICE)
    }

    val shareInit: IShareInit? by lazy {
        URouter.provideCallBack(IShareInit::class.java, ROUTE_SHARE_INIT)
    }

    val buglyInit: IBuglyInit? by lazy {
        URouter.provideCallBack(IBuglyInit::class.java, ROUTE_BUGLY_INIT)
    }

    val buglyService: IBuglyService? by lazy {
        URouter.provideCallBack(IBuglyService::class.java, ROUTE_BUGLY_SERVICE)
    }

    val videoInit: IVideoInit? by lazy {
        URouter.provideCallBack(IVideoInit::class.java, ROUTE_VIDEO_INIT)
    }
    val videoService: IVideoService? by lazy {
        URouter.provideCallBack(IVideoService::class.java, ROUTE_VIDEO_SERVICE)
    }

    val updateService: IUpdateService? by lazy {
        URouter.provideCallBack(IUpdateService::class.java, ROUTE_UPDATE_SERVICE)
    }

    val aliapmInit: IAliApmInit? by lazy {
        URouter.provideCallBack(IAliApmInit::class.java, ROUTE_ALI_INIT)
    }

    val aliapmService: IAliApmService? by lazy {
        URouter.provideCallBack(IAliApmService::class.java, ROUTE_ALI_SERVICE)
    }

    val testInit: ITestInit? by lazy {
        URouter.provideCallBack(ITestInit::class.java, ROUTE_TEST_INIT)
    }

    val youdaoInit: IYoudaoInit? by lazy {
        URouter.provideCallBack(IYoudaoInit::class.java, ROUTE_YOUDAO_INIT)
    }

    val homeworkService: IHomeWorkService? by lazy {
        URouter.provideCallBack(IHomeWorkService::class.java, ROUTE_HOMEWORK_SERVICE)
    }

    val imService: IIMService? by lazy {
        URouter.provideCallBack(IIMService::class.java, ROUTE_IM_SERVICE)
    }
}