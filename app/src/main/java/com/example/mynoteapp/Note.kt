package com.example.mynoteapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class Note(


    @ColumnInfo(name = "note_title")
    var title: String,

    @ColumnInfo(name = "note_description")
    var description: String,

    @ColumnInfo(name = "note_time")
    var timestamp: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}
