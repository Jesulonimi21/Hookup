package com.ambgen.naijahookup.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.ambgen.naijahookup.R;
import com.ambgen.naijahookup.commercial_fragments.account.AccountFragment;
import com.ambgen.naijahookup.regular_fragment.regular_account.RegularAccountFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import io.paperdb.Paper;

public class RegularHomeActivity extends AppCompatActivity implements RegularAccountFragment.imagePickerListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regular_home);
        Paper.init(this);
    }

    RegularAccountFragment fragment;
    @Override
    public void chooseImg(RegularAccountFragment fragment) {
        this.fragment=fragment;
        Intent i=new Intent();
        i.setAction(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i,9);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9 && resultCode == RESULT_OK && data != null & data.getData() != null) {
            Uri uri = data.getData();
            fragment.imageChosen(uri);
        }
    }
}
