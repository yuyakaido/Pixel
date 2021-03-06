package com.yuyakaido.android.pixel;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHighlightShadowFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter;

abstract class Editor<T extends GPUImageFilter> {

    abstract int getNameResourceId();
    abstract int getIconResourceId();
    abstract int getDefaultPercentage();
    abstract int getCurrentPercentage();
    abstract T getFilter();
    abstract void apply(int percentage);

    float normalize(int percentage, float start, float end) {
        return (end - start) * percentage / 100.0f + start;
    }

    boolean isDirty() {
        return getCurrentPercentage() != getDefaultPercentage();
    }

    static List<Editor> newEditors() {
        return new ArrayList<Editor>() {{
            add(new Brightness());
            add(new Contrast());
            add(new Saturation());
            add(new Highlight());
            add(new Shadow());
            add(new Sharpen());
        }};
    }

    static class Brightness extends Editor<GPUImageBrightnessFilter> {
        private static final int DEFAULT = 50;
        private GPUImageBrightnessFilter filter = new GPUImageBrightnessFilter(0.0f);
        private int percentage = DEFAULT;
        @Override
        int getNameResourceId() {
            return R.string.editor_brightness;
        }
        @Override
        int getIconResourceId() {
            return R.drawable.ic_brightness;
        }
        @Override
        int getDefaultPercentage() {
            return DEFAULT;
        }
        @Override
        public int getCurrentPercentage() {
            return percentage;
        }
        @Override
        public GPUImageBrightnessFilter getFilter() {
            return filter;
        }
        @Override
        void apply(int percentage) {
            this.percentage = percentage;
            filter.setBrightness(normalize(percentage, -1.0f, 1.0f));
        }
    }

    static class Contrast extends Editor<GPUImageContrastFilter> {
        private static final int DEFAULT = 50;
        private GPUImageContrastFilter filter = new GPUImageContrastFilter(1.0f);
        private int percentage = DEFAULT;
        @Override
        int getNameResourceId() {
            return R.string.editor_contrast;
        }
        @Override
        int getIconResourceId() {
            return R.drawable.ic_contrast;
        }
        @Override
        int getDefaultPercentage() {
            return DEFAULT;
        }
        @Override
        public int getCurrentPercentage() {
            return percentage;
        }
        @Override
        public GPUImageContrastFilter getFilter() {
            return filter;
        }
        @Override
        void apply(int percentage) {
            this.percentage = percentage;
            filter.setContrast(normalize(percentage, 0.0f, 2.0f));
        }
    }

    static class Saturation extends Editor<GPUImageSaturationFilter> {
        private static final int DEFAULT = 50;
        private GPUImageSaturationFilter filter = new GPUImageSaturationFilter(1.0f);
        private int percentage = DEFAULT;
        @Override
        int getNameResourceId() {
            return R.string.editor_saturation;
        }
        @Override
        int getIconResourceId() {
            return R.drawable.ic_saturation;
        }
        @Override
        int getDefaultPercentage() {
            return DEFAULT;
        }
        @Override
        public int getCurrentPercentage() {
            return percentage;
        }
        @Override
        public GPUImageSaturationFilter getFilter() {
            return filter;
        }
        @Override
        void apply(int percentage) {
            this.percentage = percentage;
            filter.setSaturation(normalize(percentage, 0.0f, 2.0f));
        }
    }

    static class Highlight extends Editor<GPUImageHighlightShadowFilter> {
        private static final int DEFAULT = 50;
        private GPUImageHighlightShadowFilter filter = new GPUImageHighlightShadowFilter();
        private int percentage = DEFAULT;
        @Override
        int getNameResourceId() {
            return R.string.editor_highlight;
        }
        @Override
        int getIconResourceId() {
            return R.drawable.ic_highlight;
        }
        @Override
        int getDefaultPercentage() {
            return DEFAULT;
        }
        @Override
        public int getCurrentPercentage() {
            return percentage;
        }
        @Override
        public GPUImageHighlightShadowFilter getFilter() {
            return filter;
        }
        @Override
        void apply(int percentage) {
            this.percentage = percentage;
            filter.setHighlights(normalize(percentage, 0.0f, 1.0f));
        }
    }

    static class Shadow extends Editor<GPUImageHighlightShadowFilter> {
        private static final int DEFAULT = 50;
        private GPUImageHighlightShadowFilter filter = new GPUImageHighlightShadowFilter();
        private int percentage = DEFAULT;
        @Override
        int getNameResourceId() {
            return R.string.editor_shadow;
        }
        @Override
        int getIconResourceId() {
            return R.drawable.ic_shadow;
        }
        @Override
        int getDefaultPercentage() {
            return DEFAULT;
        }
        @Override
        public int getCurrentPercentage() {
            return percentage;
        }
        @Override
        public GPUImageHighlightShadowFilter getFilter() {
            return filter;
        }
        @Override
        void apply(int percentage) {
            this.percentage = percentage;
            filter.setShadows(normalize(percentage, 0.0f, 1.0f));
        }
    }

    static class Sharpen extends Editor<GPUImageSharpenFilter> {
        private static final int DEFAULT = 50;
        private GPUImageSharpenFilter filter = new GPUImageSharpenFilter(0.0f);
        private int percentage = DEFAULT;
        @Override
        int getNameResourceId() {
            return R.string.editor_sharpen;
        }
        @Override
        int getIconResourceId() {
            return R.drawable.ic_sharpen;
        }
        @Override
        int getDefaultPercentage() {
            return DEFAULT;
        }
        @Override
        public int getCurrentPercentage() {
            return percentage;
        }
        @Override
        public GPUImageSharpenFilter getFilter() {
            return filter;
        }
        @Override
        void apply(int percentage) {
            this.percentage = percentage;
            filter.setSharpness(normalize(percentage, -4.0f, 4.0f));
        }
    }

}
