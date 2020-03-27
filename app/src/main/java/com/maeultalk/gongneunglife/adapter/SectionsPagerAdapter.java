package com.maeultalk.gongneunglife.adapter;


import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import android.text.TextUtils;

import com.maeultalk.gongneunglife.fragment.placeActivity.InfoFragment;
import com.maeultalk.gongneunglife.fragment.placeActivity.StoreFragment;
import com.maeultalk.gongneunglife.fragment.placeActivity.TimeLineFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private Fragment timelineFragment, storeFragment, infoFragment;

    private FloatingActionButton fab;

    private String placeCode;
    private String placeName;
    private String contentId;
    private boolean scrap;

    public SectionsPagerAdapter(FragmentManager fm, FloatingActionButton fab, String placeCode, String placeName) {
        super(fm);
        this.fab = fab;
        this.placeCode = placeCode;
        this.placeName = placeName;
    }

    public SectionsPagerAdapter(FragmentManager fm, FloatingActionButton fab, String placeCode, String placeName, String contentId, boolean scrap) {
        super(fm);
        this.fab = fab;
        this.placeCode = placeCode;
        this.placeName = placeName;
        this.contentId = contentId;
        this.scrap = scrap;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);

        switch (position) {
            case 0:
//                fab.show();
                if(timelineFragment == null) {
                    timelineFragment = new TimeLineFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("place_code", placeCode); // Key, Value
                    bundle.putString("place_name", placeName); // Key, Value
                    if(!TextUtils.isEmpty(contentId)) {
                        bundle.putString("content_id", contentId); // Key, Value
                        bundle.putBoolean("scrap", scrap); // Key, Value
                    }
                    timelineFragment.setArguments(bundle);
                    return timelineFragment;
                } else {
                    return timelineFragment;
                }
            case 2:
//                fab.hide();
                if(storeFragment == null) {
                    storeFragment = new StoreFragment();
                    return storeFragment;
                } else {
                    return storeFragment;
                }
            case 1:
//                fab.hide();
                if(infoFragment == null) {
                    infoFragment = new InfoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("place_code", placeCode); // Key, Value
                    bundle.putString("place_name", placeName); // Key, Value
                    infoFragment.setArguments(bundle);
                    return infoFragment;
                } else {
                    return infoFragment;
                }
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }


}
