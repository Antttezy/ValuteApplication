package com.antkumachev.valuteapplication.util;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class DownloadTask extends AsyncTask<String, Void, String> {

    private String downloadFromUrl(String url) throws InterruptedException {
        String data = "";

        try {
            URL downloadUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) downloadUrl.openConnection();
            connection.setConnectTimeout(15000);

            Charset charset = Charset.forName("Windows-1251");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));

            String line;
            while ((line = reader.readLine()) != null) {
                data += line;
            }

            reader.close();
        }
        catch (Exception e){
            Log.d("DownloadError", e.getMessage());
        }

        return data;
    }

    @Override
    protected String doInBackground(String... strings) {
        String data = "";

        try {
            data =  downloadFromUrl(strings[0]);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        return data;
    }
}
