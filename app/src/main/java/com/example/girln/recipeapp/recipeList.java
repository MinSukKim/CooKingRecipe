package com.example.girln.recipeapp;

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

import com.example.girln.recipeapp.models.RecipeModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link recipeList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link recipeList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class recipeList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    private TextView tvTotal;
    private DatabaseReference mData;
    private ArrayList<RecipeModel> recipe = new ArrayList<>();
    private Map tmp;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public recipeList() {
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
                Object RList = dataSnapshot.child("Recipes").getValue();
                List<String> keyList = new ArrayList<String>();
                tmp = (HashMap) RList;
                keyList.addAll(tmp.keySet());
//                System.out.println(keyList);
//                for (String key : keyList) {
//                    Map n = (HashMap) tmp.get(key);
//                    System.out.println(n);
//                    String uid = (String) n.get("userID");
//                    String title = (String) n.get("recipeName");
//                    ArrayList pic = (ArrayList) n.get("cookingPictures");
//                    ArrayList inge = (ArrayList) n.get("cookingIngredients");
//                    ArrayList steps = (ArrayList) n.get("cookingSteps");
//                    ArrayList tags = (ArrayList) n.get("cookingTags");
//
//                    if (user != null) {
//                        for (UserInfo rUser : user.getProviderData()) {
//                            if (uid.equals(rUser.getUid())) {
////                                item_recipeArrayList.add(new item_recipe(uid, pic, title, 0.0, inge, steps, tags, key,true));
//                                recipe.add(key);
//                            }
//                        }
//                    }
//                }
                MyAdapter myAdapter = new MyAdapter(keyList);
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
