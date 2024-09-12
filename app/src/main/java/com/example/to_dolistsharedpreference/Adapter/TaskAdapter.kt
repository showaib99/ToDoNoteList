package com.example.to_dolistsharedpreference.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolistsharedpreference.Data.Task
import com.example.to_dolistsharedpreference.databinding.ListItemBinding

class TaskAdapter(private val taskList:MutableList<Task>, private val clicklisten:TaskClickLister):
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    interface TaskClickLister {
        fun onEditClick(position: Int)
        fun onDeleteClick(position: Int)
    }
    class TaskViewHolder(val binding: ListItemBinding):RecyclerView.ViewHolder(binding.root){
            fun bind(task: Task){
                binding.textView.text = task.title
            }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskAdapter.TaskViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return taskList.size

    }

    override fun onBindViewHolder(holder: TaskAdapter.TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.bind(task)
        holder.binding.editBtn.setOnClickListener {
            clicklisten.onEditClick(position)
        }
        holder.binding.deleteBtn.setOnClickListener {
            clicklisten.onDeleteClick(position)
        }
        holder.binding.checkbox.isChecked = task.IsCompleted

        holder.binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            task.IsCompleted = isChecked
        }


    }
    }


