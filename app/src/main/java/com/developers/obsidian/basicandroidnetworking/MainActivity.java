package com.developers.obsidian.basicandroidnetworking;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.developers.obsidian.basicandroidnetworking.drawerfragments.BasicImageFragment;
import com.developers.obsidian.basicandroidnetworking.drawerfragments.BasicJSONFragment;
import com.developers.obsidian.basicandroidnetworking.drawerfragments.BasicXMLFragment;

public class MainActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mLeftDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    TextView title;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mFragmentTitles = {"Basic Json", "Basic XML", "Basic Image Loading"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mLeftDrawer = (ListView) findViewById(R.id.left_drawer);

        title = (TextView) findViewById(R.id.title);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        mLeftDrawer.setAdapter(new CustomAdapter(this, mFragmentTitles));

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(R.drawable.invisible_0);

        mLeftDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
                ((CustomAdapter) parent.getAdapter()).selectItem(position);
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if (savedInstanceState == null) {
            selectItem(0);
        }

//        mDrawerLayout.openDrawer(mLeftDrawer);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mLeftDrawer);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//       MenuInflater inflater = getMenuInflater();
//       inflater.inflate(R.menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(mLeftDrawer)) {
                    mDrawerLayout.closeDrawer(mLeftDrawer);
                } else {
                    mDrawerLayout.openDrawer(mLeftDrawer);
                }
                return true;
        }
        return true;
    }

    private void selectItem(int position) {
        Fragment newFragment = new BasicJSONFragment();
        FragmentManager fm = getSupportFragmentManager();
        switch (position) {
            case 0:
                newFragment = new BasicJSONFragment();
                break;

            case 1:
                newFragment = new BasicXMLFragment();
                break;

            case 2:
                newFragment = new BasicImageFragment();
                break;

        }

        fm.beginTransaction().replace(R.id.content_frame, newFragment).commit();
        mLeftDrawer.setItemChecked(position, true);
        setTitle(mFragmentTitles[position]);
        mDrawerLayout.closeDrawer(mLeftDrawer);

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);

    }

    public class CustomAdapter extends BaseAdapter {

        Context context;
        String[] mTitle;
        LayoutInflater inflater;
        private int selectedItem;

        public CustomAdapter(Context context, String[] title) {
            this.context = context;
            this.mTitle = title;
        }

        public void selectItem(int selectedItem) {
            this.selectedItem = selectedItem;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTitle.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitle[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            TextView txtTitle;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View itemView = inflater.inflate(R.layout.new_drawer_list_item, parent, false);

            txtTitle = (TextView) itemView.findViewById(R.id.drawerItemText);

            txtTitle.setText(mTitle[position]);
            txtTitle.setTypeface(null, position == selectedItem ? Typeface.BOLD : Typeface.NORMAL);

            return itemView;
        }
    }
}