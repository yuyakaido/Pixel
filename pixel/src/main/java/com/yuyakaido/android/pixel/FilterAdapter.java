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

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private final Context context;
    private final List<Filter> filters;
    private final FilterListener listener;

    FilterAdapter(Context context, List<Filter> filters, FilterListener listener) {
        this.context = context;
        this.filters = filters;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Filter filter = filters.get(position);
        holder.name.setText(filter.getNameResourceId());
        holder.icon.setImageResource(filter.getIconResourceId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectFilter(filter);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filters.size();
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
