package com.kangraoo.basektlib.tools.task

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

class TaskThreadFactory(var name: String) : ThreadFactory {
    private val mThreadNumber = AtomicInteger(1)
    override fun newThread(r: Runnable): Thread {
        return Thread(r, name + "#" + mThreadNumber.getAndIncrement())
    }
}
