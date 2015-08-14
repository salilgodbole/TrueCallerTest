package com.example.truecallertest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.truecallertest.io.NetworkManager;
import com.example.truecallertest.listeners.ResponseListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView textView1 = null;
    private TextView textView2 = null;
    private TextView textView3 = null;

    private NetworkManager networkManager = NetworkManager.getInstance(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) findViewById(R.id.txt_result_one);
        textView2 = (TextView) findViewById(R.id.txt_result_two);
        textView3 = (TextView) findViewById(R.id.txt_result_three);
    }

    public void onExecuteClicked(View view) {
        networkManager.getCharacterAtIndex(Constants.URL, Constants.CHARACTER_INDEX, new ResponseListener<Character>() {
            @Override
            public void onResponse(Character c) {
                textView1.setText(String.valueOf(c));
            }

            @Override
            public void onError(com.example.truecallertest.models.Error error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        networkManager.getEveryCharacterAtIndex(Constants.URL, Constants.CHARACTER_INDEX, new ResponseListener<Character[]>() {
            @Override
            public void onResponse(Character[] characters) {

                StringBuilder builder = new StringBuilder();
                for (Character c : characters) {
                    builder.append(String.valueOf(c));
                    builder.append(" ");
                }

                textView2.setText(builder.toString());
            }

            @Override
            public void onError(com.example.truecallertest.models.Error error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        networkManager.getWordCount(Constants.URL, new ResponseListener<Map<String, Integer>>() {
            @Override
            public void onResponse(Map<String, Integer> wordOccurrenceMap) {
                textView3.setText("" + wordOccurrenceMap.get("truecaller"));
            }

            @Override
            public void onError(com.example.truecallertest.models.Error error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
