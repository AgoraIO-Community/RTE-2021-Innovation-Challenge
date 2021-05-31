package com.dong.circlelive.base


import android.os.Build
import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import java.util.Collections.unmodifiableList


class Timber private constructor() {

    /** A facade for handling logging calls. Install instances via [Timber.plant()][.plant].  */
    abstract class Tree {
        open var defaultTag: String? = null

        open fun getHeadInfoAndDefaultTag(throwable: Throwable?): Pair<String?, String?> {
            return Pair(defaultTag, getThreadInfo())
        }

        /** Log a verbose exception and a message with optional format args.  */
        open fun v(tag: String? = null, t: Throwable? = null, lazyMessage: () -> Any) {
            prepareLog(Log.VERBOSE, tag, t, lazyMessage = lazyMessage)
        }

        open fun v(tag: String? = null, t: Throwable) {
            prepareLog(Log.VERBOSE, tag, t = t)
        }


        /** Log a debug exception and a message with optional format args.  */
        open fun d(tag: String? = null, t: Throwable? = null, lazyMessage: () -> Any) {
            prepareLog(Log.DEBUG, tag, t, lazyMessage = lazyMessage)
        }

        /** Log a debug exception.  */
        open fun d(tag: String? = null, t: Throwable) {
            prepareLog(Log.DEBUG, tag, t, null)
        }


        /** Log an info exception and a message with optional format args.  */
        open fun i(tag: String? = null, t: Throwable? = null, lazyMessage: () -> Any) {
            prepareLog(Log.INFO, tag, t, lazyMessage = lazyMessage)
        }

        /** Log an info exception.  */
        open fun i(tag: String? = null, t: Throwable) {
            prepareLog(Log.INFO, tag, t, null)
        }

        /** Log a warning exception and a message with optional format args.  */
        open fun w(tag: String? = null, t: Throwable? = null, lazyMessage: () -> Any) {
            prepareLog(Log.WARN, tag, t, lazyMessage = lazyMessage)
        }

        /** Log a warning exception.  */
        open fun w(tag: String? = null, t: Throwable) {
            prepareLog(Log.WARN, tag, t, null)
        }


        /** Log an error exception and a message with optional format args.  */
        open fun e(tag: String? = null, t: Throwable? = null, lazyMessage: () -> Any) {
            prepareLog(Log.ERROR, tag, t, lazyMessage = lazyMessage)
        }

        /** Log an error exception.  */
        open fun e(tag: String? = null, t: Throwable) {
            prepareLog(Log.ERROR, tag, t, null)
        }


        /** Log an assert exception and a message with optional format args.  */
        open fun wtf(tag: String? = null, t: Throwable? = null, lazyMessage: () -> Any) {
            prepareLog(Log.ASSERT, tag, t, lazyMessage = lazyMessage)
        }

        /** Log an assert exception.  */
        open fun wtf(tag: String? = null, t: Throwable) {
            prepareLog(Log.ASSERT, tag, t, null)
        }


        /** Log at `priority` an exception and a message with optional format args.  */
        open fun log(
            priority: Int,
            tag: String? = null,
            t: Throwable? = null,
            lazyMessage: () -> Any
        ) {
            prepareLog(priority, tag, t, lazyMessage = lazyMessage)
        }

        /** Log at `priority` an exception.  */
        open fun log(priority: Int, tag: String? = null, t: Throwable) {
            prepareLog(priority, tag, t, null)
        }


        /** Return whether a message at `priority` or `defaultTag` should be logged.  */
        open fun isLoggable(tag: String?, priority: Int): Boolean {
            return true
        }

        private fun prepareLog(
            priority: Int,
            tag: String?,
            t: Throwable? = null,
            lazyMessage: (() -> Any)? = null
        ) {
            if (!isLoggable(tag, priority)) {
                return
            }

            val params = getHeadInfoAndDefaultTag(t)

            val headInfo = params.second
            val message: String = if (lazyMessage != null) lazyMessage().toString() else ""
            val stackTraceInfo = if (t != null) getStackTraceString(t) else ""

            var tagVal = if (tag.isNullOrEmpty()) params.first else tag
            // Tag length limit was removed in API 24.
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N && tagVal != null && tagVal.length > MAX_TAG_LENGTH) {
                tagVal = tagVal.substring(0, MAX_TAG_LENGTH)
            }
            log(priority, tagVal, "$headInfo -- $message \n $stackTraceInfo", t)
        }

        protected fun getThreadInfo(): String {
            val pid = android.os.Process.myPid()
            return "pid:$pid ${Thread.currentThread()}"
        }

        private fun getStackTraceString(t: Throwable): String {
            // Don't replace this with Log.getStackTraceString() - it hides
            // UnknownHostException, which is not what we want.
            val sw = StringWriter(256)
            val pw = PrintWriter(sw, false)
            t.printStackTrace(pw)
            pw.flush()
            return sw.toString()
        }

        /**
         * Write a log message to its destination. Called for all level-specific methods by default.
         *
         * @param priority Log level. See [Log] for constants.
         * @param tag Explicit or inferred defaultTag. May be `null`.
         * @param message Formatted log message. May be `null`, but then `t` will not be.
         * @param t Accompanying exceptions. May be `null`, but then `message` will not be.
         */
        protected abstract fun log(
            priority: Int, tag: String?, message: String,
            t: Throwable?
        )

        companion object {
            val CALL_STACK_INDEX = 7
            val JAVA_SUFFIX = ".java"
            val KOTLIN_SUFFIX = ".kt"
            val MAX_TAG_LENGTH = 23

        }
    }

    /** A [Tree] for debug builds. Automatically infers the defaultTag from the calling class.  */
    class DebugTree : Tree() {

        override fun getHeadInfoAndDefaultTag(throwable: Throwable?): Pair<String?, String?> {
            val stackTrace = Throwable().stackTrace
            if (stackTrace.size <= CALL_STACK_INDEX) {
                return super.getHeadInfoAndDefaultTag(throwable)
            }
            val targetElement = stackTrace[CALL_STACK_INDEX]

            var className = targetElement.className
            // remove package info
            className = className.substringAfterLast(".", className)
            // remove 匿名内部类
            className = className.substringBefore('$', className)

            //Kotlin 写的extension 文件 className会多一个Kt 后缀， 影响点击跳转到对应行数， 这里Remove掉。（有可能some class 真的以Kt 结尾？）
            className = className.removeSuffix("Kt")
            // 添加文件后缀，可以点击跳转

            if (!targetElement.fileName.isNullOrEmpty()) {
                className += if (targetElement.fileName.endsWith(KOTLIN_SUFFIX)) KOTLIN_SUFFIX else JAVA_SUFFIX
            }

            val methodName = targetElement.methodName
            var lineNumber = targetElement.lineNumber

            if (lineNumber < 0) {
                lineNumber = 0
            }
            return Pair(className, "[ ($className:$lineNumber)#$methodName ] - ${getThreadInfo()}")

        }

        /**
         * Break up `message` into maximum-length chunks (if needed) and send to either
         * [Log.println()][Log.println] or
         * [Log.wtf()][Log.wtf] for logging.
         *
         * {@inheritDoc}
         */
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (message.length < MAX_LOG_LENGTH) {
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, message)
                } else {
                    Log.println(priority, tag, message)
                }
                return
            }

            // Split by line, then ensure each line can fit into Log's maximum length.
            var i = 0
            val length = message.length
            while (i < length) {
                var newline = message.indexOf('\n', i)
                newline = if (newline != -1) newline else length
                do {
                    val end = Math.min(newline, i + MAX_LOG_LENGTH)
                    val part = message.substring(i, end)
                    if (priority == Log.ASSERT) {
                        Log.wtf(tag, part)
                    } else {
                        Log.println(priority, tag, part)
                    }
                    i = end
                } while (i < newline)
                i++
            }
        }

        companion object {
            private const val MAX_LOG_LENGTH = 4000
        }
    }

    companion object {

        /** Log a verbose exception and a message with optional format args.  */
        fun v(tag: String? = null, t: Throwable? = null, lazyMessage: () -> Any) {
            TREE_OF_SOULS.v(tag, t, lazyMessage)
        }


        /** Log a verbose exception and a message with optional format args.  */
        fun v(tag: String? = null, t: Throwable) {
            TREE_OF_SOULS.v(tag, t)
        }

        /** Log a debug exception and a message with optional format args.  */
        fun d(tag: String? = null, t: Throwable? = null, lazyMessage: () -> Any) {
            TREE_OF_SOULS.d(tag, t, lazyMessage)
        }

        /** Log a debug exception and a message with optional format args.  */
        fun d(tag: String? = null, lazyMessage: () -> Any) {
            TREE_OF_SOULS.d(tag, null, lazyMessage)
        }

        /** Log a debug exception and a message with optional format args.  */
        fun d(lazyMessage: () -> Any) {
            TREE_OF_SOULS.d(null, null, lazyMessage)
        }

        /** Log a debug exception and a message with optional format args.  */
        fun d(t: Throwable? = null, lazyMessage: () -> Any) {
            TREE_OF_SOULS.d(null, t, lazyMessage)
        }

        /** Log a debug exception.  */
        fun d(t: Throwable) {
            TREE_OF_SOULS.d(null, t)
        }

        /** Log an info exception and a message with optional format args.  */
        fun i(tag: String? = null, t: Throwable? = null, lazyMessage: () -> Any) {
            TREE_OF_SOULS.i(tag, t, lazyMessage)
        }

        /** Log an info exception and a message with optional format args.  */
        fun i(t: Throwable? = null, lazyMessage: () -> Any) {
            TREE_OF_SOULS.i(null, t, lazyMessage)
        }

        /** Log an info exception.  */
        fun i(t: Throwable) {
            TREE_OF_SOULS.i(null, t)
        }


        /** Log a warning exception and a message with optional format args.  */
        fun w(tag: String? = null, t: Throwable? = null, lazyMessage: () -> Any) {
            TREE_OF_SOULS.w(tag, t, lazyMessage)
        }

        /** Log a warning exception.  */
        fun w(tag: String? = null, t: Throwable) {
            TREE_OF_SOULS.w(tag, t)
        }

        /** Log an error exception and a message with optional format args.  */
        fun e(tag: String? = null, t: Throwable? = null, lazyMessage: () -> Any) {
            TREE_OF_SOULS.e(tag, t, lazyMessage)
        }

        /** Log an error exception and a message with optional format args.  */
        fun e(t: Throwable? = null, lazyMessage: () -> Any) {
            TREE_OF_SOULS.e(null, t, lazyMessage)
        }

        /** Log an error exception.  */
        fun e(t: Throwable) {
            TREE_OF_SOULS.e(null, t)
        }


        /** Log an assert exception and a message with optional format args.  */
        fun wtf(tag: String? = null, t: Throwable? = null, lazyMessage: () -> Any) {
            TREE_OF_SOULS.wtf(tag, t, lazyMessage)
        }

        /** Log an assert exception.  */
        fun wtf(tag: String? = null, t: Throwable) {
            TREE_OF_SOULS.wtf(tag, t)
        }


        /** Log at `priority` an exception and a message with optional format args.  */
        fun log(priority: Int, tag: String? = null, t: Throwable? = null, lazyMessage: () -> Any) {
            TREE_OF_SOULS.log(priority, tag, t, lazyMessage)
        }

        /** Log at `priority` an exception.  */
        fun log(priority: Int, tag: String? = null, t: Throwable) {
            TREE_OF_SOULS.log(priority, tag, t)
        }

        /**
         * A view into Timber's planted trees as a tree itself. This can be used for injecting a logger
         * instance rather than using static methods or to facilitate testing.
         */
        fun asTree(): Tree {
            return TREE_OF_SOULS
        }


        /** Add a new logging tree.  */
        // Validating public API contract.
        fun plant(tree: Tree) {
            if (tree === TREE_OF_SOULS) {
                throw IllegalArgumentException("Cannot plant Timber into itself.")
            }
            synchronized(FOREST) {
                FOREST.add(tree)
                forestAsArray = FOREST.toTypedArray()
            }
        }

        /** Adds new logging trees.  */
        // Validating public API contract.
        fun plant(vararg trees: Tree) {
            for (tree in trees) {
                if (tree === TREE_OF_SOULS) {
                    throw IllegalArgumentException("Cannot plant Timber into itself.")
                }
            }
            synchronized(FOREST) {
                Collections.addAll(FOREST, *trees)
                forestAsArray = FOREST.toTypedArray<Tree>()
            }
        }

        /** Remove a planted tree.  */
        fun uproot(tree: Tree) {
            synchronized(FOREST) {
                if (!FOREST.remove(tree)) {
                    throw IllegalArgumentException("Cannot uproot tree which is not planted: $tree")
                }
                forestAsArray = FOREST.toTypedArray<Tree>()
            }
        }

        /** Remove all planted trees.  */
        fun uprootAll() {
            synchronized(FOREST) {
                FOREST.clear()
                forestAsArray = TREE_ARRAY_EMPTY
            }
        }

        /** Return a copy of all planted [trees][Tree].  */
        fun forest(): List<Tree> {
            synchronized(FOREST) {
                return unmodifiableList(ArrayList(FOREST))
            }
        }

        fun treeCount(): Int {
            synchronized(FOREST) {
                return FOREST.size
            }
        }

        private val TREE_ARRAY_EMPTY = arrayOf<Tree>()
        // Both fields guarded by 'FOREST'.
        private val FOREST = ArrayList<Tree>()
        @Volatile
        internal var forestAsArray = TREE_ARRAY_EMPTY

        /** A [Tree] that delegates to all planted trees in the [forest][.FOREST].  */
        private val TREE_OF_SOULS = object : Tree() {

            override fun v(tag: String?, t: Throwable?, lazyMessage: () -> Any) {
                val forest = forestAsArray
                for (tree in forest) {
                    tree.v(tag, t, lazyMessage = lazyMessage)
                }
            }

            override fun v(tag: String?, t: Throwable) {
                val forest = forestAsArray
                for (tree in forest) {
                    tree.v(tag, t)
                }
            }


            override fun d(tag: String?, t: Throwable?, lazyMessage: () -> Any) {
                val forest = forestAsArray
                for (tree in forest) {
                    tree.d(tag, t, lazyMessage = lazyMessage)
                }
            }

            override fun d(tag: String?, t: Throwable) {
                val forest = forestAsArray
                for (tree in forest) {
                    tree.d(tag, t)
                }
            }


            override fun i(tag: String?, t: Throwable?, lazyMessage: () -> Any) {
                val forest = forestAsArray
                for (tree in forest) {
                    tree.i(tag, t, lazyMessage = lazyMessage)
                }
            }

            override fun i(tag: String?, t: Throwable) {
                val forest = forestAsArray
                for (tree in forest) {
                    tree.i(tag, t)
                }
            }


            override fun w(tag: String?, t: Throwable?, lazyMessage: () -> Any) {
                val forest = forestAsArray
                for (tree in forest) {
                    tree.w(tag, t, lazyMessage = lazyMessage)
                }
            }

            override fun w(tag: String?, t: Throwable) {
                val forest = forestAsArray
                for (tree in forest) {
                    tree.w(tag, t)
                }
            }


            override fun e(tag: String?, t: Throwable?, lazyMessage: () -> Any) {
                val forest = forestAsArray
                for (tree in forest) {
                    tree.e(tag, t, lazyMessage = lazyMessage)
                }
            }

            override fun e(tag: String?, t: Throwable) {
                val forest = forestAsArray
                for (tree in forest) {
                    tree.e(tag, t)
                }
            }


            override fun wtf(tag: String?, t: Throwable?, lazyMessage: () -> Any) {
                val forest = forestAsArray
                for (tree in forest) {
                    tree.wtf(tag, t, lazyMessage = lazyMessage)
                }
            }

            override fun wtf(tag: String?, t: Throwable) {
                val forest = forestAsArray
                for (tree in forest) {
                    tree.wtf(tag, t)
                }
            }


            override fun log(priority: Int, tag: String?, t: Throwable?, lazyMessage: () -> Any) {
                val forest = forestAsArray
                for (tree in forest) {
                    tree.log(priority, tag, t, lazyMessage = lazyMessage)
                }
            }

            override fun log(priority: Int, tag: String?, t: Throwable) {
                val forest = forestAsArray
                for (tree in forest) {
                    tree.log(priority, tag, t)
                }
            }

            override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                throw AssertionError("Missing override for log method.")
            }
        }
    }
}