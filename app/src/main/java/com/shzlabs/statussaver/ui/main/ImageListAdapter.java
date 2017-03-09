package com.shzlabs.statussaver.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shzlabs.statussaver.R;
import com.shzlabs.statussaver.data.model.ImageModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.PublishSubject;

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ImageListViewHolder>{

    Context ctx;
    List<ImageModel> items;
    private final PublishSubject<Integer> onClickSubject = PublishSubject.create();

    @Inject
    public ImageListAdapter(Context ctx) {
        this.ctx = ctx;
        items = new ArrayList<>();
    }

    public void setItems(List<ImageModel> items) {
        this.items.clear();
        this.items = items;
        notifyDataSetChanged();
    }

    public ImageModel getItemAtPosition(int position) {
        return items.get(position);
    }

    public Observable<Integer> getOnItemClicks() {
        return onClickSubject.asObservable();
    }

    @Override
    public ImageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(ctx).inflate(R.layout.image_list_item, parent, false);

        return new ImageListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ImageListViewHolder holder, final int position) {
        Glide.with(ctx)
                .load(new File(items.get(position).getCompletePath()))
                .crossFade()
                .listener(new RequestListener<File, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, File model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, File model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.thumbnailImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubject.onNext(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    

    class ImageListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_thumbnail)
        ImageView thumbnailImageView;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        public ImageListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}