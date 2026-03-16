package com.example.ecofriendly.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ecofriendly.R;
import com.example.ecofriendly.adapters.TaskCardAdapters;
import com.example.ecofriendly.data.Repository;
import com.example.ecofriendly.data.models.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    Repository repository;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public Home() {}

    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
        View view = inflater.inflate(R.layout.fragment_home, container,false);
        recyclerView = view.findViewById(R.id.card_recycler);
        db = FirebaseFirestore.getInstance();
        repository = new Repository();

        List<Task> listTask = new ArrayList<>();
        TaskCardAdapters adapter = new TaskCardAdapters(listTask, requireContext(), task -> {

            //bottomsheet

            Toast.makeText(requireContext(), "CardView", Toast.LENGTH_SHORT).show();
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        repository.getTask(new Repository.taskGetInfoListener() {
            @Override
            public void onLoaded(List<Task> task) {
                adapter.updateList(task);
            }

            @Override
            public void onError(String error) {
                Log.e("Home", "Error"+ error);
            }
        });

        return view;
    }

}