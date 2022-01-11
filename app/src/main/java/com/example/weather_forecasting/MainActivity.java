package com.example.weather_forecasting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_get;
    EditText txt_city;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_city = findViewById(R.id.editTextTextPersonName);
        btn_get = findViewById(R.id.button);
        btn_get.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        requestQueue = Volley.newRequestQueue(this);

    }

    @Override
    public void onClick(View view) {
        progressDialog.setMessage("Retrieving Whether information...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String city_name = txt_city.getText().toString();
        String URL_NAME = "https://api.openweathermap.org/data/2.5/weather?appid=9ba99ec6ef17d33fa9b994ec7bb53b9e&q="+ city_name;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(URL_NAME, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    progressDialog.dismiss();
                    if(response.getString("cod").equals("404"))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(response.getString("message"));
                        builder.setTitle("Weather Forecasting");
                        builder.show();
                        return;
                    }
                    //AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    JSONObject object = response.getJSONObject("main");
                    int temp =object.getInt("temp")-273;
                    double temp_min= object.getDouble("temp_min");
                    double temp_max= object.getDouble("temp_max");
                    int humidity = object.getInt("humidity");
                    StringBuilder builder= new StringBuilder();
                    builder.append("Temperature : ").append(temp).append("\n").append("Minimum temperature : "+temp_min+"\n").append("Maximum temperature :"+temp_max+"\n").append("Humidity : "+humidity+"\n");
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                    builder2.setMessage(builder.toString());
                    builder2.setTitle("Weather Forecasting");
                    //builder2.setIcon();
                    builder2.show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "An Error Occured "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}