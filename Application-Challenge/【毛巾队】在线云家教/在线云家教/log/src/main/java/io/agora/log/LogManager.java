package io.agora.log;

/**
 * 新版Log-API设计
 */

import androidx.annotation.NonNull;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.Logger;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.flattener.Flattener;
import com.elvishew.xlog.flattener.Flattener2;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.naming.ChangelessFileNameGenerator;

import java.io.File;

import static com.elvishew.xlog.LogLevel.DEBUG;
import static com.elvishew.xlog.LogLevel.ERROR;
import static com.elvishew.xlog.LogLevel.INFO;
import static com.elvishew.xlog.LogLevel.VERBOSE;
import static com.elvishew.xlog.LogLevel.WARN;

public class LogManager {
    private static File sPath;
    private static String sTag;
    private Logger logger;
    private final String p0 = "%", p1 = "%%";

    public static void init(@NonNull String logPath, @NonNull String tag) {
        sPath = new File(logPath);
        sTag = tag;
        XLog.init(new LogConfiguration.Builder()
                        .logLevel(LogLevel.ALL)
                        .tag(tag).build(),
                new AndroidPrinter(),
                new FilePrinter.Builder(getPath().getAbsolutePath())
                        .fileNameGenerator(new ChangelessFileNameGenerator(tag + ".log"))
                        .flattener(new MyFlattener())
                        .build());
    }

    public LogManager(String sTag) {
        logger = XLog.tag(getTag() + "-" + sTag).build();
    }

    private String check(String msg, Object... args) {
        if (args.length == 0 && msg.contains(p0)) {
            msg = msg.replaceAll(p0, p1);
        }
        return msg;
    }

    public void logMsg(String msg, int level) {
        switch (level) {
            case DEBUG:
                d(msg);
                break;
            case WARN:
                w(msg);
                break;
            case ERROR:
                e(msg);
                break;
            case INFO:
            default:
                i(msg);
                break;
        }
    }

    public void d(String msg, Object... args) {
        msg = check(msg, args);
        logger.d(msg, args);
    }

    public void i(String msg, Object... args) {
        msg = check(msg, args);
        logger.i(msg, args);
    }

    public void w(String msg, Object... args) {
        msg = check(msg, args);
        logger.w(msg, args);
    }

    public void e(String msg, Object... args) {
        msg = check(msg, args);
        logger.e(msg, args);
    }

    public static File getPath() throws IllegalStateException {
        if (sPath == null)
            throw new IllegalStateException("LogManager is not initialized. Please call init() before use!");
        return sPath;
    }

    public static String getTag() throws IllegalStateException {
        if (sTag == null)
            throw new IllegalStateException("LogManager is not initialized. Please call init() before use!");
        return sTag;
    }
}
