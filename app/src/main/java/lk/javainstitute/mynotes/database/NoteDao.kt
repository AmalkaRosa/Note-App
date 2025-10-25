package lk.javainstitute.mynotes.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import lk.javainstitute.mynotes.model.Note

@Dao
interface NoteDao {
    @Insert suspend fun insert(note: Note)
    @Update suspend fun update(note: Note)
    @Delete suspend fun delete(note: Note)

    @Query("SELECT * FROM `note_table` ORDER BY `id` DESC")
    fun getAllNotes(): LiveData<List<Note>>
}