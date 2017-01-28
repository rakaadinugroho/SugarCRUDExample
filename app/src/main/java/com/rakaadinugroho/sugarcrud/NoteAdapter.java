package com.rakaadinugroho.sugarcrud;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Raka Adi Nugroho on 1/27/17.
 *
 * @Github github.com/rakaadinugroho
 * @Contact nugrohoraka@gmail.com
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {
    private Context context;
    private List<Notes> notesList;
    OnItemClickListener clickListener;

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public NoteAdapter(Context context, List<Notes> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view   = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        final Notes notes    = notesList.get(position);
        holder.note_title.setText(notes.getTitle());
        holder.note_content.setText(notes.getContent());
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView note_title;
        public TextView note_content;
        public NoteViewHolder(View itemView) {
            super(itemView);

            note_title  = (TextView) itemView.findViewById(R.id.note_title);
            note_content    = (TextView) itemView.findViewById(R.id.note_content);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(v, getAdapterPosition());
        }
    }
    public interface OnItemClickListener{
        public void onItemClick(View view, int position);
    }
}
