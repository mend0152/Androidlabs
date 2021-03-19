package com.cst2335.mend0152;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.Cache;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatRoomActivity extends AppCompatActivity {
    private static int ACTIVITY_VIEW_CONTACT = 33;
    int positionClicked = 0;
    MyListAdapter myAdapter;
    Message object;
    SQLiteDatabase db;
    DetailFragment dFragment;

    private AppCompatActivity parentActivity;

    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static  final boolean IS_SENT = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        loadDataFromDatabase(); //get any previously saved Contact objects

        //initialize the list
        myAdapter = new MyListAdapter();

        //retrieve the FrameLayout
        FrameLayout frame = findViewById(R.id.frameLayout);


        //retrieve the ListView from .xml
        ListView myList = findViewById(R.id.chatListView);
        myList.setAdapter( myAdapter );

        boolean isTablet = findViewById(R.id.frameLayout) != null;//check if the FrameLayout is loaded

        //Click listener for ListView
        myList.setOnItemClickListener( (par, view, pos, id) -> {

            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, messageList.get(pos).getMessage() );
            dataToPass.putInt(ITEM_POSITION, pos);
            dataToPass.putLong(ITEM_ID, id);
            dataToPass.putBoolean(String.valueOf(IS_SENT), messageList.get(pos).getIsSend());

                    if(isTablet) {
                        dFragment = new DetailFragment(); // add a detailFragment
                        dFragment.setArguments( dataToPass ); //pass it a bundle for information
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.frameLayout, dFragment) //Add the fragment in FrameLayout
                                .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
                    }
                    else //isPhone
                    {
                        Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                        nextActivity.putExtras(dataToPass); //send data to next activity
                        startActivity(nextActivity); //make the transition
                    }
            showMessage( pos );
        });

        //retrieving buttons and Edit Text
        Button sendButton = findViewById(R.id.lab4_sendButton);
        Button receiveButton = findViewById(R.id.lab4_receiveButton);
        EditText typeMessage = findViewById(R.id.lab4_typedMessage);

        sendButton.setOnClickListener(click->{
            String Message = typeMessage.getText().toString();
            String People = "Sender";

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();

            //Now provide a value for every database column defined in MyOpener.java:
            //put string name in the NAME column:
            newRowValues.put(MyOpener.COL_MESSAGE, Message);
            //put string email in the EMAIL column:
            newRowValues.put(MyOpener.COL_NAME, People);

            //Now insert in the database:
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            //Added the new contact to the list
            messageList.add(new Message(Message,true, newId));

            //Clear the edit text field
            typeMessage.setText("");

            //update the listView
            myAdapter.notifyDataSetChanged();//re-run populating the table

            //Show the id of the inserted item:
            Toast.makeText(this, "Inserted item id:"+newId, Toast.LENGTH_LONG).show();
        });

        receiveButton.setOnClickListener(click->{
            String Message = typeMessage.getText().toString();
            String People = "Receiver";

            //add to the database and get the new ID
            ContentValues newRowValues = new ContentValues();

            //Now provide a value for every database column defined in MyOpener.java:
            //put string MESSAGE in the MESSAGE column:
            newRowValues.put(MyOpener.COL_MESSAGE, Message);
            //put string NAME in the NAME column:
            newRowValues.put(MyOpener.COL_NAME, People);
            //Now insert in the database:
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);

            //Added the new contact to the list
            messageList.add(new Message(Message,false, newId));

            //Clear the edit text field
            typeMessage.setText("");

            //update the listView
            myAdapter.notifyDataSetChanged();//re-run populating the table

            //Show the id of the inserted item:
            Toast.makeText(this, "Inserted item id:"+newId, Toast.LENGTH_LONG).show();
        });

        myList.setAdapter( myAdapter );//populate the list


    }

    //Arraylist type Message
    private ArrayList<Message> messageList = new ArrayList<>();

    private void loadDataFromDatabase()
    {
        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {MyOpener.COL_ID, MyOpener.COL_MESSAGE, MyOpener.COL_NAME};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        printCursor(results, 1);

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int messageColumnIndex = results.getColumnIndex(MyOpener.COL_MESSAGE);
        int nameColIndex = results.getColumnIndex(MyOpener.COL_NAME);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String message = results.getString(messageColumnIndex);
            String name = results.getString(nameColIndex);
            long id = results.getLong(idColIndex);

            if(name.equals("Sender")) {
                //add the new Contact to the array list:
                messageList.add(new Message(message, true, id));
            } else
                //add the new Contact to the array list:
                messageList.add(new Message(message, false, id));

        }


        //At this point, the contactsList array has loaded every row from the cursor.
    }

    protected void showMessage(int position)
    {

        Message selectedMessage = messageList.get(position);
        View message_view;
        TextView rowMessage;
        TextView rowId;

        if(messageList.get(position).getIsSend()) {

            message_view = getLayoutInflater().inflate(R.layout.activity_chat_room_send, null);

            //get the TextViews
            rowMessage = message_view.findViewById(R.id.textViewSend);
            rowId = message_view.findViewById(R.id.row_id);


        }else{
            message_view = getLayoutInflater().inflate(R.layout.activity_chat_room_receive, null);

            //get the TextViews
            rowMessage = message_view.findViewById(R.id.textViewReceive);
            rowId = message_view.findViewById(R.id.row_id);

        }

        //set the fields for the alert dialog
        rowMessage.setText(selectedMessage.getMessage());
        rowId.setText("id:" + selectedMessage.getId());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("You clicked on item #" + position)
                .setMessage("You can delete")
                .setView(message_view) //add the 3 edit texts showing the contact information
                .setNegativeButton("Delete", (click, b) -> {
                    deleteContact(selectedMessage); //remove the contact from database
                    messageList.remove(position); //remove the contact from contact list
                    myAdapter.notifyDataSetChanged(); //there is one less item so update the list
                })
                .setNeutralButton("dismiss", (click, b) -> { })
                .setPositiveButton("Delete and Close", (click, b) -> {
                    deleteContact(selectedMessage); //remove the contact from database
                    messageList.remove(position); //remove the contact from contact list
                    myAdapter.notifyDataSetChanged(); //there is one less item so update the list
                    getSupportFragmentManager()
                            .beginTransaction()
                            .remove(dFragment) //remove the fragment in FrameLayout
                            .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
                })
                .create().show();
    }

    protected void deleteContact(Message m)
    {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(m.getId())});
    }


    //an inner class
    class MyListAdapter extends BaseAdapter{

        @Override //number of items in the list
        public int getCount() { return messageList.size(); }

        @Override //what to show at row position
        public Message getItem(int position) {
            return messageList.get(position);
        }

        @Override //returns the database id
        public long getItemId(int i) {
            return getItem(i).getId();
        }

        @Override //how to show it: button, textView, checkbox?
        public View getView(int row, View view, ViewGroup viewGroup) {

            View newView = view;
            LayoutInflater inflater = getLayoutInflater();
            Message thisRow = getItem(row);

           // will check in each list row if the button pushed was send or receive and update the row.
            if ( messageList.get(row).getIsSend()) {
                newView = inflater.inflate(R.layout.activity_chat_room_send, viewGroup, false);
                TextView tView = newView.findViewById(R.id.textViewSend);
                tView.setText(thisRow.getMessage());
                TextView tidView = newView.findViewById(R.id.row_id);
                tidView.setText("Id:" + thisRow.getId());
                //return newView;
            } else {
                newView = inflater.inflate(R.layout.activity_chat_room_receive, viewGroup, false);
                TextView tView = newView.findViewById(R.id.textViewReceive);
                tView.setText(thisRow.getMessage());
                TextView tidView = newView.findViewById(R.id.row_id);
                tidView.setText("Id:" + thisRow.getId());

                //return newView;
            }
            //tView.setText(getItem(row).toString());
            return newView;

//            return null;
        }

    }

    public void printCursor (Cursor c, int version){

        String S1 = "the DB Version is: " + db.getVersion();
        Log.d("Print", S1);
        Log.e("Print", S1);
        String S2 = "The number of columns in the cursor: " + c.getColumnCount();
        Log.d("Print", S2);
        Log.e("Print", S2);
        String[] columnNames = c.getColumnNames();
        String S3 = "The name of the columns in the cursor: " + Arrays.toString(columnNames);
        Log.d("Print", S3);
        Log.e("Print", S3);
        String S4 = "The number of rows in the cursor: " + c.getCount();
        Log.d("Print", S4);
        Log.e("Print", S4);

    }

}

