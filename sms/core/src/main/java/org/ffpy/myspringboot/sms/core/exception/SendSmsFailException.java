package org.ffpy.myspringboot.sms.core.exception;

public class SendSmsFailException extends Exception {

    /** 失败响应数据 */
    private String response;

    public SendSmsFailException(String response) {
        this(response, null);
    }

    public SendSmsFailException(Throwable t) {
        super(t);
    }

    public SendSmsFailException(String response, Throwable t) {
        super(response, t);
        this.response = response;
    }

    @Override
    public String getMessage() {
        return response == null ? super.getMessage() : response;
    }
}
