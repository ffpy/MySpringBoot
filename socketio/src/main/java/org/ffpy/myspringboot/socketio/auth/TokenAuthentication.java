package org.ffpy.myspringboot.socketio.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenlongsheng
 * @date 2020/2/27
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenAuthentication implements Authentication {
    private String token;
}
