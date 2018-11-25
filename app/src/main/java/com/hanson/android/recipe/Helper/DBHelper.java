package com.hanson.android.recipe.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.hanson.android.recipe.Model.CategoryItem;
import com.hanson.android.recipe.Model.RecipeIngr;
import com.hanson.android.recipe.Model.RecipeItem;
import com.hanson.android.recipe.Model.SearchResultItem;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;


public class DBHelper extends SQLiteOpenHelper
{
    ImageHelper imageHelper = new ImageHelper();

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table
        db.execSQL("CREATE TABLE RECIPES( _id INTEGER PRIMARY KEY AUTOINCREMENT, category TEXT,"
                + "recipeName TEXT, howTo TEXT, description TEXT,"
                + "thumbnail BLOB, mainImg BLOB, liked INTEGER);");

        db.execSQL("CREATE TABLE INGREDIENTS( _id INTEGER PRIMARY KEY AUTOINCREMENT, recipeID INTEGER, ingreName TEXT, ingreM TEXT, ingreQ TEXT);");

        db.execSQL("CREATE TABLE SHOPPINGLIST( _id INTEGER PRIMARY KEY AUTOINCREMENT, ingreName TEXT, ingreM TEXT, ingreQ TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void recipes_Insert(String category, String recipeName, String howTo, String description,
                               byte[] thumbnail, byte[] mainImg, int liked) {

        // open read and write database
        SQLiteDatabase db = getWritableDatabase();
        // execute insert query
        //db.execSQL("INSERT INTO RECIPES VALUES(null, '" + category + "', '" + recipeName + "', " + thumbnail  + ");");
        SQLiteStatement p = db.compileStatement("INSERT INTO RECIPES values(?,?,?,?,?,?,?,?);");
        p.bindNull(1);
        p.bindString(2, category);
        p.bindString(3, recipeName);
        p.bindString(4, howTo);
        p.bindString(5, description);
        p.bindBlob(6, thumbnail);
        p.bindBlob(7, mainImg);
        p.bindLong(8, liked);
        p.execute();
        db.close();
    }

    public  void  ingredients_Insert(int recipeid, String ingreName)
    {
        SQLiteDatabase db = getWritableDatabase();
        if(Character.isDigit(ingreName.charAt(0))){
            String[] ingreAll = ingreName.split(" ", 3);
            db.execSQL("INSERT INTO INGREDIENTS ( _id , recipeID, ingreName, ingreM, ingreQ) VALUES (null, " + recipeid + ", '" + ingreAll[2] + "', '" + ingreAll[1] + "', '" + ingreAll[0] + "');");
        } else{
            db.execSQL("INSERT INTO INGREDIENTS (_id, recipeID, ingreName) VALUES (null, " + recipeid + ", '" + ingreName + "');");
        }
        db.close();
    }


    public  void  shoppinglist_Update(String ingreName, String ingreM, String ingreQ)
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id, ingreName, ingreM, ingreQ FROM SHOPPINGLIST WHERE ingreName = '" + ingreName + "';", null);
        if (cursor == null){
            if(ingreQ != null){
                db.execSQL("INSERT INTO SHOPPINGLIST ( _id , ingreName, ingreM, ingreQ) VALUES (null, " + ", '" + ingreName + "', '" + ingreM + "', '" + ingreQ + "');");
            } else{
                db.execSQL("INSERT INTO SHOPPINGLIST (_id, ingreName) VALUES (null, " + ", '" + ingreName + "');");
            }
        } else {
            if (ingreQ == null){
                while (cursor.moveToNext()) {
                    if (cursor.getString(3) == null) {
                        cursor.close();
                        db.close();
                        return;
                    }
                }
                db.execSQL("INSERT INTO SHOPPINGLIST (_id, ingreName) VALUES (null, " + ", '" + ingreName + "');");
            } else {
                while (cursor.moveToNext()) {
                    // TODO convert units and add quantities
                    if (cursor.getString(2) == ingreM) {
                        String newingreQ = Double.toString(Double.parseDouble(cursor.getString(3) + Double.parseDouble(ingreQ)));
                        db.execSQL("UPDATE SHOPPINGLIST SET IngreQ = '" + newingreQ + "' WHERE _id = "+ cursor.getString(0) + ";");
                    } else {
                        db.execSQL("INSERT INTO SHOPPINGLIST ( _id , ingreName, ingreM, ingreQ) VALUES (null, " + ", '" + ingreName + "', '" + ingreM + "', '" + ingreQ + "');");
                    }
                }
            }
        }
        cursor.close();
        db.close();
    }

    public void shoppinglist_Item_Delete(int _id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM SHOPPINGLIST WHERE _id = " + _id + ";");
        db.close();
    }

    public ArrayList<String> ingredients_SelectByRecipeId(int id)
    {
        // Open available reading database
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> ingredients = new ArrayList<>();
        // Get all recipes data
        Cursor cursor = db.rawQuery("SELECT ingreQ, ingreM, ingreName FROM INGREDIENTS WHERE recipeID = " + id, null);
        if (cursor != null)
        {
            while (cursor.moveToNext()) {
                if (cursor.getString(0) != null) {
                    ingredients.add(cursor.getString(0)+ " " + cursor.getString(1) + " " + cursor.getString(2));
                } else{
                    ingredients.add(cursor.getString(2));
                }
            }
        }
        cursor.close();
        db.close();

        return ingredients;


    }

    //Miju
    public ArrayList<SearchResultItem> ingredients_selectRecipeByIngredientName(ArrayList<String> ingredientsName){
        // Open available reading database
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<SearchResultItem> idRecipes = new ArrayList<>();
        String strNames = "";
        for (int i=0; i < ingredientsName.size(); i++)
        {
            strNames += "ingreName = '" + ingredientsName.get(i) + "'";
            if (i != ingredientsName.size()-1)
            {
                strNames += " OR ";
            }
        }

        Cursor cursor = db.rawQuery("SELECT recipeID, count(*) FROM INGREDIENTS WHERE "+
                strNames + " GROUP BY recipeID", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                idRecipes.add(new SearchResultItem(cursor.getInt(0), cursor.getInt(1)));
            }
        }
        cursor.close();
        db.close();
        return idRecipes;

    }

    //return the id of the recipes based on ingredientName TO FIX!
    public ArrayList<Integer> ingredients_selectIdRecipeByIngredientName(ArrayList<String> ingredientsName){
        // Open available reading database
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> idRecipes = new ArrayList<>();
        // query the db findind the recipeID through the
        for(int i =0; i<ingredientsName.size();i++) {
            Cursor cursor = db.rawQuery("SELECT recipeID FROM INGREDIENTS WHERE ingreName = '" + ingredientsName.get(1) + "'", null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    idRecipes.add(cursor.getInt(0));
                }
            }
            cursor.close();
        }

        db.close();
        return idRecipes;

    }


    /*
    return the number of ingredients of a recipe based on idRecipe TO FIX!
     */
    public int countIngredientsPerRecipes(int idRecipes){
        // Open available reading database
        SQLiteDatabase db = getReadableDatabase();
        int count = 0;

        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM INGREDIENTS WHERE recipeID= " + idRecipes, null );
        if (cursor != null)
        {
            while (cursor.moveToNext()) {
                count++;
            }
        }
        cursor.close();
        db.close();

        return count;
    }



    public int recipes_GetIdByName(String name)
    {
        // Open available reading database
        SQLiteDatabase db = getReadableDatabase();
        //variable id
        int id= -1;
        Cursor cursor = db.rawQuery("SELECT _id FROM RECIPES WHERE recipeName = '" + name + "' ORDER BY _id DESC LIMIT 1", null);
        if(cursor != null)
        {
            while (cursor.moveToNext()){
                id = cursor.getInt(0);
            }
        }

        cursor.close();
        db.close();
        return id;
    }

    public ArrayList<RecipeItem> recipes_SelectNew()
    {
        // Open available reading database
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<RecipeItem> allRecipes = new ArrayList<>();
        // Get all recipes data
        Cursor cursor = db.rawQuery("SELECT * FROM RECIPES ORDER BY _id DESC LIMIT 3", null);
        if (cursor != null)
        {
            while (cursor.moveToNext()) {
                allRecipes.add(new RecipeItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getBlob(5),
                        cursor.getBlob(6),
                        cursor.getInt(7)
                ));
            }
        }
        cursor.close();
        db.close();

        return allRecipes;
    }

    public ArrayList<CategoryItem> recipes_SelectCategory()
    {
        // Open available reading database
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<CategoryItem> categoryList = new ArrayList<>();
        // Get all recipes data
        Cursor cursor = db.rawQuery("SELECT category, mainImg FROM RECIPES GROUP BY category HAVING max(_id)", null);
        if (cursor != null)
        {
            while (cursor.moveToNext()) {
                categoryList.add(new CategoryItem(
                        cursor.getString(0),
                        cursor.getBlob(1)
                ));
            }
        }
        cursor.close();
        db.close();
        return categoryList;
    }

    public ArrayList<RecipeItem> recipes_SelectBest()
    {
        // Open available reading database
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<RecipeItem> allRecipes = new ArrayList<>();
        // Get all recipes data
        Cursor cursor = db.rawQuery("SELECT * FROM RECIPES ORDER BY liked DESC LIMIT 3", null);
        if (cursor != null)
        {
            while (cursor.moveToNext()) {
                allRecipes.add(new RecipeItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getBlob(5),
                        cursor.getBlob(6),
                        cursor.getInt(7)
                ));
            }
        }
        cursor.close();
        db.close();

        return allRecipes;
    }

    public ArrayList<RecipeItem> recipes_SelectAll()
    {
        // Open available reading database
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<RecipeItem> allRecipes = new ArrayList<>();
        // Get all recipes data
        Cursor cursor = db.rawQuery("SELECT * FROM RECIPES", null);
        if (cursor != null)
        {
            while (cursor.moveToNext()) {
                allRecipes.add(new RecipeItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getBlob(5),
                        cursor.getBlob(6),
                        cursor.getInt(7)
                ));
            }
        }
        cursor.close();
        db.close();

        return allRecipes;
    }

    public ArrayList<RecipeItem> recipes_SelectByCategory(String category)
    {
        // Open available reading database
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<RecipeItem> allRecipes = new ArrayList<>();
        // Get all recipes data
        Cursor cursor = db.rawQuery("SELECT * FROM RECIPES WHERE category = '" + category + "'", null);
        if (cursor != null)
        {
            while (cursor.moveToNext()) {
                allRecipes.add(new RecipeItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getBlob(5),
                        cursor.getBlob(6),
                        cursor.getInt(7)
                ));
            }
        }
        cursor.close();
        db.close();

        return allRecipes;
    }

    public RecipeItem recipes_SelectByName(String name)
    {
        // Open available reading database
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM RECIPES WHERE recipeName = '" + name + "'", null);
        if (cursor != null)
        {
            while (cursor.moveToNext()) {
                RecipeItem recipe = new RecipeItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getBlob(5),
                        cursor.getBlob(6),
                        cursor.getInt(7)
                );
                cursor.close();
                db.close();
                return recipe;
            }


        }
        return  null;
    }

    public RecipeItem recipes_SelectById(int id)
    {
        // Open available reading database
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM RECIPES WHERE _id = " + id, null);
        if (cursor != null)
        {
            while (cursor.moveToNext()) {
                RecipeItem recipe = new RecipeItem(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getBlob(5),
                        cursor.getBlob(6),
                        cursor.getInt(7)
                );
                cursor.close();
                db.close();
                return recipe;
            }


        }
        return  null;
    }

    public int recipes_AddLike(String userId, int recipeId)
    {
        // Open available reading database
        SQLiteDatabase db = getReadableDatabase();
        int count= -1;
        Cursor cursor = db.rawQuery("SELECT liked FROM RECIPES WHERE _id = " + recipeId , null);
        if(cursor != null)
        {
            while (cursor.moveToNext()){
                count = cursor.getInt(0);
            }
        }
        cursor.close();

        db = getWritableDatabase();
        db.execSQL("UPDATE RECIPES SET liked = " + (count +1) + " WHERE _id = " + recipeId + ";");
        db.close();
        return count;
    }

    public int recipes_MinusLike(String userId, int recipeId)
    {
        // Open available reading database
        SQLiteDatabase db = getReadableDatabase();
        int count= -1;
        Cursor cursor = db.rawQuery("SELECT liked FROM RECIPES WHERE _id = " + recipeId , null);
        if(cursor != null)
        {
            while (cursor.moveToNext()){
                count = cursor.getInt(0);
            }
        }
        cursor.close();

        db = getWritableDatabase();
        db.execSQL("UPDATE RECIPES SET liked = " + (count - 1) + " WHERE _id = " + recipeId + ";");
        db.close();
        return count;
    }

    public boolean like_GetLiked(String userId, int recipeId)
    {

        SQLiteDatabase db = getReadableDatabase();
        int count= -1;
        Cursor cursor = db.rawQuery("SELECT liked FROM RECIPES WHERE _id = " + recipeId , null);
        if(cursor != null)
        {
            while (cursor.moveToNext()){
                count = cursor.getInt(0);
            }
        }
        cursor.close();
        db.close();

        if(count != 0)
        {
          return true;
        }
        else
        {
          return false;
        }

    }

}