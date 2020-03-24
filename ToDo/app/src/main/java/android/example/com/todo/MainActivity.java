package android.example.com.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private LinkedList<Task> mTaskData;
    private DataAdapter mDataAdapter;
    private TaskOpenHelper mDB;

    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDB = new TaskOpenHelper(this);

        mDataAdapter = new DataAdapter(this, mDB);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(mDataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewTask.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                String heading = data.getStringExtra(NewTask.EXTRA_RETURN_HEADING);
                String detail = data.getStringExtra(NewTask.EXTRA_RETURN_DETAIL);

                mDB.insertTask(heading, detail, 1);
                mDataAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch(item.getTitle().toString()){
            case "Mark Done":
            case "Mark Pending":
                int updated = mDB.editTaskAtPos(item.getGroupId());
                if(updated == 1){
                    mDataAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Edited Task!", Toast.LENGTH_SHORT).show();
                }
                return true;
            case "Delete":
                int deleted = mDB.deleteTaskAtPos(item.getGroupId());
                if(deleted == 1){
                    mDataAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "Deleted Task!", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
