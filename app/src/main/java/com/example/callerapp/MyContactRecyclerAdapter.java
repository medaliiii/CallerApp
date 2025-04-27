package com.example.callerapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MyContactRecyclerAdapter extends RecyclerView.Adapter<MyContactRecyclerAdapter.MyViewHolder> {
    private static final int GALLERY_REQUEST_CODE = 1001;
    private Context context;
    private ArrayList<Contact> contacts;
    private ContactManager contactManager;
    private int currentContactPosition = -1;

    public MyContactRecyclerAdapter(Context context, ArrayList<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
        this.contactManager = new ContactManager(context);
        this.contactManager.ouvrir();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_contact, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.bind(contact);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void updateContacts(ArrayList<Contact> newContacts) {
        this.contacts = newContacts;
        notifyDataSetChanged();
    }

    public void handleGalleryResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null && currentContactPosition != RecyclerView.NO_POSITION) {
                Contact contact = contacts.get(currentContactPosition);
                contact.setLocalPhotoUri(selectedImageUri.toString());
                contactManager.updateContactPhoto(contact.getId(), selectedImageUri.toString());
                notifyItemChanged(currentContactPosition);
            }
        }
    }

    public void openGalleryForCurrentContact() {
        if (currentContactPosition != RecyclerView.NO_POSITION) {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            ((Activity) context).startActivityForResult(intent, GALLERY_REQUEST_CODE);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvNumber, tvPseudo;
        private ImageView ivCall, ivUpdate, ivDelete, ivAvatar, ivChangePhoto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvnom_contact);
            tvPseudo = itemView.findViewById(R.id.tvprenom_contact);
            tvNumber = itemView.findViewById(R.id.tvnum_contact);
            ivCall = itemView.findViewById(R.id.imageviewcall_contact);
            ivUpdate = itemView.findViewById(R.id.imageviewupdate_contact);
            ivDelete = itemView.findViewById(R.id.imageviewdelete_contact);
            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            ivChangePhoto = itemView.findViewById(R.id.iv_change_photo);

            setupClickListeners();
        }

        public void bind(Contact contact) {
            tvName.setText(contact.getNom());
            tvPseudo.setText(contact.getPseudo());
            tvNumber.setText(contact.getNum());
            loadAvatar(contact.getAvatarUrl());

            if (contact.getLocalPhotoUri() != null && !contact.getLocalPhotoUri().isEmpty()) {
                Picasso.get()
                        .load(Uri.parse(contact.getLocalPhotoUri()))
                        .placeholder(R.drawable.ic_default_avatar)
                        .error(R.drawable.ic_default_avatar)
                        .into(ivChangePhoto);  // Charger dans iv_change_photo au lieu de iv_avatar
            } else {
                ivChangePhoto.setImageResource(R.drawable.ic_camera); // Icône caméra par défaut
            }
        }

        private void setupClickListeners() {
            ivCall.setOnClickListener(v -> makePhoneCall());
            ivUpdate.setOnClickListener(v -> showUpdateDialog());
            ivDelete.setOnClickListener(v -> showDeleteConfirmation());
            ivChangePhoto.setOnClickListener(v -> {
                currentContactPosition = getAdapterPosition();
                if (currentContactPosition != RecyclerView.NO_POSITION) {
                    checkGalleryPermission();
                }
            });
        }

        private void checkGalleryPermission() {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        GALLERY_REQUEST_CODE);
            } else {
                openGalleryForCurrentContact();
            }
        }

        private void loadAvatar(String avatarUrl) {
            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                Picasso.get()
                        .load(avatarUrl)
                        .placeholder(R.drawable.ic_default_avatar)
                        .error(R.drawable.ic_default_avatar)
                        .into(ivAvatar);
            } else {
                ivAvatar.setImageResource(R.drawable.ic_default_avatar);
            }
        }

        private void loadFallbackAvatar() {
            try {
                String fallbackUrl = "https://ui-avatars.com/api/" +
                        "?name=" + URLEncoder.encode(tvName.getText().toString(), "UTF-8") +
                        "&background=E91E63" +
                        "&color=fff" +
                        "&size=100";

                Picasso.get()
                        .load(fallbackUrl)
                        .placeholder(R.drawable.ic_default_avatar)
                        .error(R.drawable.ic_default_avatar)
                        .into(ivAvatar);  // Toujours charger dans iv_avatar
            } catch (UnsupportedEncodingException e) {
                Log.e("Avatar", "Erreur d'encodage", e);
                ivAvatar.setImageResource(R.drawable.ic_default_avatar);
            }
        }

        private void makePhoneCall() {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                String phoneNumber = contacts.get(position).getNum();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                context.startActivity(intent);
            }
        }

        private void showUpdateDialog() {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            Contact contact = contacts.get(position);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View dialogView = LayoutInflater.from(context).inflate(R.layout.view_dialogue, null);

            EditText etName = dialogView.findViewById(R.id.et_nom);
            EditText etPseudo = dialogView.findViewById(R.id.et_pseudo);
            EditText etNumber = dialogView.findViewById(R.id.et_num);
            Button btnCancel = dialogView.findViewById(R.id.button);
            Button btnSave = dialogView.findViewById(R.id.btn_save);

            etName.setText(contact.getNom());
            etPseudo.setText(contact.getPseudo());
            etNumber.setText(contact.getNum());

            builder.setView(dialogView);
            AlertDialog dialog = builder.create();

            btnCancel.setOnClickListener(v -> dialog.dismiss());

            btnSave.setOnClickListener(v -> {
                String newName = etName.getText().toString().trim();
                String newPseudo = etPseudo.getText().toString().trim();
                String newNumber = etNumber.getText().toString().trim();

                if (validateInputs(newName, newPseudo, newNumber)) {
                    updateContact(position, contact, newName, newPseudo, newNumber);
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        private boolean validateInputs(String name, String pseudo, String number) {
            if (name.isEmpty() || pseudo.isEmpty() || number.isEmpty()) {
                Toast.makeText(context, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }

        private void updateContact(int position, Contact contact, String newName, String newPseudo, String newNumber) {
            contactManager.modifierContact(contact.getId(), newName, newPseudo, newNumber);
            contact.setNom(newName);
            contact.setPseudo(newPseudo);
            contact.setNum(newNumber);
            notifyItemChanged(position);
            Toast.makeText(context, "Contact mis à jour", Toast.LENGTH_SHORT).show();
        }

        private void showDeleteConfirmation() {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) return;

            new AlertDialog.Builder(context)
                    .setTitle("Confirmation")
                    .setMessage("Êtes-vous sûr de vouloir supprimer ce contact?")
                    .setPositiveButton("Oui", (dialog, which) -> deleteContact(position))
                    .setNegativeButton("Non", null)
                    .show();
        }

        private void deleteContact(int position) {
            Contact contact = contacts.get(position);
            contactManager.supprimerContact(contact.getId());
            contacts.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, contacts.size());
            Toast.makeText(context, "Contact supprimé", Toast.LENGTH_SHORT).show();
        }
    }
}