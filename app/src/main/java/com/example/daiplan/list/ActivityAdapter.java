package com.example.daiplan.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.daiplan.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Comparator;

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
        ((TextView) convertView.findViewById(R.id.startTimeView)).setText(activity.hourOfStart + ":" + activity.minuteOfStart);
        ((TextView) convertView.findViewById(R.id.endTimeView)).setText(activity.hourOfEnd + ":" + activity.minuteOfEnd);

        return convertView;
    }

    public ArrayList<Activity> getActivityList() {
        return activityList;
    }

    public void sort() {
        Comparator<Activity> comparator = new Comparator<Activity>() {
            @Override
            public int compare(Activity o1, Activity o2) {
                return (int)( (o1.hourOfStart * 60 + o1.minuteOfStart ) - (o2.hourOfStart * 60 + o2.minuteOfStart) );
            }
        };

        super.sort(comparator);
        notifyDataSetChanged();
    }

}
