package com.example.biblioteca;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LibroAdapter extends RecyclerView.Adapter<LibroAdapter.LibroViewHolder>{
    private ArrayList<Libro> libros;
    private OnItemClickListener listener;

    public LibroAdapter(ArrayList<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public LibroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cada_libro_lista, parent, false);
        return new LibroViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LibroViewHolder holder, int position) {
        Libro libro = libros.get(position);
        System.out.println(libro);
        holder.titulo.setText(libro.getTitulo());
        holder.autor.setText(libro.getAutor());}

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.listener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    class LibroViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, autor;
        ImageView imagen;

        LibroViewHolder(View view) {
            super(view);
            imagen = view.findViewById(R.id.image_libro);
            titulo = view.findViewById(R.id.titulo_libro);
            autor = view.findViewById(R.id.autor_libro);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    System.out.println(position);
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
