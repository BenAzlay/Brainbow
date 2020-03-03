package com.efrei.brainbow.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.efrei.brainbow.Controller.AboutFragment1;
import com.efrei.brainbow.Controller.SectionsStatePagerAdapter;
import com.efrei.brainbow.R;

public class AboutActivity extends AppCompatActivity {
    private SectionsStatePagerAdapter sectionsStatePagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Log.e("PLACE:", "onCreate of AboutActivity");

        sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.container);

        setupViewPager(viewPager); //To enable fragments
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsStatePagerAdapter adapter = new SectionsStatePagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AboutFragment1());
        adapter.addFragment(new AboutFragment2());

        viewPager.setAdapter(adapter);
    }

    public void setNewPager(int fragmentNumber){
        viewPager.setCurrentItem(fragmentNumber);
    }
}
