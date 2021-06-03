package io.agora.log;

import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.flattener.Flattener2;

import java.text.SimpleDateFormat;

public class MyFlattener implements Flattener2 {

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

    @Override
    public CharSequence flatten(long timeMillis, int logLevel, String tag, String message) {
        StringBuffer stringBuffer = new StringBuffer();
        return stringBuffer.append(simpleDateFormat.format(timeMillis)).append("|")
                .append(LogLevel.getLevelName(logLevel)).append("|")
                .append(tag).append("|")
                .append(message).toString();
    }
}
