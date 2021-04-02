package com.android.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final String TAG = MainActivity.class.getSimpleName();
    private static final String ID = "id";
    private static final String DESCRIPTION = "description";
    private static final String PRIORITY = "priority";
    private static final String DATE = "updated";

    private ListView mListView;
    private ListAdapter mListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

        mListView = findViewById(R.id.taskList);
        RelativeLayout emptyView = findViewById(R.id.empty_view);
        mListView.setEmptyView(emptyView);
        mListView.setOnItemClickListener(this);
        showTasks(getDb());

    }


    //database işlemleri
    private Database getDb() {
        return CouchDatabase.getInstance(
                getApplicationContext());
    }


    private void showTasks(final Database database) {
        final ArrayList<TaskEntry> taskList = new ArrayList<>();
        final int count = (int) database.getCount();
        Log.i(TAG, "count " + count);
//couchbase kaynaklı hatalar saptandığında try catch çalışacak.
        try {
            database.inBatch(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < count; i++) {
                        Document document = database.getDocument(
                                String.valueOf(i));

                        if (document != null) {
                            String id = document.getString(ID);
                            String desc = document.getString(DESCRIPTION);
                            int priority = document.getInt(PRIORITY);
                            Date updated = document.getDate(DATE);

                            taskList.add(new TaskEntry(id, priority, desc, updated));
                        }
                    }
                }
            });
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        mListAdapter = new ListAdapter(this, taskList);
        mListView.setAdapter(mListAdapter);
    }

//tüm verileri silmek için.
    private void deleteAllTasks() {
        final Database database = getDb();
        final int count = (int) database.getCount();
        Log.i(TAG, "Delete : " + count);

        try {
            database.inBatch(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < count; i++) {
                        try {
                            database.delete(database.getDocument(String.valueOf(i)));
                        } catch (CouchbaseLiteException e) {
                            Log.i(TAG, "Delete i : " + e.toString());
                        }
                    }
                }
            });
        } catch (CouchbaseLiteException e) {
            Log.i(TAG, "Delete : " + e.toString());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            deleteAllTasks();
            mListAdapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(MainActivity.this, ListActivity.class);
        Log.i(TAG, "pos : " + String.valueOf(position));
        intent.putExtra(ID, String.valueOf(position));
        startActivity(intent);
    }

}
