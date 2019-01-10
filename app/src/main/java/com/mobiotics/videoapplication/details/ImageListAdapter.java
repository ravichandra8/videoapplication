package com.mobiotics.videoapplication.details;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobiotics.videoapplication.modal.pojo.Response;
import  com.mobiotics.videoapplication.R;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    private List<Response> imageList;
    private DetailsContract.ImageClickListener listener;

    public ImageListAdapter(List<Response> imageList) {
        this.imageList = imageList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.details_image_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,  int position) {
        final int pos=position;
        final Response image = imageList.get(position);
        holder.title.setText(image.getTitle().toString());
        holder.description.setText(image.getDescription().toString());

        Picasso.get()
                .load(image.getThumb())
                .placeholder(R.drawable.shimmer_background)
                .into(holder.image);

        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(imageList.get(position).getId());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView title, description;

        public MyViewHolder(View view) {
            super(view);
            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            description = view.findViewById(R.id.description);

        }
    }
}