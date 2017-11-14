package com.stevenodecreation.gstbill.util;

import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.stevenodecreation.gstbill.GstBillApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Created by Lenovo on 12/10/2017.
 */

public final class FileUtil {

    public static <T> T getFromAssetsFolder(String filename, Type type) {
        AssetManager manager = GstBillApplication.getInstance().getAssets();
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        T object = null;
        try {
            reader = new BufferedReader(new InputStreamReader(manager.open(filename)));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                response.append(mLine);
            }
            object = new Gson().fromJson(response.toString(), type);

        } catch (IOException | JsonSyntaxException e) {
          e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return object;
    }
}
