package com.parrot.freeflight.media;

import android.graphics.drawable.BitmapDrawable;
import com.parrot.freeflight.vo.MediaVO;
import java.io.IOException;
import java.util.List;

public interface MediaProvider {

    public enum MediaType {
        ALL,
        PHOTOS,
        VIDEOS
    }

    public enum ThumbSize {
        MINI,
        MICRO
    }

    void abortAnyOperations();

    List<MediaVO> getMediaList(MediaType mediaType);

    int getPhotoCount();

    BitmapDrawable getThumbnail(MediaVO mediaVO, ThumbSize thumbSize) throws IOException;

    int getVideoCount();
}
