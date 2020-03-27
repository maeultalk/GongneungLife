package com.maeultalk.gongneunglife.activity;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import com.maeultalk.gongneunglife.R;

public class SearchActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        };

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        CollapsingToolbarLayout mCollapsingToolbarLayout;

        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbarLayout.setTitleEnabled(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        /*ActionBar actionBar = getActionBar();
        SearchView searchView = new SearchView(this);
        actionBar.setCustomView(searchView);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        searchView.setQuery("test",true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchMenuItem = menu.findItem( R.id.action_search_activity ); // get my MenuItem with placeholder submenu

        searchMenuItem.expandActionView(); // Expand the search menu item in order to show by default the query

        /*searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Toast.makeText(getApplicationContext(), "onMenuItemActionExpand", Toast.LENGTH_SHORT);
                TextView text = (TextView)findViewById(R.id.textView);

                text.setText("현재 상태 : 확장됨");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Toast.makeText(getApplicationContext(), "onMenuItemActionExpand", Toast.LENGTH_SHORT);
                TextView text = (TextView)findViewById(R.id.textView);

                text.setText("현재 상태 : 축소됨");

                finish();
                return true;
            }
        });*/



        /*final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // This is where you can be notified when the `SearchView` is closed
                // and change your views you see fit.
                Log.d("warren", "searchView back pressed");
                Toast.makeText(getApplicationContext(), "searchView back pressed", Toast.LENGTH_SHORT);
                return false;
            }
        });

        searchMenuItem.setActionView(searchView);*/

        /*searchView.setQueryHint("장소 검색");
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();*/


        // Inflate the menu; this adds items to the action bar if it is present.
        /*getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


        ActionBar actionBar = getActionBar();
//        SearchView searchView = new SearchView(this);
//        actionBar.setCustomView(searchView);
//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        searchView.setQuery("test",true);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();*/

//        searchView.setIconifiedB
// yDefault(false);
//        searchView.requestFocus();




        /*searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                System.out.println(s);
                return false;
            }
        });
        return true;*/
        return true;

    }
}
