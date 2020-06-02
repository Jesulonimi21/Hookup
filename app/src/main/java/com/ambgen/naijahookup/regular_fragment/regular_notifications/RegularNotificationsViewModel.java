package com.ambgen.naijahookup.regular_fragment.regular_notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegularNotificationsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RegularNotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}