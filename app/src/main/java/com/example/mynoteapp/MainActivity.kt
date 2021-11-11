package com.example.mynoteapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynoteapp.databinding.ActivityMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), NoteClickInterface, NoteClickDeleteInterface,
    NoteClickInterfaceWithoutDesc, NoteClickDeleteInterfaceWithoutDesc {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var typeOfAdapter: String? = "with"
    private var nightMode: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModel::class.java)

        binding.rvNotes.layoutManager = LinearLayoutManager(this)

        sharedPreferences = getSharedPreferences("AppSittingPref", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        typeOfAdapter = sharedPreferences.getString("ADAPTER", "with")
        nightMode = sharedPreferences.getBoolean("NightMode", false)

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        if (typeOfAdapter.equals("without")) {
            setRecycleViewAdapterWithoutDescription()
        } else {
            setRecycleViewAdapterWithDescription()
        }

        binding.addBtn.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    private fun setRecycleViewAdapterWithDescription() {
        val noteAdapter = NoteAdapter(this, this, this)
        binding.rvNotes.adapter = noteAdapter
        viewModel.allNotes.observe(this, Observer { list ->
            list?.let {
                noteAdapter.update(it)
            }
        })
        editor.putString("ADAPTER", "with").apply()
    }

    private fun setRecycleViewAdapterWithoutDescription() {
        val noteAdapter = NoteWithoutDescAdapter(this, this, this)
        binding.rvNotes.adapter = noteAdapter
        viewModel.allNotes.observe(this, Observer { list ->
            list?.let {
                noteAdapter.update(it)
            }
        })
        editor.putString("ADAPTER", "without").apply()
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
        intent.putExtra("EXTRA_NOTE_TYPE", "Edit")
        intent.putExtra("EXTRA_TITLE", note.title)
        intent.putExtra("EXTRA_DESC", note.description)
        intent.putExtra("EXTRA_ID", note.id)
        startActivity(intent)
        this.finish()
    }

    override fun onDeleteIconClick(note: Note) {
        viewModel.delete(note)
        Toast.makeText(this, " Note Deleted...", Toast.LENGTH_SHORT).show()
    }


    //Create Toolbar menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemClose -> finish()
            R.id.itemDelete -> {
                val deleteDialog = AlertDialog.Builder(this)
                    .setTitle("Delete Notes")
                    .setMessage("Do you want to  delete notes?")
                    .setIcon(R.drawable.ic_baseline_delete_24)
                    .setPositiveButton("Yes") { _, _ ->
                        viewModel.deleteAllNotes()
                        Toast.makeText(this, "All notes deleted...", Toast.LENGTH_LONG).show()
                    }
                    .setNegativeButton("No") { _, _ -> }
                    .create()
                deleteDialog.show()
            }
            // Change recycle view item from "with description" to "without description" and vice versa
            R.id.itemHideShow -> {
                typeOfAdapter = sharedPreferences.getString("ADAPTER", "with")
                if (typeOfAdapter.equals("with")){
                    setRecycleViewAdapterWithoutDescription()
                } else {
                    setRecycleViewAdapterWithDescription()
                }
            }
            R.id.nightDayModeItem -> {
                if (nightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    editor.putBoolean("NightMode", false).apply()
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    editor.putBoolean("NightMode", true).apply()
                }
            }
        }
        return true
    }
}