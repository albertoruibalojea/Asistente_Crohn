package com.aro.asistente_crohn.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aro.asistente_crohn.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home);

        //we cannot use openFragment to avoid the backstack null with a blank screen
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentView, new HomeFragment());
        fragmentTransaction.commit();
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentView, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openFragment(Fragment fragment, Date date) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        SimpleDateFormat simpleDateFormat =new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss zzz", new Locale("es", "ES"));
        bundle.putString("date", simpleDateFormat.format(date));
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragmentView, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}