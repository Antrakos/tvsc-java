package com.tvsc.core.exception


import java.util.concurrent.Callable

/**
 * @author Taras Zubrei
 */
object ExceptionUtil {

    @JvmStatic
    fun <T, K> wrapCheckedException(exception: K, statement: Callable<T>): T where K : ApplicationException {
        try {
            return statement.call()
        } catch (e: RuntimeException) {
            throw e
        } catch (e: Throwable) {
            val constructor = exception.javaClass.getConstructor(String::class.java, Throwable::class.java)
            throw constructor.newInstance(exception.message, e)
        }
    }
}
