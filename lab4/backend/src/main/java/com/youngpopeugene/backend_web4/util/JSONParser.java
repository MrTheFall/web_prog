package com.example.AreaChecker.util;

import com.example.AreaChecker.controller.GlobalBean;
import com.example.AreaChecker.model.entity.Shot;
import jakarta.ejb.EJB;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONParser {
    @EJB
    private static GlobalBean globalBean;
    public static String toJSON(ArrayList<Shot> collection) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        if (collection.size() != 0) {
            for (int i = 0; i < collection.size(); i++) {
                if (i != collection.size() - 1) {
                    json.append(toJSON(collection.get(i))).append(",\n");
                } else {
                    json.append(toJSON(collection.get(i))).append("\n");
                }
            }
        }
        json.append("]");
        return json.toString();
    }

    public static String toJSON(Shot shot) {
        return "    {\n" +
                "       \"id\": \"" + shot.getId() + "\",\n" +
                "       \"X\": \"" + shot.getX() + "\",\n" +
                "       \"Y\": \"" + shot.getY() + "\",\n" +
                "       \"R\": \"" + shot.getR() + "\",\n" +
                "       \"script_time\": \"" + shot.getScriptTime() + "\",\n" +
                "       \"date\": \"" + shot.getCurrentTime() + "\",\n" +
                "       \"status\": \"" + shot.isStatus() + "\"\n" +
                "   }";
    }

    public static String usersToJSON(ArrayList<String> collection) {
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        if (collection.size() != 0) {
            for (int i = 0; i < collection.size(); i++) {
                if (i != collection.size() - 1) {
                    json.append(collection.get(i)).append(",\n");
                } else {
                    json.append(collection.get(i)).append("\n");
                }
            }
        }
        json.append("]");
        return json.toString();
    }

}
