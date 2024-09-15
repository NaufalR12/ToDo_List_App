package com.example.todolistapp.Model;

// Kelas ToDoModel merepresentasikan sebuah model data untuk task dalam aplikasi to-do list
public class ToDoModel {

    // Deklarasi variabel instance (atribut) id, status, dan task
    private int id, status;  // 'id' untuk identifikasi task, 'status' untuk status task (selesai atau belum)
    private String task;  // 'task' untuk menyimpan deskripsi atau nama tugas

    // Getter untuk mengambil nilai id dari task
    public int getId() {
        return id;
    }

    // Setter untuk mengatur nilai id dari task
    public void setId(int id) {
        this.id = id;
    }

    // Getter untuk mengambil nilai status dari task
    public int getStatus() {
        return status;
    }

    // Setter untuk mengatur nilai status dari task (1 untuk selesai, 0 untuk belum selesai)
    public void setStatus(int status) {
        this.status = status;
    }

    // Getter untuk mengambil teks tugas (task description)
    public String getTask() {
        return task;
    }

    // Setter untuk mengatur teks tugas (task description)
    public void setTask(String task) {
        this.task = task;
    }
}
