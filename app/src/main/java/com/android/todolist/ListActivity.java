package com.android.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseChange;
import com.couchbase.lite.DatabaseChangeListener;
import com.couchbase.lite.Document;
import com.couchbase.lite.MutableDocument;

import java.util.Date;


public class ListActivity extends AppCompatActivity implements View.OnClickListener, DatabaseChangeListener {

    private final String TAG = ListActivity.class.getSimpleName();
    private static final String DEFAULT_TASK_ID="-1";

    // todolist fields
    private final String ID = "id";
    private final String DESCRIPTION = "description";
    private final String PRIORITY = "priority";
    private final String DATE = "updated";

    private EditText mEditText;
    private static String mTaskId=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mEditText = findViewById(R.id.editTextTaskDescription);
        Button mButton = findViewById(R.id.saveButton);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mTaskId = bundle.getString(ID);
            setEditorTitle(1);
            populateUi(mTaskId);
        } else {
            mTaskId=DEFAULT_TASK_ID;
            setEditorTitle(0);
        }

        mButton.setOnClickListener(this);

        getDb().addChangeListener(this);

    }
//database operations
    private Database getDb() {
        return CouchDatabase.getInstance(
                getApplicationContext());
    }

//data edited.
    private void setEditorTitle(int flag) {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            if (flag == 0) {
                actionBar.setTitle(getString
                        (R.string.editor_activity_title_new_task));
            } else {
                actionBar.setTitle(getString(R.string.
                        editor_activity_title_edit_task));
            }
        }

    }

//ui text
    private void populateUi(String docId) {
        Document document = getDb().getDocument(docId);

        if (document != null) {
            mEditText.setText(document.getString(DESCRIPTION));
        }
    }


    @Override
    public void onClick(View v) {
        if (!mTaskId.equals(DEFAULT_TASK_ID)) {
            Log.i(TAG,"1111>>>>> "+mTaskId);
            updateDocument(getDb());
        } else {
            Log.i(TAG,"2222>>>>> "+null);
            addDocument(getDb());
        }
    }

//data added.
    private void addDocument(Database database) {
        int count = (int) database.getCount();
        Log.i(TAG, "count : " + count);

        try {
            MutableDocument addDoc = new MutableDocument(
                    String.valueOf(count));

            addDoc.setString(ID, String.valueOf(count));
            addDoc.setString(DESCRIPTION, mEditText.getText().toString().trim());
            addDoc.setDate(DATE, new Date());

            database.save(addDoc);
            Toast.makeText(this, "New Task Added.", Toast.LENGTH_SHORT).show();
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Add task : " + e.toString());
        }
    }


//data updated
    private void updateDocument(Database database) {
        Document document = database.getDocument(mTaskId);

        if (document != null) {
            try {
                MutableDocument updateDoc = document.toMutable();

                updateDoc.setString(ID, mTaskId);
                updateDoc.setString(DESCRIPTION, mEditText.getText().toString().trim());
                updateDoc.setDate(DATE, new Date());

                database.save(updateDoc);
                Toast.makeText(this, "Task Updated!", Toast.LENGTH_SHORT).show();

            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Edit task : " + e.toString());
            }
        }
    }

//data initialization
    @Override
    public void changed(DatabaseChange change) {
        Intent intent = new Intent(
                ListActivity.this, MainActivity.class);
        startActivity(intent);
    }

}
