package com.shzlabs.statussaver.ui.main.saved;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.view.ViewCompat;
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

public class SavedImageListAdapter extends RecyclerView.Adapter<SavedImageListAdapter.ImageListViewHolder>{

    private Context ctx;
    private List<ImageModel> items;
    private final PublishSubject<Integer> onClickSubject = PublishSubject.create();
    private final PublishSubject<Integer> onLongClickSubject = PublishSubject.create();
    private final PublishSubject<Boolean> onSelectItemsStatusSubject = PublishSubject.create();
    private int selectedItemFilterColor;
    // Variables to handle selecting multiple items in recyclerview
    private boolean selectItemsOn = false;
    private List<Integer> selectedItemsList;

    @Inject
    public SavedImageListAdapter(Context ctx) {
        this.ctx = ctx;
        items = new ArrayList<>();
        selectedItemsList = new ArrayList<>();
        selectedItemFilterColor = ctx.getResources().getColor(R.color.savedSelectedItemColor);
    }

    public void setItems(List<ImageModel> items) {
        this.items.clear();
        this.items = items;
//        setSelectItemsOn(false);
        notifyDataSetChanged();
    }

    public ImageModel getItemAtPosition(int position) {
        return items.get(position);
    }

    public Observable<Integer> getOnItemClicks() {
        return onClickSubject.asObservable();
    }

    public Observable<Integer> getOnLongItemClicks() {
        return onLongClickSubject.asObservable();
    }

    public Observable<Boolean> getOnSelectItemsStatusChange() {
        return onSelectItemsStatusSubject.asObservable();
    }

    @Override
    public ImageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(ctx).inflate(R.layout.saved_image_list_item, parent, false);

        return new ImageListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ImageListViewHolder holder, int position) {

        if (isIndexSelected(position)) {
            holder.itemView.setPadding(8,8,8,8);
            holder.thumbnailImageView.setColorFilter(selectedItemFilterColor, PorterDuff.Mode.MULTIPLY);
        }else{
            holder.itemView.setPadding(0,0,0,0);
            holder.thumbnailImageView.setColorFilter(null);
        }

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
                onClickSubject.onNext(holder.getAdapterPosition());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onLongClickSubject.onNext(holder.getAdapterPosition());
                return true;
            }
        });

        // Display play icon if its a video/gif
        if (items.get(position).isPlayableMedia()) {
            holder.playImageView.setVisibility(View.VISIBLE);
        }else{
            holder.playImageView.setVisibility(View.GONE);
        }

    }

    private boolean isIndexSelected(int position) {
        return selectedItemsList.contains(position);
    }

    public void toggleSelected(Integer position) {
        if (selectedItemsList.contains(position)) {
            selectedItemsList.remove(position);
        }else{
            selectedItemsList.add(position);
        }
        notifyDataSetChanged();
    }

    public boolean isSelectItemsOn() {
        return selectItemsOn;
    }

    public void setSelectItemsOn(boolean status) {
        selectItemsOn = status;
        selectedItemsList.clear();
        onSelectItemsStatusSubject.onNext(status);
    }

    public List<Integer> getSelectedItemsList() {
        return selectedItemsList;
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
        @BindView(R.id.play_image_view)
        ImageView playImageView;

        public ImageListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}