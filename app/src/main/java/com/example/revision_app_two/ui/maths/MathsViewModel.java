package com.example.revision_app_two.ui.maths;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MathsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MathsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }


    public LiveData<String> getText() {
        return mText;
    }
}