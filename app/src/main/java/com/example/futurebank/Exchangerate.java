package com.example.futurebank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class Exchangerate extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchangerate);

        View mainView = findViewById(R.id.exchangeRateActivity);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hideKeyboard();
                }
                return false;
            }
        });
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void error(String message){
        Toasty.error(this, message, Toast.LENGTH_LONG, true).show();
    }

    public void convert(View v) {
        EditText from = findViewById(R.id.conversion);
        TextView to = findViewById(R.id.convertedTo);

        double num = Double.parseDouble(from.getText().toString());


        String apiKey = "59931a41a8b52a2d88422cbf";
        String sourceCurrency = "LKR";
        String targetCurrency = "USD";

        String url = "https://v6.exchangerate-api.com/v6/"+apiKey+"/latest/" + sourceCurrency ;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            double conversionRate = response.getJSONObject("conversion_rates").getDouble(targetCurrency);
                            double finalRate = conversionRate * num;
                            to.setText(String.format("%.2f "+sourceCurrency+" = %.2f "+targetCurrency, num, finalRate));
                            // Use the conversion rate for further calculations or display

                        } catch (JSONException e) {
                            error(e.getMessage().toString());
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error(error.getMessage().toString());
                    }
                });
            Volley.newRequestQueue(this).add(jsonObjectRequest);

    }
}