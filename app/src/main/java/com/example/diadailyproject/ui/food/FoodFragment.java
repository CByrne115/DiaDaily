package com.example.diadailyproject.ui.food;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.diadailyproject.FoodDatabase;
import com.example.diadailyproject.FoodModel;
import com.example.diadailyproject.R;
import com.example.diadailyproject.databinding.FragmentFoodBinding;
import com.example.diadailyproject.databinding.FragmentFoodBinding;

public class FoodFragment extends Fragment {

    Button btn_add_food, btn_viewAll_food;
    EditText et_date, et_time, et_sugar, et_foodName;
    ListView lv_foodList;
    ArrayAdapter foodArrayAdapter;

    FoodDatabase foodDatabase;

    private FragmentFoodBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFoodBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // find views
        btn_add_food = root.findViewById(R.id.btn_add_food);
        btn_viewAll_food = root.findViewById(R.id.btn_viewAll_food);
        et_date = root.findViewById(R.id.et_date);
        et_time = root.findViewById(R.id.et_time);
        et_sugar = root.findViewById(R.id.et_sugar);
        et_foodName = root.findViewById(R.id.et_foodName);
        lv_foodList = root.findViewById(R.id.lv_foodList);

        // set up database and list view
        foodDatabase = new FoodDatabase(getContext());
        ShowFoodOnList();

        // set up button listeners
        btn_add_food.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FoodModel foodModel;
                try {
                    foodModel = new FoodModel(-1, et_foodName.getText().toString(), (et_sugar.getText().toString()), et_time.getText().toString(), et_date.getText().toString());
                    Toast.makeText(getContext(), foodModel.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    foodModel = new FoodModel(-1, "error", "0", "0", "0");
                }

                boolean success = foodDatabase.addOne(foodModel);
                Toast.makeText(getContext(), "Success=" + success, Toast.LENGTH_SHORT).show();
                ShowFoodOnList();
            }
        });

        btn_viewAll_food.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShowFoodOnList();
            }
        });

        lv_foodList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FoodModel selectedFood = (FoodModel) adapterView.getItemAtPosition(i);
                foodDatabase.deleteFood(selectedFood);
                ShowFoodOnList();
                Toast.makeText(getContext(), "Deleted" + selectedFood.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void ShowFoodOnList() {
        foodArrayAdapter = new ArrayAdapter<FoodModel>(getContext(), android.R.layout.simple_list_item_1, foodDatabase.getFood());
        lv_foodList.setAdapter(foodArrayAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}