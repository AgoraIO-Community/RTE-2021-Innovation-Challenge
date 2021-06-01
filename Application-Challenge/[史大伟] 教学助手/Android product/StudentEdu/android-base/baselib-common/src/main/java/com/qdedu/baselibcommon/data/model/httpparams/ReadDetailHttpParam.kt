package com.qdedu.baselibcommon.data.model.httpparams

import com.kangraoo.basektlib.data.model.BParam

class ReadDetailHttpParam (var shareFlg:Int = 1,var role:Int?= null,var recordId:Long?=null,var from_pager:Int? = 1):BParam()