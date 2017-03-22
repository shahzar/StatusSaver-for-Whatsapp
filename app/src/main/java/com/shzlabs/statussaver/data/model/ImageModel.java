package com.shzlabs.statussaver.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shaz on 6/3/17.
 */

public class ImageModel implements Parcelable{

    private String fileName;
    private String completePath;
    private long lastModified;
    private boolean savedLocally = false;
    private boolean playableMedia = false;

    public ImageModel(String fileName, String completePath, long lastModified) {
        this.fileName = fileName;
        this.completePath = completePath;
        this.lastModified = lastModified;
    }

    public ImageModel(String fileName, String completePath, long lastModified, boolean isSavedLocally,
                      boolean isVideoOrGif) {
        this.fileName = fileName;
        this.completePath = completePath;
        this.lastModified = lastModified;
        this.savedLocally = isSavedLocally;
        this.playableMedia = isVideoOrGif;
    }


    public String getFileName() {
        return fileName;
    }

    public String getCompletePath() {
        return completePath;
    }

    public long getLastModified() {
        return lastModified;
    }

    public boolean isSavedLocally() {
        return savedLocally;
    }

    public boolean isPlayableMedia() {
        return playableMedia;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageModel) {
            ImageModel data = (ImageModel) obj;
            if (data.getFileName().equals(this.getFileName())
                    && data.getCompletePath().equals(this.getCompletePath())
                    && data.getLastModified() == this.getLastModified()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                fileName, completePath
        });
        dest.writeLong(lastModified);
        dest.writeInt(savedLocally ? 1 : 0);
    }

    private ImageModel(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);
        fileName = data[0];
        completePath = data[1];
        lastModified = in.readLong();
        savedLocally= in.readInt() != 0;
    }

    public static final Parcelable.Creator<ImageModel> CREATOR
            = new Creator<ImageModel>() {
        @Override
        public ImageModel createFromParcel(Parcel source) {
            return new ImageModel(source);
        }

        @Override
        public ImageModel[] newArray(int size) {
            return new ImageModel[size];
        }
    };

}
