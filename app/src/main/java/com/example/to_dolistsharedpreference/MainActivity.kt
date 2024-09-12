package com.example.to_dolistsharedpreference

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolistsharedpreference.Adapter.TaskAdapter
import com.example.to_dolistsharedpreference.Data.Task

class MainActivity : AppCompatActivity() {
    private lateinit var taskList: MutableList<Task>
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editTaskEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("tasks", Context.MODE_PRIVATE)

        recyclerView = findViewById(R.id.recyclerview)

        editTaskEditText = findViewById(R.id.edittaskEt)

        taskList = retriveTask()



        val saveButton: Button = findViewById(R.id.saveBtn)

        saveButton.setOnClickListener{
            val taskText = editTaskEditText.text.toString()
            if(taskText.isNotEmpty()){
                val task = Task(taskText, false)
                taskList.add(task)
                saveTasks(taskList)
                taskAdapter.notifyItemInserted(taskList.size-1)
                editTaskEditText.text.clear()
            }
            else{
                Toast.makeText(this, "Task title is empty.", Toast.LENGTH_SHORT).show()
            }
        }

        taskAdapter = TaskAdapter(taskList, object:TaskAdapter.TaskClickLister{
            override fun onEditClick(position: Int) {
                editTaskEditText.setText(taskList[position].title)
                taskList.removeAt(position)
                taskAdapter.notifyDataSetChanged()
            }

            override fun onDeleteClick(position: Int) {
                val alertDialog = AlertDialog.Builder(this@MainActivity)
                alertDialog.setTitle("Delete Task")
                alertDialog.setMessage("Are you sure to want to delete this message?")
                alertDialog.setPositiveButton("Yes") {_, _ ->
                    deleteTask(position)
                }

                alertDialog.setNegativeButton("No"){_, _ -> }
                alertDialog.show()
            }
        })

        recyclerView.adapter = taskAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun saveTasks(taskList: MutableList<Task>) {
        val editor = sharedPreferences.edit()
        val taskset = HashSet<String>()

        taskList.forEach{taskset.add(it.title)}
        editor.putStringSet("tasks", taskset)
        editor.apply()

    }

    private fun deleteTask(position: Int) {
        taskList.removeAt(position)
        taskAdapter.notifyItemRemoved(position)
        saveTasks(taskList)
    }

    private fun retriveTask(): MutableList<Task> {

        val tasks = sharedPreferences.getStringSet("tasks", HashSet()) ?:HashSet()
        return tasks.map { Task(it,false)}.toMutableList()
    }
}