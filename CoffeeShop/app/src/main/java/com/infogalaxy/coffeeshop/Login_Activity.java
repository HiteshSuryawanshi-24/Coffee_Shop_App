package com.infogalaxy.coffeeshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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

public class Login_Activity extends AppCompatActivity {
    TextInputEditText txtUserName, txtUserPassword;
    Spinner spnUType;
    String[] UType = {"Select User", "Owner", "Customer"};
    Button btnLogin, btnCancel;
    String Name, MobNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        txtUserName = findViewById(R.id.txtUserName);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        txtUserName = findViewById(R.id.txtUserName);
        txtUserPassword = findViewById(R.id.txtUserPassword);
        spnUType = findViewById(R.id.spnUType);
        btnLogin = findViewById(R.id.btnLogin);
        btnCancel = findViewById(R.id.btnCancel);


        ArrayAdapter<String> adp = new ArrayAdapter<String>(Login_Activity.this, android.R.layout.simple_dropdown_item_1line, UType);
        spnUType.setAdapter(adp);

    }

    public void userLogin(View view) {

        if (spnUType.getSelectedItem().toString().equalsIgnoreCase("Owner")
                && txtUserName.getText().toString().equalsIgnoreCase("Admin")
                && txtUserPassword.getText().toString().equalsIgnoreCase("Admin")) {
            startActivity(new Intent(Login_Activity.this, Admin_Home_Acitvity.class));
        } else if (spnUType.getSelectedItem().toString().equalsIgnoreCase("Customer")) {
            ProgressDialog pd;
            pd = new ProgressDialog(Login_Activity.this);
            pd.setTitle("Searching User to Server...");
            pd.setMessage("Please wait... Serching User to Server...");
            pd.show();

            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://coffeeshopeapp.000webhostapp.com/CheckLoginAdmin.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            pd.dismiss();
                            //6   Toast.makeText(Login_Activity.this, "Response: " + response, Toast.LENGTH_LONG).show();
                            String[] data = response.split(",");
                            Name = data[0];
                            MobNo = data[1];
                            //Toast.makeText(Login_Activity.this, "Name="+Name+"\nMobileNO="+MobNo, Toast.LENGTH_LONG).show();

                            SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);
                            SharedPreferences.Editor ed = sp.edit();
                            ed.putString("Name", Name.trim());
                            ed.putString("MobNo", MobNo.trim());
                            ed.commit();

                            startActivity(new Intent(Login_Activity.this, Customer_Home_Activity.class));
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(Login_Activity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                }
            }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> p = new HashMap<>();
                    p.put("txtUserName", txtUserName.getText().toString().trim());
                    p.put("txtPassword", txtUserPassword.getText().toString().trim());
                    return p;
                }
            };
            queue.add(stringRequest);
        } else {
            Toast.makeText(Login_Activity.this, "Please Enter Valid Username and Password", Toast.LENGTH_LONG).show();
        }
    }

    public void newUserRegistration(View view) {
        startActivity(new Intent(Login_Activity.this, New_User_Acitvity.class));
    }
}
