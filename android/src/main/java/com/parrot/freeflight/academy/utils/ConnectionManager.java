package com.parrot.freeflight.academy.utils;

import android.content.Context;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.Country;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class ConnectionManager {
    Country f330c;
    private Context context;

    class C10501 implements Runnable {
        C10501() {
        }

        public void run() {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(ConnectionManager.this.context.getString(C0984R.string.url_countries).concat(ConnectionManager.this.f330c.getIso())).openConnection();
                httpURLConnection.connect();
                JSONObject jSONObject = new JSONObject(JSONParser.readStream(httpURLConnection.getInputStream()));
                ConnectionManager.this.f330c = new Country(jSONObject);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            } catch (JSONException e3) {
                e3.printStackTrace();
            }
        }
    }

    public ConnectionManager(Context context) {
        this.context = context;
    }

    public Runnable getInfo() {
        return new C10501();
    }
}
