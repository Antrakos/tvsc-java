package com.tvsc.web.advice

import com.tvsc.core.exception.ApplicationException
import com.tvsc.web.Error
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

import javax.servlet.http.HttpServletRequest

/**
 *
 * @author Taras Zubrei
 */
@ControllerAdvice
class ExceptionHandlingAdvice {
    @Autowired
    ResourceBundleMessageSource messageSource

    @ExceptionHandler(ApplicationException)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handleHttpException(ApplicationException ex, HttpServletRequest request) {
        new Error(status: HttpStatus.INTERNAL_SERVER_ERROR.value(),
                title: messageSource.getMessage("errors.${ex.cause.class.simpleName}.title", null, request.getLocale()),
                message: messageSource.getMessage("errors.${ex.cause.class.simpleName}.message", null, request.getLocale()), //TODO: Maybe? LocaleContextHolder.getLocale()
                exception: ex.cause)
    }
}
