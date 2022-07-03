package com.hui1601.coffeeport;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hui1601.coffeeport.constant.Config;
import com.hui1601.coffeeport.dto.ResponseDTO;
import com.hui1601.coffeeport.dto.TaskDTO;
import com.hui1601.coffeeport.handler.CommandHandler;
import com.hui1601.coffeeport.handler.TaskHandler;
import com.hui1601.coffeeport.sigleton.TaskListManager;
import com.hui1601.coffeeport.utils.FileUtil;
import com.hui1601.coffeeport.utils.ResponseUtil;
import com.hui1601.coffeeport.utils.SystemUtil;
import fi.iki.elonen.NanoHTTPD;

import javax.net.ssl.KeyManagerFactory;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends NanoHTTPD {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());
    public Main() throws IOException {
//        원래 값을 적당히 가져와서 호스트, 포트를 바꿀 수 있도록 할려 했으나, 보안 문제가 있을 수 있어 제한함.
        super(Config.host, Config.httpsPort);
        loadKeyStore();
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        LOG.info("Coffeeport Started!");
    }

    private void loadKeyStore(){
        try {
            if(!FileUtil.exists(SystemUtil.getKeystorePath())){
                KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
                char[] pwdArray = SystemUtil.getHashedUUID().toCharArray();
                ks.load(null, pwdArray);
                ks.store(new FileOutputStream(SystemUtil.getKeystorePath()), pwdArray);
                LOG.info("KeyStore Created!");
            }

            super.makeSecure();
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        LOG.info("Waiting for Ready...");
        try {
            new Main();
            new TaskHandler().start();
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
        Map<String, List<String>> parameters = session.getParameters();
        LOG.log(Level.OFF, method.toString() + ": " + uri);
        if(method == Method.POST){
            Map <String, String> body = new HashMap<>();
            try {
                session.parseBody(body);
            } catch (IOException|ResponseException e) {
                return NanoHTTPD.newFixedLengthResponse("");
            }
            JsonElement element = new Gson().fromJson(parameters.get("data").get(0), JsonElement.class);
            JsonObject obj = element.getAsJsonObject();
            task.setData(new Gson().toJson(obj.get("data")));
            TaskListManager.getInstance().setTask(obj.get("sid").getAsString(), task);
            return NanoHTTPD.newFixedLengthResponse("");
        }
        else if(!parameters.containsKey("callback") || !parameters.containsKey("data")) {
            LOG.warning("Invalid request!");
            response.setRes(-1);
            response.setData("Invalid request!");
            return NanoHTTPD.newFixedLengthResponse("try {alert(\"Invalid Request!\");} catch(e){}");
        }
        else {
            task.setCallback(parameters.get("callback").get(0));
            LOG.info(parameters.get("data").get(0));
            task.setData(parameters.get("data").get(0));
            if(parameters.containsKey("sid")) task.setSid(parameters.get("sid").get(0));
            if(task.getData().getSid() != null) task.setSid(task.getData().getSid());
            // handle command
            CommandHandler.handleCommand(response, task);
            return ResponseUtil.getCallback(task.getCallback(), response);
        }
    }
}
