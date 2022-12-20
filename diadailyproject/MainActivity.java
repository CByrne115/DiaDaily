package com.example.diadailyproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.diadailyproject.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Button btn_add, btn_viewAll;
    EditText et_date, et_time, et_sugar, et_foodName;
    ListView lv_foodList;

    ArrayAdapter foodArrayAdapter;
    FoodDatabase foodDatabase;


    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //bottom nav bar

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_food, R.id.navigation_exercise, R.id.navigation_scan, R.id.navigation_reminders)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // nav bar end //



        // food page constraints

        btn_add = findViewById(R.id.btn_add);
        btn_viewAll = findViewById(R.id.btn_viewAll);
        et_date = findViewById(R.id.et_date);
        et_time = findViewById(R.id.et_time);
        et_sugar = findViewById(R.id.et_sugar);
        et_foodName = findViewById(R.id.et_foodName);
        lv_foodList = findViewById(R.id.lv_foodList);

        foodDatabase = new FoodDatabase(MainActivity.this);

        ShowFoodOnList(foodDatabase);

        // button listeners
        btn_add.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FoodModel foodModel;
                try {
                    foodModel = new FoodModel(-1, et_foodName.getText().toString(), (et_sugar.getText().toString()), et_time.getText().toString(), et_date.getText().toString());
                    Toast.makeText(MainActivity.this, foodModel.toString(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    foodModel = new FoodModel(-1, "error","0", "0", "0");


                }


                FoodDatabase dataBaseHelper = new FoodDatabase(MainActivity.this);

                boolean success = dataBaseHelper.addOne(foodModel);

                Toast.makeText(MainActivity.this, "Success=" +success, Toast.LENGTH_SHORT).show();
                ShowFoodOnList(foodDatabase);


            }
        });

        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FoodDatabase foodDatabase = new FoodDatabase(MainActivity.this);


                ShowFoodOnList(foodDatabase);
            }
        });

        lv_foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FoodModel selectedFood = (FoodModel) adapterView.getItemAtPosition(i);
                foodDatabase.deleteFood(selectedFood);
                ShowFoodOnList(foodDatabase);
                Toast.makeText(MainActivity.this, "Deleted" + selectedFood.toString(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    private void ShowFoodOnList(FoodDatabase foodDatabase) {
        foodArrayAdapter = new ArrayAdapter<FoodModel>(MainActivity.this, android.R.layout.simple_list_item_1, this.foodDatabase.getFood());
        lv_foodList.setAdapter(foodArrayAdapter);
    }

}

