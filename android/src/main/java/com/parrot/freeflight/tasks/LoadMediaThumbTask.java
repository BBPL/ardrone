package com.parrot.freeflight.tasks;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.parrot.freeflight.media.MediaProvider;
import com.parrot.freeflight.media.MediaProvider.ThumbSize;
import com.parrot.freeflight.vo.MediaVO;
import java.io.IOException;

public class LoadMediaThumbTask extends AsyncTask<Void, Void, Drawable> {
    private final ImageView imageView;
    private final MediaVO media;
    private MediaProvider mediaProvider;
    private ProgressBar progressBar;

    public LoadMediaThumbTask(MediaProvider mediaProvider, MediaVO mediaVO, ImageView imageView, ProgressBar progressBar) {
        this.media = mediaVO;
        this.imageView = imageView;
        this.progressBar = progressBar;
        this.mediaProvider = mediaProvider;
    }

    protected Drawable doInBackground(Void... voidArr) {
        try {
            return this.mediaProvider.getThumbnail(this.media, ThumbSize.MINI);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onPostExecute(Drawable drawable) {
        if (drawable != null) {
            if (this.progressBar != null) {
                this.progressBar.setVisibility(8);
            }
            this.imageView.setImageDrawable(drawable);
        }
    }
}
