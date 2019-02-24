package com.yuyakaido.android.pixel;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.List;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageLookupFilter;

abstract class Filter {

    abstract int getLookupTableResourceId();
    abstract GPUImageLookupFilter getLookupFilter();

    static List<Filter> newFilters(final Resources resources) {
        return new ArrayList<Filter>() {{
            add(new Neutral(resources));
            add(new Amatorka(resources));
            add(new MissEtikate(resources));
            add(new SoftElegance1(resources));
            add(new SoftElegance2(resources));
        }};
    }

    static class Neutral extends Filter {
        private final GPUImageLookupFilter lookupFilter = new GPUImageLookupFilter();
        Neutral(Resources resources) {
            Bitmap bitmap = BitmapFactory.decodeResource(resources, getLookupTableResourceId());
            lookupFilter.setBitmap(bitmap);
        }
        @Override
        int getLookupTableResourceId() {
            return R.drawable.lookup_neutral;
        }
        @Override
        GPUImageLookupFilter getLookupFilter() {
            return lookupFilter;
        }
    }

    static class Amatorka extends Filter {
        private final GPUImageLookupFilter lookupFilter = new GPUImageLookupFilter();
        Amatorka(Resources resources) {
            Bitmap bitmap = BitmapFactory.decodeResource(resources, getLookupTableResourceId());
            lookupFilter.setBitmap(bitmap);
        }
        @Override
        int getLookupTableResourceId() {
            return R.drawable.lookup_amatorka;
        }
        @Override
        GPUImageLookupFilter getLookupFilter() {
            return lookupFilter;
        }
    }

    static class MissEtikate extends Filter {
        private final GPUImageLookupFilter lookupFilter = new GPUImageLookupFilter();
        MissEtikate(Resources resources) {
            Bitmap bitmap = BitmapFactory.decodeResource(resources, getLookupTableResourceId());
            lookupFilter.setBitmap(bitmap);
        }
        @Override
        int getLookupTableResourceId() {
            return R.drawable.lookup_miss_etikate;
        }
        @Override
        GPUImageLookupFilter getLookupFilter() {
            return lookupFilter;
        }
    }

    static class SoftElegance1 extends Filter {
        private final GPUImageLookupFilter lookupFilter = new GPUImageLookupFilter();
        SoftElegance1(Resources resources) {
            Bitmap bitmap = BitmapFactory.decodeResource(resources, getLookupTableResourceId());
            lookupFilter.setBitmap(bitmap);
        }
        @Override
        int getLookupTableResourceId() {
            return R.drawable.lookup_soft_elegance_1;
        }
        @Override
        GPUImageLookupFilter getLookupFilter() {
            return lookupFilter;
        }
    }

    static class SoftElegance2 extends Filter {
        private final GPUImageLookupFilter lookupFilter = new GPUImageLookupFilter();
        SoftElegance2(Resources resources) {
            Bitmap bitmap = BitmapFactory.decodeResource(resources, getLookupTableResourceId());
            lookupFilter.setBitmap(bitmap);
        }
        @Override
        int getLookupTableResourceId() {
            return R.drawable.lookup_soft_elegance_2;
        }
        @Override
        GPUImageLookupFilter getLookupFilter() {
            return lookupFilter;
        }
    }

}
