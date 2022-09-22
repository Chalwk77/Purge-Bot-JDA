// Copyright (c) 2022, Jericho Crosby <jericho.crosby227@gmail.com>

package com.jericho.purgebot.Utilities;
import org.json.JSONObject;
import java.io.*;

public class FileIO {

    // Path to resources file:
    private static final String path = "src/main/resources/";

    //
    // Load a JSON Object from file (at the moment this only loads settings.json):
    //
    public static JSONObject loadJSONObject(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path + file));
        String line = reader.readLine();
        StringBuilder stringBuilder = new StringBuilder();
        while (line != null) {
            stringBuilder.append(line);
            line = reader.readLine();
        }
        String content = stringBuilder.toString();
        if (content.equals("")) {
            return new JSONObject();
        } else {
            return new JSONObject(content);
        }
    }
}
