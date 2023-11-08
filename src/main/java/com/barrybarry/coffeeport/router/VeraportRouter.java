package com.barrybarry.coffeeport.router;

import com.barrybarry.coffeeport.converter.VeraDataConverter;
import com.barrybarry.coffeeport.data.*;
import com.barrybarry.coffeeport.data.task.TaskData;
import com.barrybarry.coffeeport.data.task.VeraData;
import com.barrybarry.coffeeport.data.xml.Data;
import com.barrybarry.coffeeport.data.xml.VeraTaskData;
import com.barrybarry.coffeeport.handler.CommandHandler;
import com.barrybarry.coffeeport.sigleton.TaskListManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.linecorp.armeria.common.HttpRequest;
import com.linecorp.armeria.server.ServiceRequestContext;
import com.linecorp.armeria.server.annotation.Get;
import com.linecorp.armeria.server.annotation.Param;
import com.linecorp.armeria.server.annotation.Post;
import com.linecorp.armeria.server.annotation.ResponseConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VeraportRouter {
    private static final Logger logger = LoggerFactory.getLogger(VeraportRouter.class);

    @ResponseConverter(VeraDataConverter.class)
    @Get("/")
    public VeraData getTaskResultRouter(ServiceRequestContext ctx, HttpRequest req, @Param("data") String data, @Param("callback") String callback) {
        VeraData result = new VeraData();
        TaskData reqTask = new TaskData();
        ResponseData response = new ResponseData();
        reqTask.setCallback(callback);
        reqTask.setData(data);
        reqTask.setSid(reqTask.getData().getSid());
        result.setCallback(callback);
        CommandHandler.handleCommand(response, reqTask);
        result.setResponseData(response);
        return result;
    }

    @Post("/")
    public String taskPlanningRouter(ServiceRequestContext ctx, HttpRequest req, @Param("data") String data) {
        TaskData task = new TaskData();
        JsonElement element = new Gson().fromJson(data, JsonElement.class);
        JsonObject obj = element.getAsJsonObject();
        task.setData(new Gson().fromJson(obj.get("data").getAsJsonObject(), VeraTaskData.class));
        task.getData().setData(new Gson().fromJson(obj.get("data").getAsJsonObject(), Data.class));
        task.setOrigin(req.headers().get("origin"));
        TaskListManager.getInstance().setTask(obj.get("sid").getAsString(), task);
        return "";
    }
}
