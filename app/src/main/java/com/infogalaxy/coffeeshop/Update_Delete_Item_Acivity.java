package com.infogalaxy.coffeeshop;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Update_Delete_Item_Acivity extends AppCompatActivity {
   ListView lstManage;
   ImageView imgUpdate;
    ArrayList<String> SrNo = new ArrayList<>();
    ArrayList<String> Name = new ArrayList<>();
    ArrayList<String> Price = new ArrayList<>();
    ArrayList<String> Desc = new ArrayList<>();
    ArrayList<String> ImgPath = new ArrayList<>();
    String option;
    String SelectedSrNo;
    int SelItemSrNo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_view_manage_layout);

        imgUpdate = findViewById(R.id.imgUpdate);
        lstManage = findViewById(R.id.lstManage);
        Bundle B = getIntent().getExtras();
        option = B.getString("Option");

        getItemData();

    }
    //Data access code for Item
    public void getItemData() {
        ProgressDialog pd = new ProgressDialog(Update_Delete_Item_Acivity.this);
        pd.setTitle("User Registering to Server...");
        pd.setMessage("Please wait... User Registering to Server...");
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(Update_Delete_Item_Acivity.this);
        String url = "https://coffeeshopeapp.000webhostapp.com/AccessItemData.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Toast.makeText(Update_Delete_Item_Acivity.this, "Data"+response, Toast.LENGTH_SHORT).show();
                        String[] data = response.split(",");
                        for(int i=0;i<data.length;i=i+5) {
                            SrNo.add(data[i+0]);
                            Name.add(data[i+1]);
                            Price.add(data[i+2]);
                            Desc.add(data[i+3]);
                            ImgPath.add(data[i+4]);
                            //Toast.makeText(Update_Delete_Item_Acivity.this, "Name="+Name.get(i), Toast.LENGTH_SHORT).show();
                        }
                        data = null;
                        Toast.makeText(Update_Delete_Item_Acivity.this, "Name="+Name, Toast.LENGTH_SHORT).show();
                        MyAdapter MA = new MyAdapter();
                        lstManage.setAdapter(MA);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Update_Delete_Item_Acivity.this, "Error: "+error, Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    class MyAdapter extends BaseAdapter{

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
            view = getLayoutInflater().inflate(R.layout.update_delete_item_layout,null);
            ImageView imgImage = view.findViewById(R.id.imgItem);
            TextView txtIName = view.findViewById(R.id.txtIName);
            TextView txtIPrice = view.findViewById(R.id.txtIPrice);
            ImageView imgUpdate = view.findViewById(R.id.imgUpdate);
            if(option.equalsIgnoreCase("Update")) {
                imgUpdate.setImageDrawable(getResources().getDrawable(R.drawable.edit));
            }
            if(option.equalsIgnoreCase("Delete")) {
                imgUpdate.setImageDrawable(getResources().getDrawable(R.drawable.deleteuserdata));
            }
            if(option.equalsIgnoreCase("View")) {
                imgUpdate.setImageDrawable(getResources().getDrawable(R.drawable.view));
            }

            imgUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelectedSrNo = SrNo.get(i);
                    SelItemSrNo = i;
                    operation();
                }
            });

            Picasso.with(Update_Delete_Item_Acivity.this)
                    .load("https://coffeeshopeapp.000webhostapp.com/itemimages/"+ImgPath.get(i))
                    .into(imgImage);

            txtIName.setText(Name.get(i));
            txtIPrice.setText(Price.get(i)+" ");

            return view;
        }
    }

    public void operation() {
        if(option.equalsIgnoreCase("Delete")) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(Update_Delete_Item_Acivity.this);
            dlg.setTitle("DELETE ITEM???");
            dlg.setMessage("Are you sure you want to Delete "+Name.get(SelItemSrNo)+" ?");
            dlg.setPositiveButton("YES.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ProgressDialog pd = new ProgressDialog(Update_Delete_Item_Acivity.this);
                    pd.setTitle("Deleteing Item From Server...");
                    pd.setMessage("Please wait... Deleteing Item From Server...");
                    pd.show();
                    RequestQueue queue = Volley.newRequestQueue(Update_Delete_Item_Acivity.this);
                    String url = "https://coffeeshopeapp.000webhostapp.com/DeleteItem.php";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    pd.dismiss();
                                    Toast.makeText(Update_Delete_Item_Acivity.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                                    SrNo.clear();
                                    Name.clear();
                                    Price.clear();
                                    Desc.clear();
                                    ImgPath.clear();
                                    getItemData();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.dismiss();
                            Toast.makeText(Update_Delete_Item_Acivity.this, "Error: "+error, Toast.LENGTH_LONG).show();
                        }
                    }){
                        @Nullable
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> P = new HashMap<>();
                            P.put("SrNo",SelectedSrNo);
                            return P;
                        }
                    };
                    queue.add(stringRequest);
                }
            });
            dlg.setNegativeButton("NO.",null);
            dlg.setCancelable(false);
            dlg.show();
        }
    }
}
