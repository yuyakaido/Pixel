package com.yuyakaido.android.pixel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class EditorAdapter extends RecyclerView.Adapter<EditorAdapter.ViewHolder> {

    private final Context context;
    private final State state;
    private final EditorListener listener;

    EditorAdapter(Context context, State state, EditorListener listener) {
        this.context = context;
        this.state = state;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_editor, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Editor editor = state.getEditors().get(position);
        holder.name.setText(editor.getNameResourceId());
        holder.icon.setImageResource(editor.getIconResourceId());
        if (state.isDirty(editor)) {
            holder.isDirty.setVisibility(View.VISIBLE);
        } else {
            holder.isDirty.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectEditor(editor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return state.getEditors().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;
        View isDirty;
        ViewHolder(View view) {
            super(view);
            this.name = view.findViewById(R.id.name);
            this.icon = view.findViewById(R.id.icon);
            this.isDirty = view.findViewById(R.id.is_dirty);
        }
    }

}
