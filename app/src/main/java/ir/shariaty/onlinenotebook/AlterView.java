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

import ir.shariaty.onlinenotebook.Model.Note;

public class AlterView extends AppCompatActivity {

    Toolbar myToolbar;
    private TextView edit;
    private TextView delete;
    private TextView time;
    private TextView title;
    private TextView description;
    private FirebaseAuth mAuth;
    private Note mNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterview);

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColorPrimary));
        setSupportActionBar(myToolbar);


        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);

        mAuth = FirebaseAuth.getInstance();

        title.setText(mNote.getTitle());
        description.setText(mNote.getDescription());
        Date date_time = new Date(mNote.getTime());
        String pattern_date = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(pattern_date);
        String Date = simpleDateFormat1.format( date_time);
        String pattern_time = " HH:mm:ss.SSSZ";
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(pattern_time);
        String Time = simpleDateFormat2.format( date_time);
        time.setText(Date  + " " + Time);


    }

    private void setSupportActionBar(Toolbar myToolbar) {
    }
}