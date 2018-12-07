package com.example.girln.recipeapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link basic.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link basic#newInstance} factory method to
 * create an instance of this fragment.
 */
public class basic extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Fragment recipeFragment = new recipeList();

    private FirebaseStorage storage = FirebaseStorage.getInstance();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public basic() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment basic.
     */
    // TODO: Rename and change types and number of parameters
    public static basic newInstance(String param1, String param2) {
        basic fragment = new basic();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_basic, container, false);

        ImageView recent = (ImageView) view.findViewById(R.id.recent);
        ImageView popular = (ImageView) view.findViewById(R.id.popular);

        Button write = (Button) view.findViewById(R.id.write);

        recent.setOnClickListener(this);
        popular.setOnClickListener(this);
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), Upload.class);
                startActivity(intent);
            }
        });

        //have to change the pics, and name should be changed as well.
        StorageReference refSimple = storage.getReference("recommend.JPG");
        StorageReference refPopular = storage.getReference("garlic_chicken_pasta.jpg");

        System.out.println(refSimple);

        GlideApp.with(view)
                .load(refSimple)
                .into(recent);

        GlideApp.with(view)
                .load(refPopular)
                .into(popular);

        return view;

    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (v.getId() == R.id.recent) {
//            Intent intent = new Intent(getActivity().getApplicationContext(), recent.class);
//            startActivity(intent);
            transaction.replace(R.id.container, recipeFragment);
        }
//        else if (v.getId() == R.id.popular) {
//            Intent intent = new Intent(getActivity().getApplicationContext(), popular.class);
//            startActivity(intent);
//        }
        transaction.commit();

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
