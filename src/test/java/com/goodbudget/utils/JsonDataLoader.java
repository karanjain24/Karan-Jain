package com.goodbudget.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class JsonDataLoader
{
    public static JsonObject loadJson(String filename)
    {
        InputStream inputStream = Objects.requireNonNull(
                JsonDataLoader.class.getClassLoader().getResourceAsStream(filename),
                "File not found: " + filename
        );

        return JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
    }
}
