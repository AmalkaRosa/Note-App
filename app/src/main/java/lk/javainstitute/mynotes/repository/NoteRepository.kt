package lk.javainstitute.mynotes.repository

import lk.javainstitute.mynotes.database.NoteDao
import lk.javainstitute.mynotes.model.Note

class NoteRepository(private val noteDao: NoteDao) {
    val allNotes = noteDao.getAllNotes()

    suspend fun insert(note: Note) = noteDao.insert(note)
    suspend fun update(note: Note) = noteDao.update(note)
    suspend fun delete(note: Note) = noteDao.delete(note)
}
