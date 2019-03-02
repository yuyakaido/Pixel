package com.yuyakaido.android.pixel;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLookupFilter;

public class PixelActivity extends AppCompatActivity implements FilterListener, EditorListener {

    private State state = new State();

    // Preview
    private GPUImageView gpuImageView;
    private GPUImageFilterGroup group;

    // Filter
    private View filterContainer;
    private FilterAdapter filterAdapter;

    // Editor
    private View editorContainer;
    private EditorAdapter editorAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixel);
        setupFilter();
        setupEditor();
        setupGPUImageView();
        setupButtons();
    }

    @Override
    public void selectFilter(Filter filter) {
        if (state.getFilter() == filter) {
            return;
        } else {
            state.setFilter(filter);
        }

        List<GPUImageFilter> oldFilters = group.getFilters();
        List<GPUImageFilter> newFilters = new ArrayList<>();
        for (GPUImageFilter oldFilter : oldFilters) {
            if (!(oldFilter instanceof GPUImageLookupFilter)) {
                newFilters.add(oldFilter);
            }
        }
        newFilters.add(filter.getLookupFilter(this));
        this.group = new GPUImageFilterGroup(newFilters);

        gpuImageView.setFilter(group);

        filterAdapter.notifyDataSetChanged();
    }

    @Override
    public void selectEditor(Editor editor) {
        if (state.getEditor() == editor) {
            return;
        } else {
            state.setEditor(editor);
        }

        if (!group.getFilters().contains(editor.getFilter())) {
            group.addFilter(editor.getFilter());
        }

        gpuImageView.setFilter(group);

        SeekBar seekBar = findViewById(R.id.seek_bar);
        seekBar.setProgress(editor.getCurrentPercentage());

        editorAdapter.notifyDataSetChanged();
    }

    private void setupFilter() {
        filterContainer = findViewById(R.id.filter_container);
        filterAdapter = new FilterAdapter(filterContainer.getContext(), state, this);

        RecyclerView recyclerView = filterContainer.findViewById(R.id.filter_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(filterAdapter);
    }

    private void setupEditor() {
        editorContainer = findViewById(R.id.editor_container);
        editorAdapter = new EditorAdapter(editorContainer.getContext(), state, this);

        RecyclerView recyclerView = editorContainer.findViewById(R.id.editor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(editorAdapter);

        SeekBar seekBar = editorContainer.findViewById(R.id.seek_bar);
        seekBar.setProgress(50);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    state.getEditor().apply(progress);
                    editorAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void setupGPUImageView() {
        group = new GPUImageFilterGroup();
        group.addFilter(state.getFilter().getLookupFilter(this));
        group.addFilter(state.getEditor().getFilter());

        gpuImageView = findViewById(R.id.gpu_image_view);
        gpuImageView.setImage(Uri.parse("file:///android_asset/sample.jpg"));
        gpuImageView.setRenderMode(GPUImageView.RENDERMODE_CONTINUOUSLY);
        gpuImageView.setFilter(group);
    }

    private void setupButtons() {
        Button filterButton = findViewById(R.id.filter_button);
        Button editorButton = findViewById(R.id.editor_button);

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterContainer.setVisibility(View.VISIBLE);
                editorContainer.setVisibility(View.GONE);
            }
        });
        editorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterContainer.setVisibility(View.GONE);
                editorContainer.setVisibility(View.VISIBLE);
            }
        });
    }

}
