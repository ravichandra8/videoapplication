package com.mobiotics.videoapplication.homescreen;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mobiotics.videoapplication.modal.pojo.Video;
import  com.mobiotics.videoapplication.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.MyViewHolder> {

    private List<Video> videoList;
    private MainContract.ItemClickListener listener;

    public VideosListAdapter(List<Video> videoList, MainContract.ItemClickListener listener) {
        this.videoList = videoList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,int position) {
        final Video video = videoList.get(position);
        holder.title.setText(video.getTitle().toString());
        holder.description.setText(video.getDescription().toString());

        Picasso.get()
                .load(video.getThumb())
                .placeholder(R.drawable.shimmer_background)
                .into(holder.image);


        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(video.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    @Override
    public long getItemId(int position) {
        return Integer.parseInt(videoList.get(position).getId());
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