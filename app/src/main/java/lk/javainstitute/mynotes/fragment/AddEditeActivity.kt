package lk.javainstitute.mynotes.fragment

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lk.javainstitute.mynotes.R
import lk.javainstitute.mynotes.database.NoteDatabase
import lk.javainstitute.mynotes.model.Note

class AddEditeActivity : AppCompatActivity() {

    private lateinit var textTitle: EditText
    private lateinit var textContent: EditText
    private lateinit var headerTextView: TextView

    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_edite)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textTitle = findViewById(R.id.editTextText)
        textContent = findViewById(R.id.editTextText2)
        headerTextView = findViewById(R.id.textView)

        noteId = intent.getIntExtra("note_id",-1)
        val noteTitle = intent.getStringExtra("note_title")
        val noteContent = intent.getStringExtra("note_content")

        if(noteId != -1){
            headerTextView.text = "Edit Note"
            textTitle.setText(noteTitle)
            textContent.setText(noteContent)
        }else{
            headerTextView.text = "Add New Note"
        }
    }

    @Deprecated("Use OnBackPressedDispatcher instead")
    override fun onBackPressed() {
        val title = textTitle.text.toString().trim()
        val content = textContent.text.toString().trim()

        if (title.isEmpty() && content.isEmpty()) {
            super.onBackPressed()
            return
        }

        val db = NoteDatabase.getDatabase(this)
        val noteDao = db.noteDao()

        CoroutineScope(Dispatchers.IO).launch {
            if (noteId != -1) {
                val updatedNote = Note(id = noteId, title = title, content = content)
                noteDao.update(updatedNote)
            } else {
                val newNote = Note(title = title, content = content)
                noteDao.insert(newNote)
            }
        }

        Toast.makeText(
            this,
            if (noteId != -1) "Note Updated." else "Note Saved.",
            Toast.LENGTH_SHORT
        ).show()

        super.onBackPressed()
    }
}