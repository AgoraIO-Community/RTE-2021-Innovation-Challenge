package com.kangraoo.basektlib.tools.rx.schedulers

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 提供不同线程的调度器用于计算，IO和UI的操作，并单例
 */
class SchedulerProvider : BaseSchedulerProvider {

    companion object {
        val instance: SchedulerProvider by lazy {
            SchedulerProvider()
        }
    }

    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    override fun io(): Scheduler {
        return Schedulers.io()
    }

    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    override fun newThread(): Scheduler {
        return Schedulers.newThread()
    }
}
