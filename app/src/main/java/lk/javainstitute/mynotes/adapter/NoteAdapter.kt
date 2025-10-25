package lk.javainstitute.mynotes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lk.javainstitute.mynotes.R
import lk.javainstitute.mynotes.model.Note

class NoteAdapter(
    private val onItemClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var notes = listOf<Note>()

    inner class NoteViewHolder(val view: View): RecyclerView.ViewHolder(view){
        val title: TextView = view.findViewById(R.id.textViewTitle)
        val content: TextView = view.findViewById(R.id.textViewContent)
        val deleteIcon = itemView.findViewById<ImageView>(R.id.deleteIcon)

        fun bind(note: Note) {
            title.text = note.title
            content.text = note.content

            // Initially hide delete icon
            deleteIcon.visibility = View.GONE

            // Normal click
            itemView.setOnClickListener {
                onItemClick(note)
            }

            // Long press to toggle delete icon
            itemView.setOnLongClickListener {
                deleteIcon.visibility = if (deleteIcon.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                true
            }

            // Delete click
            deleteIcon.setOnClickListener {
                onDeleteClick(note)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note,parent,false)
        return NoteViewHolder(view)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note)
    }

    fun setNotes(newNotes: List<Note>){
        notes = newNotes
        notifyDataSetChanged()
    }

    fun updateNote(updatedNote: Note) {
        val index = notes.indexOfFirst { it.id == updatedNote.id }
        if (index != -1) {
            notes = notes.toMutableList().apply { set(index, updatedNote) }
            notifyItemChanged(index)
        }
    }
}
