package com.hui1601.coffeeport;

import com.google.gson.Gson;
import com.hui1601.coffeeport.constant.Config;
import com.hui1601.coffeeport.dto.ResponseDTO;
import com.hui1601.coffeeport.dto.TaskDTO;
import com.hui1601.coffeeport.handler.CommandHandler;
import com.hui1601.coffeeport.utils.ResponseUtil;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends NanoHTTPD {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());
    public Main() throws IOException {
//        원래 값을 적당히 가져와서 호스트, 포트를 바꿀 수 있도록 할려 했으나, 보안 문제가 있을 수 있어 제한함.
        super(Config.host, Config.httpPort);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        LOG.info("Coffeeport Started!");
    }

    public static void main(String[] args) {
        LOG.info("Waiting for Ready...");
        try {
            new Main();
        } catch (IOException|RuntimeException err){
            LOG.severe("Failed to start Coffeeport");
            err.printStackTrace();
        }
    }
    @Override
    public Response serve(IHTTPSession session){
        Method method = session.getMethod();
        String uri = session.getUri();
        ResponseDTO response = new ResponseDTO();
        TaskDTO task = new TaskDTO();
        LOG.log(Level.OFF, method.toString() + ": " + uri);
        Map<String, List<String>> parameters = session.getParameters();
        if(parameters.get("callback") == null || parameters.get("data") == null || parameters.get("callback").size() != 1 || parameters.get("data").size() != 1 || parameters.get("callback").get(0).isEmpty() || parameters.get("data").get(0).isEmpty()) {
            LOG.warning("Invalid request!");
            response.setRes(-1);
            response.setData("Invalid request!");
            return ResponseUtil.getCallback("alert", response);
        }
        task.setCallback(parameters.get("callback").get(0));
        task.setData(parameters.get("data").get(0));

        // handle command
        CommandHandler.handleCommand(response, task);
        return ResponseUtil.getCallback(task.getCallback(), response);
    }
}
