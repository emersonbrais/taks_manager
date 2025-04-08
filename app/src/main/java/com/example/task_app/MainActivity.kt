package com.example.task_app;

import TaskViewModel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.task_app.adapters.ItemAdapter
import com.example.task_app.model.Task
import com.example.task_app.utils.TaskPreferences
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var taskPreferences: TaskPreferences
    private lateinit var viewModel: TaskViewModel
    private lateinit var adapter:ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_main)

        taskPreferences = TaskPreferences(this)
        viewModel = TaskViewModel(taskPreferences)

        val listView: ListView = findViewById(R.id.list_view)
        val searchBar: EditText = findViewById(R.id.search_bar)
        val fabAdd: FloatingActionButton = findViewById(R.id.fab_add)

        adapter = ItemAdapter(
            this,
            emptyList(),
            onItemClick = { position, task -> showEditTaskDialog(position, task) },
            onDeleteClick = { position -> showDeleteConfirmationDialog(position) },
            onFinishClick = { position -> viewModel.markCompleted(position) }
        )

        listView.adapter = adapter

        viewModel.filteredItems.observe(this, Observer { items ->
            adapter.updateItems(items)
        })

        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setSearchQuery(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        fabAdd.setOnClickListener {
            showAddTaskDialog()
        }

        viewModel.updateFilteredItems()
    }

    private fun showAddTaskDialog() {
        val input = EditText(this)
        input.hint = "Digite a descrição da tarefa"

        val dialog = AlertDialog.Builder(this)
            .setTitle("Adicionar Tarefa")
            .setMessage("Insira a descrição da tarefa:")
            .setView(input)
            .setPositiveButton("Salvar") { _, _ ->
                val description = input.text.toString()
                if (description.isNotBlank()) {
                    viewModel.saveTaskDescription(description) // Salva a descrição no ViewModel
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }


    private fun showEditTaskDialog(position: Int, task: Task) {
        val input = EditText(this)
        input.setText(task.description)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Editar Tarefa")
            .setMessage("Atualize a descrição da tarefa:")
            .setView(input)
            .setPositiveButton("Salvar") { _, _ ->
                val newDescription = input.text.toString()
                if (newDescription.isNotBlank()) {
                    viewModel.updateItem(position, newDescription)
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()

        dialog.show()
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage("Você tem certeza de que deseja excluir esta tarefa?")
            .setPositiveButton("Sim") { _, _ ->
                viewModel.deleteItem(position)
            }
            .setNegativeButton("Não", null)
            .create()

        dialog.show()
    }
}