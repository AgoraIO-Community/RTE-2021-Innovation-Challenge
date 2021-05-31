package com.kangraoo.basektlib.tools.task

import android.annotation.TargetApi
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.SSystem
import java.util.*
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

class TaskExecutor(var config: TaskConfig, startup: Boolean) : Executor {

    val serial = AtomicInteger(0) // 主要获取添加任务

    constructor() : this(SApplication.instance().sConfiger.taskConfig, true)
    init {
        if (startup) {
            startup()
        }
    }

    var service: ExecutorService? = null

    fun startup() {
        synchronized(this) {
            if (service != null && !service!!.isShutdown()) {
                return
            }
            service = createExecutor(config)
        }
    }

    private fun executeRunnable(runnable: PRunnable) {
        synchronized(this) {
            if (service == null || service!!.isShutdown) {
                return
            }
            runnable.serial = serial.getAndIncrement()
            service!!.execute(runnable)
        }
    }

    fun execute(runnable: Runnable, delayed: Long) {
        val timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                execute(runnable)
            }
        }, delayed)
    }

    override fun execute(command: Runnable) {
        if (command is PRunnable) {
            executeRunnable(command)
        } else {
            executeRunnable(PRunnable(command))
        }
    }

    fun execute(
        runnable: Runnable,
        @Priority priority: Int
    ) {
        executeRunnable(PRunnable(priority, runnable))
    }

    /**
     * 线程数目说明：
     * 如果队列发过来的任务，发现线程池中正在运行的线程的数量小于核心线程，则立即创建新的线程，无需进入队列等待。
     * 如果正在运行的线程等于或者大于核心线程，则必须参考提交的任务能否加入队列中去。
     * 队列情况说明：
     * 1）如果提交的任务能加入队列，队列如果没有限定，那么理论上队列是无穷大的，那么最大线程数目就是核心数目
     * 2）如果提交的任务能加入队列，队列的值是有限定的，那么首先任务进入队列中去等待，一旦队列中满了，则新增加的任务就进入线程池中创建新的线程。一旦线程池中的最大线程数超过了，那么就会拒绝后面的任务。
     * 队列的三种策略
     * SynchronousQueue  直接提交，也就是上面讲到的所有任务不进入队列去等待。此时小于核心线程就增加，多于或等于核心线程数时，还是增加线程，最大为线程池中的最大允许。超出就拒绝。
     * LinkedBlockingQueue  无界队列 此时超过核心线程后的任务全部加入队列等待，系统最多只能运行核心线程数量的线程。这种方法相当于控制了并发的线程数量。
     * ArrayBlockingQueue   有界队列  此时超过核心线程后的任务先加入队列等待，超出队列范围后的任务就生成线程，但创建的线程最多不超过线程池的最大允许值。
     */
    private fun createExecutor(config: TaskConfig): ExecutorService? {
        val service =
            ThreadPoolExecutor(
                SSystem.cpuCore() + 1,
                2 * SSystem.cpuCore() + 1,
                config.timeout,
                TimeUnit.MILLISECONDS,
                PriorityBlockingQueue(
                    config.queueInitCapacity,
                    mQueueComparator
                ),
                TaskThreadFactory(config.taskName),
                ThreadPoolExecutor.DiscardPolicy()
            )
        allowCoreThreadTimeOut(
            service,
            config.allowCoreTimeOut
        )
        return service
    }

    fun isBusy(): Boolean {
        synchronized(this) {
            if (service == null || service!!.isShutdown) {
                return false
            }
            if (service is ThreadPoolExecutor) {
                val tService =
                    service as ThreadPoolExecutor
                return tService.activeCount >= tService.corePoolSize
            }
            return false
        }
    }

    private fun allowCoreThreadTimeOut(
        service: ThreadPoolExecutor,
        value: Boolean
    ) {
        allowCoreThreadTimeOut9(service, value)
    }

    @TargetApi(9)
    private fun allowCoreThreadTimeOut9(
        service: ThreadPoolExecutor,
        value: Boolean
    ) {
        service.allowCoreThreadTimeOut(value)
    }

//    private var mQueueFIFOComparator:Comparator<Runnable> = kotlin.Comparator { lhs, rhs ->
//        if(lhs is PRunnable && rhs is PRunnable) PRunnable.compareFIFO(lhs,rhs) else 0
//    }
//
//    private var mQueueLIFOComparator: Comparator<Runnable> = kotlin.Comparator { lhs, rhs ->
//        if(lhs is PRunnable && rhs is PRunnable) PRunnable.compareLIFO(lhs,rhs) else 0
//    }

    private var mQueueComparator: Comparator<Runnable> = kotlin.Comparator { lhs, rhs ->
        if (lhs is PRunnable && rhs is PRunnable) lhs.compareTo(rhs) else 0
    }

    fun shutdown() {
        var executor: ExecutorService? = null
        synchronized(this) {
            // 交换变量
            if (service != null) {
                executor = service
                service = null
            }
        }
        if (executor != null) {
            // 停止线程
            if (!executor!!.isShutdown) {
                executor!!.shutdown()
            }

            // 回收变量
            executor = null
        }
    }
}
