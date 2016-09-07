package com.tvsc.web.advice;

import com.tvsc.core.exception.ApplicationException;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Locale;

import static org.springframework.context.i18n.LocaleContextHolder.getLocale;

/**
 * @author Antrakos
 */
@ControllerAdvice
public class ExceptionHandlingAdvice {
    @Autowired
    private MessageSource messageSource;
    private Locale defaultLocale = new Locale("en", "US");

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ResponseBody
//    public Error handleCompletionException(rx.exceptions.CompositeException ex) {
//        return handleException(ex.getCause());
//    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Error handleApplicationException(ApplicationException ex) {
        return handleException(ex);
    }

    private Error handleException(Throwable t) {
        String defaultTitle = messageSource.getMessage("errors." + t.getClass().getSimpleName() + ".title", null, defaultLocale);
        String defaultMessage = messageSource.getMessage("errors." + t.getClass().getSimpleName() + ".message", null, defaultLocale);

        return new Error().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .title(messageSource.getMessage("errors." + t.getClass().getSimpleName() + ".title", null, defaultTitle, getLocale()))
                .message(messageSource.getMessage("errors." + t.getClass().getSimpleName() + ".message", null, defaultMessage, getLocale()))
                .exception(t.toString());
    }

    @Data
    @Accessors(fluent = true)
    private class Error {
        private int status;
        private String title;
        private String message;
        private String exception;
    }
}
