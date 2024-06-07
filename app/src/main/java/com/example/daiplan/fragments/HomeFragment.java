package com.example.daiplan.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import com.example.daiplan.databinding.FragmentHomeBinding;
import com.example.daiplan.list.Activity;
import com.example.daiplan.list.ActivityAdapter;
import com.example.daiplan.list.ListJsonAdapter;
import com.example.daiplan.list.myDialog;
import com.google.android.material.tabs.TabLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class HomeFragment extends Fragment {
    private final ListJsonAdapter jsonAdapter;
    @SuppressLint("StaticFieldLeak")
    private static FragmentHomeBinding binding;
    private boolean isEditable = false;
    private final ArrayList<Activity>[] activityArrayList = new ArrayList[7];
    private static ActivityAdapter adapter;
    private final Context actContext;

    public HomeFragment(Context context) {
        super();
        actContext = context;
        jsonAdapter = new ListJsonAdapter(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        for (int i =0; i<7; i++) {
            activityArrayList[i] = new ArrayList<>();
        }

        jsonAdapter.activityListSetup(activityArrayList);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("u");
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(Integer.parseInt(dateFormat.format(new Date())) - 1));

        adapter = new ActivityAdapter(this.getContext(), activityArrayList[binding.tabLayout.getSelectedTabPosition()]);
        binding.listView.setAdapter(adapter);

        updateFreeTime();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Context context = adapter.getContext();

                adapter = new ActivityAdapter(context, activityArrayList[tab.getPosition()]);
                binding.listView.setAdapter(adapter);

                updateFreeTime();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                activityArrayList[tab.getPosition()] = adapter.getActivityList();
            }
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        binding.switch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditable = binding.switch2.isChecked();

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
                myDialog dialog = new myDialog(null, false, binding.tabLayout.getSelectedTabPosition(), adapter, activityArrayList);
                dialog.show(getActivity().getSupportFragmentManager(), "Tag");
            }
        });

        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEditable) {
                    myDialog dialog = new myDialog(position, true, binding.tabLayout.getSelectedTabPosition(),adapter, activityArrayList);
                    dialog.show(getActivity().getSupportFragmentManager(), "Tag");
                } else {
                    Toast.makeText(actContext, adapter.getActivityList().get(position).description, Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public static void updateFreeTime() {
        //обнавляем FreeTime
        int busyTime = 0;
        for (int i = 0; i < adapter.getActivityList().size(); i++) {
            busyTime += ( (adapter.getActivityList().get(i).hourOfEnd * 60 + adapter.getActivityList().get(i).minuteOfEnd)
                        - (adapter.getActivityList().get(i).hourOfStart * 60 + adapter.getActivityList().get(i).minuteOfStart));
        }

        binding.freeTime.setText("Free Time: " + String.valueOf((int) ((24 * 60) - busyTime) / 60) + "  hr");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        jsonAdapter.activityListSave(activityArrayList);
    }
}
