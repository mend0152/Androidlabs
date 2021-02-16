package com.cst2335.mend0152;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    MyListAdapter myAdapter;
    int flag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //initialize the list
        myAdapter = new MyListAdapter();
        //retrieve the ListView from .xml
        ListView myList = (ListView) findViewById(R.id.listview);

        Button sendButton = findViewById(R.id.sendbutton);
        Button receiveButton = findViewById(R.id.receivebutton);
        EditText typeMessage = findViewById(R.id.editTextChat);

        sendButton.setOnClickListener(click->{
            list.add(typeMessage.getText().toString());
            SR.add("1");
            typeMessage.setText("");
            myAdapter.notifyDataSetChanged();//re-run populating the table
        });

        receiveButton.setOnClickListener(click->{
            list.add(typeMessage.getText().toString());
            SR.add("2");
            typeMessage.setText("");
            myAdapter.notifyDataSetChanged();//re-run populating the table
        });

        ListView myListView = findViewById(R.id.listview);
       // myListView.setAdapter( myAdapter = new MyListAdapter());

        myListView.setOnItemLongClickListener( (p, b, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want delete this?")

                    //What is the message:
                    .setMessage("The selected row is" + pos +
                                "The database id id:" + id)

                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {
                        list.remove(pos);
                        SR.remove(pos);
                        myAdapter.notifyDataSetChanged();
                    })
                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> { });

            return true;
        });

        myList.setAdapter( myAdapter );//populate the list

    }

    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<String> SR = new ArrayList<>();

    //an inner class
    class MyListAdapter extends BaseAdapter{

        @Override //number of items in the list
        public int getCount() { return list.size(); }

        @Override //what to show at row position
        public String getItem(int position) {
            return list.get(position);
        }

        @Override //returns the database id
        public long getItemId(int i) {
            return (long)i; //return 0 is fine not using database
        }

        @Override //how to show it: button, textView, checkbox?
        public View getView(int row, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
           // will check in each list row if the button pushed was send or receive and update the row.
            if ( SR.get(row).equals("1")) {
                View newView = inflater.inflate(R.layout.activity_chat_room_send, viewGroup, false);
                TextView tView = newView.findViewById(R.id.textViewSend);
                tView.setText(getItem(row).toString());
                return newView;
            } else if ( SR.get(row).equals("2")) {
                View newView = inflater.inflate(R.layout.activity_chat_room_receive, viewGroup, false);
                TextView tView = newView.findViewById(R.id.textViewReceive);
                tView.setText(getItem(row).toString());
                return newView;
            }
            flag = 0;
            return null;
        }

    }
}