package com.example.daiplan.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.daiplan.R;
import com.example.daiplan.databinding.FragmentHomeBinding;
import com.example.daiplan.list.Activity;
import com.example.daiplan.list.ActivityAdapter;
import com.example.daiplan.list.ListJsonAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class HomeFragment extends Fragment {

    private ListJsonAdapter jsonAdapter = new ListJsonAdapter(getContext());
    private FragmentHomeBinding binding;
    private boolean isEditable = false;
    private ArrayList<Activity>[] activityArrayList = new ArrayList[7];
    private ActivityAdapter adapter;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        for (int i =0; i<7; i++) {
            activityArrayList[i] = new ArrayList<>();
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("u");
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(Integer.parseInt(dateFormat.format(new Date())) - 1));
        binding.listView.setClickable(false);

        adapter = new ActivityAdapter(this.getContext(), activityArrayList[binding.tabLayout.getSelectedTabPosition()]);
        binding.listView.setAdapter(adapter);

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Context context = adapter.getContext();

                adapter = new ActivityAdapter(context, activityArrayList[tab.getPosition()]);
                binding.listView.setAdapter(adapter);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                activityArrayList[tab.getPosition()] = adapter.getActivityList();
            }
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        binding.switch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditable = binding.switch1.isChecked();

                if (isEditable) {
                    binding.floatingActionButton.setVisibility(View.VISIBLE);
                } else  {
                    binding.floatingActionButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getContext());
                showDialog(dialog, null, false);
            }
        });

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEditable) {
                    Dialog dialog = new Dialog(getContext());
                    showDialog(dialog, position, true);
                    //jsonAdapter.activityListSave(activityArrayList);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void showDialog(Dialog dialog, Integer activityPosition, boolean isEdit) {
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_layout);

        Button confirmButton = dialog.findViewById(R.id.confirmButton);
        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        FloatingActionButton deleteButton = dialog.findViewById(R.id.delButton);

        EditText editName = dialog.findViewById(R.id.editName);
        EditText editDescription = dialog.findViewById(R.id.editDescription);
        TextView deleteTitle = dialog.findViewById(R.id.delTitle);

        if (!isEdit) {
            deleteButton.setVisibility(View.INVISIBLE);
            deleteTitle.setVisibility(View.INVISIBLE);
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.remove(adapter.getItem(activityPosition));
                dialog.cancel();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {

                    adapter.getItem(activityPosition).name = editName.getText().toString();
                    adapter.getItem(activityPosition).description = editDescription.getText().toString();

                    adapter.notifyDataSetChanged();
                } else {

                    Activity activity = new Activity();

                    activity.name = editName.getText().toString();
                    activity.description = editDescription.getText().toString();

                    adapter.add(activity);
                }
                dialog.cancel();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //jsonAdapter.activityListSave(activityArrayList);
    }
}
