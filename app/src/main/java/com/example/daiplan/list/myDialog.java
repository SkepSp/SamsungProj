package com.example.daiplan.list;

import static com.example.daiplan.fragments.HomeFragment.updateFreeTime;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.daiplan.R;
import com.example.daiplan.databinding.DialogLayoutBinding;
import com.example.daiplan.notification.NotificRegister;
import com.example.daiplan.notification.TimePickerFragment;

import java.util.ArrayList;
import java.util.Objects;

public class myDialog extends DialogFragment {

    private Integer activityPosition;
    private boolean isEdit;
    private int dayOfWeek;
    private ActivityAdapter adapter;
    private ArrayList<Activity>[] activityArrayList;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        DialogLayoutBinding binding = DialogLayoutBinding.inflate(getLayoutInflater());

        TimePickerFragment timeStartPickerFragment = new TimePickerFragment(binding.startTime);
        TimePickerFragment timeEndPickerFragment = new TimePickerFragment(binding.endTime);

        if (!isEdit) {
            binding.delButton.setVisibility(View.INVISIBLE);
            binding.delTitle.setVisibility(View.INVISIBLE);
        }



        binding.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "You deleted " + adapter.getItem(activityPosition).name, Toast.LENGTH_SHORT).show();
                adapter.remove(adapter.getItem(activityPosition));

                NotificRegister register = new NotificRegister(getActivity(), activityArrayList);
                register.synchronizeNotificaton();

                updateFreeTime();
                Objects.requireNonNull(getDialog()).cancel();
            }
        });

        binding.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeStartPickerFragment.show(getActivity().getSupportFragmentManager(), "SetTime");
            }
        });


        binding.endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeEndPickerFragment.show(getActivity().getSupportFragmentManager(), "SetTime");
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        boolean isEverythOk = true;

                        if (binding.editName.getText().toString().equals("")) {
                            isEverythOk = false;
                            Toast.makeText(getActivity(), "You must set a name", Toast.LENGTH_SHORT).show();
                        }

                        for (int i = 0; i < 7 && isEverythOk; i++) {
                            for (int j = 0; j < activityArrayList[i].size(); j++) {
                                if (binding.editName.getText().toString().equals(activityArrayList[i].get(j).name)) {
                                    isEverythOk = false;
                                    Toast.makeText(getActivity(), "You must set a unique name", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }

                        if (timeStartPickerFragment.getHour() * 60 + timeStartPickerFragment.getMinute()
                                > timeEndPickerFragment.getHour() * 60 + timeEndPickerFragment.getMinute()) {
                            Toast.makeText(getActivity(), "You must set correct start time", Toast.LENGTH_SHORT).show();
                            isEverythOk = false;
                        }

                        if (timeStartPickerFragment.isUsed() && timeEndPickerFragment.isUsed()) {
                            for (int i = 0; i < activityArrayList[dayOfWeek].size() && isEverythOk; i++) {
                                if (timeStartPickerFragment.getHour() * 60 + timeStartPickerFragment.getMinute()
                                        > activityArrayList[dayOfWeek].get(i).hourOfStart * 60 + activityArrayList[dayOfWeek].get(i).minuteOfStart
                                        && timeStartPickerFragment.getHour() * 60 + timeStartPickerFragment.getMinute()
                                        < activityArrayList[dayOfWeek].get(i).hourOfEnd * 60 + activityArrayList[dayOfWeek].get(i).minuteOfEnd) {

                                    Toast.makeText(getActivity(), "You must set correct start time", Toast.LENGTH_SHORT).show();
                                    isEverythOk = false;
                                    break;
                                }

                                if (timeEndPickerFragment.getHour() * 60 + timeEndPickerFragment.getMinute()
                                        > activityArrayList[dayOfWeek].get(i).hourOfStart * 60 + activityArrayList[dayOfWeek].get(i).minuteOfStart
                                        && timeEndPickerFragment.getHour() * 60 + timeEndPickerFragment.getMinute()
                                        < activityArrayList[dayOfWeek].get(i).hourOfEnd * 60 + activityArrayList[dayOfWeek].get(i).minuteOfEnd) {

                                    Toast.makeText(getActivity(), "You must set correct end time", Toast.LENGTH_SHORT).show();
                                    isEverythOk = false;
                                    break;
                                }
                            }

                        } else {
                            isEverythOk = false;
                            Toast.makeText(getActivity(), "You must set time", Toast.LENGTH_SHORT).show();
                        }

                        if (isEverythOk) {

                            if (isEdit) {

                                adapter.getItem(activityPosition).name = binding.editName.getText().toString();
                                Objects.requireNonNull(adapter.getItem(activityPosition)).description = binding.editDescription.getText().toString();

                                adapter.getItem(activityPosition).hourOfStart = timeStartPickerFragment.getHour();
                                adapter.getItem(activityPosition).minuteOfStart = timeStartPickerFragment.getMinute();
                                adapter.getItem(activityPosition).hourOfEnd = timeEndPickerFragment.getHour();
                                adapter.getItem(activityPosition).minuteOfEnd = timeEndPickerFragment.getMinute();

                                adapter.sort();
                                //adapter.notifyDataSetChanged();

                                Toast.makeText(getActivity(), "You changed an activity", Toast.LENGTH_SHORT).show();
                            } else {

                                Activity activity = new Activity();

                                activity.name = binding.editName.getText().toString();
                                activity.description = binding.editDescription.getText().toString();

                                activity.hourOfStart = timeStartPickerFragment.getHour();
                                activity.minuteOfStart = timeStartPickerFragment.getMinute();
                                activity.hourOfEnd = timeEndPickerFragment.getHour();
                                activity.minuteOfEnd = timeEndPickerFragment.getMinute();


                                adapter.add(activity);
                                adapter.sort();

                                Toast.makeText(getActivity(), "You added an activity", Toast.LENGTH_SHORT).show();
                            }

                            NotificRegister register = new NotificRegister(getActivity(), activityArrayList);
                            register.synchronizeNotificaton();

                            updateFreeTime();
                            dialog.cancel();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setView(binding.getRoot())
                .setTitle("ACTIVITY REDACTOR");

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.dialog_shape));
        this.setCancelable(false);
        return dialog;
    }

    public myDialog(Integer activityPosition, boolean isEdit, int dayOfWeek, @NonNull ActivityAdapter adapter,
                    @NonNull ArrayList<Activity>[] activityArrayList) { //если крашит то мб из-за NonNull
        this.activityPosition = activityPosition;
        this.isEdit = isEdit;
        this.dayOfWeek = dayOfWeek;
        this.adapter = adapter;
        this.activityArrayList = activityArrayList;
    }
}

