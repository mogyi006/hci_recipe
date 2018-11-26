package com.hanson.android.recipe;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.hanson.android.recipe.Helper.DBHelper;
import com.hanson.android.recipe.Model.ShoppingCartItem;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingCartFragment extends Fragment {


    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_shoppingcart, container, false);
        ListView listView = (ListView)view.findViewById(R.id.listVeiw_shoppingcart);

        final DBHelper dbHelper = new DBHelper(getContext(), "Recipes.db", null, 1);

        final ArrayList<ShoppingCartItem> shoppingList = dbHelper.get_shoppinglist();

        listView.setAdapter(new shoppingcart_adapter(this.getContext(), shoppingList, R.layout.fragment_shoppingcart_item ));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShoppingCartItem selectIngredient = shoppingList.get(position);
                Integer ID = selectIngredient.get_id();
            }
        });

        return view;

    }
}
