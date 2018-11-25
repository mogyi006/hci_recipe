package com.hanson.android.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hanson.android.recipe.Model.ShoppingCartItem;

import java.util.ArrayList;

public class shoppingcart_adapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<ShoppingCartItem> shoppingList;
    private int layout;

    public shoppingcart_adapter(Context context, ArrayList<ShoppingCartItem> shoppingList, int layout) {
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.shoppingList = shoppingList;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return shoppingList.size();
    }

    @Override
    public Object getItem(int position) {
        return shoppingList.get(position).get_ingredient_name();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        ShoppingCartItem shoppingItem = shoppingList.get(position);

        TextView name = (TextView)convertView.findViewById(R.id.shoppingcartitem_text);
        name.setText(shoppingItem.get_ingredient_name());
        return convertView;
    }
}
