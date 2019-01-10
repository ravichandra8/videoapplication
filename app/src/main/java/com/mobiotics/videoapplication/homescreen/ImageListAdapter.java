package com.mobiotics.videoapplication.homescreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobiotics.videoapplication.modal.pojo.Response;
import  com.mobiotics.videoapplication.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.MyViewHolder> {

    private List<Response> imageList;
    private MainContract.ImageClickListener listener;

    public ImageListAdapter(List<Response> imageList, MainContract.ImageClickListener listener) {
        this.imageList = imageList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_layout, parent, false);

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


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImageClick(image.getId());
            }
        });
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