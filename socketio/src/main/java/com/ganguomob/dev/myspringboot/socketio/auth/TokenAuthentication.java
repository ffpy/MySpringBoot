package com.ganguomob.dev.myspringboot.socketio.auth;

import lombok.Data;

/**
 * @author wenlongsheng
 * @date 2020/2/27
 */
@Data
public class TokenAuthentication implements Authentication {
    private final String token;
}
