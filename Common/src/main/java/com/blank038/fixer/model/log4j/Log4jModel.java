package com.blank038.fixer.model.log4j;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

import java.util.regex.Pattern;

/**
 * 修复 Log4j2 远程执行漏洞
 *
 * @author Blank038
 * @since 2021-12-10
 */
public class Log4jModel extends AbstractFilter {
    protected final static Pattern PATTERN = Pattern.compile(".*\\$\\{jndi:.*}.*");

    public Log4jModel() {
        Logger logger = (org.apache.logging.log4j.core.Logger) LogManager.getRootLogger();
        logger.addFilter(this);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return validateMessage(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return validateMessage(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return msg == null ? Result.NEUTRAL : validateMessage(msg.toString());
    }

    @Override
    public Result filter(LogEvent event) {
        return event != null ? validateMessage(event.getMessage()) : Result.NEUTRAL;
    }

    private static Result validateMessage(Message message) {
        return message == null ? Result.NEUTRAL : validateMessage(message.getFormattedMessage());
    }

    private static Result validateMessage(String message) {
        return PATTERN.matcher(message).find() ? Result.DENY : Result.NEUTRAL;
    }
}
