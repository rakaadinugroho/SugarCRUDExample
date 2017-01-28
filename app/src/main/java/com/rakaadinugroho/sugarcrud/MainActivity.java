package com.rakaadinugroho.sugarcrud;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.orm.SugarContext;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView notes_recyclerview;
    private List<Notes> notes   = new ArrayList<>();
    private NoteAdapter noteAdapter;
    int totaldata;
    int positionmodif = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SugarContext.init(this);
        initViews();
        loadData();

        ItemTouchHelper.SimpleCallback simpleCallback   = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position  = viewHolder.getAdapterPosition();
                final Notes note    = notes.get(position);
                notes.remove(position);
                noteAdapter.notifyItemRemoved(position);

                note.delete();
                totaldata   -= 1;
                Toast.makeText(MainActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(notes_recyclerview);

        noteAdapter.setClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Notes detailnotes   = notes.get(position);

                Log.d("Detail", "Detail");
                Log.d("Title", detailnotes.getTitle());
                /* Update Data */
                Intent intent   = new Intent(MainActivity.this, AddNote.class);
                intent.putExtra("isEdit", true);
                intent.putExtra("title", detailnotes.getTitle());
                intent.putExtra("content", detailnotes.getContent());
                intent.putExtra("time", detailnotes.getTime());

                positionmodif   = position;
                startActivity(intent);
            }
        });
    }
    private void loadData() {
        totaldata   = (int) Notes.count(Notes.class);
        if (totaldata >= 0){
            notes   = Notes.listAll(Notes.class);
            noteAdapter = new NoteAdapter(this, notes);
            notes_recyclerview.setAdapter(noteAdapter);

            if (notes.isEmpty())
                Toast.makeText(this, "Belum Ada Data", Toast.LENGTH_SHORT).show();
        }
    }
    private void searchData(String param){
        List<Notes> notes   = Notes.findWithQuery(Notes.class, "SELECT title FROM Notes WHERE title LIKE ? ", "%"+param+"%");
        if (notes.size() > 0){
            Notes note  = notes.get(0);
            Toast.makeText(this, "Ada Data" + note.getTitle(), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Data Tidak Ditemukan!", Toast.LENGTH_SHORT).show();
        }
    }
    private void initViews() {
        notes_recyclerview = (RecyclerView) findViewById(R.id.notes_recyclerview);
        StaggeredGridLayoutManager layoutManager    = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        notes_recyclerview.setLayoutManager(layoutManager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater   = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        MenuItem menuItem   = menu.findItem(R.id.search_note);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchData(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.create_note:
                Intent intent   = new Intent(this, AddNote.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        final long totaldatanew = Notes.count(Notes.class);
        if (totaldatanew>totaldata){
            Notes note  = Notes.last(Notes.class);
            notes.add(note);
            noteAdapter.notifyItemInserted((int) totaldatanew);
            totaldata   = (int) totaldatanew;
        }
        if (positionmodif != -1) {
            notes.set(positionmodif, Notes.listAll(Notes.class).get(positionmodif));
            noteAdapter.notifyItemChanged(positionmodif);
        }
    }
}
