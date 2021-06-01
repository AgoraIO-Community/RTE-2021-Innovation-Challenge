package com.kangraoo.basektlib.tools.task

class TaskConfig(var timeout: Long, var allowCoreTimeOut: Boolean, var fifo: Boolean, var queueInitCapacity: Int, var taskName: String) {

    private constructor(builder: Builder) : this(builder.timeout, builder.allowCoreTimeOut, builder.fifo, builder.queueInitCapacity, builder.taskName)

    class Builder {
        var taskName = "SYS_THTEAD_EXECUTOR"

        /**
         * 超时时间
         */
        var timeout = 30 * 1000L

        /**
         * 队列大小
         */
        var queueInitCapacity = 20

        // 该值为true，则线程池数量最后销毁到0个
        // 该值为false 销毁机制：超过核心线程数时，而且（超过最大值或者timeout过），就会销毁。
        var allowCoreTimeOut = false

        var fifo = true

        fun build(): TaskConfig = TaskConfig(this)
    }

    companion object {
        @JvmStatic inline fun build(block: TaskConfig.Builder.() -> Unit) = TaskConfig.Builder().apply(block).build()
    }
}
