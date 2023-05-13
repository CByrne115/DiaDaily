package com.example.diadailyproject.ui.exercise;

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

import com.example.diadailyproject.ExerciseDatabase;
import com.example.diadailyproject.ExerciseModel;

import com.example.diadailyproject.R;
import com.example.diadailyproject.databinding.FragmentExerciseBinding;


public class ExerciseFragment extends Fragment {

    Button  btn_add_exercise, btn_viewAll_exercise;
    EditText et_time, et_exercise,  et_calories, et_duration;
    ListView lv_exerciseList;
    ArrayAdapter exerciseArrayAdapter;
    ExerciseDatabase exerciseDatabase;

    private FragmentExerciseBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentExerciseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // find views
        btn_add_exercise = root.findViewById(R.id.btn_add_exercise);
        et_calories = root.findViewById(R.id.et_calories);
        et_exercise = root.findViewById(R.id.et_exercise);
        et_duration = root.findViewById(R.id.et_duration);
        et_time = root.findViewById(R.id.et_time);
        lv_exerciseList = root.findViewById(R.id.lv_exerciseList);
        btn_viewAll_exercise = root.findViewById(R.id.btn_viewAll_exercise);

        // set up database and list view
        exerciseDatabase = new ExerciseDatabase(getContext());
        ShowExerciseList();

        // set up button listeners
        btn_add_exercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ExerciseModel exerciseModel;
                try {
                    exerciseModel = new ExerciseModel(-1, et_exercise.getText().toString(), (et_duration.getText().toString()), et_calories.getText().toString(), et_time.getText().toString());
                    Toast.makeText(getContext(), exerciseModel.toString(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                    exerciseModel = new ExerciseModel(-1, "error", "0", "0", "0");
                }

                boolean success = exerciseDatabase.addOne(exerciseModel);
                Toast.makeText(getContext(), "Success=" + success, Toast.LENGTH_SHORT).show();
                ShowExerciseList();
            }
        });

        btn_viewAll_exercise.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShowExerciseList();
            }
        });

        lv_exerciseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ExerciseModel selectedExercise = (ExerciseModel) adapterView.getItemAtPosition(i);
                exerciseDatabase.deleteExercise(selectedExercise);
                ShowExerciseList();
                Toast.makeText(getContext(), "Deleted" + selectedExercise.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void ShowExerciseList() {
        exerciseArrayAdapter = new ArrayAdapter<ExerciseModel>(getContext(), android.R.layout.simple_list_item_1, exerciseDatabase.getExercise());
        lv_exerciseList.setAdapter(exerciseArrayAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}