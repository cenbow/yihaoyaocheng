//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyw.yhyc.order.exception;

import java.util.Vector;

public class ServiceException extends ApplicationException {
    protected String errorMessage;
    private static final long serialVersionUID = 1L;

    public String getErrorMessage() {
        return this.errorMessage == null?this.getMessage():this.errorMessage;
    }

    public ServiceException() {
        super("error.lymc.serviceError");
    }

    public ServiceException(Exception exception) {
        this();
        this.printExcStackTrace(exception);
    }

    public ServiceException(String messageKey) {
        super(messageKey);
    }

    public ServiceException(String messageKey, Exception exception) {
        this(messageKey);
        this.printExcStackTrace(exception);
    }

    public ServiceException(String messageKey, Vector parameters) {
        super(messageKey, parameters.toArray());
    }

    public ServiceException(String messageKey, Vector parameters, Exception exception) {
        this(messageKey, parameters.toArray(), exception);
    }

    public ServiceException(String messageKey, Object[] parameters) {
        super(messageKey, parameters);
    }

    public ServiceException(String messageKey, Object[] parameters, Exception exception) {
        super(messageKey, parameters, exception);
    }

    public ServiceException(String messageKey, String parameter1) {
        super(messageKey, new Object[]{parameter1});
    }

    public ServiceException(String messageKey, String parameter1, Exception exception) {
        super(messageKey, new Object[]{parameter1}, exception);
    }

    public ServiceException(String messageKey, String parameter1, String parameter2) {
        super(messageKey, new Object[]{parameter1, parameter2});
    }

    public ServiceException(String messageKey, String parameter1, String parameter2, Exception exception) {
        super(messageKey, new Object[]{parameter1, parameter2}, exception);
    }

    public ServiceException(String messageKey, String parameter1, String parameter2, String parameter3) {
        super(messageKey, new Object[]{parameter1, parameter2, parameter3});
    }

    public ServiceException(String messageKey, String parameter1, String parameter2, String parameter3, Exception exception) {
        super(messageKey, new Object[]{parameter1, parameter2, parameter3}, exception);
    }

    public ServiceException(String messageKey, String parameter1, String parameter2, String parameter3, String parameter4) {
        super(messageKey, new Object[]{parameter1, parameter2, parameter3, parameter4});
    }

    public ServiceException(String messageKey, String parameter1, String parameter2, String parameter3, String parameter4, Exception exception) {
        super(messageKey, new Object[]{parameter1, parameter2, parameter3, parameter4}, exception);
    }
}
