package com.example.todolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.todolistapp.Adaptor.ToDoAdapter;
import com.example.todolistapp.Model.ToDoModel;
import com.example.todolistapp.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Aktivitas utama dari aplikasi ToDoList.
 *
 * Aktivitas ini mengelola tampilan daftar tugas, memungkinkan pengguna untuk menambahkan
 * tugas baru melalui FloatingActionButton, dan memperbarui daftar tugas setelah penambahan.
 */
public class MainActivity extends AppCompatActivity implements AddNewTask.TaskAddedListener {

    private DatabaseHandler db; // Mengelola interaksi dengan database
    private RecyclerView tasksRecyclerView; // Menampilkan daftar tugas
    private ToDoAdapter tasksAdapter; // Adapter untuk RecyclerView
    private FloatingActionButton fab; // Tombol untuk menambahkan tugas baru

    private List<ToDoModel> taskList; // Daftar tugas yang diambil dari database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Mengatur tampilan aktivitas

        // Menyembunyikan action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Inisialisasi DatabaseHandler untuk mengelola database
        db = new DatabaseHandler(this);
        db.openDatabase();

        // Menyiapkan RecyclerView untuk menampilkan daftar tugas
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this)); // Menetapkan layout manager
        tasksAdapter = new ToDoAdapter(db, MainActivity.this); // Inisialisasi adapter
        tasksRecyclerView.setAdapter(tasksAdapter); // Mengatur adapter pada RecyclerView

        // Menambahkan ItemTouchHelper untuk mendukung penghapusan item dengan gesekan
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        // Menyediakan FloatingActionButton untuk menambahkan tugas baru
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Menampilkan dialog untuk menambahkan tugas baru
                AddNewTask addNewTask = AddNewTask.newInstance(MainActivity.this);
                addNewTask.show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        // Mengambil daftar tugas dari database dan membalik urutannya
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList); // Mengatur daftar tugas pada adapter
    }

    @Override
    public void onTaskAdded() {
        // Memperbarui daftar tugas setelah tugas baru ditambahkan
        refreshTaskList();
    }

    private void refreshTaskList() {
        // Mengambil kembali daftar tugas dari database dan membalik urutannya
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList); // Memperbarui adapter dengan daftar tugas yang baru
        tasksAdapter.notifyDataSetChanged(); // Memberitahu adapter bahwa data telah berubah
    }
}
