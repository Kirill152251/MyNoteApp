package com.example.mynoteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import com.example.mynoteapp.databinding.ActivityAddEditNoteBinding
import java.text.SimpleDateFormat
import java.util.*

class AddEditNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddEditNoteBinding
    private lateinit var viewModel: NoteViewModel
    var noteId = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)
        val noteType = intent.getStringExtra("EXTRA_NOTE_TYPE")
        if (noteType.equals("Edit")) {
            noteId = intent.getLongExtra("EXTRA_ID", -1)
            binding.apply {
                addUpdateBtn.text = "Update Note"
                etTitle.setText(intent.getStringExtra("EXTRA_TITLE"))
                etDesc.setText(intent.getStringExtra("EXTRA_DESC"))
            }
        } else {
            binding.addUpdateBtn.text = "Save Note"
        }
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.addUpdateBtn.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val desc = binding.etDesc.text.toString()
            if (noteType.equals("Edit")) {
                if (title.isNotEmpty() && desc.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate:String = sdf.format(Date())
                    val updatedNote = Note(title, desc, currentDate)
                    updatedNote.id = noteId
                    viewModel.update(updatedNote)
                    Toast.makeText(this, "Note Updated...", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    this.finish()
                } else {
                    Toast.makeText(this, "Enter note Title and Description", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                if (title.isNotEmpty() && desc.isNotEmpty()) {
                    val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                    val currentDate = sdf.format(Date())
                    val newNote = Note(title, desc, currentDate)
                    viewModel.insert(newNote)
                    Toast.makeText(this, "Note Added...", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    this.finish()
                } else {
                    Toast.makeText(this, "Enter note Title and Description", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this@AddEditNoteActivity, MainActivity::class.java)
        startActivity(intent)
        this.finish()
        return true
    }

    override fun onBackPressed() {
        val intent = Intent(this@AddEditNoteActivity, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}