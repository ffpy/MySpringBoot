package com.ganguomob.dev.myspringboot.socketio.auth;

import com.corundumstudio.socketio.HandshakeData;
import com.corundumstudio.socketio.SocketIOClient;
import com.ganguomob.dev.myspringboot.socketio.service.SocketUserDetailsService;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wenlongsheng
 * @date 2020/2/27
 */
public abstract class AbstractTokenUserDetailsService implements SocketUserDetailsService {
    private static final String PARAM_TOKEN = "token";
    private static final String KEY_TOKEN = "token";

    /**
     * 验证Authentication
     */
    public abstract boolean verifyAuthentication(TokenAuthentication authentication);

    /**
     * 解码Token
     */
    public String decodeToken(String token) {
        return token;
    }

    @Override
    public Authentication loadAuthentication(HandshakeData data) {
        String token = data.getSingleUrlParam(PARAM_TOKEN);
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return new TokenAuthentication(decodeToken(token));
    }

    @Override
    public boolean verifyAuthentication(Authentication authentication) {
        return verifyAuthentication((TokenAuthentication) authentication);
    }

    @Override
    public void setAuthentication(SocketIOClient client, Authentication authentication) {
        client.set(KEY_TOKEN, authentication);
    }

    @Override
    public TokenAuthentication getAuthentication(SocketIOClient client) {
        return client.get(KEY_TOKEN);
    }
}
