package com.yuyakaido.android.pixel;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SeekBar;

import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;

public class PixelActivity extends AppCompatActivity implements FilterListener {

    private List<Filter> filters = Filter.newFilters();
    private Filter filter = filters.get(0);
    private GPUImageFilterGroup group = new GPUImageFilterGroup();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixel);
        setupRecyclerView();
        setupGPUImageView();
        setupSeekBar();
    }

    @Override
    public void selectFilter(Filter filter) {
        if (this.filter == filter) {
            return;
        }

        this.filter = filter;
        if (!group.getFilters().contains(filter.getFilter())) {
            group.addFilter(filter.getFilter());
        }
        GPUImageView gpuImageView = findViewById(R.id.gpu_image_view);
        gpuImageView.setFilter(group);

        SeekBar seekBar = findViewById(R.id.seek_bar);
        seekBar.setProgress(filter.getPercentage());
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(new FilterAdapter(recyclerView.getContext(), filters, this));
    }

    private void setupGPUImageView() {
        group.addFilter(filter.getFilter());

        GPUImageView gpuImageView = findViewById(R.id.gpu_image_view);
        gpuImageView.setImage(Uri.parse("file:///android_asset/sample.jpg"));
        gpuImageView.setRenderMode(GPUImageView.RENDERMODE_CONTINUOUSLY);
        gpuImageView.setFilter(group);
    }

    private void setupSeekBar() {
        SeekBar seekBar = findViewById(R.id.seek_bar);
        seekBar.setProgress(50);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    filter.apply(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

}
