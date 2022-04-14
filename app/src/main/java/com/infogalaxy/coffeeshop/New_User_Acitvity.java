package com.infogalaxy.coffeeshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class New_User_Acitvity extends AppCompatActivity {
    TextInputEditText txtName, txtMobileNo, txtCity, txtGender, txtUserName, txtUserPassword;
    ProgressDialog pd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user_layout);


        txtName = findViewById(R.id.txtName);
        txtMobileNo = findViewById(R.id.txtMobileNo);
        txtCity = findViewById(R.id.txtCity);
        txtGender = findViewById(R.id.txtGender);
        txtUserName = findViewById(R.id.txtUserName);
        txtUserPassword = findViewById(R.id.txtUserPassword);

    }

    public void UserRegister(View view) {
        if (txtName.getText().toString().isEmpty()) {
            txtName.setError(" Enter The Name");
            txtName.requestFocus();
        } else if (txtMobileNo.getText().toString().length() < 10 || txtMobileNo.getText().toString().length() > 10) {
            txtMobileNo.setError("Enter the 10 Digit No ");
            txtMobileNo.requestFocus();
        } else if (txtCity.getText().toString().isEmpty()) {
            txtCity.setError("Enter the City");
            txtCity.requestFocus();
        } else if (txtGender.getText().toString().isEmpty()) {
            txtGender.setError("Please Enter The Gender ");
            txtGender.requestFocus();
        } else if (txtUserName.getText().toString().isEmpty()) {
            txtUserName.setError("Plese Enter The Valid UserName");
            txtUserName.requestFocus();
        } else if (txtUserPassword.getText().toString().length() < 6 || txtUserPassword.getText().toString().length() > 6) {
            txtUserPassword.setError("Plese Enter The 6 Digit Password");
            txtUserPassword.requestFocus();

        } else {
            // startActivity(new Intent(New_User_Acitvity.this, Admin_Home_Acitvity.class))
            ProgressDialog pd = new ProgressDialog(New_User_Acitvity.this);
            pd.setTitle("User Registering to Server...");
            pd.setMessage("Please wait... User Registering to Server...");
            pd.show();
            RequestQueue queue = Volley.newRequestQueue(New_User_Acitvity.this);
            String url = "https://coffeeshopeapp.000webhostapp.com/UserRegisterData.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();

                            txtName.getText().clear();
                            txtMobileNo.getText().clear();
                            txtCity.getText().clear();
                            txtGender.getText().clear();
                            txtUserName.getText().clear();
                            txtUserPassword.getText().clear();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    Toast.makeText(New_User_Acitvity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> p = new HashMap<>();

                    p.put("txtName", txtName.getText().toString().trim());
                    p.put("txtMobileNo", txtMobileNo.getText().toString().trim());
                    p.put("txtCity", txtCity.getText().toString().trim());
                    p.put("txtGender", txtGender.getText().toString().trim());
                    p.put("txtUserName", txtUserName.getText().toString().trim());
                    p.put("txtUserPassword", txtUserPassword.getText().toString().trim());

                    return p;
                }
            };
            queue.add(stringRequest);
        }


    }

}



