package com.room.desire.ccmanual;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;


public class MainActivity extends FragmentActivity implements DrawerFragment.OnDrawerChildSelected {
    private DrawerLayout mDrawer;

    private ContentFragment mContentFragment;
    private DrawerFragment mListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(this, TickingService.class));

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        mContentFragment = new ContentFragment();
        transaction.replace(R.id.main_content_container, mContentFragment);
        mListFragment = new DrawerFragment();
        transaction.replace(R.id.main_drawer_container, mListFragment);
        transaction.commit();

        mDrawer = (DrawerLayout) findViewById(R.id.main_drawer_layout);
    }

    @Override
    public void onDrawerChildSelected(int groupId, int childId) {
        mContentFragment.setContent(groupId, childId);
        mDrawer.closeDrawers();
    }
}
