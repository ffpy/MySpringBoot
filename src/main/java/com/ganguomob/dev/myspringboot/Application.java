package com.ganguomob.dev.myspringboot;

import com.ganguomob.dev.myspringboot.socketio.scan.SocketServiceScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SocketServiceScan
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args).start();
    }
}
