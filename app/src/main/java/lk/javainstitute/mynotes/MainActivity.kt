package lk.javainstitute.mynotes

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lk.javainstitute.mynotes.R.id.main
import lk.javainstitute.mynotes.adapter.NoteAdapter
import lk.javainstitute.mynotes.database.NoteDatabase
import lk.javainstitute.mynotes.fragment.AddEditeActivity
import lk.javainstitute.mynotes.model.Note as Note1

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: View
    private lateinit var adapter: NoteAdapter
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var editNoteLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.homeRecyclerView)
        emptyView = findViewById(R.id.emptyView)
        fabAdd = findViewById(R.id.fabAdd)

        editNoteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val updatedNote = result.data?.getParcelableExtra<Note1>(/* name = */ "updated_note")
                updatedNote?.let { adapter.updateNote(it) }
            }
        }
        
        //recyclerView
        adapter = NoteAdapter(
            onItemClick = { note ->
                val intent = Intent(this, AddEditeActivity::class.java)
                intent.putExtra("note_id", note.id)
                intent.putExtra("note_title", note.title)
                intent.putExtra("note_content", note.content)
                editNoteLauncher.launch(intent)
            },
            onDeleteClick = { note ->
                deleteNote(note)
            }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        //FAB click listener
        fabAdd.setOnClickListener {
            val intent = Intent(this, AddEditeActivity::class.java)
            startActivity(intent)
        }

        loadNotes()
    }

    private fun loadNotes() {
        val db = NoteDatabase.getDatabase(this)
        val noteDao = db.noteDao()

        noteDao.getAllNotes().observe(this) { notes ->
            if (notes.isEmpty()) {
                recyclerView.visibility = View.GONE
                emptyView.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                emptyView.visibility = View.GONE
                adapter.setNotes(notes)
            }
        }
    }

    private fun deleteNote(note: Note1) {
        val db = NoteDatabase.getDatabase(this)
        val noteDao = db.noteDao()

        CoroutineScope(Dispatchers.IO).launch {
            noteDao.delete(note)
            withContext(Dispatchers.Main) {
                Toast.makeText(this@MainActivity, "Note deleted", Toast.LENGTH_SHORT).show()
                loadNotes()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }
}