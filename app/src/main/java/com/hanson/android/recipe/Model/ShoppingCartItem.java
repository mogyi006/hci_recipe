package com.hanson.android.recipe.Model;


public class ShoppingCartItem {
    private int id;
    private String ingreName;
    private String ingreM;
    private String ingreQ;


    public int get_id(){ return id; }
    public String get_ingredient_name(){return ingreName;}
    public String get_ingreM(){return ingreM;}
    public String get_ingreQ(){return ingreQ;}


    public ShoppingCartItem(int id, String ingredient_name,String ingreM,String ingreQ) {
        this.id = id;
        this.ingreName = ingredient_name;
        this.ingreM = ingreM;
        this.ingreQ = ingreQ;
    }
}
