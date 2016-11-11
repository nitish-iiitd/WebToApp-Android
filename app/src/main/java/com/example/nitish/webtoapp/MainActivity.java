package com.example.nitish.webtoapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getAboutUs(View v) {

        if (!connected()) {
            Toast.makeText(this, "No Internet Connectivity!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

            new GetAboutUsTask().execute();

//            String text = "This is text";
//            TextView aboutusview = (TextView) findViewById(R.id.aboutus);
//            aboutusview.setText(text);
        }
    }

    public boolean connected() {
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() ==
                android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() ==
                        android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() ==
                        android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() ==
                                android.net.NetworkInfo.State.DISCONNECTED) {
            //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    public static String getHtml(String url) throws IOException {
        // Build and set timeout values for the request.
        URLConnection connection = (new URL(url)).openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();

        // Read and store the result line by line then return the entire string.
        InputStream in = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder html = new StringBuilder();
        for (String line; (line = reader.readLine()) != null; ) {
            html.append(line);
        }
        in.close();

        return html.toString();
    }


    private class GetAboutUsTask extends AsyncTask<Void, Void, Document> {


        @Override
        protected Document doInBackground(Void... params)
        {
           // String content="hello";

            Document doc = null;
            try {
                doc = Jsoup.connect("https://www.iiitd.ac.in/about").get();
                System.out.println(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return doc;

        }

        @Override
        protected void onPostExecute(Document document) {
            //if you had a ui element, you could display the title
            //((TextView)findViewById (R.id.myTeoaxtView)).setText (result);
            if (document == null) {
                Toast.makeText(getApplicationContext(), "Cannot load document !", Toast.LENGTH_SHORT).show();
            } else {
                TextView aboutusview = (TextView) findViewById(R.id.aboutus);
                aboutusview.setMovementMethod(new ScrollingMovementMethod());
                String content = "";
                Elements els = document.select("p");
                int i=0;

               // content = "<html><body><p align=\"justify\">";



                for(Element ele:els)
                {
                    if(i>5)
                    {
                        content = content + ele.text()+"\n";
                    }
                    i++;

                }
               // content+= "</p></body></html>";

                aboutusview.setText(content);

            }
        }

    }
}
