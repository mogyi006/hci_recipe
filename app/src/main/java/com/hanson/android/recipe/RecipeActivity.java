package com.hanson.android.recipe;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hanson.android.recipe.Helper.DBHelper;
import com.hanson.android.recipe.Helper.ImageHelper;
import com.hanson.android.recipe.Model.RecipeItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;

public class RecipeActivity extends AppCompatActivity {
    ImageHelper imageHelper = new ImageHelper();
    RecipeItem recipeItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        //Connect DB
        final DBHelper dbHelper = new DBHelper(this, "Recipes.db", null, 1);

        //Show backbutton
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        //set userid
        SharedPreferences pref = getSharedPreferences("Login", Activity.MODE_PRIVATE);
        final String userID = pref.getString("userID","");

        //connect with view
        TextView recipeName = (TextView)findViewById(R.id.txt_recipeName);
        TextView country = (TextView)findViewById(R.id.txt_recipeCategory);
        TextView ingredients = (TextView)findViewById(R.id.txt_recipeIngredients);
        TextView description = (TextView)findViewById(R.id.txt_recipeDescription);
        TextView howto = (TextView)findViewById(R.id.txt_recipeHowto);
        ImageView mainImg = (ImageView)findViewById(R.id.img_recipeMainImg);
        CheckBox like = (CheckBox) findViewById(R.id.chk_recipeLike);
        Button add2cart = (Button) findViewById(R.id.btnAdd2Cart);

        //set for received data
        Intent intent = getIntent();
        String name = intent.getStringExtra("recipe");
        //Toast.makeText(this,name,Toast.LENGTH_SHORT).show();
        //set to selected recipe data
        //recipeItem = (RecipeItem)intent.getSerializableExtra("recipe");


        recipeItem = dbHelper.recipes_SelectByName(name);

        if (recipeItem != null)
        {
            //data bind
            mainImg.setImageBitmap(imageHelper.getBitmapFromByteArray(recipeItem.get_mainImg()));
            recipeName.setText(recipeItem.get_recipeName());
            country.setText(recipeItem.get_category());
            description.setText(recipeItem.get_Description());
            howto.setText(recipeItem.get_howTo());

            ArrayList<String> ingredentList = dbHelper.ingredients_SelectByRecipeId(recipeItem.get_id());
            String tempIngre = "";

            for (int i = 0; i < ingredentList.size(); i++)
            {
                tempIngre += ingredentList.get(i).toString();
                if (i != (ingredentList.size() -1))
                    tempIngre += " / ";
            }

            ingredients.setText(tempIngre);

        }
        else
        {
            Toast.makeText(this,"error",Toast.LENGTH_SHORT).show();
        }

        like.setChecked(dbHelper.like_GetLiked(userID, recipeItem.get_id()));

        like.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //check again userid
                SharedPreferences pref = getSharedPreferences("Login", Activity.MODE_PRIVATE);
                final String userID = pref.getString("userID","");
                if (isChecked)
                {
                    int count = dbHelper.recipes_AddLike(userID, recipeItem.get_id());
                }
                else
                 {
                     int count = dbHelper.recipes_MinusLike(userID, recipeItem.get_id());
                 }
            }
        });

        add2cart.setOnClickListener(new View.OnClickListener() {
            ArrayList<String> ingredentList = dbHelper.ingredients_SelectByRecipeId(recipeItem.get_id());
            @Override
            public void onClick(View view) {
                for (int counter = 0; counter < ingredentList.size(); counter++) {
                    String ingreName = ingredentList.get(counter);
                    if(Character.isDigit(ingreName.charAt(0))){
                        String[] ingreAll = ingreName.split(" ", 3);
                        dbHelper.shoppinglist_Insert(ingreAll[2], ingreAll[0] ,ingreAll[1]);
                    }
                    else {
                        dbHelper.shoppinglist_Insert(ingreName, "" ,"");
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
