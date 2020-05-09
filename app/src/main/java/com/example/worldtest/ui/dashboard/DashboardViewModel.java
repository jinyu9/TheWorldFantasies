package com.example.worldtest.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("该板块将于第二次迭代实现，敬请期待。");
    }

    public LiveData<String> getText() {
        return mText;
    }
}