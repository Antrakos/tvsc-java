package com.tvsc.web.advice

import com.tvsc.core.exception.ApplicationException
import com.tvsc.web.Error
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

import java.util.concurrent.CompletionException

import static org.springframework.context.i18n.LocaleContextHolder.getLocale

/**
 *
 * @author Taras Zubrei
 */
@ControllerAdvice
class ExceptionHandlingAdvice {
    @Autowired
    MessageSource messageSource
    Locale defaultLocale = new Locale("en", "US")

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error handleCompletionException(CompletionException ex) {
        handleException(ex.cause)
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error handleApplicationException(ApplicationException ex) {
        handleException(ex)
    }

    private Error handleException(Throwable t) {
        def defaultTitle = messageSource.getMessage("errors.${t.class.simpleName}.title", null, defaultLocale);
        def defaultMessage = messageSource.getMessage("errors.${t.class.simpleName}.message", null, defaultLocale);
        new Error(status: HttpStatus.INTERNAL_SERVER_ERROR.value(),
                title: messageSource.getMessage("errors.${t.class.simpleName}.title", null, defaultTitle, getLocale()),
                message: messageSource.getMessage("errors.${t.class.simpleName}.message", null, defaultMessage, getLocale()),
                exception: t.toString())
    }
}
