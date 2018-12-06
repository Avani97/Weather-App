package com.avani.weatherapp1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText city;
    TextView result;
    public void findweather(View view) {
        InputMethodManager manage=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        manage.hideSoftInputFromWindow(city.getWindowToken(),0);

        DownloadTask task = new DownloadTask();

        try {
            String out = task.execute("http://api.openweathermap.org/data/2.5/weather?q="+city.getText().toString()+"&APPID=ea574594b9d36ab688642d5fbeab847e" ).get();
            JSONObject jj = new JSONObject(out);
            String visible=""+(Integer.parseInt(jj.getString("visibility"))/1000);
            String mainbaby=jj.getString("main");
            String tempinfo="";
            String outmini = jj.getString("weather");
            String main="";
            String message="";
            String description="";
            JSONObject tempmini=new JSONObject(mainbaby);

            JSONArray arr = new JSONArray(outmini);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jjmini = arr.getJSONObject(i);//we didnt use jjmini=new JSONObject(arr[i]) becuase it is not an array of strings, it is an array of JSON objects so we just have to GET them            }
                main= jjmini.getString("main");
              description= jjmini.getString("description");
                if(main!=""&&description!="")
                {
                    message=main+": "+description+"\r\n";
                }
            }

            double mintemp=Double.parseDouble(tempmini.getString("temp_min"))-273.15;
            double maxtemp=Double.parseDouble(tempmini.getString("temp_max"))-273.15;

            message+="Max Temperature: "+maxtemp+"°C"+"\r\n"+"Min Temperature: "+mintemp+"°C"+"\r\n"+"Humidity: "+tempmini.getString("humidity")+"%"+"\r\n"+"Visibility: "+visible+" km"+"\r\n";

            if(message!="")
            {
                result.setText(message);
            }

        } catch (Exception e) {
            result.setText("");
            Toast.makeText(MainActivity.this,"Please Enter A Valid City Name",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
       }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city=(EditText) findViewById(R.id.cityname);
        result=(TextView)findViewById(R.id.resulttext);

    }

        public class DownloadTask extends AsyncTask<String,Void,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            String result="";
            URL url;
            HttpURLConnection connection;
            try {
                url=new URL(strings[0]);
                connection=(HttpURLConnection)url.openConnection();
                InputStream in=connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1)
                {
                    char add=(char)data;
                    result+=add;
                    data=reader.read();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
