package com.yuyakaido.android.pixel;

import android.content.Context;
import android.graphics.BitmapFactory;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageLookupFilter;

enum Filter {
    Neutral(R.drawable.lookup_neutral),
    Amatorka(R.drawable.lookup_amatorka),
    MissEtikate(R.drawable.lookup_miss_etikate),
    SoftElegance1(R.drawable.lookup_soft_elegance_1),
    SoftElegance2(R.drawable.lookup_soft_elegance_2);

    int lookupTableResourceId;

    Filter(int lookupTableResourceId) {
        this.lookupTableResourceId = lookupTableResourceId;
    }

    GPUImageLookupFilter getLookupFilter(Context context) {
        GPUImageLookupFilter filter = new GPUImageLookupFilter();
        filter.setBitmap(BitmapFactory.decodeResource(context.getResources(), lookupTableResourceId));
        return filter;
    }
}
