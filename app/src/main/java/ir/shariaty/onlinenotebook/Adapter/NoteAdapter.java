package ir.shariaty.onlinenotebook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ir.shariaty.onlinenotebook.Model.Note;
import ir.shariaty.onlinenotebook.R;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context mContext;
    private List<Note> mNote;
    final private ListItemClickListener mOnClickListener;


    public NoteAdapter(Context mContext, ListItemClickListener mOnClickListener, List<Note> mNote) {
        this.mContext = mContext;
        this.mOnClickListener = mOnClickListener;
        this.mNote = mNote;
    }


    public interface ListItemClickListener {
        void onListItemClick(Note note);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.note_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(mNote.get(position).getTitle());
        holder.description.setText(mNote.get(position).getDescription());

        String pattern_date = "MM-dd-yyyy HH:mm:ss.SSSZ";
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(pattern_date);
        String date = simpleDateFormat1.format(mNote.get(position).getTime());
        holder.time.setText(date);

    }

    @Override
    public int getItemCount() {
        return mNote.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView description;
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            time = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(viewClick -> getNotePosition());

        }

        private void getNotePosition() {
            if (mOnClickListener != null) {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    mOnClickListener.onListItemClick(mNote.get(pos));
                }
            }
        }


    }
}


