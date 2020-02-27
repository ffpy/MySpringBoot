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

    @Override
    public Authentication loadAuthentication(HandshakeData data) {
        String token = data.getSingleUrlParam(PARAM_TOKEN);
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return new TokenAuthentication(token);
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
