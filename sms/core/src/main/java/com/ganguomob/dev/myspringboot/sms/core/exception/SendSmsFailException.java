package com.ganguomob.dev.myspringboot.sms.core.exception;

/**
 * 短信发送失败异常
 *
 * @author wenlongsheng
 */
public class SendSmsFailException extends Exception {

    /** 发送失败时的响应数据 */
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
