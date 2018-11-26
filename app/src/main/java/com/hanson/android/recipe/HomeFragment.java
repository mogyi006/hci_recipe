package com.hanson.android.recipe;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.hanson.android.recipe.Helper.DBHelper;
import com.hanson.android.recipe.Helper.ImageHelper;
import com.hanson.android.recipe.Model.RecipeItem;

import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {
    ImageHelper imageHelper = new ImageHelper();
    ArrayList<RecipeItem> bestList;
    ArrayList<RecipeItem> newList;

    Date today = new Date();

    public HomeFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check search recipes menu on the slide menu
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                navigationView.setCheckedItem(R.id.nav_search);
                //connect to search recipes page
                FragmentManager manager = getActivity().getSupportFragmentManager();
                SearchFragment searchFragment = new SearchFragment();
                manager.beginTransaction().replace(R.id.root_layout, searchFragment, searchFragment.getTag()).addToBackStack(null).commit();
            }
        });
        GridView newGridView = (GridView)view.findViewById(R.id.GridView_New);

        //Connect DB
        DBHelper dbHelper = new DBHelper(getContext(), "Recipes.db", null, 1);

        ArrayList<RecipeItem> defaultDataList = dbHelper.recipes_SelectAll();
        if(defaultDataList == null || defaultDataList.size() == 0)
        {
            //Set dummy data
            Drawable drawable = getResources().getDrawable(R.drawable.bananas, getActivity().getTheme());
            byte[] bananas = imageHelper.getByteArrayFromDrawable(drawable);

            dbHelper.recipes_Insert("Dessert", "Bananas in Caramel Sauce",
                    "1. Melt butter in a large, heavy skillet over medium heat. Stir in sugar and cook, stirring, until sugar is melted and light brown. Slowly stir in the cream (mixture will bubble up). \n " +
                            "2. Let mixture boil 1 minute, then reduce heat to low. Place the bananas in the pan and cook until heated through, about 2 minutes. Serve hot. \n",
                    "A delicious, fast dessert. Impressive served when the sauce is still bubbling! Serve with coconut ice cream, if desired.",
                    bananas, bananas, 0);

            int bananasId = dbHelper.recipes_GetIdByName("Bananas in Caramel Sauce");
            ArrayList<String> bananasIngre = new ArrayList<>();
            bananasIngre.add("0.5 cup butter");
            bananasIngre.add("1 cup superfine sugar");
            bananasIngre.add("0.25 cups heavy cream");
            bananasIngre.add("4 pc bananas");

            for (int i = 0; i < bananasIngre.size(); i++)
            {
                dbHelper.ingredients_Insert(bananasId,bananasIngre.get(i));
            }

            //Set dummy data
            drawable = getResources().getDrawable(R.drawable.honeymch, getActivity().getTheme());
            byte[] honeymch = imageHelper.getByteArrayFromDrawable(drawable);

            dbHelper.recipes_Insert("Main Course", "Honey Mustard Chicken",
                    "1. Preheat oven to 350 degrees F (175 degrees C). \n" +
                            "2. Sprinkle chicken breasts with salt and pepper to taste, and place in a lightly greased 9x13 inch baking dish. In a small bowl, combine the honey, mustard, basil, paprika, and parsley. Mix well. Pour 1/2 of this mixture over the chicken, and brush to cover. \n" +
                            "3. Bake in the preheated oven for 30 minutes. Turn chicken pieces over and brush with the remaining 1/2 of the honey mustard mixture. Bake for an additional 10 to 15 minutes, or until chicken is no longer pink and juices run clear. Let cool 10 minutes before serving. \n",
                    "Quick and easy to prepare, and the kids love it too!",
                    honeymch, honeymch, 1);

            int honeymchId = dbHelper.recipes_GetIdByName("Honey Mustard Chicken");
            ArrayList<String> honeymchIngre = new ArrayList<>();
            honeymchIngre.add("6 chicken breast");
            honeymchIngre.add("1 ts dried basil");
            honeymchIngre.add("1 ts paprika");
            honeymchIngre.add("0.5 cup honey");
            honeymchIngre.add("0.5 cup mustard");
            honeymchIngre.add("0.5 ts parsley");

            for (int i = 0; i < honeymchIngre.size(); i++)
            {
                dbHelper.ingredients_Insert(honeymchId,honeymchIngre.get(i));
            }

            //Set dummy data
            drawable = getResources().getDrawable(R.drawable.bolognese, getActivity().getTheme());
            byte[] bolognese = imageHelper.getByteArrayFromDrawable(drawable);

            dbHelper.recipes_Insert("Pasta", "Bolognese",
                    "1. Put the onion and oil in a large pan and fry over a fairly high heat for 3-4 mins. Add the garlic and mince and fry until they both brown. Add the mushrooms and herbs, and cook for another couple of mins.\n" +
                            "2. Stir in the tomatoes, beef stock, tomato ketchup or purée, Worcestershire sauce and seasoning. Bring to the boil, then reduce the heat, cover and simmer, stirring occasionally, for 30 mins.\n" +
                            "3. Meanwhile, cook the spaghetti in a large pan of boiling, salted water, according to packet instructions. Drain well, run hot water through it, put it back in the pan and add a dash of olive oil, if you like, then stir in the meat sauce. Serve in hot bowls and hand round Parmesan cheese, for sprinkling on top.",
                    "Make our traditional spaghetti Bolognese recipe with homemade Bolognese sauce and tender chunks of beef, making this dish a family favourite.",
                    bolognese, bolognese, 1);

            int bologneseId = dbHelper.recipes_GetIdByName("Bolognese");
            ArrayList<String> bologneseIngre = new ArrayList<>();
            bologneseIngre.add("4 pc onion");
            bologneseIngre.add("1 cupolive oil");
            bologneseIngre.add("spaghetti");
            bologneseIngre.add("0.15 kg Parmesan");
            bologneseIngre.add("3 pc mushroom");
            bologneseIngre.add("0.5 ts oregano");
            bologneseIngre.add("5 pc tomatoes");
            bologneseIngre.add("beef stock");
            bologneseIngre.add("beef");

            for (int i = 0; i < bologneseIngre.size(); i++)
            {
                dbHelper.ingredients_Insert(bologneseId,bologneseIngre.get(i));
            }

            //Set dummy data
            drawable = getResources().getDrawable(R.drawable.chickencacciatore, getActivity().getTheme());
            byte[] chickencacciatore = imageHelper.getByteArrayFromDrawable(drawable);

            dbHelper.recipes_Insert("Main Course", "Chicken Cacciatore ",
                    "1. Combine the flour, salt and pepper in a plastic bag. Shake the chicken pieces in flour until coated. Heat the oil in a large skillet (one that has a cover/lid). Fry the chicken pieces until they are browned on both sides. Remove from skillet.\n" +
                            "2. Add the onion, garlic and bell pepper to the skillet and saute until the onion is slightly browned. Return the chicken to the skillet and add the tomatoes, oregano and wine. Cover and simmer for 30 minutes over medium low heat.\n" +
                            "3. Add the mushrooms and salt and pepper to taste. Simmer for 10 more minutes.",
                    "Many food names reflect various occupations or trades.",
                    chickencacciatore, chickencacciatore, 0);

            int chickencacciatoreId = dbHelper.recipes_GetIdByName("Chicken Cacciatore ");
            ArrayList<String> chickencacciatoreIngre = new ArrayList<>();
            chickencacciatoreIngre.add("0.25 kg flour");
            chickencacciatoreIngre.add("salt");
            chickencacciatoreIngre.add("black pepper");
            chickencacciatoreIngre.add("0.5 kg chicken");
            chickencacciatoreIngre.add("1 cup vegetable oil");
            chickencacciatoreIngre.add("2 pc onion");
            chickencacciatoreIngre.add("4 pc tomatoes");
            chickencacciatoreIngre.add("0.5 ts oregano");
            chickencacciatoreIngre.add("1 bottle of wine");

            for (int i = 0; i < chickencacciatoreIngre.size(); i++)
            {
                dbHelper.ingredients_Insert(chickencacciatoreId,chickencacciatoreIngre.get(i));
            }

            //Set dummy data
            drawable = getResources().getDrawable(R.drawable.cookie, getActivity().getTheme());
            byte[] cookie = imageHelper.getByteArrayFromDrawable(drawable);

            dbHelper.recipes_Insert("Dessert", "Peanut Butter Cookies",
                    "1. Preheat oven to 350 degrees F (175 degrees C). Grease cookie sheets. \n" +
                            "2. In a medium bowl, stir peanut butter and sugar together until smooth. " +
                            "3. Beat in the eggs, one at a time, then stir in the baking soda, salt, and vanilla. " +
                            "4. Roll dough into 1 inch balls and place them 2 inches apart onto the prepared cookie sheets. " +
                            "5. Press a criss-cross into the top using the back of a fork. \n" +
                            "6. Meanwhile, cook the spaghetti in a large pan of boiling, salted water, according to packet instructions. " +
                            "7. Drain well, run hot water through it, put it back in the pan and add a dash of olive oil, if you like, then stir in the meat sauce. " +
                            "8. Serve in hot bowls and hand round Parmesan cheese, for sprinkling on top.",
                    "This is an amazing no-flour peanut butter cookie. It is so easy, even kids like to make it.",
                    cookie, cookie, 1);

            int cookieId = dbHelper.recipes_GetIdByName("Peanut Butter Cookies");
            ArrayList<String> cookieIngre = new ArrayList<>();
            cookieIngre.add("2 cups peanut butter");
            cookieIngre.add("2 cups white sugar");
            cookieIngre.add("2 pc eggs");
            cookieIngre.add("2 ts baking soda");
            cookieIngre.add("1 pinch salt");
            cookieIngre.add("1 ts vanilla extract");

            for (int i = 0; i < cookieIngre.size(); i++)
            {
                dbHelper.ingredients_Insert(cookieId,cookieIngre.get(i));
            }

            //Set dummy data
            drawable = getResources().getDrawable(R.drawable.salmon, getActivity().getTheme());
            byte[] salmon = imageHelper.getByteArrayFromDrawable(drawable);

            dbHelper.recipes_Insert("Main Course", "Baked Salmon Fillets",
                    "1. Preheat oven to 400 degrees F (200 degrees C). Line a shallow baking pan with aluminum foil.\n" +
                            "2. Place salmon skin-side down on foil. Spread a thin layer of mustard on the top of each fillet, and season with salt and pepper. Top with bread crumbs, then drizzle with melted butter.\n" +
                            "3. Bake in a preheated oven for 15 minutes, or until salmon flakes easily with a fork. \n" +
                            "    Tip\n" +
                            "    Aluminum foil can be used to keep food moist, cook it evenly, and make clean-up easier.\n",
                    "Delicious baked salmon coated with Dijon-style mustard and seasoned bread crumbs, and topped with butter.",
                    salmon, salmon, 0);

            int salmonId = dbHelper.recipes_GetIdByName("Baked Salmon Fillets");
            ArrayList<String> salmonIngre = new ArrayList<>();
            salmonIngre.add("4 ounce fillets salmon");
            salmonIngre.add("3 ts mustard");
            salmonIngre.add("0.25 cup bread crumbs");
            salmonIngre.add("0.25 cup butter");

            for (int i = 0; i < salmonIngre.size(); i++)
            {
                dbHelper.ingredients_Insert(salmonId,salmonIngre.get(i));
            }
        }

        TextView resultTextView = (TextView) view.findViewById(R.id.txt_DBresult);
        //resultTextView.setText(tempcategory);

        newList = dbHelper.recipes_SelectNew();

        newGridView.setAdapter(new MainRecipeAdapter(this.getContext(), newList, R.layout.fragment_home_recipeitem));

        newGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                RecipeItem selectRecipe = newList.get(position);
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                intent.putExtra("recipe", selectRecipe.get_recipeName());
                startActivity(intent);
                //Toast.makeText(view.getContext(),selectRecipe.get_recipeName(),Toast.LENGTH_SHORT).show();
            }
        });


        //connect GrieView code to UI
        GridView bestGridView = (GridView)view.findViewById(R.id.GridView_Best);

        bestList = dbHelper.recipes_SelectBest();

        bestGridView.setAdapter(new MainRecipeAdapter(this.getContext(), bestList, R.layout.fragment_home_recipeitem));

        bestGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                RecipeItem selectRecipe = bestList.get(position);
                Intent intent = new Intent(getActivity(), RecipeActivity.class);
                intent.putExtra("recipe", selectRecipe.get_recipeName());
                startActivity(intent);
                //Toast.makeText(view.getContext(),selectRecipe.get_recipName(),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
