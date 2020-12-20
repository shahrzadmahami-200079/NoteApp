package ir.shariaty.onlinenotebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import ir.shariaty.onlinenotebook.Adapter.NoteAdapter;
import ir.shariaty.onlinenotebook.Model.Note;

public class AlterView extends AppCompatActivity  {

    Toolbar myToolbar;
    private TextView edit;
    private TextView delete;
    private TextView time;
    private TextView title;
    private TextView description;
    private FirebaseAuth mAuth;
    private Note mNote;
    private NoteAdapter noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterview);

        mNote = new Note(
                getIntent().getStringExtra("title"),
                getIntent().getStringExtra("description"),
                Long.parseLong(getIntent().getStringExtra("time")));

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColorPrimary));
        setSupportActionBar(myToolbar);


        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);


        String pattern_date = "MM-dd-yyyy HH:mm:ss.SSSZ";
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(pattern_date);
        String date = simpleDateFormat1.format( mNote.getTime());

        time.setText(date);
        title.setText(mNote.getTitle());
        description.setText(mNote.getDescription());

        mAuth = FirebaseAuth.getInstance();

        edit.setOnClickListener(editView -> updateNote());
        delete.setOnClickListener(deleteView -> deleteNote());


    }

    private void deleteNote() {
    }

    private void updateNote() {
    }

    private void setSupportActionBar(Toolbar myToolbar) {
    }


}