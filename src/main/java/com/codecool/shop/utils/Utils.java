package com.codecool.shop.utils;

import com.codecool.shop.model.Order;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.util.Random;

public class Utils {

    private static final Random RANDOM = new Random();
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public static int randomTwoDigitId() {
        return RANDOM.nextInt(90) + 10;
    }

    public static void writeOrderToFile(Order order) {
        String json = gson.toJson(order);
        try {
            FileWriter fw = new FileWriter("Order- " + order.getId() + ".json");
            fw.write(json);
            fw.close();
        }
        catch (Exception e) {
            e.getStackTrace();
        }

    }
}
