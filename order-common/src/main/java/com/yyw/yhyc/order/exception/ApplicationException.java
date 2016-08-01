//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyw.yhyc.order.exception;

import com.yyw.yhyc.order.utils.ResourceBundleMessageSourceHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class ApplicationException extends RuntimeException {
    private Log logger = LogFactory.getLog(this.getClass());
    private String message;
    private static final long serialVersionUID = 1L;

    protected ApplicationException() {
    }

    protected ApplicationException(String messageKey) {
        super(messageKey);
        this.setMessage(messageKey, (Object[])null, (Exception)null);
    }

    protected ApplicationException(String messageKey, Object[] parameters) {
        super(messageKey);
        this.setMessage(messageKey, parameters, (Exception)null);
    }

    protected ApplicationException(String messageKey, Object[] parameters, Exception exception) {
        super(messageKey);
        this.setMessage(messageKey, parameters, exception);
    }

    protected void printExcStackTrace(Exception e) {
        if(null != e) {
            this.logger.error("", e);
            e.printStackTrace();
        }

    }

    private void setMessage(String messageKey, Object[] parameters, Exception exception) {
        if(null != messageKey) {
            try {
                this.message = ResourceBundleMessageSourceHelper.getMessage(messageKey, parameters);
            } catch (Exception var5) {
                this.message = var5.getMessage();
                this.printExcStackTrace(var5);
            }
        }

        this.printExcStackTrace(exception);
    }

    public String getMessage() {
        return this.message;
    }
}
