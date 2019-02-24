package com.yuyakaido.android.pixel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class EditorAdapter extends RecyclerView.Adapter<EditorAdapter.ViewHolder> {

    private final Context context;
    private final List<Editor> editors;
    private final EditorListener listener;

    EditorAdapter(Context context, List<Editor> editors, EditorListener listener) {
        this.context = context;
        this.editors = editors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_editor, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Editor editor = editors.get(position);
        holder.name.setText(editor.getNameResourceId());
        holder.icon.setImageResource(editor.getIconResourceId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectEditor(editor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return editors.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;
        ViewHolder(View view) {
            super(view);
            this.name = view.findViewById(R.id.name);
            this.icon = view.findViewById(R.id.icon);
        }
    }

}
