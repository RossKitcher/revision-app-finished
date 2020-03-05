package com.example.revision_app_two.ui.psychology;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PsychologyViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PsychologyViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}