package com.kangraoo.basektlib.tools.task

import androidx.annotation.IntDef
import com.kangraoo.basektlib.app.SApplication

class PRunnable(@Priority val priority: Int, val runnable: Runnable) : Runnable, Comparable<PRunnable> {
    companion object {
        /**
         * 线程队列方式 先进先出
         * @param r1
         * @param r2
         * @return
         */
        @JvmStatic
        fun compareFIFO(r1: PRunnable, r2: PRunnable): Int {
            val result = r1.priority - r2.priority
            return if (result == 0) r1.serial - r2.serial else result
        }

        /**
         * 线程队列方式 后进先出
         * @param r1
         * @param r2
         * @return
         */
        @JvmStatic
        fun compareLIFO(r1: PRunnable, r2: PRunnable): Int {
            val result = r1.priority - r2.priority
            return if (result == 0) r2.serial - r1.serial else result
        }
    }

    constructor(runnable: Runnable) : this (NORMAL, runnable)

    var serial = 0

    override fun run() {
        runnable.run()
    }

    override fun compareTo(other: PRunnable): Int {
        return if (SApplication.instance().sConfiger.taskConfig.fifo) {
            compareFIFO(this, other)
        } else {
            compareLIFO(this, other)
        }
    }
}

const val HIGH = 1 // 优先级高
const val NORMAL = 2 // 优先级中等
const val LOW = 3 // 优先级低

@IntDef(HIGH, NORMAL, LOW)
@Retention(AnnotationRetention.SOURCE)
annotation class Priority
