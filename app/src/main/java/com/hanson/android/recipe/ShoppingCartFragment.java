package com.hanson.android.recipe;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        final FragmentTransaction ft = getFragmentManager().beginTransaction();

        final View view = inflater.inflate(R.layout.fragment_shoppingcart, container, false);
        ListView listView = (ListView)view.findViewById(R.id.listVeiw_shoppingcart);

        final DBHelper dbHelper = new DBHelper(getContext(), "Recipes.db", null, 1);

        final ArrayList<ShoppingCartItem> shoppingList = dbHelper.get_shoppinglist();

        listView.setAdapter(new shoppingcart_adapter(this.getContext(), shoppingList, R.layout.fragment_shoppingcart_item ));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ShoppingCartItem selectIngredient = shoppingList.get(position);
                final Integer ID = selectIngredient.get_id();

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Did you buy this?");
                builder.setItems(new CharSequence[]
                                {"Not yet", "YES"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        break;
                                    case 1:
                                        dbHelper.shoppinglist_Item_Delete(ID);
                                        ft.detach(ShoppingCartFragment.this).attach(ShoppingCartFragment.this).commit();
                                        Toast.makeText(getActivity(),selectIngredient.get_ingredient_name() + " has been deleted from the cart!", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                builder.create().show();

            }
        });

        return view;

    }
}
