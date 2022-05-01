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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Update_item_Activity extends AppCompatActivity {
    TextInputEditText txtItemName, txtPrice, txtDescription;
    Button btnAdd, btnCancel;
    ImageView imgItem;
    Bitmap bmp;
    boolean iscaptured = false;
    byte[] imgBytes;
    ByteArrayOutputStream baos;
    String imgTostr;
    String SrNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_item_layout);
        Bundle B = getIntent().getExtras();
        SrNo = B.getString("SrNo");
       // Toast.makeText(Update_item_Activity.this, "SrNO=" + SrNo, Toast.LENGTH_LONG).show();
        txtItemName = findViewById(R.id.txtItemName);
        txtPrice = findViewById(R.id.txtPrice);
        txtDescription = findViewById(R.id.txtDesciption);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        imgItem = findViewById(R.id.imgItem);

        txtItemName.setText(B.getString("Name"));
        txtPrice.setText(B.getString("Price"));
        txtDescription.setText(B.getString("Desc"));
        Picasso.with(Update_item_Activity.this)
                .load("https://coffeeshopeapp.000webhostapp.com/itemimages/" + B.getString("ImgPath"))
                .into(imgItem);
    }

    public void UpdateItem(View view) {
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
            Toast.makeText(Update_item_Activity.this, "Plese Captured The Image..", Toast.LENGTH_SHORT).show();
        }

        ProgressDialog pd;
        pd = new ProgressDialog(Update_item_Activity.this);
        pd.setTitle("Updating Item to Server...");
        pd.setMessage("Please wait... Item Updating to Server...");
        pd.show();
        RequestQueue queue = Volley.newRequestQueue(Update_item_Activity.this);
        String url = "https://coffeeshopeapp.000webhostapp.com/UpdateItemData.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.dismiss();
                        Toast.makeText(Update_item_Activity.this, "Item Updated Succesfully !!!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(Update_item_Activity.this, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("SrNo", SrNo);
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
