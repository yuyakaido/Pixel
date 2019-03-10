package com.yuyakaido.android.pixel;

import android.graphics.Bitmap;
import android.graphics.Matrix;
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
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.callback.BitmapCropCallback;
import com.yalantis.ucrop.view.CropImageView;
import com.yalantis.ucrop.view.GestureCropImageView;
import com.yalantis.ucrop.view.OverlayView;
import com.yalantis.ucrop.view.TransformImageView;
import com.yalantis.ucrop.view.UCropView;
import com.yalantis.ucrop.view.widget.HorizontalProgressWheelView;

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

    // Adjust
    private View adjustContainer;

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
        setupAdjust();
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

    private void setupAdjust() {
        uCropView = findViewById(R.id.ucrop_view);
        final GestureCropImageView cropImageView = uCropView.getCropImageView();
        final OverlayView overlayView = uCropView.getOverlayView();
        adjustContainer = findViewById(R.id.adjust_container);
        final TextView rotationAngle = adjustContainer.findViewById(R.id.rotation_angle);
        View rotationCancel = adjustContainer.findViewById(R.id.rotation_cancel);
        View rotationLeft = adjustContainer.findViewById(R.id.rotation_left);
        HorizontalProgressWheelView rotationWheel = adjustContainer.findViewById(R.id.rotation_wheel);
        Button cancelButton = adjustContainer.findViewById(R.id.adjust_cancel);
        Button doneButton = adjustContainer.findViewById(R.id.adjust_done);

        rotationAngle.setText(getString(R.string.angle, 0));

        cropImageView.setTransformImageListener(new TransformImageView.TransformImageListener() {
            @Override
            public void onLoadComplete() {
                rotationAngle.setText(getString(R.string.angle, 0));
            }
            @Override
            public void onLoadFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
            @Override
            public void onRotate(float currentAngle) {
                rotationAngle.setText(getString(R.string.angle, (int) currentAngle));
            }
            @Override
            public void onScale(float currentScale) {}
        });
        rotationCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotationAngle.setText(getString(R.string.angle, 0));
                cropImageView.setImageMatrix(new Matrix());
                cropImageView.setImageToWrapCropBounds();
            }
        });
        rotationLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.postRotate(-90);
                cropImageView.setImageToWrapCropBounds();
            }
        });
        rotationWheel.setScrollingListener(new HorizontalProgressWheelView.ScrollingListener() {
            @Override
            public void onScrollStart() {
                cropImageView.cancelAllAnimations();
            }
            @Override
            public void onScroll(float delta, float totalDistance) {
                int sensitivity = 50;
                cropImageView.postRotate(delta / sensitivity);
            }
            @Override
            public void onScrollEnd() {
                cropImageView.setImageToWrapCropBounds();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(Feature.Filter);
                cropImageView.setImageMatrix(new Matrix());
                rotationAngle.setText(getString(R.string.angle, 0));
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.cropAndSaveImage(
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
                                        try {
                                            cropImageView.setImageUri(state.getInputUri(), state.getOutputUri());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
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

        try {
            cropImageView.setRotateEnabled(false);
            cropImageView.setPadding(0, 0, 0, 0);
            cropImageView.setTargetAspectRatio(CropImageView.SOURCE_IMAGE_ASPECT_RATIO);
            cropImageView.setImageUri(state.getInputUri(), state.getOutputUri());
            overlayView.setPadding(0, 0, 0, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        Button adjustButton = findViewById(R.id.adjust_button);
        Button filterButton = findViewById(R.id.filter_button);
        Button editorButton = findViewById(R.id.editor_button);

        toggle(Feature.Filter);

        adjustButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle(Feature.Adjust);
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
            case Adjust:
                uCropView.setVisibility(View.VISIBLE);
                gpuImageView.setVisibility(View.GONE);
                buttonContainer.setVisibility(View.GONE);
                adjustContainer.setVisibility(View.VISIBLE);
                filterContainer.setVisibility(View.GONE);
                editorContainer.setVisibility(View.GONE);
                break;
            case Filter:
                uCropView.setVisibility(View.GONE);
                gpuImageView.setVisibility(View.VISIBLE);
                buttonContainer.setVisibility(View.VISIBLE);
                adjustContainer.setVisibility(View.GONE);
                filterContainer.setVisibility(View.VISIBLE);
                editorContainer.setVisibility(View.GONE);
                break;
            case Editor:
                uCropView.setVisibility(View.GONE);
                gpuImageView.setVisibility(View.VISIBLE);
                buttonContainer.setVisibility(View.VISIBLE);
                adjustContainer.setVisibility(View.GONE);
                filterContainer.setVisibility(View.GONE);
                editorContainer.setVisibility(View.VISIBLE);
                break;
        }
    }

}
