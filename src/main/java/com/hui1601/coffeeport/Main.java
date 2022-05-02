package com.hui1601.coffeeport;

import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;

public class Main extends NanoHTTPD {
    public Main() throws IOException {
//        원래 값을 적당히 가져와서 호스트, 포트를 바꿀 수 있도록 할려 했으나, 보안 문제가 있을 수 있어 제한함.
        super("127.0.0.1", 16106);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    public static void main(String[] args) {
        System.out.println("Waiting for Ready...");
        try {
            new Main();
        } catch (IOException|RuntimeException err){
            System.out.println("Failed to start Coffeeport");
            err.printStackTrace();
        }
    }
}
