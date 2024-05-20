package com.example.daiplan.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.daiplan.R;

import java.util.ArrayList;

public class ActivityAdapter extends ArrayAdapter <Activity> {
    private ArrayList <Activity> activityList;

    public ActivityAdapter(Context context, ArrayList<Activity> activityList) {
        super(context, R.layout.adapter_item, activityList);
        this.activityList = activityList;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final Activity activity = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item, null);
        }

        ((TextView) convertView.findViewById(R.id.activityName)).setText(activity.name);
        ((TextView) convertView.findViewById(R.id.activityDescription)).setText(activity.description);

        return convertView;
    }

    public ArrayList<Activity> getActivityList() {
        return activityList;
    }

    //сортировку сюда
}
