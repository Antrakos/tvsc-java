package com.tvsc.core.exception


import org.slf4j.LoggerFactory

/**
 * @author Taras Zubrei
 */
object ExceptionUtil {

    private val LOGGER = LoggerFactory.getLogger(ExceptionUtil::class.java)

    fun <T> wrapCheckedException(exception: ApplicationException, statement: StatementWithReturnValue<T>): T {
        try {
            return statement.evaluate()
        } catch (e: RuntimeException) {
            throw e
        } catch (e: Throwable) {
            exception.setCause(e)
            LOGGER.error(exception.toString())
            throw exception
        }

    }
}
