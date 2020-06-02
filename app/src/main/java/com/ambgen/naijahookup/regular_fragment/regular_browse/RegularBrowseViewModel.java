package com.ambgen.naijahookup.regular_fragment.regular_browse;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegularBrowseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RegularBrowseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}