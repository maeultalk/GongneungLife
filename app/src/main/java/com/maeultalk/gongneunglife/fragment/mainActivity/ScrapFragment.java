package com.maeultalk.gongneunglife.fragment.mainActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.activity.AddPlaceActivity;
import com.maeultalk.gongneunglife.activity.MyActivity;
import com.maeultalk.gongneunglife.activity.MyPageActivity;
import com.maeultalk.gongneunglife.activity.PlaceActivity;
import com.maeultalk.gongneunglife.activity.SettingsActivity;
import com.maeultalk.gongneunglife.adapter.FavoriteAdapter;
import com.maeultalk.gongneunglife.adapter.ScrapPagerAdapter;
import com.maeultalk.gongneunglife.adapter.SectionsPagerAdapter;
import com.maeultalk.gongneunglife.model.Favorite;
import com.maeultalk.gongneunglife.request.LoadFavoriteRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class ScrapFragment extends Fragment {

    private RecyclerView recyclerView;

    private String[] names = {"Charlie","Andrew","Han","Liz","Thomas","Sky","Andy","Lee","Park"};
    private RecyclerView.Adapter adapter;
    public static ArrayList<Favorite> mItems = new ArrayList<>();

    ListView listView;
    FavoriteAdapter favoriteAdapter;

    View header;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private ScrapPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public ScrapFragment() {
        // Required empty public constructor
    }

    public static ScrapFragment newInstance() {
        ScrapFragment fragment = new ScrapFragment();
        return fragment;
    }

    public void move() {
        listView.smoothScrollToPosition(0);
    }

    @Override
    public void onResume() {
        super.onResume();

//        loadData();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scrap, container, false);

        mSectionsPagerAdapter = new ScrapPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_scrap, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivityForResult(intent, 400);
            return true;
        } else if (id == R.id.action_my) {
            Intent intent = new Intent(getActivity(), MyPageActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
