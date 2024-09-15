package com.example.todolistapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolistapp.Adaptor.ToDoAdapter;

/**
 * Kelas ini menangani interaksi swipe pada item dalam RecyclerView.
 *
 * Dengan menggunakan ItemTouchHelper, kita dapat menambahkan fitur swipe ke kanan atau kiri pada item
 * dalam RecyclerView untuk melakukan aksi tertentu seperti menghapus, edit item.
 */
public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private ToDoAdapter adapter; // Adapter yang digunakan untuk mengelola item dalam RecyclerView

    /**
     * Konstruktor untuk RecyclerItemTouchHelper.
     *
     * @param adapter Adapter yang digunakan oleh RecyclerView.
     */
    public RecyclerItemTouchHelper(ToDoAdapter adapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT); // Mendukung gesekan ke kiri dan kanan
        this.adapter = adapter;
    }

    /**
     * Metode ini tidak digunakan dalam implementasi ini, sehingga selalu mengembalikan false.
     *
     * @param recyclerView RecyclerView yang berisi item.
     * @param viewHolder ViewHolder item yang digerakkan.
     * @param target ViewHolder target tempat item yang digerakkan akan dipindahkan.
     * @return false karena kita tidak mendukung pemindahan item.
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    /**
     * Metode ini dipanggil ketika item di-swipe. Bergantung pada arah gesekan, kita
     * dapat melakukan tindakan seperti menghapus atau mengedit item.
     *
     * @param viewHolder ViewHolder item yang di-swipe.
     * @param direction Arah gesekan (kiri atau kanan).
     */
    @Override
    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition(); // Mendapatkan posisi item yang di-swipe
        if (direction == ItemTouchHelper.LEFT) { // Jika gesekan ke kiri
            // Menampilkan dialog konfirmasi untuk menghapus item
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you sure you want to delete this Task?");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteItem(position); // Menghapus item dari adapter
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition()); // Mengembalikan item jika aksi dibatalkan
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            // Mengedit item jika gesekan ke kanan
            adapter.editItem(position);
        }
    }

    /**
     * Metode ini menggambar background dan ikon pada item saat di-swipe.
     *
     * @param c Canvas yang digunakan untuk menggambar.
     * @param recyclerView RecyclerView yang berisi item.
     * @param viewHolder ViewHolder item yang digerakkan.
     * @param dX Perubahan posisi horizontal item saat di-swipe.
     * @param dY Perubahan posisi vertikal item saat di-swipe (tidak digunakan dalam implementasi ini).
     * @param actionState Status aksi saat ini (swipe atau drag).
     * @param isCurrentlyActive Apakah aksi saat ini sedang aktif.
     */
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;

        View itemView = viewHolder.itemView;
        int backgroundCornerOffset = 20; // Offset sudut background

        if (dX > 0) { // Jika gesekan ke kanan
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_edit);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(), com.google.android.material.R.color.design_default_color_primary_dark));
        } else { // Jika gesekan ke kiri
            icon = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_baseline_delete);
            background = new ColorDrawable(Color.RED);
        }

        assert icon != null;
        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
        int iconBottom = iconTop + icon.getIntrinsicHeight();

        if (dX > 0) { // Swiping to the right
            int iconLeft = itemView.getLeft() + iconMargin;
            int iconRight = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getLeft(), itemView.getTop(),
                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
        } else if (dX < 0) { // Swiping to the left
            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
            int iconRight = itemView.getRight() - iconMargin;
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0);
        }

        background.draw(c); // Menggambar background
        icon.draw(c); // Menggambar ikon
    }
}
