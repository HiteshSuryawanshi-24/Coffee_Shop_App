package com.infogalaxy.coffeeshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Add_Item_Acivity extends AppCompatActivity {
    TextInputEditText txtItemName, txtPrice, txtDescription;
    Button btnAdd, btnCancel;
    ImageView imgItem;
    Bitmap bmp;
    boolean iscaptured = false;
    byte[] imgBytes;
    ByteArrayOutputStream baos;
    String imgTostr;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item_layout);
        txtItemName = findViewById(R.id.txtItemName);
        txtPrice = findViewById(R.id.txtPrice);
        txtDescription = findViewById(R.id.txtDesciption);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        imgItem = findViewById(R.id.imgItem);
    }

    public void AddItem(View view) {
        if (txtItemName.getText().toString().isEmpty()) {
            txtItemName.setError(" Enter The Item Name :");
            txtItemName.requestFocus();
        } else if (txtPrice.getText().toString().isEmpty()) {
            txtPrice.setError("Enter the Price :");
            txtPrice.requestFocus();
        } else if (txtDescription.getText().toString().isEmpty()) {
            txtDescription.setError("Please Enter The Description ");
            txtDescription.requestFocus();
        } else if (iscaptured == false) {
            Toast.makeText(Add_Item_Acivity.this, "Plese Captured The Image..", Toast.LENGTH_SHORT).show();
        }

        ProgressDialog pd;
        pd = new ProgressDialog(Add_Item_Acivity.this);
        pd.setTitle("Adding Item to Server...");
        pd.setMessage("Please wait... Item Adding to Server...");
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(Add_Item_Acivity.this);
        String url = "https://coffeeshopeapp.000webhostapp.com/StoreItemData.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Toast.makeText(Add_Item_Acivity.this, "Item Added Succesfully !!!", Toast.LENGTH_SHORT).show();
                        txtItemName.getText().clear();
                        txtPrice.getText().clear();
                        txtDescription.getText().clear();
                        imgItem.setImageDrawable(getResources().getDrawable(R.drawable.coffeecup));
                        txtItemName.requestFocus();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Toast.makeText(Add_Item_Acivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("txtName", txtItemName.getText().toString().trim());
                p.put("txtPrice", txtPrice.getText().toString().trim());
                p.put("txtDesc", txtDescription.getText().toString().trim());
                p.put("img", imgTostr);
                p.put("txtImgPath", txtItemName.getText().toString().replace(" ", "") + ".jpg");
                return p;
            }
        };
        queue.add(stringRequest);
    }

    public void OpenCamera(View view) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 111);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && data != null) {
            bmp = (Bitmap) data.getExtras().get("data");
            imgItem.setImageBitmap(bmp);
            iscaptured = true;
            baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imgBytes = baos.toByteArray();
            imgTostr = Base64.getEncoder().encodeToString(imgBytes);
        }
    }
}

