package com.example.daiplan.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.daiplan.R;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class ActivityAdapter extends ArrayAdapter <Activity> {
    private final ArrayList <Activity> activityList;

    public ActivityAdapter(Context context, ArrayList<Activity> activityList) {
        super(context, R.layout.adapter_item, activityList);
        this.activityList = activityList;
    }

    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        final Activity activity = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item, null);
        }

        assert activity != null;
        ((TextView) convertView.findViewById(R.id.activityName)).setText(activity.name);

        TextView textView = convertView.findViewById(R.id.activityDescription);
        if (Objects.equals(activity.description, "")) {
            textView.setVisibility(View.INVISIBLE);
        } else {
            textView.setText(activity.description);
        }

        if (activity.minuteOfStart < 10) {
            ((TextView) convertView.findViewById(R.id.startTimeView)).setText(activity.hourOfStart + ":0" + activity.minuteOfStart);
        }else {
            ((TextView) convertView.findViewById(R.id.startTimeView)).setText(activity.hourOfStart + ":" + activity.minuteOfStart);
        }

        if (activity.minuteOfEnd < 10) {
            ((TextView) convertView.findViewById(R.id.endTimeView)).setText(activity.hourOfEnd + ":0" + activity.minuteOfEnd);
        } else {
            ((TextView) convertView.findViewById(R.id.endTimeView)).setText(activity.hourOfEnd + ":" + activity.minuteOfEnd);
        }

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
