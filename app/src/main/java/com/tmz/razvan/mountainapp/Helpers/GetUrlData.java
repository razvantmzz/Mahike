package com.tmz.razvan.mountainapp.Helpers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.tmz.razvan.mountainapp.Interfaces.IHikeData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetUrlData extends AsyncTask<String, Void, String> {
    Context mContext;
    private String url;
    TaskLoadedCallback taskCallback;

    public GetUrlData(Context mContext) {
        this.mContext = mContext;
    }

    public GetUrlData setOnTaskLoadedCallback(TaskLoadedCallback taskCallback)
    {
        this.taskCallback = taskCallback;
        return this;
    }

    public GetUrlData execute(String url, HashMap<String, String> data)
    {
        String params = "";
        for (Map.Entry<String, String> entry:
             data.entrySet())
        {
            params = entry.getKey() + "=" + entry.getValue() + "&";
        }
        String finalUrl = url + "?" + params;
        this.url = finalUrl;
        super.execute(finalUrl);
        return this;
    }

    public GetUrlData execute(String url)
    {
        this.url = url;
        super.execute(url);
        return this;
    }

    @Override
    protected String doInBackground(String... strings) {
        // For storing data from web service
        String data = "";
        try {
            // Fetching the data from web service
            data = downloadUrl(strings[0]);
            if(taskCallback != null)
            {
                taskCallback.onTaskDone(data);
            }
            Log.d("mylog", "Background task data " + data.toString());
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("mylog", "Downloaded URL: " + data.toString());
            br.close();
        } catch (Exception e) {
            Log.d("mylog", "Exception downloading URL: " + e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
