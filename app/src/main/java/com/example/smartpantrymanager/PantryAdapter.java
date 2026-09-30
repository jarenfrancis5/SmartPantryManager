package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class PantryAdapter
        extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private final ArrayList<PantryItem> pantryItems;
    private final OnPantryItemClickListener listener;


    // Constructor
    public PantryAdapter(
            ArrayList<PantryItem> pantryItems,
            OnPantryItemClickListener listener
    ) {
        this.pantryItems = pantryItems;
        this.listener = listener;
    }


    // Creates a new ViewHolder when RecyclerView needs one
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_pantry,
                        parent,
                        false
                );

        return new PantryViewHolder(view);
    }


    // Places PantryItem data into a ViewHolder
    @Override
    public void onBindViewHolder(
            @NonNull PantryViewHolder holder,
            int position
    ) {

        PantryItem pantryItem = pantryItems.get(position);

        // Set pantry item name
        holder.nameTextView.setText(
                pantryItem.getName()
        );


        // Set quantity and unit
        String quantityText =
                "Quantity: " +
                        pantryItem.getQuantity() +
                        " " +
                        pantryItem.getUnit();

        holder.quantityTextView.setText(quantityText);


        // Set expiry date
        String expiryText =
                "Expiry Date: " +
                        pantryItem.getExpiryDate();

        holder.expiryTextView.setText(expiryText);


        // Edit button
        holder.editButton.setOnClickListener(view -> {

            int adapterPosition =
                    holder.getBindingAdapterPosition();

            if (adapterPosition
                    != RecyclerView.NO_POSITION) {

                listener.onEditClick(
                        pantryItems.get(adapterPosition)
                );
            }
        });


        // Delete button
        holder.deleteButton.setOnClickListener(view -> {

            int adapterPosition =
                    holder.getBindingAdapterPosition();

            if (adapterPosition
                    != RecyclerView.NO_POSITION) {

                listener.onDeleteClick(
                        pantryItems.get(adapterPosition)
                );
            }
        });
    }


    // Tells RecyclerView how many items exist
    @Override
    public int getItemCount() {
        return pantryItems.size();
    }


    // ViewHolder
    public static class PantryViewHolder
            extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView quantityTextView;
        TextView expiryTextView;

        MaterialButton editButton;
        MaterialButton deleteButton;


        public PantryViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            nameTextView =
                    itemView.findViewById(
                            R.id.text_pantry_name
                    );

            quantityTextView =
                    itemView.findViewById(
                            R.id.text_pantry_quantity
                    );

            expiryTextView =
                    itemView.findViewById(
                            R.id.text_pantry_expiry
                    );

            editButton =
                    itemView.findViewById(
                            R.id.button_edit_pantry
                    );

            deleteButton =
                    itemView.findViewById(
                            R.id.button_delete_pantry
                    );
        }
    }


    // Interface for Edit and Delete button clicks
    public interface OnPantryItemClickListener {

        void onEditClick(PantryItem pantryItem);

        void onDeleteClick(PantryItem pantryItem);
    }
}