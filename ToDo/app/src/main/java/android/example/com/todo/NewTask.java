package android.example.com.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewTask extends AppCompatActivity {

    private EditText mHeadingEditText;
    private EditText mDetailEditText;
    private TaskOpenHelper mDB;

    public static final String EXTRA_RETURN_HEADING = "com.android.example.heading";
    public static final String EXTRA_RETURN_DETAIL = "com.android.example.detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mHeadingEditText = findViewById(R.id.new_task_title);
        mDetailEditText = findViewById(R.id.new_task_detail);

        mDB = new TaskOpenHelper(this);
    }

    public void addTask(View view) {
        String heading = mHeadingEditText.getText().toString();
        String detail = mDetailEditText.getText().toString();

        if(heading.equals("") || detail.equals("")){
            Toast.makeText(this, "Enter details!", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent returnIntent = new Intent();
            returnIntent.putExtra(EXTRA_RETURN_HEADING, heading);
            returnIntent.putExtra(EXTRA_RETURN_DETAIL, detail);

            setResult(RESULT_OK, returnIntent);
            finish();
        }
    }
}
