package com.tvsc.core.exception

import groovy.util.logging.Slf4j
import spock.lang.Specification

/**
 *
 * @author Taras Zubrei
 */
@Slf4j
class ExceptionUtilSpecification extends Specification {
    private static ApplicationException exception
    private static IOException checkedExceptionToWrap
    private static RuntimeException uncheckedExceptionToWrap

    def setupSpec() {
        exception = new ApplicationException('Application Exception is thrown') {}
        checkedExceptionToWrap = new IOException('Exception to be wrapped')
        uncheckedExceptionToWrap = new RuntimeException('Exception to be wrapped')
    }

    def "when wrap checked exception then throws wrapped exception with it as cause"() {
        when:
        ExceptionUtil.wrapCheckedException(exception) {
            throw checkedExceptionToWrap
        }
        then:
        ApplicationException ex = thrown()
        ex.cause == checkedExceptionToWrap
    }

    def "when wrap unchecked exception then throws it and wrapping exception is ignored"() {
        when:
        ExceptionUtil.wrapCheckedException(exception) {
            throw uncheckedExceptionToWrap
        }
        then:
        thrown(uncheckedExceptionToWrap.class)
    }
}
