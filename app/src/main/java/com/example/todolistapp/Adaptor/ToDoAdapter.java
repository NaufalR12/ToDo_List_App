package com.example.todolistapp.Adaptor;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp.AddNewTask;
import com.example.todolistapp.MainActivity;
import com.example.todolistapp.Model.ToDoModel;
import com.example.todolistapp.R;
import com.example.todolistapp.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    // Deklarasi variabel
    private List<ToDoModel> todoList;  // Menyimpan daftar to-do tasks
    private DatabaseHandler db;  // Mengelola database untuk tugas to-do
    private MainActivity activity;  // Referensi ke activity utama

    // Konstruktor untuk adapter yang menerima database dan activity sebagai parameter
    public ToDoAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    // Method untuk membuat ViewHolder dan menginflate layout dari task_layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate layout task_layout untuk setiap item dalam RecyclerView
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);  // Kembalikan ViewHolder yang sudah di-inflate
    }

    // Method untuk mengikat data dengan view (CheckBox) pada posisi tertentu
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();  // Buka koneksi ke database

        final ToDoModel item = todoList.get(position);  // Ambil item dari list berdasarkan posisi
        holder.task.setText(item.getTask());  // Atur teks di CheckBox sesuai dengan tugas
        holder.task.setChecked(toBoolean(item.getStatus()));  // Atur status CheckBox berdasarkan status tugas (1 untuk true, 0 untuk false)

        // Tambahkan listener untuk mengubah status ketika CheckBox diubah
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);  // Jika di-check, update status menjadi 1 (selesai)
                } else {
                    db.updateStatus(item.getId(), 0);  // Jika di-uncheck, update status menjadi 0 (belum selesai)
                }
            }
        });
    }

    // Method untuk mengkonversi status integer menjadi boolean
    private boolean toBoolean(int n) {
        return n != 0;  // Jika n tidak sama dengan 0, return true (status selesai)
    }

    // Method untuk mendapatkan jumlah item dalam todoList
    @Override
    public int getItemCount() {
        return todoList.size();
    }

    // Method untuk mendapatkan context dari activity
    public Context getContext() {
        return activity;
    }

    // Method untuk mengatur data baru ke dalam todoList dan refresh tampilan RecyclerView
    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;  // Atur data baru ke todoList
        notifyDataSetChanged();  // Beritahu adapter bahwa data telah berubah
    }

    // Method untuk menghapus item dari todoList dan update RecyclerView
    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);  // Ambil item yang akan dihapus berdasarkan posisi
        db.deleteTask(item.getId());  // Hapus item dari database berdasarkan ID
        todoList.remove(position);  // Hapus item dari list
        notifyItemRemoved(position);  // Beritahu RecyclerView bahwa item telah dihapus
    }

    // Method untuk mengedit item dalam todoList
    public void editItem(int position) {
        ToDoModel item = todoList.get(position);  // Ambil item yang akan diedit berdasarkan posisi
        Bundle bundle = new Bundle();  // Buat Bundle untuk mengirim data ke fragment
        bundle.putInt("id", item.getId());  // Masukkan ID tugas ke dalam bundle
        bundle.putString("task", item.getTask());  // Masukkan teks tugas ke dalam bundle

        // Buat fragment AddNewTask dan set argument bundle yang berisi data tugas
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);

        // Tampilkan fragment untuk mengedit tugas
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    // Class ViewHolder untuk menghubungkan komponen view dengan data
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;  // Deklarasi CheckBox untuk setiap item

        // Konstruktor untuk ViewHolder, menghubungkan CheckBox dari layout dengan objek Java
        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);  // Hubungkan CheckBox dari layout task_layout
        }
    }
}
