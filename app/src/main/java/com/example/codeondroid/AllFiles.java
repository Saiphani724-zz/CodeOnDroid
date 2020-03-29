package com.example.codeondroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllFiles#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllFiles extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    ListView myfiles;
    String[] files;
    SharedPreferences sf;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;




    public AllFiles() {
        // Required empty public constructor

    }



    public static AllFiles newInstance(String param1, String param2) {
        AllFiles fragment = new AllFiles();
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
        View view =inflater.inflate(R.layout.fragment_all_files, container, false);
//        myfiles = view.findViewById(R.id.myfiles);

        File f = new File("" + getActivity().getFilesDir());
        FilenameFilter fileFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(".");
            }
        };
        files = f.list(fileFilter);

//        ArrayAdapter<String> ada = new ArrayAdapter<String>
//                (getActivity(), android.R.layout.simple_list_item_1, files){
//            @Override
//            public View getView(int position, View convertView, ViewGroup parent){
//                // Get the Item from ListView
//                View view = super.getView(position, convertView, parent);
//
//                // Initialize a TextView for ListView each Item
//                TextView tv = (TextView) view.findViewById(android.R.id.text1);
//
//                // Set the text color of TextView (ListView Item)
//                tv.setTextColor(getResources().getColor(R.color.CodeColor));
//
//                // Generate ListView Item using TextView
//                return view;
//            }
//        };
//
//        // DataBind ListView with items from ArrayAdapter
//        myfiles.setAdapter(ada);
//        myfiles.setOnItemClickListener(this);
//

        mRecyclerView = (RecyclerView) view.findViewById(R.id.myfiles);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CardViewDataAdapter(files);
        mRecyclerView.setAdapter(mAdapter);



        return view;
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("TAG", "onItemClick: " + files[position]);

        sf = getActivity().getSharedPreferences("myfile1", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=sf.edit();
        edit.clear(); // remove existing entries
        edit.putString("filename",files[position]);
        edit.commit();

        startActivity(new Intent(getActivity(),EditorActivity.class));

    }

}
