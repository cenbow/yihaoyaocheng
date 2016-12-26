package com.yyw.yhyc.exception;

/**
 * Created by lizhou on 2016/12/1
 * 自定义异常
 */
public class ServiceException extends Exception{

    /**
     * <p/>
     * Default constructor, which simply delegates exception
     * handling up the inheritance chain to <code>Exception</code>.
     * </p>
     */
    public ServiceException() {
        super();
    }

    /**
     * <p/>
     * This constructor allows a message to be supplied indicating the source
     * of the problem that occurred.
     * </p>
     *
     * @param message <code>String</code> identifying the cause of the problem.
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * <p/>
     * This constructor allows a "root cause" exception to be supplied,
     * which may later be used by the wrapping application.
     * </p>
     *
     * @param rootCause <code>Throwable</code> that triggered the problem.
     */
    public ServiceException(Throwable rootCause) {
        super(rootCause);
    }

    /**
     * This constructor allows both a message identifying the
     * problem that occurred as well as a "root cause" exception
     * to be supplied, which may later be used by the wrapping
     * application.
     *
     * @param message   <code>String</code> identifying the cause of the problem.
     * @param rootCause <code>Throwable</code> that triggered this problem.
     */
    public ServiceException(String message, Throwable rootCause) {
        super(message, rootCause);
    }
}
