package com.example.todolistapp;

import android.content.DialogInterface;

/**
 * Interface yang digunakan untuk menangani penutupan dialog.
 *
 * Interface ini mendefinisikan metode `handleDialogClose` yang harus diimplementasikan oleh kelas
 * yang mengimplementasikan interface ini. Metode ini akan dipanggil ketika dialog ditutup.
 */
public interface DialogCloseListener {
    /**
     * Metode ini akan dipanggil ketika dialog ditutup.
     *
     * @param dialog Objek DialogInterface yang mewakili dialog yang ditutup.
     */
    public void handleDialogClose(DialogInterface dialog);
}
