package com.vbix.vishwas.notes.adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.vbix.vishwas.notes.R;
import com.vbix.vishwas.notes.entities.Note;
import com.vbix.vishwas.notes.listeners.NotesListeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    List<Note> ListNotes;
    private NotesListeners notesListeners;
    private Timer timer;
    private List<Note> notesSource;

    public NotesAdapter(List<Note> listNotes,NotesListeners notesListeners) {
        ListNotes = listNotes;
        this.notesListeners = notesListeners;
        notesSource = ListNotes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.recycle_item_note,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, final int position) {
        holder.setNote(ListNotes.get(position));
        holder.recycle_item_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notesListeners.onNoteClicked(ListNotes.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListNotes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{

        // View Declaration for Item Layout of RecyclerView
        TextView itemTextTitle, itemTextSubtitle, itemTextDateTime;
        LinearLayout recycle_item_note;
        RoundedImageView itemImageNote;
        // Constructor
        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTextTitle = itemView.findViewById(R.id.itemTextTitle);
            itemTextSubtitle = itemView.findViewById(R.id.itemTextSubtitle);
            itemTextDateTime = itemView.findViewById(R.id.itemTextDateTime);
            recycle_item_note = itemView.findViewById(R.id.layoutItemNote);
            itemImageNote = itemView.findViewById(R.id.itemImageNote);
        }
        // Set Note Method
        void setNote(Note note){
            itemTextTitle.setText(note.getTitle());
            if(note.getSubtitle().trim().isEmpty()){
                itemTextSubtitle.setVisibility(View.GONE);
            }
            else{
                itemTextSubtitle.setText(note.getSubtitle());
            }
            itemTextDateTime.setText(note.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) recycle_item_note.getBackground();
            if(note.getColor() != null){
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }
            else {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if(note.getImagePath() != null){
                itemImageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                itemImageNote.setVisibility(View.VISIBLE);
            }
            else{
                itemImageNote.setVisibility(View.GONE);
            }
        }
    }

    public void searchNotes(final String searchKeyword){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()){
                    ListNotes = notesSource;
                }else{
                    ArrayList<Note> temp = new ArrayList<>();
                    for (Note note: notesSource){
                        if (note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()) || note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase()) || note.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(note);
                        }
                    }
                    ListNotes = temp;
                }
               new Handler(Looper.getMainLooper()).post(new Runnable() {
                   @Override
                   public void run() {
                        notifyDataSetChanged();
                   }
               });
            }
        },500);
    }

    public void cancelTimer(){
        if (timer != null){
            timer.cancel();
        }
    }

}
