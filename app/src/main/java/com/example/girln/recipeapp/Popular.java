package com.example.girln.recipeapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Popular.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Popular#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Popular extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mData;



    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private recipeList.OnFragmentInteractionListener mListener;

    public Popular() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment recipeList.
     */
    // TODO: Rename and change types and number of parameters
    public static recipeList newInstance(String param1, String param2) {
        recipeList fragment = new recipeList();
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

        mData = FirebaseDatabase.getInstance().getReference();
        mData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map tmp,rate;
                double sum = 0.0d;
                Object RList = dataSnapshot.child("Recipes").getValue();
                List<String> keyList = new ArrayList<String>();
                tmp = (HashMap) RList;
                keyList.addAll(tmp.keySet());
                tmp.clear();
                for (String key : keyList) {
                    sum = 0.0d;
                    Object Ratings = dataSnapshot.child("Ratings").child(key).getValue();
                    rate = (HashMap) Ratings;
                    if (rate != null) {
                        int size = rate.size();
                        for (Object i : rate.values()) {
                            double f = new Double(i.toString()).doubleValue();
                            sum += f;
                        }
                        sum = sum / size;
                    }
                    tmp.put(key, sum);
                }
//                System.out.print(tmp+"\n");
//                keyList.clear();
                List <String> it = sortByValue(tmp);
                System.out.print(it);
                MyAdapter myAdapter = new MyAdapter(it);
                mRecyclerView.setAdapter(myAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup basicView = (ViewGroup) inflater.inflate(R.layout.fragment_recipelist, container, false);

        mRecyclerView = (RecyclerView) basicView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return basicView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public static List<String> sortByValue(final Map<String, Double> map){

        List<String> list = new ArrayList<String>();
        list.addAll(map.keySet());

        Collections.sort(list,new Comparator<Object>(){

            @SuppressWarnings("unchecked")

            public int compare(Object o1,Object o2){
                Object v1 = map.get(o1);
                Object v2 = map.get(o2);
                return ((Comparable<Object>) v2).compareTo(v1);
            }

        });

        return list;
    }



//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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
