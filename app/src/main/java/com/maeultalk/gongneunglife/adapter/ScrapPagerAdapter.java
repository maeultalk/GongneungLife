package com.maeultalk.gongneunglife.adapter;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.maeultalk.gongneunglife.fragment.mainActivity.scrapFragment.ContentsFragment;
import com.maeultalk.gongneunglife.fragment.mainActivity.scrapFragment.PlaceFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class ScrapPagerAdapter extends FragmentPagerAdapter {

    private Fragment placeFragment, contentsFragment;
    Context context;

    public ScrapPagerAdapter(FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);

        switch (position) {
            case 0:
                if(placeFragment == null) {
                    placeFragment = new PlaceFragment();
                    /*Bundle bundle = new Bundle();
                    bundle.putString("place_code", placeCode); // Key, Value
                    bundle.putString("place_name", placeName); // Key, Value
                    if(!TextUtils.isEmpty(contentId)) {
                        bundle.putString("content_id", contentId); // Key, Value
                        bundle.putBoolean("scrap", scrap); // Key, Value
                    }
                    placeFragment.setArguments(bundle);*/
                    return placeFragment;
                } else {
                    return placeFragment;
                }
            case 1:
                if(contentsFragment == null) {
                    contentsFragment = new ContentsFragment();
                    /*Bundle bundle = new Bundle();
                    bundle.putString("place_code", placeCode); // Key, Value
                    bundle.putString("place_name", placeName); // Key, Value
                    infoFragment.setArguments(bundle);*/
                    return contentsFragment;
                } else {
                    return contentsFragment;
                }
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }


}
