package com.qdedu.baselibcommon.data.model.params

import com.kangraoo.basektlib.data.model.BParam
import com.kangraoo.basektlib.ui.action.CURRENT_PAGE
import com.kangraoo.basektlib.ui.action.PAGE_COUNT_SIZE


open class BasePageParam:BParam(){
    var pageSize:Int =
        PAGE_COUNT_SIZE
    var currentPage:Int = CURRENT_PAGE
}