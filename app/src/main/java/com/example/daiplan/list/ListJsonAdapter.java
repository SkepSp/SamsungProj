package com.example.daiplan.list;


import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.ContactsContract;
import android.security.FileIntegrityManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ListJsonAdapter {

    private Context context;
    private String fileName;
    public ListJsonAdapter(Context context) {
        this.context = context;
        fileName = "DaiPlanData.json";
    }
    public void activityListSetup(ArrayList<Activity>[] list) {
        try {
            String recivingString = "";

            InputStream inputStream = context.openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuilder = new StringBuilder();
            while ((recivingString = bufferedReader.readLine()) != null) {
                stringBuilder.append(recivingString);
            }
            inputStream.close();
            recivingString = stringBuilder.toString();

            JSONObject mainRoot = new JSONObject(recivingString);

            JSONObject Week = (JSONObject) mainRoot.get("Week");
            JSONArray daysArray = (JSONArray) Week.getJSONArray("Days");

            for (int i = 0; i < daysArray.length(); i++) {
                JSONArray activityArray = daysArray.getJSONArray(i);

                for (int j = 0; j < activityArray.length(); j++) {
                    JSONObject jsonObject = activityArray.getJSONObject(j);
                    Activity activity = new Activity();

                    activity.name = (String) jsonObject.get("Name");
                    activity.description = (String) jsonObject.get("Description");

                    activity.hourOfStart = jsonObject.getInt("StartTimeHours");
                    activity.minuteOfStart = jsonObject.getInt("StartTimeMinutes");
                    activity.hourOfEnd = jsonObject.getInt("EndTimeHours");
                    activity.minuteOfEnd = jsonObject.getInt("EndTimeMinutes");

                    list[i].add(activity);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void activityListSave(ArrayList<Activity>[] list) {
        try {
        JSONObject mainRoot = new JSONObject();
        JSONArray daysArray = new JSONArray();
        JSONObject week = new JSONObject();

        week.put("Days", daysArray);
        mainRoot.put("Week", week);

        for (int i = 0; i < list.length; i++) {
            JSONArray activityArray = new JSONArray();

            for (int j = 0; j < list[i].size(); j++) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("Name",list[i].get(j).name);
                jsonObject.put("Description",list[i].get(j).description);

                jsonObject.put("StartTimeHours",list[i].get(j).hourOfStart);
                jsonObject.put("StartTimeMinutes",list[i].get(j).minuteOfStart);
                jsonObject.put("EndTimeHours",list[i].get(j).hourOfEnd);
                jsonObject.put("EndTimeMinutes",list[i].get(j).minuteOfEnd);

                activityArray.put(jsonObject);
            }
            daysArray.put(activityArray);
        }

        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
        String str = mainRoot.toString();
        outputStreamWriter.write(mainRoot.toString());
        outputStreamWriter.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
