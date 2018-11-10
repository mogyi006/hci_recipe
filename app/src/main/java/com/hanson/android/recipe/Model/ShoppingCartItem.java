package com.hanson.android.recipe.Model;

public class ShoppingCartItem {
    private String ingredient;

    public String get_ingredient(){return ingredient;}

    public ShoppingCartItem(String ingredient) {
        this.ingredient = ingredient;
    }
}
