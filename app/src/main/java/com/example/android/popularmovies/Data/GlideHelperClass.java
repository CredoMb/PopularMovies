package com.example.android.popularmovies.Data;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

public class GlideHelperClass extends AppGlideModule {

    private Context mContext;
    private String mImageLink;
    private int mPlaceHolderId;
    private ImageView mTargetIv;

    private RequestBuilder<android.graphics.drawable.Drawable> mErrorImageId;

    public GlideHelperClass(Context c, String imageLink, int placeHolderId,
                            ImageView targetIv/*,
                            RequestBuilder<android.graphics.drawable.Drawable> errorId*/) {
        mContext = c;
        mImageLink = imageLink;
        mPlaceHolderId = placeHolderId;
        mTargetIv = targetIv;
       // mErrorImageId = errorId;
    }
    public void loadImage() {

        if (!TextUtils.isEmpty(mImageLink) && mImageLink != null) {
            Glide.with(mContext)
                    .load(mImageLink)
                    .apply(new RequestOptions().placeholder(mPlaceHolderId))
                    .into(mTargetIv);
            //.error(mErrorImageId)
        }
    }
}
