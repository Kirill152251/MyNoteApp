package com.example.mynoteapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    val context: Context,
    val noteClickInterface: NoteClickInterface,
    val noteClickDeleteInterface: NoteClickDeleteInterface
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private val allNotes = ArrayList<Note>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val noteTitle = itemView.findViewById<TextView>(R.id.noteTitle)
        val noteTime = itemView.findViewById<TextView>(R.id.noteTime)
        val delete = itemView.findViewById<ImageView>(R.id.deleteNote)
        val noteDesc = itemView.findViewById<TextView>(R.id.noteDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTitle.setText(allNotes.get(position).title)
        holder.noteDesc.setText(allNotes.get(position).description)
        holder.noteTime.setText("Last update: ${allNotes.get(position).timestamp}")

        holder.delete.setOnClickListener {
            noteClickDeleteInterface.onDeleteIconClick(allNotes.get(position))
        }

        holder.itemView.setOnClickListener {
            noteClickInterface.onNoteClick(allNotes.get(position))
        }
    }

    override fun getItemCount(): Int {
        return allNotes.size
    }

    fun update(newList: List<Note>) {
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }
}

interface NoteClickDeleteInterface {
    fun onDeleteIconClick(note: Note)
}

interface NoteClickInterface {
    fun onNoteClick(note: Note)
}