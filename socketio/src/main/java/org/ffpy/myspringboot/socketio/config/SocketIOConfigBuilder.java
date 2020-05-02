package org.ffpy.myspringboot.socketio.config;

import com.corundumstudio.socketio.AckMode;
import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.Transport;
import com.corundumstudio.socketio.listener.ExceptionListener;
import com.corundumstudio.socketio.store.StoreFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ffpy.myspringboot.socketio.util.SocketServiceUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

/**
 * @author wenlongsheng
 * @date 2020/2/27
 */
public class SocketIOConfigBuilder {
    private Configuration config = new Configuration();

    public SocketIOConfigBuilder() {
        config.setRandomSession(true);
    }

    public SocketIOConfigBuilder setHostname(String hostname) {
        if (StringUtils.isEmpty(hostname)) {
            throw new IllegalArgumentException("hostname不能为空");
        }
        config.setHostname(hostname);
        return this;
    }

    public SocketIOConfigBuilder setPort(int port) {
        config.setPort(port);
        return this;
    }

    public SocketIOConfigBuilder setRandomSession(boolean randomSession) {
        config.setRandomSession(randomSession);
        return this;
    }

    public SocketIOConfigBuilder setObjectMapper(@Nullable ObjectMapper objectMapper) {
        if (objectMapper != null) {
            config.setJsonSupport(SocketServiceUtils.createJsonSupport(objectMapper));
        }
        return this;
    }

    public SocketIOConfigBuilder setBossThreads(int bossThreads) {
        config.setBossThreads(bossThreads);
        return this;
    }

    public SocketIOConfigBuilder setWorkerThreads(int workerThreads) {
        config.setWorkerThreads(workerThreads);
        return this;
    }

    /**
     * Ping interval
     *
     * @param heartbeatIntervalSecs - time in milliseconds
     */
    public SocketIOConfigBuilder setPingInterval(int heartbeatIntervalSecs) {
        config.setPingInterval(heartbeatIntervalSecs);
        return this;
    }

    /**
     * Ping timeout
     * Use <code>0</code> to disable it
     *
     * @param heartbeatTimeoutSecs - time in milliseconds
     */
    public SocketIOConfigBuilder setPingTimeout(int heartbeatTimeoutSecs) {
        config.setPingTimeout(heartbeatTimeoutSecs);
        return this;
    }

    /**
     * Transports supported by server
     *
     * @param transports - list of transports
     */
    public SocketIOConfigBuilder setTransports(Transport... transports) {
        config.setTransports(transports);
        return this;
    }

    /**
     * SSL key store password
     *
     * @param keyStorePassword - password of key store
     */
    public SocketIOConfigBuilder setKeyStorePassword(String keyStorePassword) {
        config.setKeyStorePassword(keyStorePassword);
        return this;
    }

    /**
     * Data store - used to store session data and implements distributed pubsub.
     * Default is {@code MemoryStoreFactory}
     *
     * @param clientStoreFactory - implements StoreFactory
     * @see com.corundumstudio.socketio.store.MemoryStoreFactory
     * @see com.corundumstudio.socketio.store.RedissonStoreFactory
     * @see com.corundumstudio.socketio.store.HazelcastStoreFactory
     */
    public SocketIOConfigBuilder setStoreFactory(StoreFactory clientStoreFactory) {
        config.setStoreFactory(clientStoreFactory);
        return this;
    }

    /**
     * Authorization listener invoked on every handshake.
     * Accepts or denies a client by {@code AuthorizationListener.isAuthorized} method.
     * <b>Accepts</b> all clients by default.
     *
     * @param listener - authorization listener itself
     * @see com.corundumstudio.socketio.AuthorizationListener
     */
    public SocketIOConfigBuilder setAuthorizationListener(AuthorizationListener listener) {
        config.setAuthorizationListener(listener);
        return this;
    }

    /**
     * Exception listener invoked on any exception in
     * SocketIO listener
     *
     * @param listener - listener
     * @see com.corundumstudio.socketio.listener.ExceptionListener
     */
    public SocketIOConfigBuilder setExceptionListener(ExceptionListener listener) {
        config.setExceptionListener(listener);
        return this;
    }

    /**
     * Auto ack-response mode
     * Default is {@code AckMode.AUTO_SUCCESS_ONLY}
     *
     * @param ackMode - ack mode
     * @see AckMode
     */
    public SocketIOConfigBuilder setAckMode(AckMode ackMode) {
        config.setAckMode(ackMode);
        return this;
    }

    /**
     * Set <b>Access-Control-Allow-Origin</b> header value for http each
     * response.
     * Default is <code>null</code>
     * <p>
     * If value is <code>null</code> then request <b>ORIGIN</b> header value used.
     *
     * @param origin - origin
     */
    public SocketIOConfigBuilder setOrigin(String origin) {
        config.setOrigin(origin);
        return this;
    }

    /**
     * Activate http protocol compression. Uses {@code gzip} or
     * {@code deflate} encoding choice depends on the {@code "Accept-Encoding"} header value.
     * <p>
     * Default is <code>true</code>
     *
     * @param httpCompression - <code>true</code> to use http compression
     */
    public SocketIOConfigBuilder setHttpCompression(boolean httpCompression) {
        config.setHttpCompression(httpCompression);
        return this;
    }

    /**
     * Activate websocket protocol compression.
     * Uses {@code permessage-deflate} encoding only.
     * <p>
     * Default is <code>true</code>
     *
     * @param websocketCompression - <code>true</code> to use websocket compression
     */
    public SocketIOConfigBuilder setWebsocketCompression(boolean websocketCompression) {
        config.setWebsocketCompression(websocketCompression);
        return this;
    }

    public Configuration build() {
        return config;
    }
}
