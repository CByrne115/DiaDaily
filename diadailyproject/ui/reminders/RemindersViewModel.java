package com.example.diadailyproject.ui.reminders;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RemindersViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RemindersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Reminder fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}