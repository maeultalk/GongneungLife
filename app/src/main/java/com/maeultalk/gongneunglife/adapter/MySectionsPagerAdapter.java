package com.maeultalk.gongneunglife.adapter;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.maeultalk.gongneunglife.fragment.myActivity.CommentsFragment;
import com.maeultalk.gongneunglife.fragment.myActivity.ContentsFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class MySectionsPagerAdapter extends FragmentPagerAdapter {

    private Fragment contentsFragment, commentsFragment;

    public MySectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);

        switch (position) {
            case 0:
                if(contentsFragment == null) {
                    contentsFragment = new ContentsFragment();
                    Bundle bundle = new Bundle();
//                    bundle.putString("place_code", placeCode); // Key, Value
//                    bundle.putString("place_name", placeName); // Key, Value
                    contentsFragment.setArguments(bundle);
                    return contentsFragment;
                } else {
                    return contentsFragment;
                }
            case 1:
                if(commentsFragment == null) {
                    commentsFragment = new CommentsFragment();
                    Bundle bundle = new Bundle();
//                    bundle.putString("place_code", placeCode); // Key, Value
//                    bundle.putString("place_name", placeName); // Key, Value
                    commentsFragment.setArguments(bundle);
                    return commentsFragment;
                } else {
                    return commentsFragment;
                }
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }


}
