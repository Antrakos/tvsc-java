package com.tvsc.core.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Taras Zubrei
 */
public class ExceptionUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(ExceptionUtil.class);

    public static <T> T wrapCheckedException(StatementWithReturnValue<T> statement, ApplicationException exception) {
        try {
            return statement.evaluate();
        } catch (RuntimeException e) {
            exception.afterThrowing();
            throw e;
        } catch (Throwable e) {
            exception.setCause(e);
            exception.afterThrowing();
            throw exception;
        }
    }
}
