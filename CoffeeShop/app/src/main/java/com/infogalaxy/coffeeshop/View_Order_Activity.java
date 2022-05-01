package com.infogalaxy.coffeeshop;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class View_Order_Activity extends AppCompatActivity {
    ListView lstManage;
    String DDate, DTime;
    ArrayList<String> SrNo = new ArrayList<>();
    ArrayList<String> Name = new ArrayList<>();
    ArrayList<String> MobNo = new ArrayList<>();
    ArrayList<String> ODate = new ArrayList<>();
    ArrayList<String> OTime = new ArrayList<>();
    ArrayList<String> Total = new ArrayList<>();
    ArrayList<String> Status = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_manage_layout);
        lstManage = findViewById(R.id.lstManage);
        getOrderData();
    }

    public void getOrderData() {
        RequestQueue queue = Volley.newRequestQueue(View_Order_Activity.this);
        String url = "https://coffeeshopeapp.000webhostapp.com/OrderAccess.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(View_Order_Activity.this, "Data"+response, Toast.LENGTH_SHORT).show();
                        String[] data = response.split(",");
                        for (int i = 0; i < data.length; i = i + 7) {
                            SrNo.add(data[i + 0]);
                            Name.add(data[i + 1]);
                            MobNo.add(data[i + 2]);
                            ODate.add(data[i + 3]);
                            OTime.add(data[i + 4]);
                            Total.add(data[i + 5]);
                            Status.add(data[i + 6]);

                            //Toast.makeText(Update_Delete_Item_Acivity.this, "Name="+Name.get(i), Toast.LENGTH_SHORT).show();
                        }
                        MyAdapter MA = new MyAdapter();
                        lstManage.setAdapter(MA);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(View_Order_Activity.this, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        }) {
        };
        queue.add(stringRequest);
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
            view = getLayoutInflater().inflate(R.layout.view_order_layout, null);
            TextView txtSrNo = view.findViewById(R.id.txtSrNo);
            TextView txtIName = view.findViewById(R.id.txtIName);
            TextView txtMobileNo = view.findViewById(R.id.txtMobileNo);
            TextView txtTotal = view.findViewById(R.id.txtTotal);
            TextView txtDate = view.findViewById(R.id.txtDate);
            TextView txtTime = view.findViewById(R.id.txtTime);
            Button btnAccept = view.findViewById(R.id.btnAccept);

            if (Status.get(i).equalsIgnoreCase("Success")) {
                btnAccept.setText("SUCCESS");
                btnAccept.setBackgroundColor(Color.GREEN);
                btnAccept.setEnabled(false);
            }

            btnAccept.setOnClickListener(new View.OnClickListener() {
                int loc = i;

                @Override
                public void onClick(View view) {
                    //acceptOrder(SrNo.get(i));

                    AlertDialog.Builder dlg = new AlertDialog.Builder(View_Order_Activity.this);
                    dlg.setTitle("Delivery Status");
                    dlg.setMessage("Set the Delivery Status PLease.");
                    View myview = getLayoutInflater().inflate(R.layout.orderstatus, null);

                    Button btnDate = myview.findViewById(R.id.btnDate);
                    btnDate.setOnClickListener(new View.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(View view) {
                            DatePickerDialog dpd = new DatePickerDialog(View_Order_Activity.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                                    DDate = i2 + "/" + (i1 + 1) + "/" + i;
                                    btnDate.setText(DDate);
                                }
                            }, 2022, 3, 06);
                            dpd.show();
                        }
                    });
                    Button btnTime = myview.findViewById(R.id.btnTime);
                    btnTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            TimePickerDialog tpd = new TimePickerDialog(View_Order_Activity.this, new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                    DTime = i + ":" + i1;
                                    btnTime.setText(DTime);
                                }
                            }, 11, 11, true);
                            tpd.show();
                        }
                    });

                    dlg.setView(myview);
                    dlg.setPositiveButton("SET.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ProgressDialog pd = new ProgressDialog(View_Order_Activity.this);
                            pd.setTitle("Accepting Order.");
                            pd.setMessage("Please wait... Order Accepting in progresss...");
                            pd.show();
                            RequestQueue queue = Volley.newRequestQueue(View_Order_Activity.this);
                            String url = "https://coffeeshopeapp.000webhostapp.com/AcceptOrder.php";
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            pd.dismiss();
                                            Toast.makeText(View_Order_Activity.this, "Order Accepted!!!", Toast.LENGTH_SHORT).show();
                                            btnAccept.setText("SUCCESS");
                                            btnAccept.setBackgroundColor(Color.GREEN);
                                            btnAccept.setEnabled(false);
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    pd.dismiss();
                                    Toast.makeText(View_Order_Activity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                                }
                            }) {
                                @Nullable
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> P = new HashMap<>();
                                    P.put("SrNo", SrNo.get(loc));
                                    P.put("DDate", DDate);
                                    P.put("DTime", DTime);
                                    return P;
                                }
                            };
                            queue.add(stringRequest);
                        }
                    });
                    dlg.setNegativeButton("CANCEL.", null);
                    dlg.show();
                }
            });


            txtSrNo.setText(SrNo.get(i) + " ) ");
            txtIName.setText(Name.get(i));
            txtMobileNo.setText(MobNo.get(i));
            txtDate.setText(ODate.get(i));
            txtTime.setText(OTime.get(i));
            txtTotal.setText(Total.get(i) + "  ");

            return view;
        }
    }
}
