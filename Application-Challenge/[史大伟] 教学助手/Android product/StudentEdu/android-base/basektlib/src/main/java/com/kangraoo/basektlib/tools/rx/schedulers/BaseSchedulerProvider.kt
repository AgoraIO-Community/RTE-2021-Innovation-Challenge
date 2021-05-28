package com.kangraoo.basektlib.tools.rx.schedulers

import io.reactivex.Scheduler

/**
 * 用来提供不同的调度器
 */
interface BaseSchedulerProvider {
    fun computation(): Scheduler
    fun io(): Scheduler
    fun ui(): Scheduler
    fun newThread(): Scheduler
}
