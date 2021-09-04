package com.benope.template.core_backend.common.exception;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 *
 */
public class LocalizedException extends RuntimeException {

    private static final String BASE_NAME = "message.messages";

    private String messageKey;
    private String[] vararg;

    public LocalizedException(String messageKey) {
        Objects.requireNonNull(messageKey);

        this.messageKey = messageKey;
    }

    public LocalizedException(String messageKey, String... vararg) {
        Objects.requireNonNull(messageKey);

        this.messageKey = messageKey;
        this.vararg = vararg;
    }

    protected void setVararg(String... vararg) {
        this.vararg = vararg;
    }

    @Override
    public String getMessage() {
        String message = ResourceBundle.getBundle(BASE_NAME, Locale.getDefault()).getString(messageKey);
        return MessageFormat.format(message, vararg);
    }
}
