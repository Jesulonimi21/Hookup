package com.ambgen.naijahookup.regular_fragment.regular_notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ambgen.naijahookup.R;

public class RegularNotificationsFragment extends Fragment {

    private RegularNotificationsViewModel regularNotificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        regularNotificationsViewModel =
                ViewModelProviders.of(this).get(RegularNotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_regular_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        regularNotificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
