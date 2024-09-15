package com.example.todolistapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.todolistapp.Model.ToDoModel;
import com.example.todolistapp.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

// AddNewTask adalah class untuk menampilkan form penambahan atau pengeditan task dalam bentuk BottomSheet dialog
public class AddNewTask extends BottomSheetDialogFragment {

    // Tag untuk menandai fragment
    public static final String TAG = "ActionBottomDialog";

    // Variabel untuk menyimpan referensi ke EditText dan Button pada layout
    private EditText newTaskText;
    private Button newTaskSaveButton;

    // Objek DatabaseHandler untuk melakukan operasi pada database
    private DatabaseHandler db;

    // Listener yang digunakan untuk mengirim callback ketika task baru ditambahkan
    private TaskAddedListener taskAddedListener;

    // Method untuk membuat instance baru dari AddNewTask dengan listener
    public static AddNewTask newInstance(TaskAddedListener listener) {
        AddNewTask fragment = new AddNewTask();
        fragment.taskAddedListener = listener; // Set listener
        return fragment;
    }

    // Method ini dipanggil ketika fragment pertama kali dibuat
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set style dialog untuk BottomSheet
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    // Method ini digunakan untuk menginflate layout dari BottomSheet dialog
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout dari 'new_task' dan mengembalikan View
        View view = inflater.inflate(R.layout.new_task, container, false);
        // Set agar input keyboard menyesuaikan ukuran layar
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    // Method ini dipanggil setelah View dari fragment dibuat
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi EditText dan Button dari layout
        newTaskText = requireView().findViewById(R.id.newTaskText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);

        boolean isUpdate = false;  // Flag untuk mengecek apakah sedang dalam mode update atau insert

        // Mengecek apakah ada data yang diteruskan melalui Bundle
        final Bundle bundle = getArguments();
        if (bundle != null) {
            // Jika ada data, berarti ini mode update
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);  // Menampilkan task yang akan diupdate
            assert task != null;
            if (task.length() > 0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_primary));
        }

        // Inisialisasi DatabaseHandler dan membuka database
        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        // Tambahkan listener untuk EditText untuk memeriksa perubahan teks
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Jika teks kosong, tombol save tidak aktif dan warna berubah menjadi abu-abu
                if (s.toString().equals("")) {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                } else {
                    // Jika ada teks, tombol save aktif dan warna berubah menjadi warna utama
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(requireContext(), com.google.android.material.R.color.design_default_color_primary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Menyimpan flag isUpdate untuk digunakan di dalam listener
        final boolean finalIsUpdate = isUpdate;

        // Tambahkan listener untuk tombol save task
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();  // Ambil teks dari inputan task

                if (finalIsUpdate) {
                    // Jika ini update, panggil method updateTask dengan ID task
                    db.updateTask(bundle.getInt("id"), text);
                } else {
                    // Jika ini penambahan task baru, buat objek ToDoModel baru
                    ToDoModel task = new ToDoModel();
                    task.setTask(text);  // Set task baru
                    task.setStatus(0);  // Set status default menjadi 0 (belum selesai)
                    db.insertTask(task);  // Simpan task ke database
                }

                // Jika ada listener, panggil callback onTaskAdded setelah task ditambahkan
                if (taskAddedListener != null) {
                    taskAddedListener.onTaskAdded();
                }

                // Tutup dialog setelah task disimpan
                dismiss();
            }
        });
    }

    // Method yang dipanggil ketika dialog ditutup
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Activity activity = getActivity();
        // Jika activity mengimplementasikan DialogCloseListener, panggil method handleDialogClose
        if (activity instanceof DialogCloseListener)
            ((DialogCloseListener) activity).handleDialogClose(dialog);
    }

    // Interface TaskAddedListener digunakan untuk memberi notifikasi ke MainActivity setelah task ditambahkan
    public interface TaskAddedListener {
        void onTaskAdded();
    }
}
