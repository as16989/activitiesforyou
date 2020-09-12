package com.example.as16989;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class BFIActivity extends AppCompatActivity {

    private static final String TAG = "BFIActivity";
    RadioGroup r1, r2, r3, r4, r5, r6, r7, r8, r9, r10;
    RadioButton radioButton1, radioButton2;
    Button btnDone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bfi);
        btnDone = findViewById(R.id.btnDone);
        btnDone.setOnClickListener(v -> calcScore());
        r1 = findViewById(R.id.scoreRadios1);
        r2 = findViewById(R.id.scoreRadios2);
        r3 = findViewById(R.id.scoreRadios3);
        r4 = findViewById(R.id.scoreRadios4);
        r5 = findViewById(R.id.scoreRadios5);
        r6 = findViewById(R.id.scoreRadios6);
        r7 = findViewById(R.id.scoreRadios7);
        r8 = findViewById(R.id.scoreRadios8);
        r9 = findViewById(R.id.scoreRadios9);
        r10 = findViewById(R.id.scoreRadios10);
    }

    private void calcScore() {
        float o, c, e, a, n;
        if (r1.getCheckedRadioButtonId() == -1 ||
                r2.getCheckedRadioButtonId() == -1 ||
                r3.getCheckedRadioButtonId() == -1 ||
                r4.getCheckedRadioButtonId() == -1 ||
                r5.getCheckedRadioButtonId() == -1 ||
                r6.getCheckedRadioButtonId() == -1 ||
                r7.getCheckedRadioButtonId() == -1 ||
                r8.getCheckedRadioButtonId() == -1 ||
                r9.getCheckedRadioButtonId() == -1 ||
                r10.getCheckedRadioButtonId() == -1)
            Toast.makeText(this, "Please answer all the questions!", Toast.LENGTH_SHORT).show();
        else {
            radioButton1 = findViewById(r1.getCheckedRadioButtonId());
            radioButton2 = findViewById(r6.getCheckedRadioButtonId());
            e = (6 - parseInt(radioButton1.getText().toString())) + parseInt(radioButton2.getText().toString());
            e /= 2;
            radioButton1 = findViewById(r2.getCheckedRadioButtonId());
            radioButton2 = findViewById(r7.getCheckedRadioButtonId());
            a = (6 - parseInt(radioButton2.getText().toString())) + parseInt(radioButton1.getText().toString());
            a /= 2;
            radioButton1 = findViewById(r3.getCheckedRadioButtonId());
            radioButton2 = findViewById(r8.getCheckedRadioButtonId());
            c = (6 - parseInt(radioButton1.getText().toString())) + parseInt(radioButton2.getText().toString());
            c /= 2;
            radioButton1 = findViewById(r4.getCheckedRadioButtonId());
            radioButton2 = findViewById(r9.getCheckedRadioButtonId());
            n = (6 - parseInt(radioButton1.getText().toString())) + parseInt(radioButton2.getText().toString());
            n /= 2;
            radioButton1 = findViewById(r5.getCheckedRadioButtonId());
            radioButton2 = findViewById(r10.getCheckedRadioButtonId());
            o = (6 - parseInt(radioButton1.getText().toString())) + parseInt(radioButton2.getText().toString());
            o /= 2;
            //Toast.makeText(this, "" + o + " " + c + " " + e + " " + a + " " + n, Toast.LENGTH_SHORT).show();

            ArrayList<String> traitList = new ArrayList<>();
            traitList.add(Float.toString(o));
            traitList.add(Float.toString(c));
            traitList.add(Float.toString(e));
            traitList.add(Float.toString(a));
            traitList.add(Float.toString(n));

            saveArrayList(traitList, "traits");

            //change is first time opening variable to false
            SharedPreferences isFirst = getApplicationContext().getSharedPreferences("isFirst", 0);
            SharedPreferences.Editor editor = isFirst.edit();
            editor.putBoolean("isFirstTimeOpening", false);
            editor.apply();

            finish();
        }
    }

    public void saveArrayList(ArrayList<String> list, String key){
        SharedPreferences prefs = getApplicationContext().getSharedPreferences("traits", 0);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

}
