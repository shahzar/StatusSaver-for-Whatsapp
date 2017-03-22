package com.shzlabs.statussaver.ui.main.recentscreen;

import android.content.Context;
import android.os.SystemClock;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

public class RecentImageListAdapter extends RecyclerView.Adapter<RecentImageListAdapter.ImageListViewHolder>{

    private Context ctx;
    private List<ImageModel> items;
    private final PublishSubject<Integer> onClickSubject = PublishSubject.create();
    private final PublishSubject<Integer> onSaveClickSubject = PublishSubject.create();
    private final PublishSubject<Integer> onDeleteClickSubject = PublishSubject.create();

    @Inject
    public RecentImageListAdapter(Context ctx) {
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

    public Observable<Integer> getOnSaveItemClicks() {
        return onSaveClickSubject.asObservable();
    }

    public Observable<Integer> getOnDeleteItemClicks() {
        return onDeleteClickSubject.asObservable();
    }

    public void showSaveProgress(View itemView) {
        ProgressBar progressBarForSave = (ProgressBar) itemView.findViewById(R.id.progress_bar_for_save);
        ImageView saveImageView = (ImageView) itemView.findViewById(R.id.save_image_view);

        saveImageView.setVisibility(View.GONE);
        progressBarForSave.setVisibility(View.VISIBLE);
    }

    public void showDeleteButton(View itemView) {
        ProgressBar progressBarForSave = (ProgressBar) itemView.findViewById(R.id.progress_bar_for_save);
        ImageView deleteImageView = (ImageView) itemView.findViewById(R.id.delete_image_view);

        progressBarForSave.setVisibility(View.GONE);
        deleteImageView.setVisibility(View.VISIBLE);

    }

    public void showSaveButton(View itemView) {
        ImageView saveImageView = (ImageView) itemView.findViewById(R.id.save_image_view);
        ImageView deleteImageView = (ImageView) itemView.findViewById(R.id.delete_image_view);

        deleteImageView.setVisibility(View.GONE);
        saveImageView.setVisibility(View.VISIBLE);
    }

    @Override
    public ImageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(ctx).inflate(R.layout.recent_image_list_item, parent, false);

        return new ImageListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ImageListViewHolder holder, int position) {
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

        ViewCompat.setTransitionName(holder.thumbnailImageView, items.get(position).getFileName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 9/3/17 Change position to holder.get...
                onClickSubject.onNext(holder.getAdapterPosition());
            }
        });

        holder.saveImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClickSubject.onNext(holder.getAdapterPosition());
            }
        });

        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteClickSubject.onNext(holder.getAdapterPosition());
            }
        });

        // Display Save/Delete icon
        if (items.get(position).isSavedLocally()) {
            holder.saveImageView.setVisibility(View.GONE);
            holder.deleteImageView.setVisibility(View.VISIBLE);
        }else{
            holder.saveImageView.setVisibility(View.VISIBLE);
            holder.deleteImageView.setVisibility(View.GONE);
        }

        // Display play icon if its a video/gif
        if (items.get(position).isPlayableMedia()) {
            holder.playImageView.setVisibility(View.VISIBLE);
        }else{
            holder.playImageView.setVisibility(View.GONE);
        }

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
        @BindView(R.id.save_image_view)
        ImageView saveImageView;
        @BindView(R.id.delete_image_view)
        ImageView deleteImageView;
        @BindView(R.id.progress_bar_for_save)
        ProgressBar progressBarForSave;
        @BindView(R.id.play_image_view)
        ImageView playImageView;


        public ImageListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}