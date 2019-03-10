package com.yuyakaido.android.pixel;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.view.CropImageView;
import com.yalantis.ucrop.view.UCropView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.GPUImageView;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLookupFilter;

public class PixelActivity extends AppCompatActivity implements FilterListener, EditorListener {

    private State state = new State();

    // Preview
    private UCropView uCropView;
    private GPUImageView gpuImageView;
    private GPUImageFilterGroup group;

    // Feature
    private View buttonContainer;

    // Crop
    private View cropContainer;

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
        setupSampleImage();
        setupCrop();
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

    private void setupSampleImage() {
        try {
            File sampleFile = new File(getCacheDir(), "sample.jpg");
            sampleFile.createNewFile();

            InputStream inputStream = getResources().getAssets().open("sample.jpg");
            OutputStream outputStream = new FileOutputStream(sampleFile);
            byte buffer[] = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();

            File inputFile = new File(getCacheDir(), "sample.jpg");
            File outputFile = new File(getCacheDir(), System.currentTimeMillis() + ".jpg");
            outputFile.createNewFile();

            inputStream = new FileInputStream(inputFile);
            outputStream = new FileOutputStream(outputFile);
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();

            state.setInputUri(Uri.fromFile(inputFile));
            state.setOutputUri(Uri.fromFile(outputFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupCrop() {
        try {
            uCropView = findViewById(R.id.ucrop_view);
            uCropView.getCropImageView().setRotateEnabled(false);
            uCropView.getCropImageView().setPadding(0, 0, 0, 0);
            uCropView.getCropImageView().setTargetAspectRatio(CropImageView.SOURCE_IMAGE_ASPECT_RATIO);
            uCropView.getCropImageView().setImageUri(state.getInputUri(), state.getOutputUri());
            uCropView.getOverlayView().setPadding(0, 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cropContainer = findViewById(R.id.crop_container);
        Button cancelButton = cropContainer.findViewById(R.id.crop_cancel_button);
        Button doneButton = cropContainer.findViewById(R.id.crop_done_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(Feature.Filter);
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uCropView.getCropImageView().cropAndSaveImage(
                        Bitmap.CompressFormat.PNG,
                        100,
                        new BitmapCropCallback() {
                            @Override
                            public void onBitmapCropped(@NonNull Uri resultUri, int offsetX, int offsetY, int imageWidth, int imageHeight) {
                                Picasso.get().invalidate(resultUri);
                                state.setOutputUri(resultUri);
                                gpuImageView.setImage(resultUri);
                                filterAdapter.notifyDataSetChanged();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toggle(Feature.Filter);
                                    }
                                }, 200);
                            }
                            @Override
                            public void onCropFailure(@NonNull Throwable t) {
                                t.printStackTrace();
                            }
                        }
                );
            }
        });
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
        gpuImageView.setImage(state.getInputUri());
        gpuImageView.setRenderMode(GPUImageView.RENDERMODE_CONTINUOUSLY);
        gpuImageView.setFilter(group);
    }

    private void setupButtons() {
        buttonContainer = findViewById(R.id.button_container);
        Button cropButton = findViewById(R.id.crop_button);
        Button filterButton = findViewById(R.id.filter_button);
        Button editorButton = findViewById(R.id.editor_button);

        toggle(Feature.Filter);

        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(Feature.Crop);
            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(Feature.Filter);
            }
        });
        editorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(Feature.Editor);
            }
        });
    }

    private void toggle(Feature feature) {
        switch (feature) {
            case Crop:
                uCropView.setVisibility(View.VISIBLE);
                gpuImageView.setVisibility(View.GONE);
                buttonContainer.setVisibility(View.GONE);
                cropContainer.setVisibility(View.VISIBLE);
                filterContainer.setVisibility(View.GONE);
                editorContainer.setVisibility(View.GONE);
                break;
            case Filter:
                uCropView.setVisibility(View.GONE);
                gpuImageView.setVisibility(View.VISIBLE);
                buttonContainer.setVisibility(View.VISIBLE);
                cropContainer.setVisibility(View.GONE);
                filterContainer.setVisibility(View.VISIBLE);
                editorContainer.setVisibility(View.GONE);
                break;
            case Editor:
                uCropView.setVisibility(View.GONE);
                gpuImageView.setVisibility(View.VISIBLE);
                buttonContainer.setVisibility(View.VISIBLE);
                cropContainer.setVisibility(View.GONE);
                filterContainer.setVisibility(View.GONE);
                editorContainer.setVisibility(View.VISIBLE);
                break;
        }
    }

}
