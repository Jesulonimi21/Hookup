package com.ambgen.naijahookup.regular_fragment.regular_chats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegularChatsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RegularChatsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}