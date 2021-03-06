package com.geekgirl.android.baking.ui.custumViews;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.geekgirl.android.baking.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Rim Gazzah on 23/09/18
 */
public class PicassoImageView extends RelativeLayout {

    ImageView mImageView;
    String mUrl;
    Context mContext;

    public PicassoImageView(Context context) {
        super(context);
        mContext = context;
    }

    public PicassoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs);
    }

    public PicassoImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attr) {
        inflate(getContext(), R.layout.picasso_image_view, this);
        mImageView = findViewById(R.id.imageview);
        mUrl = attr.getAttributeValue(R.styleable.PicassoImageView_Url);
        if (!mUrl.contains("https")) {
            mUrl.replace("http", "https");
        }
        if (!TextUtils.isEmpty(mUrl)) {
            Picasso.with(mContext).load(mUrl).noFade().into(mImageView);
        }
    }


    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
        if (!mUrl.contains("https")) {
            mUrl.replace("http", "https");
        }
        Picasso.with(mContext).load(mUrl).noFade()
                .into(mImageView);
        invalidate();
    }

    public void setUrl(Drawable drawable){
        mImageView.setImageDrawable(drawable);
        invalidate();
    }
}
