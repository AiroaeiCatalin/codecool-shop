package com.codecool.shop.utils;

import com.codecool.shop.model.Order;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AdminLog {
    private static final Logger logger = LoggerFactory.getLogger(AdminLog.class);

    private static AdminLog instance = null;

    private final List<String> logs;

    private AdminLog() {
        logs = new ArrayList<>();
    }

    public static AdminLog getInstance() {
        if (instance == null) {
            instance = new AdminLog();
        }
        return instance;
    }

    public void addLog(String addedLog) {
        // logging each message and also adding it to an admin log file
        logger.info(addedLog);
        logs.add(addedLog);
    }

    public void writeLogsToFile(Order order) {
        try {
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < logs.size(); i++) {
                jsonObject.put("Operation " + i, logs.get(i));
            }
            FileWriter fileWriter = new FileWriter(order.getId() + " - " + order.getLocalDate() + ".json");
            fileWriter.write(jsonObject.toJSONString());
            fileWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
