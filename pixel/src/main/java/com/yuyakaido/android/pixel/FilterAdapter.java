package com.yuyakaido.android.pixel;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jp.co.cyberagent.android.gpuimage.GPUImageView;

class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private final Context context;
    private final State state;
    private final FilterListener listener;

    FilterAdapter(Context context, State state, FilterListener listener) {
        this.context = context;
        this.state = state;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_filter, parent, false));
    }

    @Override
    public void onBindViewHolder(final @NonNull ViewHolder holder, int position) {
        final Filter filter = state.getFilters().get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectFilter(filter);
            }
        });
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        Filter filter = state.getFilters().get(holder.getAdapterPosition());
        holder.preview.getGPUImage().deleteImage();
        state.remoteFilterListener(filter);
    }

    @Override
    public void onViewAttachedToWindow(final @NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        final Filter filter = state.getFilters().get(holder.getAdapterPosition());
        holder.preview.setImage(Uri.parse("file:///android_asset/sample.jpg"));
        holder.preview.setFilter(filter.getLookupFilter(context));
        state.addFilterListener(filter, new State.FilterListener() {
            @Override
            public void onFilterSelect() {
                if (state.isDirty(filter)) {
                    holder.isDirty.setVisibility(View.VISIBLE);
                } else {
                    holder.isDirty.setVisibility(View.GONE);
                }
            }
        });
        if (state.isDirty(filter)) {
            holder.isDirty.setVisibility(View.VISIBLE);
        } else {
            holder.isDirty.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return state.getFilters().size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        GPUImageView preview;
        View isDirty;
        ViewHolder(View view) {
            super(view);
            this.preview = view.findViewById(R.id.preview);
            this.isDirty = view.findViewById(R.id.is_dirty);
        }
    }

}
