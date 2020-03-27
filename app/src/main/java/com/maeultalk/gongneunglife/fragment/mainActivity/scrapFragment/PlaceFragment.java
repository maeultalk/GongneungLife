package com.maeultalk.gongneunglife.fragment.mainActivity.scrapFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
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
import com.maeultalk.gongneunglife.activity.PlaceActivity;
import com.maeultalk.gongneunglife.adapter.PlaceFragmentAdapter;
import com.maeultalk.gongneunglife.model.Favorite;
import com.maeultalk.gongneunglife.request.LoadFavoritePlaceRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public static ArrayList<Favorite> mItems = new ArrayList<>();

    ListView listView;
    PlaceFragmentAdapter favoriteAdapter;

    View header;

    @Override
    public void onResume() {
        super.onResume();

        loadData();

    }

    public PlaceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaceFragment newInstance(String param1, String param2) {
        PlaceFragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_place, container, false);

        listView = (ListView) view.findViewById(R.id.listView);

        header = getLayoutInflater().inflate(R.layout.header_favorite_place, null, false);
        listView.addHeaderView(header) ;

//        loadData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), PlaceActivity.class);
                intent.putExtra("place_code", mItems.get(i-1).getPlace_code());
                intent.putExtra("place_name", mItems.get(i-1).getPlace_name());
                if(mItems.get(i-1).getType().equals("content")) {
                    intent.putExtra("content_id", mItems.get(i-1).getContent_id());
                    intent.putExtra("scrap", true);
                }
                startActivity(intent);
            }
        });

        return view;
    }

    void loadData() {

        SharedPreferences pref = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        String user = pref.getString("email", "");

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = (JSONArray) jsonResponse.get("favorites");
                    Gson gson = new Gson();
                    Type listType = new TypeToken<ArrayList<Favorite>>(){}.getType();
                    mItems = gson.fromJson(jsonArray.toString(), listType);
                    updateUI();
                    /*InboxAdapter inboxAdapter = new InboxAdapter(getActivity(), inboxItems);
                    listview.setAdapter(inboxAdapter);*/
                } catch (JSONException e) {

                }

            }
        };
        LoadFavoritePlaceRequest loadFavoritePlaceRequest = new LoadFavoritePlaceRequest(user, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(loadFavoritePlaceRequest);

    }

    private void updateUI() {
        if(favoriteAdapter == null){
            setListView();
        }else{
            favoriteAdapter.notifyDataSetChanged();
        }
    }

    private void setListView(){
        favoriteAdapter = new PlaceFragmentAdapter(getContext(), mItems);
        listView.setAdapter(favoriteAdapter);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
