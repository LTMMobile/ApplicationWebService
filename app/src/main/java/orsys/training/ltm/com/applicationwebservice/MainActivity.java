package orsys.training.ltm.com.applicationwebservice;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;/**/
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> _arrayStringImages = new ArrayList<String>();
    private Button _b_req_images, _b_req_WS;
    private String _WS = "http://maps.googleapis.com/maps/api/geocode/json?address=rue+de+La+paix+paris+France";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // remplir le tableau
        fillImagesArray();

        // Bouton requêtes images
        _b_req_images = (Button)findViewById(R.id.button_req_images);
        if( _b_req_images != null ) {
            _b_req_images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _b_req_images.setEnabled(false);
                    new DownloadImages().execute();
                }
            });
        }

        // Bouton requête WS en JSON
        _b_req_WS = (Button)findViewById(R.id.button_req_json);
        if(_b_req_WS != null){
            _b_req_WS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CallWS().execute();
                }
            });
        }
    }

    class CallWS extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = null;
                url = new URL(_WS);
                HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                //Log.v("ltm", convertStreamToString(is));
                Scanner s = new Scanner(is).useDelimiter("\\A");
                String result = s.hasNext() ? s.next() : "";
                Log.v("ltm", result);
                publishProgress(result);

            }catch(java.lang.Exception ex){
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            JSONObject o = null;
            try {
                o = new JSONObject(values[0]);
                o.getJSONObject("results");
                // etc.
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class DownloadImages extends AsyncTask<Void, Bitmap, Void> {

        private long _timing;

        // Exécution dans le Thread background (non UI)
        @Override
        protected Void doInBackground(Void... params)  {

            for(int t=0; t<_arrayStringImages.size(); t++) {
                URL url = null;
                try {
                    url = new URL(_arrayStringImages.get(t));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                    if( is != null ) {
                        Bitmap bmp = BitmapFactory.decodeStream(is);
                        publishProgress(bmp);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //if( bmp != null ) // KO
                        //_imageView.setImageBitmap( bmp ); // KO
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finally {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        // Exécution dans le Thread UI
        @Override
        protected void onPreExecute() {
            _timing = System.currentTimeMillis();
            super.onPreExecute();
        }

        // Exécution dans le Thread UI
        @Override
        protected void onProgressUpdate(Bitmap... values) {
            ImageView _imageView = (ImageView)findViewById(R.id.imageView1);
            _imageView.setImageBitmap( values[0] );

            super.onProgressUpdate(values);
        }

        // Exécution dans le Thread UI
        @Override
        protected void onPostExecute(Void s) {
            _timing = System.currentTimeMillis() - _timing;
            _b_req_images.setEnabled(true);
            super.onPostExecute(s);
        }

    }

    private void fillImagesArray() {
        _arrayStringImages.add( "http://farm9.static.flickr.com/8023/7629927952_8454fe9895_s.jpg" );
        _arrayStringImages.add( "http://farm9.static.flickr.com/8424/7629929272_9a1fcfe553_s.jpg" );
        _arrayStringImages.add( "http://farm9.static.flickr.com/8142/7629943170_ed5255e984_s.jpg" );
        _arrayStringImages.add( "http://farm9.static.flickr.com/8430/7629930796_e96f1d4d19_s.jpg" );
        _arrayStringImages.add( "http://farm8.static.flickr.com/7246/7629933356_c96f1844d6_s.jpg" );
        _arrayStringImages.add( "http://farm8.static.flickr.com/7139/7629932118_b745bfb543_s.jpg" );
        _arrayStringImages.add( "http://farm9.static.flickr.com/8424/7629934640_c876c3175f_s.jpg" );
        _arrayStringImages.add( "http://farm9.static.flickr.com/8150/7629935970_b8ba43d0c4_s.jpg" );
        _arrayStringImages.add( "http://farm9.static.flickr.com/8022/7629936684_da7498b0c5_s.jpg" );
        _arrayStringImages.add( "http://farm9.static.flickr.com/8004/7629948368_61d4ecaa68_s.jpg" );
        _arrayStringImages.add( "http://farm9.static.flickr.com/8286/7629950334_a315811f79_s.jpg" );
        _arrayStringImages.add( "http://farm8.static.flickr.com/7271/7629951222_83dcbf2da4_s.jpg" );
        _arrayStringImages.add( "http://farm9.static.flickr.com/8001/7629951674_8af891f39c_s.jpg" );
        _arrayStringImages.add( "http://farm9.static.flickr.com/8434/7629952540_3461c2fc9c_s.jpg" );
        _arrayStringImages.add( "http://farm9.static.flickr.com/8158/7629952122_837ba7d88f_s.jpg" );
        _arrayStringImages.add( "http://farm8.static.flickr.com/7263/7629952972_f09ee61823_s.jpg" );
        _arrayStringImages.add( "http://farm8.static.flickr.com/7273/7629953386_9cb06b80cb_s.jpg" );
        _arrayStringImages.add( "http://farm9.static.flickr.com/8003/7629954082_0598efc125_s.jpg" );
    }

    private String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {
            return "";
        }
    }
}
