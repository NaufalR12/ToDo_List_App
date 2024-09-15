package com.example.todolistapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todolistapp.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

// DatabaseHandler adalah class untuk mengelola operasi database SQLite, seperti membuat, mengupdate, menghapus data.
public class DatabaseHandler extends SQLiteOpenHelper {

    // Konstanta untuk nama database, versi, nama tabel, dan kolom-kolom tabel
    private static final int VERSION = 1;  // Versi database
    private static final String NAME = "toDoListDatabase";  // Nama database
    private static final String TODO_TABLE = "todo";  // Nama tabel yang digunakan
    private static final String ID = "id";  // Kolom untuk ID tugas
    private static final String TASK = "task";  // Kolom untuk deskripsi tugas
    private static final String STATUS = "status";  // Kolom untuk status tugas (selesai atau belum)

    // Query untuk membuat tabel todo di database
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "("
            + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "  // ID autoincrement
            + TASK + " TEXT, "  // Kolom untuk menyimpan deskripsi tugas
            + STATUS + " INTEGER)";  // Kolom untuk menyimpan status tugas

    // Objek SQLiteDatabase yang digunakan untuk melakukan operasi database
    private SQLiteDatabase db;

    // Konstruktor untuk DatabaseHandler, memanggil superclass SQLiteOpenHelper dengan parameter context, nama database, dan versinya
    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    // Method yang dipanggil ketika database pertama kali dibuat
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Eksekusi query untuk membuat tabel
        db.execSQL(CREATE_TODO_TABLE);
    }

    // Method yang dipanggil ketika database diupgrade (misalnya versi database berubah)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop (hapus) tabel yang ada jika sudah ada versi lama
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Buat tabel baru
        onCreate(db);
    }

    // Method untuk membuka database agar bisa ditulis (writable)
    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    // Method untuk menambahkan tugas baru ke dalam tabel todo
    public void insertTask(ToDoModel task) {
        ContentValues cv = new ContentValues();  // Digunakan untuk menyimpan pasangan key-value
        cv.put(TASK, task.getTask());  // Menambahkan task (deskripsi tugas) ke dalam ContentValues
        cv.put(STATUS, 0);  // Status default adalah 0 (belum selesai)
        db.insert(TODO_TABLE, null, cv);  // Masukkan data ke dalam tabel todo
    }

    // Method untuk mengambil semua tugas dari tabel todo
    public List<ToDoModel> getAllTasks() {
        List<ToDoModel> taskList = new ArrayList<>();  // List untuk menyimpan semua tugas
        Cursor cur = null;  // Cursor digunakan untuk menavigasi hasil query
        db.beginTransaction();  // Mulai transaksi database
        try {
            // Query untuk mendapatkan semua data dari tabel todo
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        // Buat objek ToDoModel untuk setiap baris hasil query
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));  // Set ID dari database
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));  // Set task (deskripsi)
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));  // Set status tugas
                        taskList.add(task);  // Tambahkan tugas ke dalam list
                    }
                    while (cur.moveToNext());  // Ulangi sampai semua baris selesai diproses
                }
            }
        } finally {
            db.endTransaction();  // Akhiri transaksi database
            assert cur != null;
            cur.close();  // Tutup cursor setelah selesai
        }
        return taskList;  // Kembalikan list tugas
    }

    // Method untuk mengupdate status tugas berdasarkan ID
    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);  // Ubah status tugas
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});  // Update tabel todo berdasarkan ID
    }

    // Method untuk mengupdate deskripsi tugas berdasarkan ID
    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);  // Ubah deskripsi tugas
        db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});  // Update tabel todo berdasarkan ID
    }

    // Method untuk menghapus tugas dari database berdasarkan ID
    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "= ?", new String[]{String.valueOf(id)});  // Hapus tugas dari tabel todo berdasarkan ID
    }
}
