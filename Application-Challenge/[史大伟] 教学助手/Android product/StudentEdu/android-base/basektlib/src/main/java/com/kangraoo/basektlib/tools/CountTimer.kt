package com.kangraoo.basektlib.tools

import com.kangraoo.basektlib.tools.log.ULog
import java.util.*

/**
 * @author shidawei
 * 创建日期：2021/3/26
 * 描述：
 */
private const val PREPARE: Int = 0
private const val START = 1
private const val PASUSE = 2
private const val TIMER_UNIT: Long = 1000

class CountTimer(var mCountTimerListener: CountTimerListener) {

    private var timer_couting: Long = 0
    private var timer: Timer? = null
    private var timerTask: MyTimerTask? = null

    private var timerStatus: Int = PREPARE

    /**
     * init timer status
     */
    private fun initTimerStatus() {
        timer_couting = 0
        timerStatus = PREPARE
    }

    interface CountTimerListener {
        fun onChange(time: Long)
    }

    /**
     * start
     */
    fun startCount() {
        startTimer()
        timerStatus = START
    }

    /**
     * paust
     */
    fun pauseCount() {
        if (timer != null) {
            timer!!.cancel()
            timerStatus = PASUSE
        }
    }

    /**
     * stop
     */
    fun stopCount() {
        if (timer != null) {
            timer!!.cancel()
            initTimerStatus()
        }
    }

    /**
     * start count down
     */
    private fun startTimer() {
        timer = Timer()
        timerTask = MyTimerTask()
        timer!!.scheduleAtFixedRate(timerTask, 0, TIMER_UNIT)
    }


    /**
     * count task
     */
    inner class MyTimerTask : TimerTask() {
        override fun run() {
            ULog.d("timmer", timer_couting.toString())
            mCountTimerListener.onChange(timer_couting)
            timer_couting += TIMER_UNIT
        }
    }

}