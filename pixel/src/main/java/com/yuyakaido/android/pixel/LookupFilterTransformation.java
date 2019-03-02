package com.yuyakaido.android.pixel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.squareup.picasso.Transformation;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLookupFilter;

// https://github.com/wasabeef/picasso-transformations
class LookupFilterTransformation implements Transformation {

    private final Context context;
    private final int resourceId;

    LookupFilterTransformation(Context context, int resourceId) {
        this.context = context;
        this.resourceId = resourceId;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        GPUImageLookupFilter filter = new GPUImageLookupFilter();
        filter.setBitmap(BitmapFactory.decodeResource(context.getResources(), resourceId));

        GPUImage gpuImage = new GPUImage(context);
        gpuImage.setImage(source);
        gpuImage.setFilter(filter);

        Bitmap bitmap = gpuImage.getBitmapWithFilterApplied();
        source.recycle();

        return bitmap;
    }

    @Override
    public String key() {
        return String.valueOf(resourceId);
    }

}
