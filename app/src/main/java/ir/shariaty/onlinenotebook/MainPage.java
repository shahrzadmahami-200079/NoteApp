package ir.shariaty.onlinenotebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ir.shariaty.onlinenotebook.Adapter.NoteAdapter;
import ir.shariaty.onlinenotebook.Model.Note;

public class MainPage extends AppCompatActivity implements NoteAdapter.ListItemClickListener {

    Toolbar myToolbar;

    private FirebaseAuth mAuth;
    private ImageView addNote;
    private TextView user_name;

    private RecyclerView recyclerViewNoteItem;
    private NoteAdapter noteAdapter;
    private List<Note> mNote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

//
//        myToolbar = findViewById(R.id.toolbar);
//        myToolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.textColorPrimary));
//        setSupportActionBar(myToolbar);


        mAuth = FirebaseAuth.getInstance();


        recyclerViewNoteItem = findViewById(R.id.listNotes);
        recyclerViewNoteItem.setHasFixedSize(true);


        recyclerViewNoteItem.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewNoteItem.setLayoutManager(mLayoutManager);
        mNote = new ArrayList<>();
        noteAdapter = new NoteAdapter(this, this, mNote);
        recyclerViewNoteItem.setAdapter(noteAdapter);
        readNote();



        /*addNote = findViewById(R.id.ivAddNote);
        user_name = findViewById(R.id.ivUserName);*/


        ImageView addNote = findViewById(R.id.ivAddNote);
        user_name = findViewById(R.id.ivUserName);

        addNote.setOnClickListener(addView -> {
            startActivity(new Intent(MainPage.this, CreateNoteActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));

            finish();
        });


    }

    private void readNote() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Notes")
                .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        mNote.clear();
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String description = Objects.requireNonNull(snapshot.child("description").getValue()).toString();
                Long time = (Long) snapshot.child("time").getValue();
                String title = Objects.requireNonNull(snapshot.child("title").getValue()).toString();
                Note myNote = new Note(title, description, time);
                mNote.add(myNote);

                noteAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void setSupportActionBar(Toolbar myToolbar) {
    }


    @Override
    public void onListItemClick(Note note) {
        Intent intentAlter = new Intent(this, ActivityAlterView.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intentAlter.putExtra("title", note.getTitle());
        intentAlter.putExtra("description", note.getDescription());
        intentAlter.putExtra("time", note.getTime().toString());
        startActivity(intentAlter);
        finish();
    }
}