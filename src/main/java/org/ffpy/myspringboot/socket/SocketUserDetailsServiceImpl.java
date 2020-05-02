package org.ffpy.myspringboot.socket;

import com.corundumstudio.socketio.SocketIOClient;
import org.ffpy.myspringboot.socketio.auth.AbstractTokenUserDetailsService;
import org.ffpy.myspringboot.socketio.auth.TokenAuthentication;
import org.springframework.stereotype.Service;

/**
 * @author wenlongsheng
 * @date 2020/2/27
 */
@Service
public class SocketUserDetailsServiceImpl extends AbstractTokenUserDetailsService {

    @Override
    public boolean verifyAuthentication(TokenAuthentication authentication) {
        return true;
    }

    @Override
    public Object getUser(SocketIOClient client) {
        return "{user: " + getAuthentication(client).getToken() + "}";
    }

    @Override
    public Long getUserId(SocketIOClient client) {
        return 1L;
    }
}
