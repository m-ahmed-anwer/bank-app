package com.example.futurebank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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

    private ProgressDialog progressDialog;
    private String selectedItem2;
    private String selectedItem1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchangerate);

        View mainView = findViewById(R.id.scrollView2);
        mainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    hideKeyboard();
                }
                return false;
            }
        });

        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner3);


        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.currencies, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner2.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle item selection
                selectedItem1 = (String) parent.getItemAtPosition(position);
                spinner.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when no item is selected
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle item selection
                selectedItem2 = (String) parent.getItemAtPosition(position);
                spinner2.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when no item is selected
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
        view.clearFocus();
    }
    public void error(String message){
        Toasty.error(this, message, Toast.LENGTH_LONG, true).show();
    }

    public void back(View v){
        Intent i = new Intent(this,HomeActivity.class);
        startActivity(i);
    }
    public void convert(View v) {
        EditText from = findViewById(R.id.conversion);
        TextView to = findViewById(R.id.convertedTo);

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Calculating");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String amountCheck =from.getText().toString().trim();

        if(amountCheck.isEmpty()==false){
            double amount = Double.parseDouble(from.getText().toString());


            final String apiKey = "59931a41a8b52a2d88422cbf";
            String sourceCurrency = selectedItem1;
            String targetCurrency = selectedItem2;

            String url = "https://v6.exchangerate-api.com/v6/"+apiKey+"/latest/" + sourceCurrency ;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            hideKeyboard();
                            try {
                                double conversionRate = response.getJSONObject("conversion_rates").getDouble(targetCurrency);
                                double finalRate = conversionRate * amount;
                                to.setText(String.format("%.2f "+targetCurrency, finalRate));
                                progressDialog.dismiss();

                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                error(e.getMessage().toString());
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            error(error.getMessage().toString());
                        }
                    });
            Volley.newRequestQueue(this).add(jsonObjectRequest);

        }else{
            progressDialog.dismiss();
            Toasty.info(this, "Enter amount to convert", Toast.LENGTH_SHORT, true).show();
        }

    }
}