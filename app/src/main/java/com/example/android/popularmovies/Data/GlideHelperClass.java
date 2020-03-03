package com.example.android.popularmovies.Data;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.example.android.popularmovies.R;

public class GlideHelperClass extends AppGlideModule {

    private Context mContext;
    private String mImageLink;
    private int mPlaceHolderId;
    private RequestBuilder<android.graphics.drawable.Drawable> mErrorImageId;

    public GlideHelperClass(Context c, String imageLink, int placeHolderId, RequestBuilder<android.graphics.drawable.Drawable> errorId) {
        mContext = c;
        mImageLink = imageLink;
        mPlaceHolderId = placeHolderId;
        mErrorImageId = errorId;
    }
    public void loadImage() {

        if (!TextUtils.isEmpty(mImageLink) && mImageLink != null) {
            Glide.with(mContext)
                    .load(mImageLink)
                    .error(mErrorImageId)
                    .apply(new RequestOptions().placeholder(mPlaceHolderId));
        }
    }
}
