package ir.shariaty.onlinenotebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Objects;

import ir.shariaty.onlinenotebook.Model.Note;

public class ActivityAlterView extends AppCompatActivity {

    private TextView edit;
    private TextView delete;
    private TextView time;
    private TextView title;
    private TextView description;
    private FirebaseAuth mAuth;
    private Note mNote;
    private ProgressDialog progressDialog;
    private TextView save;
    private EditText etTitle;
    private EditText etDescription;
    private LinearLayout show;
    private RelativeLayout rlEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterview);

        mNote = new Note(
                getIntent().getStringExtra("title"),
                getIntent().getStringExtra("description"),
                Long.parseLong(getIntent().getStringExtra("time")));


        save = findViewById(R.id.tvSave);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);

        rlEdit = findViewById(R.id.rlEdit);
        show = findViewById(R.id.show);

        edit = findViewById(R.id.edit);
        delete = findViewById(R.id.delete);
        time = findViewById(R.id.time);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);

        progressDialog = new ProgressDialog(this);


        String pattern_date = "MM-dd-yyyy HH:mm:ss.SSSZ";
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(pattern_date);
        String date = simpleDateFormat1.format(mNote.getTime());

        time.setText(date);
        title.setText(mNote.getTitle());
        description.setText(mNote.getDescription());

        mAuth = FirebaseAuth.getInstance();

        edit.setOnClickListener(editView -> {
                    etDescription.setText(mNote.getDescription());
                    etTitle.setText(mNote.getTitle());
                    show.setVisibility(View.GONE);
                    rlEdit.setVisibility(View.VISIBLE);
                }
        );

        save.setOnClickListener(viewEdit -> {
            updateNote(mNote);
        });

        delete.setOnClickListener(deleteView -> deleteNote(mNote));


    }

    private void deleteNote(Note note) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query noteQuery = ref.child("Notes").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .orderByChild("time").equalTo(note.getTime());

        noteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue();

                }
                //back to Main Activity
                startActivity(new Intent(ActivityAlterView.this, MainPage.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("NoteAdapter", "onCancelledDeleteNote", databaseError.toException());
            }
        });

    }

    private void updateNote(Note noteUpdate) {
        String txtTitle = etTitle.getText().toString().trim();
        String txtDescription = etDescription.getText().toString().trim();
        Long timeNote = System.currentTimeMillis();

        if (TextUtils.isEmpty(txtTitle) || TextUtils.isEmpty(txtDescription)) {
            Toast.makeText(ActivityAlterView.this, "Please Enter All Field", Toast.LENGTH_SHORT).show();

            if (TextUtils.isEmpty(txtTitle)) etTitle.requestFocus();
            else if (TextUtils.isEmpty(txtDescription)) etDescription.requestFocus();
            else etTitle.requestFocus();


        } else {
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();

            HashMap<String, Object> mapNote = new HashMap<>();
            mapNote.put("title", txtTitle);
            mapNote.put("description", txtDescription);
            mapNote.put("time", timeNote);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            Query noteQuery = ref.child("Notes").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .orderByChild("time").equalTo(noteUpdate.getTime());

            noteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().setValue(mapNote);
                    }

                    progressDialog.dismiss();

                    show.setVisibility(View.VISIBLE);
                    rlEdit.setVisibility(View.GONE);

                    title.setText(txtTitle);
                    description.setText(txtDescription);
                    String timeNote = "MM-dd-yyyy HH:mm:ss.SSSZ";
                    SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(timeNote);
                    String date = simpleDateFormat1.format(mNote.getTime());
                    time.setText(date);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //back to Main Activity
        startActivity(new Intent(ActivityAlterView.this, MainPage.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        finish();

    }
}



