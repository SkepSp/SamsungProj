package com.example.daiplan.notification;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private int  hourOfDay, minuteOfDay;
    private boolean isUsed;
    private final Calendar calendar = Calendar.getInstance();
    private final TextView text;

    public TimePickerFragment(TextView text) {
        this.text = text;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        isUsed = false;

        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        isUsed = true;
        this.hourOfDay = hourOfDay;
        minuteOfDay = minute;

        text.setText(hourOfDay + ":" + minute);
    }

    public int getHour() {
        return hourOfDay;
    }

    public int getMinute() {
        return minuteOfDay;
    }

    public boolean isUsed() { return isUsed; }
}
