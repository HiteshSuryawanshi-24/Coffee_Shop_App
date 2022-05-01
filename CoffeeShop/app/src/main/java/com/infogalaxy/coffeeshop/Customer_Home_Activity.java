package com.infogalaxy.coffeeshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Customer_Home_Activity extends AppCompatActivity {

    GridView lstCustHome;
    ArrayList<String> SrNo = new ArrayList<>();
    ArrayList<String> Name = new ArrayList<>();
    ArrayList<String> Price = new ArrayList<>();
    ArrayList<String> Desc = new ArrayList<>();
    ArrayList<String> ImgPath = new ArrayList<>();
    String SelectedSrNo;
    int SelItemSrNo;
    TextView txtTotal;
    int total = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_home_layout);
        lstCustHome = findViewById(R.id.lstCustHome);
        txtTotal = findViewById(R.id.txtTotal);
        getItemData();
    }

    public void getItemData() {
//        ProgressDialog pd = new ProgressDialog(Customer_Home_Activity.this);
//        pd.setTitle("User Registering to Server...");
//        pd.setMessage("Please wait... User Registering to Server...");
//        pd.show();
        RequestQueue queue = Volley.newRequestQueue(Customer_Home_Activity.this);
        String url = "https://coffeeshopeapp.000webhostapp.com/AccessItemData.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // pd.dismiss();
                        //Toast.makeText(Customer_Home_Activity.this, "Data"+response, Toast.LENGTH_SHORT).show();
                        String[] data = response.split(",");
                        for (int i = 0; i < data.length; i = i + 5) {
                            SrNo.add(data[i + 0]);
                            Name.add(data[i + 1]);
                            Price.add(data[i + 2]);
                            Desc.add(data[i + 3]);
                            ImgPath.add(data[i + 4]);
                            //Toast.makeText(Update_Delete_Item_Acivity.this, "Name="+Name.get(i), Toast.LENGTH_SHORT).show();
                        }
                        data = null;
                        //Toast.makeText(Customer_Home_Activity.this, "Name="+Name, Toast.LENGTH_SHORT).show();
                        MyAdapter MA = new MyAdapter();
                        lstCustHome.setAdapter(MA);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // pd.dismiss();
                Toast.makeText(Customer_Home_Activity.this, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    public void openOrder(View view) {
        // startActivity(new Intent(Customer_Home_Activity.this, Place_Order_Activity.class));
        Intent i = new Intent(Customer_Home_Activity.this, Place_Order_Activity.class);
        i.putExtra("Total", txtTotal.getText().toString());
        startActivity(i);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Name.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            //view = getLayoutInflater().inflate(R.layout.customer_home_design,null);
            view = getLayoutInflater().inflate(R.layout.new_design, null);
            ImageView imgImage = view.findViewById(R.id.imgItem);
            TextView txtIName = view.findViewById(R.id.txtIName);
            TextView txtIPrice = view.findViewById(R.id.txtIPrice);
            //TextView txtIDesc = view.findViewById(R.id.txtIDesc);
            ImageView imgUpdate = view.findViewById(R.id.imgUpdate);

            Button btnQty = view.findViewById(R.id.btnQty);

            Button btnMinus = view.findViewById(R.id.btnMinus);
            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int subtotal = 0;
                    int a = Integer.parseInt(btnQty.getText().toString());
                    if (a == 0) {
                        Toast.makeText(Customer_Home_Activity.this, "The Minimum Quantity must be 1.", Toast.LENGTH_SHORT).show();
                    } else {
                        a--;
                        int price = Integer.parseInt(Price.get(i));
                        int total = Integer.parseInt(txtTotal.getText().toString().trim());
                        subtotal = total - price;
                    }
                    btnQty.setText("" + a);
                    txtTotal.setText("" + subtotal);
                }
            });
            Button btnPlus = view.findViewById(R.id.btnPlus);
            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int a = Integer.parseInt(btnQty.getText().toString());
                    a++;
                    btnQty.setText("" + a);
                    int price = Integer.parseInt(Price.get(i));
                    int total = Integer.parseInt(txtTotal.getText().toString().trim());
                    int subtotal = total + price;
                    txtTotal.setText("" + subtotal);
                }
            });
            Picasso.with(Customer_Home_Activity.this)
                    .load("https://coffeeshopeapp.000webhostapp.com/itemimages/" + ImgPath.get(i))
                    .into(imgImage);

            txtIName.setText(Name.get(i));
            txtIPrice.setText(Price.get(i) + " ");

            return view;
        }
    }
}
