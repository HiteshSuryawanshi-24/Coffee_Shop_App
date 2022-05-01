package com.infogalaxy.coffeeshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

public class Place_Order_Activity extends AppCompatActivity {
    TextView txtName, txtMobileNo, txtDate, txtTime, txtTotal;
    Button btnOrder;

    String Name, MobNo;
    Calendar c;

    String ODate, OTime, Total;
    int hr, min;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_order_layout);

        SharedPreferences sp = getSharedPreferences("UserData", MODE_PRIVATE);

        txtName = findViewById(R.id.txtName);
        txtMobileNo = findViewById(R.id.txtMobileNo);
        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        txtTotal = findViewById(R.id.txtTotal);
        btnOrder = findViewById(R.id.btnOrder);

        Name = sp.getString("Name", "Guest");
        MobNo = sp.getString("MobNo", "-");

        txtName.setText("Name : " + Name);
        txtMobileNo.setText("Mobile No. : " + MobNo);

        c = Calendar.getInstance();
        ODate = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
        txtDate.setText("Date : " + ODate);

        hr = c.get(Calendar.HOUR_OF_DAY);
        if (hr > 12) {
            hr = hr - 12;
        }
        OTime = hr + " : " + c.get(Calendar.MINUTE);
        txtTime.setText("Time : " + OTime);

        Bundle B = getIntent().getExtras();
        Total = B.getString("Total");
        txtTotal.setText("Total Bill : " + Total + "  ");

    }

    public void placeOrder(View view) {
        ProgressDialog pd = new ProgressDialog(Place_Order_Activity.this);
        pd.setTitle("Placing Order to Server...");
        pd.setMessage("Please wait... Order Placing to Server...");
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(Place_Order_Activity.this);
        String url = "https://coffeeshopeapp.000webhostapp.com/PlaceOrder.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Toast.makeText(Place_Order_Activity.this, "Order Placed Succesfully...!!!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Place_Order_Activity.this, Customer_Home_Activity.class));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Place_Order_Activity.this, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();

                p.put("Name", Name);
                p.put("MobileNo", MobNo);
                p.put("ODate", ODate);
                p.put("OTime", OTime);
                p.put("Total", Total);

                return p;
            }
        };
        queue.add(stringRequest);
    }
}
