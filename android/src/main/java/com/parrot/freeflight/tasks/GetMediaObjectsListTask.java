package com.parrot.freeflight.tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.parrot.freeflight.media.MediaProvider;
import com.parrot.freeflight.media.MediaProvider.MediaType;
import com.parrot.freeflight.vo.MediaVO;
import java.util.List;

public class GetMediaObjectsListTask extends AsyncTask<Object, Void, List<MediaVO>> {
    private final MediaType filter;
    private MediaProvider mediaProvider;

    public GetMediaObjectsListTask(Context context, MediaProvider mediaProvider, MediaType mediaType) {
        this.filter = mediaType;
        this.mediaProvider = mediaProvider;
    }

    protected List<MediaVO> doInBackground(Object... objArr) {
        return this.mediaProvider.getMediaList(this.filter);
    }
}
