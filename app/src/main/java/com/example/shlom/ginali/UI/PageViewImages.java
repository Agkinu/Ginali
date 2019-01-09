package com.example.shlom.ginali.UI;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.shlom.ginali.R;
import com.squareup.picasso.Picasso;

public class PageViewImages extends PagerAdapter {
    private Context mContext;
    private String[] mImageURL;

    public PageViewImages(Context mContext, String[] mImageURL) {
        this.mContext = mContext;
        this.mImageURL = mImageURL;
    }

    @Override
    public int getCount() {
        if(mImageURL!=null){
            return mImageURL.length;
        }else{
            return 1;
        }

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        if(mImageURL!=null){
            String photo = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+mImageURL[position]+"&key="+mContext.getString(R.string.google_maps_key)+"";
            Uri googleUri = Uri.parse(photo);
            Picasso.with(mContext).load(googleUri).fit().centerCrop().into(imageView);
        }
        else {
            Picasso.with(mContext).load(R.drawable.park_pic).into(imageView);

        }
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(((View)object));
    }
}
