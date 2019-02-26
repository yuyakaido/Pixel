package com.yuyakaido.android.pixel;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageView;

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
        final Filter currentFilter = filters.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectFilter(currentFilter);
            }
        });
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.preview.getGPUImage().deleteImage();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        Filter filter = filters.get(holder.getAdapterPosition());
        holder.preview.setImage(Uri.parse("file:///android_asset/sample.jpg"));
        holder.preview.setFilter(filter.getLookupFilter(context));
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        GPUImageView preview;
        ViewHolder(View view) {
            super(view);
            this.preview = view.findViewById(R.id.preview);
        }
    }

}
