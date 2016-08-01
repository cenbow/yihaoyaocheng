//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.yyw.yhyc.order.utils;

import com.yyw.yhyc.order.helper.SpringBeanHelper;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

public class ResourceBundleMessageSourceHelper {
    public ResourceBundleMessageSourceHelper() {
    }

    public static String getMessage(String messageKey) {
        return getMessage(messageKey, (Object[])null);
    }

    public static String getMessage(String messageKey, Object[] parameters) {
        ResourceBundleMessageSource messageSource = (ResourceBundleMessageSource) SpringBeanHelper.getBean("messageSource");
        String message = messageSource.getMessage(messageKey, parameters, (Locale)null);
        return message;
    }
}
