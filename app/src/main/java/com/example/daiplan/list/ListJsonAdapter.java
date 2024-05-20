package com.example.daiplan.list;


import android.content.Context;

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
import java.util.ArrayList;

public class ListJsonAdapter {

    private File file;
    Context context;
    public ListJsonAdapter(Context context) {
        this.context = context;
    }
    private void activityListSetup(ArrayList<Activity>[] list) {
        try {
            FileInputStream fileInputStream = context.openFileInput("DaiPlanData.json");

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String recivingString;
            StringBuilder stringBuilder = new StringBuilder();
            while ((recivingString = bufferedReader.readLine()) != null) {
                stringBuilder.append(recivingString);
            }
            fileInputStream.close();

            JSONObject mainRoot = new JSONObject(recivingString.toString());
            JSONArray daysArray = mainRoot.getJSONArray("Week");

            for (int i = 0; i < daysArray.length(); i++) {
                JSONArray activityArray = daysArray.getJSONArray(i);

                for (int j = 0; j < activityArray.length(); j++) {
                    JSONObject jsonObject = activityArray.getJSONObject(j);
                    Activity activity = new Activity();

                    activity.name = (String) jsonObject.get("Name");
                    activity.description = (String) jsonObject.get("Description");

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

        mainRoot.put("Days", daysArray);

        for (int i = 0; i < list.length; i++) {
            JSONArray activityArray = new JSONArray();

            for (int j = 0; j < list[i].size(); j++) {
                JSONObject jsonObject = new JSONObject();

                jsonObject.put("Name",list[i].get(j).name);
                jsonObject.put("Description",list[i].get(j).description);

                activityArray.put(jsonObject);
            }
            daysArray.put(activityArray);
        }

        FileOutputStream fileOutputStream = context.openFileOutput("DaiPlanData.json", Context.MODE_PRIVATE);
        fileOutputStream.write(mainRoot.toString().getBytes());
        fileOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
