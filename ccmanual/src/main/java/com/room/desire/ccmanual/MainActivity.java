package com.room.desire.ccmanual;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;


public class MainActivity extends FragmentActivity {

    private MainContentFragment mContentFragment;
    private MainListFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        mContentFragment = new MainContentFragment();
        transaction.replace(R.id.main_content_container, mContentFragment);
        mListFragment = new MainListFragment();
        transaction.replace(R.id.main_drawer_container, mListFragment);
        transaction.commit();
    }

}
