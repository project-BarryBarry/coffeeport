package com.barrybarry.coffeeport;

import com.barrybarry.coffeeport.constant.Config;
import com.barrybarry.coffeeport.handler.TaskHandler;
import com.barrybarry.coffeeport.router.VeraportRouter;
import com.barrybarry.coffeeport.utils.SecurityUtil;
import com.barrybarry.coffeeport.utils.SystemUtil;
import com.linecorp.armeria.server.Server;
import com.linecorp.armeria.server.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyStore;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        TaskHandler th = new TaskHandler();
        try {
            ServerBuilder sb = Server.builder();
            Server server;
            InetSocketAddress https = new InetSocketAddress(InetAddress.getByName(Config.host), Config.httpsPort);
            logger.info("Waiting for Ready...");
            sb.https(https);
            sb.tlsSelfSigned();
            KeyStore keyStore = SecurityUtil.getKeystore();
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, SystemUtil.getHashedUUID().toCharArray());
            sb.tls(keyManagerFactory);
            sb.annotatedService(new VeraportRouter());
            server = sb.build();
            server.closeOnJvmShutdown();
            th.start();
            server.start().join();
        } catch (Exception err) {
            logger.error("Failed to start Coffeeport", err);
            th.interrupt();
        }
    }
}
