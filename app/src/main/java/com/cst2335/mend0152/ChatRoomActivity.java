package com.cst2335.mend0152;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    MyListAdapter myAdapter;
    Message object;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        //initialize the list
        myAdapter = new MyListAdapter();

        //retrieve the ListView from .xml
        ListView myList = findViewById(R.id.TheChatView);
        myList.setAdapter( myAdapter );

        //Click listener for ListView
        myList.setOnItemLongClickListener( (par, view, pos, id) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Do you want delete this?")

                    //What is the message:
                    .setMessage("The selected row is" + pos +
                                "The database id id:" + id)

                    //what the Yes button does:
                    .setPositiveButton("Yes", (click, arg) -> {
                        messageList.remove(pos);
                        //SR.remove(pos);
                        myAdapter.notifyDataSetChanged();
                    })
                    //What the No button does:
                    .setNegativeButton("No", (click, arg) -> { })

                    //An optional third button:
                    .setNeutralButton("Cancel", (click, arg) -> {  })

                    //Show the dialog
                    .create().show();

            return true;

        });

        //retrieving buttons and Edit Text
        Button sendButton = findViewById(R.id.lab4SendButton);
        Button receiveButton = findViewById(R.id.lab4ReceiveButton);
        EditText typeMessage = findViewById(R.id.editTextChat);

        sendButton.setOnClickListener(click->{
            object = new Message();
            object.setMessage(typeMessage.getText().toString());
            object.setSend(true);
            messageList.add(object);
            typeMessage.setText("");
            myAdapter.notifyDataSetChanged();//re-run populating the table
        });

        receiveButton.setOnClickListener(click->{
            object = new Message();
            object.setMessage(typeMessage.getText().toString());
            object.setSend(false);
            messageList.add(object);
            typeMessage.setText("");
            myAdapter.notifyDataSetChanged();//re-run populating the table
        });

        myList.setAdapter( myAdapter );//populate the list

    }

    //Arraylist type Message
    private ArrayList<Message> messageList = new ArrayList<>();


    //class message to store messages and who is raising
    class Message
    {
        String message;
        boolean isSend;

        public String getMessage(){
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSend() {
            return isSend;
        }

        public void setSend(boolean send) {
            isSend = send;
        }
    }
    //an inner class
    class MyListAdapter extends BaseAdapter{

        @Override //number of items in the list
        public int getCount() { return messageList.size(); }

        @Override //what to show at row position
        public String getItem(int position) {
            return messageList.get(position).getMessage();
        }

        @Override //returns the database id
        public long getItemId(int i) {
            return (long)i; //return 0 is fine not using database
        }

        @Override //how to show it: button, textView, checkbox?
        public View getView(int row, View view, ViewGroup viewGroup) {

            View newView = view;
            LayoutInflater inflater = getLayoutInflater();

           // will check in each list row if the button pushed was send or receive and update the row.
            if ( messageList.get(row).isSend()) {
                newView = inflater.inflate(R.layout.activity_chat_room_send, viewGroup, false);
                TextView tView = newView.findViewById(R.id.textViewSend);
                tView.setText(getItem(row).toString());
                //return newView;
            } else {
                newView = inflater.inflate(R.layout.activity_chat_room_receive, viewGroup, false);
                TextView tView = newView.findViewById(R.id.textViewReceive);
                tView.setText(getItem(row).toString());
                //return newView;
            }
            //tView.setText(getItem(row).toString());
            return newView;

//            return null;
        }

    }
}