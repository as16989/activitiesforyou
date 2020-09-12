package com.example.as16989;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences today;
    SharedPreferences isFirst;
    private RecyclerView recyc;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ListView listCurrHome, listCurrBusy;
    private List<TableEntry> tableActs;
    TextView doneAnyActs;
    ArrayList<LogItem> mLog = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkFirstTimeOpening();
        checkDay();
        tableActs = makeTable();
        SharedPreferences currentActs = this.getSharedPreferences("currentActs", 0);
        listCurrHome = findViewById(R.id.listCurrHome);
        listCurrBusy = findViewById(R.id.listCurrBusy);
        doneAnyActs = findViewById(R.id.noActivities);
        if (!currentActs.contains("currentHome")) openDialogWelcome1(tableActs);
        else addActivitiesToList();
        ArrayList<String> temp;
        SharedPreferences numOfActs = this.getSharedPreferences("numOfActivitiesDone", 0);
        int numDone = numOfActs.getInt("numOfActivitiesDone", 0);
        if (numDone > 0) doneAnyActs.setText(" ");
        for (int i = numDone; i >= 1; i--) {
            temp = getArrayList("log", "log" + i);
            mLog.add(new LogItem(emojiFetch(Float.parseFloat(temp.get(3))), temp.get(0), "Started on " + temp.get(1), "Ended on " + temp.get(2)));
        }
        recyc = findViewById(R.id.recyc);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new LogAdapter(mLog);
        recyc.setLayoutManager(mLayoutManager);
        recyc.setAdapter(mAdapter);

    }

    private List<TableEntry> makeTable() {
        List<TableEntry> table = new ArrayList<>();
        InputStream is = getResources().openRawResource(R.raw.activitytable);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        ArrayList<String> traitList = getArrayList("traits");
        String line = "";
        SharedPreferences cooldowns = this.getSharedPreferences("cooldowns", 0);
        SharedPreferences.Editor editorCooldowns = cooldowns.edit();
        SharedPreferences actsMultis = this.getSharedPreferences("actsMultis", 0);
        int i = 0;
        int total = 0;
        int cooldown;
        float multiplier;
        try {
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                cooldown = cooldowns.getInt("cooldown" + i, 0);
                if (cooldown == 0) {
                    multiplier = actsMultis.getFloat("multi" + i, 1);
                    total += Float.parseFloat(tokens[1]) * Math.max(Float.parseFloat(traitList.get(0)) - 2, 0);
                    total += Float.parseFloat(tokens[2]) * Math.max(Float.parseFloat(traitList.get(1)) - 2, 0);
                    total += Float.parseFloat(tokens[3]) * Math.max(Float.parseFloat(traitList.get(2)) - 2, 0);
                    total += Float.parseFloat(tokens[4]) * Math.max(Float.parseFloat(traitList.get(3)) - 2, 0);
                    total += Float.parseFloat(tokens[5]) * Math.max(Float.parseFloat(traitList.get(4)) - 2, 0);
                    total *= multiplier;
                    TableEntry entry = new TableEntry(i, tokens[0], tokens[7], total, Boolean.parseBoolean(tokens[6]));
                    table.add(entry);
                }
                else {
                    editorCooldowns.putInt("cooldown" + i, cooldown - 1);
                    editorCooldowns.apply();
                }

                i++;
                total = 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(table, (o1, o2) -> Float.compare(o2.score, o1.score));
        return table;
    }

    private void openDialogWelcome1(List<TableEntry> table) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nowbeshown1)
                .setPositiveButton("OK", (dialog, id) -> openDialogShowHome(table, true));
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void openDialogWelcome2(List<TableEntry> table) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.nowbeshown2)
                .setPositiveButton("OK", (dialog, id) -> openDialogShowBusy(table, true));
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    private void openDialogShowHome(List<TableEntry> table, boolean chain) {
        ListView listView = new ListView(this);
        List<String> data = new ArrayList<>();
        List<TableEntry> homeTable = new ArrayList<>();
        SharedPreferences currentActs = this.getSharedPreferences("currentActs", 0);
        SharedPreferences.Editor editor = currentActs.edit();
        for (int j = 0; j < table.size(); j++) if (table.get(j).getID() != currentActs.getInt("currentBusy", -1)) homeTable.add(table.get(j));
        for (int i = 0; i < 5; i++) data.add(homeTable.get(i).getName());
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter2);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(listView);
        final AlertDialog dialog = builder.create();
        if (!chain) dialog.setTitle(R.string.nowchoosehome);
        dialog.setCancelable(false);
        dialog.show();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            recyc.smoothScrollToPosition(0);
            editor.putInt("currentHome", homeTable.get(position).getID());
            editor.apply();
            addDate("homeStarted");
            dialog.dismiss();
            if (chain) openDialogWelcome2(table);
            else addHomeOrBusy("currentHome", listCurrHome, true);
        });
    }

    private void openDialogShowBusy(List<TableEntry> table, boolean chain) {
        SharedPreferences currentActs = this.getSharedPreferences("currentActs", 0);
        SharedPreferences.Editor editor = currentActs.edit();
        ListView listView = new ListView(this);
        List<TableEntry> busyTable = new ArrayList<>();
        List<String> data = new ArrayList<>();
        for (int j = 0; j < table.size(); j++) if (table.get(j).isBusy() && table.get(j).getID() != currentActs.getInt("currentHome", 0)) busyTable.add(table.get(j));
        for (int i = 0; i < 5; i++) data.add(busyTable.get(i).getName());
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter3);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(listView);
        final AlertDialog dialog = builder.create();
        if (!chain) dialog.setTitle(R.string.nowchoosebusy);
        dialog.show();
        dialog.setCancelable(false);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            recyc.smoothScrollToPosition(0);
            editor.putInt("currentBusy", busyTable.get(position).getID());
            editor.apply();
            addDate("busyStarted");
            dialog.dismiss();
            if (chain) addActivitiesToList();
            else addHomeOrBusy("currentBusy", listCurrBusy, false);
        });
    }

    private void openDialogFinishedQuestion(int ID, boolean isHome, String title) {
        SharedPreferences timesdone = this.getSharedPreferences("timesdone", 0);
        int timesDoneInt = timesdone.getInt("timesdone" + ID, 0);
        SharedPreferences today = this.getSharedPreferences("dayStarted", 0);
        String startDate = isHome ? today.getString("homeStarted", "") : today.getString("busyStarted", "");
        String grammar = timesDoneInt == 1 ? "time" : "times";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String message = "Started on " + startDate + "\nThis activity has been done " + timesDoneInt + " " + grammar + " before\n\nHave you finished this activity?";
        builder.setMessage(message)
                .setPositiveButton("Yes", (dialog, id) -> openDialogShowMoodAfterActivity(ID, isHome, title, startDate))
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        final AlertDialog dialog = builder.create();
        dialog.setTitle(title);
        dialog.show();
    }

    private void openDialogShowMoodAfterActivity(int ID, boolean isHome, String title, String startDate) {
        ListView listView = new ListView(this);
        List<String> data = new ArrayList<>();
        addEmojisToList(data);
        ArrayAdapter<String> adapter6 = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter6);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(listView);
        final AlertDialog dialog = builder.create();
        dialog.setTitle("How did this activity make you feel?");
        dialog.show();
        ArrayList<String> log = new ArrayList<>();
        listView.setOnItemClickListener((parent, view, position, id) -> {

            //change multiplier
            SharedPreferences actsMultis = this.getSharedPreferences("actsMultis", 0);
            SharedPreferences.Editor editor = actsMultis.edit();
            editor.putFloat("multi" + ID, chooseMulti(position));
            editor.apply();

            //change cooldown
            SharedPreferences cooldowns = this.getSharedPreferences("cooldowns", 0);
            SharedPreferences.Editor editor2 = cooldowns.edit();
            editor2.putInt("cooldown" + ID, 2);
            editor2.apply();

            //increment times done
            SharedPreferences timesdone = this.getSharedPreferences("timesdone", 0);
            SharedPreferences.Editor editor3 = timesdone.edit();
            int currentTimesDone = timesdone.getInt("timesdone" + ID, 0);
            editor3.putInt("timesdone" + ID, currentTimesDone + 1);
            editor3.apply();

            //increment number of activities completed, this is for the log list to work
            SharedPreferences numOfActivitiesDone = this.getSharedPreferences("numOfActivitiesDone", 0);
            SharedPreferences.Editor editor4 = numOfActivitiesDone.edit();
            int numOfActivitiesDoneInt = numOfActivitiesDone.getInt("numOfActivitiesDone", 0);
            editor4.putInt("numOfActivitiesDone", numOfActivitiesDoneInt + 1);
            editor4.apply();

            String endDate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());

            log.add(title);
            log.add(startDate);
            log.add(endDate);
            log.add(Float.toString(chooseMulti(position)));

            saveArrayList(log, "log" + numOfActivitiesDone.getInt("numOfActivitiesDone", 0), "log");

            mLog.add(0, new LogItem(emojiFetch(chooseMulti(position)), title, "Started on " + startDate, "Ended on " + endDate));
            mAdapter.notifyDataSetChanged();

            doneAnyActs.setText(" ");

            dialog.dismiss();
            tableActs = makeTable();
            if (isHome) openDialogShowHome(tableActs, false);
            else openDialogShowBusy(tableActs, false);
        });
    }

    //this function is used to add the currently selected activities to the current list
    private void addActivitiesToList() {
        addHomeOrBusy("currentHome", listCurrHome, true);
        addHomeOrBusy("currentBusy", listCurrBusy, false);
    }

    private void addHomeOrBusy(String key, ListView listView, boolean isHome) {
        SharedPreferences currentActs = this.getSharedPreferences("currentActs", 0);
        SharedPreferences dayStart = this.getSharedPreferences("dayStarted", 0);
        String title = "oh no", desc = "oh no";
        ArrayList<CurrentActivityItem> data = new ArrayList<>();
        for (int i = 0; i < tableActs.size(); i++) {
            if (tableActs.get(i).getID() == currentActs.getInt(key, -1)) {
                title = tableActs.get(i).getName();
                desc = tableActs.get(i).getDesc();
                break;
            }
        }

        int imgID =  isHome ? R.drawable.ic_home : R.drawable.ic_work;
        String start = isHome ? dayStart.getString("homeStarted", null) : dayStart.getString("busyStarted", null);

        CurrentActivityItem curr = new CurrentActivityItem(imgID, title, desc, "Started on " + start);
        data.add(curr);
        ArrayAdapter<CurrentActivityItem> adapter = new CurrentActivityAdapter(this, R.layout.layout_listitem, data);
        listView.setAdapter(adapter);
        String finalTitle = title;
        listView.setOnItemClickListener((parent, view, position, id) -> openDialogFinishedQuestion(currentActs.getInt(key, -1), isHome, finalTitle));

    }

    private float chooseMulti(int i) {
        switch (i) {
            case 0: return (float) 1.5;
            case 1: return (float) 1.2;
            case 2: return (float) 0.95;
            case 3: return (float) 0.4;
            case 4: return (float) 0.1;
        }
        return -1;
    }
    private String getEmojiByUnicode(int unicode) { return new String(Character.toChars(unicode)); }
    private List<String> addEmojisToList(List<String> data) {
        data.add("Excellent " + getEmojiByUnicode(0x1F601));
        data.add("Good " + getEmojiByUnicode(0x1F603));
        data.add("Okay " + getEmojiByUnicode(0x1F60C));
        data.add("Bad " + getEmojiByUnicode(0x1F614));
        data.add("Terrible " + getEmojiByUnicode(0x1F616));
        return data;
    }
    private ArrayList<String> getArrayList(String sp){
        SharedPreferences prefs = this.getSharedPreferences(sp, 0);
        Gson gson = new Gson();
        String json = prefs.getString("traits", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    public void saveArrayList(ArrayList<String> list, String key, String sp){
        SharedPreferences prefs = this.getSharedPreferences(sp, 0);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }
    private void addDate(String key) {
        SharedPreferences today = this.getSharedPreferences("dayStarted", 0);
        SharedPreferences.Editor editorToday = today.edit();
        java.text.SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date todaysDate = Calendar.getInstance().getTime();
        String formattedDate = formatter.format(todaysDate);
        editorToday.putString(key, formattedDate);
        editorToday.apply();
    }
    private void checkDay() {
        java.text.SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date todaysDate = Calendar.getInstance().getTime();
        String formattedDate = formatter.format(todaysDate);
        today = getApplicationContext().getSharedPreferences("todayChecked", 0);
        SharedPreferences.Editor editor = today.edit();
        if (!today.getBoolean(formattedDate, false)) {
            editor.putBoolean(formattedDate, true);
            editor.apply();
        }
    }
    private void checkFirstTimeOpening() {
        isFirst = getApplicationContext().getSharedPreferences("isFirst", 0);
        if (isFirst.getBoolean("isFirstTimeOpening", true)) {
            createCooldowns();
            Intent intent = new Intent(this, BFIActivity.class);
            startActivity(intent);
        }
    }
    private void createCooldowns() {
        SharedPreferences cooldowns = this.getSharedPreferences("cooldowns", 0);
        SharedPreferences.Editor editor = cooldowns.edit();
        for (int i = 0; i < 41; i++) editor.putInt("cooldown" + i, 0);
        editor.apply();
    }

    private ArrayList<String> getArrayList(String sp, String fetch){
        SharedPreferences prefs = this.getSharedPreferences(sp, 0);
        Gson gson = new Gson();
        String json = prefs.getString(fetch, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    private int emojiFetch(Float val) {
        switch (Float.toString(val)) {
            case "1.5":
                return R.drawable.great;
            case "1.2":
                return R.drawable.good;
            case "0.95":
                return R.drawable.ok;
            case "0.4":
                return R.drawable.bad;
            case "0.1":
                return R.drawable.awful;
        }
        return 0;
    }
}
