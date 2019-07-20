package com.developer.samplepaymentapp;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import instamojo.library.InstapayListener;
import instamojo.library.InstamojoPay;
import instamojo.library.Config;
import org.json.JSONObject;
import org.json.JSONException;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private Button do_payment;
    private EditText email,phone,amount,purpose,buyer_name;
    private boolean validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        do_payment=findViewById(R.id.do_payment);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        amount=findViewById(R.id.amount);
        purpose=findViewById(R.id.purpose);
        buyer_name=findViewById(R.id.buyer_name);

        do_payment.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                if (doValidation()){
                    callInstamojoPay(email.getText().toString(), phone.getText().toString()
                            , amount.getText().toString(), purpose.getText().toString(), buyer_name.getText().toString());
                }
            }
        });
    }

    boolean doValidation() {
        validate = true;

        if (TextUtils.isEmpty(email.getText().toString())) {
            validate = false;
            email.setError("Field can't be empty");
            email.requestFocus();
        }
        if (TextUtils.isEmpty(phone.getText().toString())) {
            validate = false;
            phone.requestFocus();
            phone.setError("Enter correct number");
        }
        if (TextUtils.isEmpty(amount.getText().toString())) {
            validate = false;
            amount.requestFocus();
            amount.setError("Field can't be empty");
        }
        if (TextUtils.isEmpty(purpose.getText().toString())) {
            validate = false;
            purpose.requestFocus();
            purpose.setError("Field can't be empty");
        }

        if (TextUtils.isEmpty(buyer_name.getText().toString())) {
            validate = false;
            buyer_name.requestFocus();
            buyer_name.setError("Field can't be empty");

        }
        return validate;
    }




    private void callInstamojoPay(String email, String phone, String amount, String purpose, String buyername) {
        InstamojoPay instamojoPay = new InstamojoPay();
        IntentFilter filter = new IntentFilter("ai.devsupport.instamojo");
        registerReceiver(instamojoPay, filter);
        JSONObject pay = new JSONObject();
        try {
            pay.put("email", email);
            pay.put("phone", phone);
            pay.put("purpose", purpose);
            pay.put("amount", amount);
            pay.put("name", buyername);
            pay.put("send_sms", true);
            pay.put("send_email", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initListener();
        instamojoPay.start(MainActivity.this, pay, listener);
    }

    InstapayListener listener;



    private void initListener() {
        listener = new InstapayListener() {
            @Override
            public void onSuccess(String response) {
                if(response!=null){
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
                            .show();
                }

            }

            @Override
            public void onFailure(int code, String reason) {
                if(reason!=null){
                    Toast.makeText(getApplicationContext(), "Failed: " + reason, Toast.LENGTH_LONG)
                            .show();
                }

            }
        };
    }
}
