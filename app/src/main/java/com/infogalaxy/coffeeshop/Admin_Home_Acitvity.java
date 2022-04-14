package com.infogalaxy.coffeeshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Admin_Home_Acitvity extends AppCompatActivity {
   Button btnaddItem,btnUpdate,btnViewOrders,btnDelete;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home_layout);

        btnaddItem = findViewById(R.id.btnaddItem);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnViewOrders = findViewById(R.id.btnViewOrders);
    }

    public void addItem(View view) {
    startActivity(new Intent(Admin_Home_Acitvity.this,Add_Item_Acivity.class));
    }

    public void updateItem(View view) {
        Intent i = new Intent(Admin_Home_Acitvity.this,Update_Delete_Item_Acivity.class);
        i.putExtra("Option","Update");
        startActivity(i);
    }

    public void viewOrders(View view) {
        Intent i = new Intent(Admin_Home_Acitvity.this,View_Order_Activity.class);
       // i.putExtra("Option","View");
        startActivity(i);
    }

    public void deleteItem(View view) {
        Intent i = new Intent(Admin_Home_Acitvity.this,Update_Delete_Item_Acivity.class);
        i.putExtra("Option","Delete");

        startActivity(i);
    }
}
