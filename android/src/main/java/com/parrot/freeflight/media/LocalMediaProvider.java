package com.parrot.freeflight.media;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Thumbnails;
import com.parrot.freeflight.C0984R;
import com.parrot.freeflight.academy.model.Flight;
import com.parrot.freeflight.media.MediaProvider.MediaType;
import com.parrot.freeflight.media.MediaProvider.ThumbSize;
import com.parrot.freeflight.utils.ARDroneMediaGallery;
import com.parrot.freeflight.utils.ThumbnailUtils;
import com.parrot.freeflight.vo.MediaVO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalMediaProvider implements MediaProvider {
    private ContentResolver contentResolver;
    private Flight flight;
    private ARDroneMediaGallery mediaGallery;
    private Resources res;

    public LocalMediaProvider(Context context) {
        this.mediaGallery = new ARDroneMediaGallery(context);
        this.contentResolver = context.getContentResolver();
        this.res = context.getResources();
    }

    public LocalMediaProvider(Context context, Flight flight) {
        this(context);
        this.flight = flight;
    }

    public void abortAnyOperations() {
    }

    public void deleteMedia(List<MediaVO> list) {
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        for (MediaVO mediaVO : list) {
            if (mediaVO.isVideo()) {
                arrayList2.add(Integer.valueOf(mediaVO.getId()));
            } else {
                arrayList.add(Integer.valueOf(mediaVO.getId()));
            }
        }
        int size = arrayList.size();
        if (size > 0) {
            this.mediaGallery.deleteImages((Integer[]) arrayList.toArray(new Integer[size]));
        }
        size = arrayList2.size();
        if (size > 0) {
            this.mediaGallery.deleteVideos((Integer[]) arrayList2.toArray(new Integer[size]));
        }
    }

    public List<MediaVO> getMediaList(MediaType mediaType) {
        List<MediaVO> arrayList = new ArrayList();
        switch (mediaType) {
            case VIDEOS:
                if (this.flight == null) {
                    arrayList.addAll(this.mediaGallery.getMediaVideoList());
                    break;
                }
                arrayList.addAll(this.mediaGallery.getMediaVideoList(this.flight.getFlightTag()));
                break;
            case PHOTOS:
                if (this.flight == null) {
                    arrayList.addAll(this.mediaGallery.getMediaImageList());
                    break;
                }
                arrayList.addAll(this.mediaGallery.getMediaImageList(this.flight.getFlightTag()));
                break;
            case ALL:
                if (this.flight != null) {
                    arrayList.addAll(this.mediaGallery.getMediaVideoList(this.flight.getFlightTag()));
                } else {
                    arrayList.addAll(this.mediaGallery.getMediaVideoList());
                }
                if (this.flight == null) {
                    arrayList.addAll(this.mediaGallery.getMediaImageList());
                    break;
                }
                arrayList.addAll(this.mediaGallery.getMediaImageList(this.flight.getFlightTag()));
                break;
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    public int getPhotoCount() {
        return this.flight != null ? this.mediaGallery.countOfPhotos(this.flight.getFlightTag()) : this.mediaGallery.countOfPhotos();
    }

    public BitmapDrawable getThumbnail(MediaVO mediaVO, ThumbSize thumbSize) {
        Bitmap thumbnail;
        Bitmap createVideoThumbnail;
        int i = 3;
        switch (thumbSize) {
            case MINI:
                i = 1;
                break;
        }
        if (mediaVO.isVideo()) {
            thumbnail = Thumbnails.getThumbnail(this.contentResolver, (long) mediaVO.getId(), i, null);
            createVideoThumbnail = thumbnail == null ? ThumbnailUtils.createVideoThumbnail(mediaVO.getPath(), i) : thumbnail;
        } else {
            createVideoThumbnail = Images.Thumbnails.getThumbnail(this.contentResolver, (long) mediaVO.getId(), i, null);
        }
        thumbnail = createVideoThumbnail == null ? BitmapFactory.decodeResource(this.res, C0984R.drawable.picto_warning) : createVideoThumbnail;
        return thumbnail != null ? new BitmapDrawable(this.res, thumbnail) : null;
    }

    public int getVideoCount() {
        return this.flight != null ? this.mediaGallery.countOfVideos(this.flight.getFlightTag()) : this.mediaGallery.countOfVideos();
    }
}
