package com.example.magdi.instagramviewer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by magdi on 1/21/15.
 */
public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhotos> {

    public InstagramPhotoAdapter(Context context, List<InstagramPhotos> photos) {
        super(context, R.layout.item_photo, photos);
    }
    //overwite getView

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //take data source and return acutaul view
        //get data item
       InstagramPhotos photo = getItem(position);
        //check if we are using recycled view
      if(convertView == null){
          convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo,parent,false);
      }
        //lookup subview
        TextView tvCaption  = (TextView)convertView.findViewById(R.id.tvcaption);
        ImageView imgPhoto = (ImageView)convertView.findViewById(R.id.imgPhoto);
        ImageView imgProfile = (ImageView)convertView.findViewById(R.id.imgPhoto);
        TextView tvname  = (TextView)convertView.findViewById(R.id.tvname);
        TextView tvlikes  = (TextView)convertView.findViewById(R.id.tvlikes);
        imgPhoto.setImageResource(0);
        Picasso.with(getContext()).load(photo.imageurl).placeholder(R.drawable.sanfrancisco).fit().centerCrop().into(imgPhoto);
        imgProfile.setImageResource(0);
        Picasso.with(getContext()).load(photo.profImage).into(imgProfile);

        //populate the subview
        tvCaption.setText(Html.fromHtml("<font color='blue'>"+photo.username+" </font>"+ photo.caption +"<br><font color='green'> Comments:"+"</font>" + photo.comments ));
        tvname.setText(photo.username);
        final float density = convertView.getResources().getDisplayMetrics().density;
        final Drawable drawable = convertView.getResources().getDrawable(R.drawable.like_icon);

        final int width = Math.round(24 * density);
        final int height = Math.round(24 * density);

        drawable.setBounds(0, 0, width, height);
        tvlikes.setCompoundDrawables(drawable, null, null, null);
        tvlikes.setText(String.valueOf(photo.likescount + " likes" ));
        imgPhoto.getLayoutParams().height = photo.imageheight;

        //return the view
        return convertView;
    }
}
