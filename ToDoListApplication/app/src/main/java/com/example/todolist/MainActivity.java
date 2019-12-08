package com.example.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    final List<String> list = new ArrayList<>();
    int [] backgroundColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView listView = findViewById(R.id.listView);
        final TextAdapter adapter = new TextAdapter();
        int maxItems = 100;
        backgroundColors = new int[maxItems];
        for (int i = 0 ; i < maxItems ; i++){
            if (i % 2 == 0){
                backgroundColors[i] = Color.WHITE;
            }
            else
            {
                backgroundColors[i] = Color.LTGRAY;
            }
        }
        readInfo();
        adapter.setData(list , backgroundColors);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete it ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(position);
                                adapter.setData(list,backgroundColors);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("No" , null)
                        .create();
                        dialog.show();

            }
        });
        final Button newTaskButton = findViewById(R.id.newTaskButton);
        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskInput = new EditText(MainActivity.this);
                taskInput.setSingleLine();
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("New Task")
                        .setMessage("What is your next task ?")
                        .setView(taskInput)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.add(taskInput.getText().toString());
                                adapter.setData(list,backgroundColors);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                dialog.show();
            }
        });
        final Button deleteAllTasksButton = findViewById(R.id.deleteAllTasksButton);
        deleteAllTasksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete All Tasks")
                        .setMessage("Do you really want to delete all tasks ?")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                list.clear();
                                adapter.setData(list,backgroundColors);
                                saveInfo();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                        dialog.show();


            }
        });
    }

    private void saveInfo(){
        try {
            File file = new File(this.getFilesDir(),"Saved");
            FileOutputStream Out = new FileOutputStream(file);
            BufferedWriter fWrite = new BufferedWriter(new OutputStreamWriter(Out));
            for (int i = 0 ; i < list.size() ; i++)
            {
                fWrite.write(list.get(i));
                fWrite.newLine();
            }
            fWrite.close();
            Out.close();
        }
        catch (Exception e){
        e.printStackTrace();
        }
    }
    private void readInfo()
    {
        File file = new File (this.getFilesDir(),"Saved");
        if (!file.exists()){
            return;
        }
        try{
            FileInputStream In = new FileInputStream(file);
            BufferedReader fRead = new BufferedReader(new InputStreamReader(In));
            String line = fRead.readLine();
            while (line!=null){
                list.add(line);
                line = fRead.readLine();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    class TextAdapter extends BaseAdapter{
        List<String> list = new ArrayList<>();
        int [] backgroundColors ;
        void setData(List<String> mList , int [] mBackgroundColors){
            list.clear();
            list.addAll(mList);
            backgroundColors = new int [list.size()];
            for (int i = 0 ; i < list.size() ; i++) {
            backgroundColors[i] = mBackgroundColors [i];
            }
            notifyDataSetChanged();
        }
        @Override
        public int getCount(){
            return list.size();
        }
        @Override
        public Object getItem(int position){
            return  null;
        }
        @Override
        public long getItemId(int position){
            return  0;
        }
        @Override
        public View getView(int position , View convertView , ViewGroup parent){
            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater)
                        MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item, parent, false);
            }
            final TextView textView = convertView.findViewById(R.id.task);
            textView.setBackgroundColor(backgroundColors[position]);

            textView.setText(list.get(position));
                return convertView;
        }
    }
}
