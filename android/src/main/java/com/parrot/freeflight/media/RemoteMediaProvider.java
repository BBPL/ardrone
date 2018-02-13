package com.parrot.freeflight.media;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import com.parrot.freeflight.academy.model.Flight;
import com.parrot.freeflight.academy.model.Media;
import com.parrot.freeflight.media.MediaProvider.MediaType;
import com.parrot.freeflight.media.MediaProvider.ThumbSize;
import com.parrot.freeflight.vo.MediaVO;
import com.parrot.freeflight.vo.YouTubeMediaVO;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class RemoteMediaProvider implements MediaProvider {
    public static final String DATE_FORMAT = "yyyy'-'MM'-'dd' 'HH':'mm':'ss";
    private Context context;
    private Flight flight;
    private InputStream is;

    public RemoteMediaProvider(Context context, Flight flight) {
        this.flight = flight;
        this.context = context;
    }

    private void appendToResult(List<MediaVO> list, List<Media> list2, boolean z) {
        int size = list2.size();
        for (int i = 0; i < size; i++) {
            Media media = (Media) list2.get(i);
            MediaVO youTubeMediaVO = new YouTubeMediaVO();
            youTubeMediaVO.setVideo(z);
            youTubeMediaVO.setId(media.getId());
            try {
                youTubeMediaVO.setDateTaken(new SimpleDateFormat("yyyy'-'MM'-'dd' 'HH':'mm':'ss", Locale.getDefault()).parse(media.getModification_date()).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            youTubeMediaVO.setPath(media.getUrl());
            list.add(youTubeMediaVO);
        }
    }

    public void abortAnyOperations() {
        if (this.is != null) {
            try {
                this.is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.is = null;
        }
    }

    public List<MediaVO> getMediaList(MediaType mediaType) {
        List<MediaVO> arrayList = new ArrayList(getVideoCount() + getPhotoCount());
        switch (mediaType) {
            case PHOTOS:
                appendToResult(arrayList, this.flight.getFlightCaptureSet(), false);
                break;
            case VIDEOS:
                appendToResult(arrayList, this.flight.getFlightVideoSet(), true);
                break;
            case ALL:
                appendToResult(arrayList, this.flight.getFlightCaptureSet(), false);
                appendToResult(arrayList, this.flight.getFlightVideoSet(), true);
                break;
        }
        Collections.sort(arrayList);
        return arrayList;
    }

    public int getPhotoCount() {
        return this.flight != null ? this.flight.getFlightCaptureSet().size() : 0;
    }

    public BitmapDrawable getThumbnail(MediaVO mediaVO, ThumbSize thumbSize) throws IOException {
        BitmapDrawable bitmapDrawable;
        synchronized (this) {
            String str = "w256";
            String str2 = "/1.jpg";
            Object obj;
            switch (thumbSize) {
                case MINI:
                    obj = "w720";
                    str = "/0.jpg";
                    break;
                case MICRO:
                    obj = "w256";
                    str = "/1.jpg";
                    break;
                default:
                    CharSequence charSequence = str;
                    str = str2;
                    break;
            }
            try {
                this.is = new BufferedInputStream((mediaVO.isVideo() ? new URL((mediaVO.getPath() + str).replace(":", ":/")) : new URL(mediaVO.getPath().replace("kPhotoThumbSize", charSequence))).openConnection().getInputStream());
                Bitmap decodeStream = BitmapFactory.decodeStream(this.is);
                if (decodeStream != null) {
                    bitmapDrawable = new BitmapDrawable(this.context.getResources(), decodeStream);
                    if (this.is != null) {
                        try {
                            this.is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        this.is = null;
                    }
                } else {
                    if (this.is != null) {
                        try {
                            this.is.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                        this.is = null;
                    }
                    bitmapDrawable = null;
                }
            } catch (MalformedURLException e3) {
                e3.printStackTrace();
                if (this.is != null) {
                    try {
                        this.is.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                    }
                    this.is = null;
                }
            } catch (Throwable th) {
                Throwable th2 = th;
                if (this.is != null) {
                    try {
                        this.is.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                    }
                    this.is = null;
                }
            }
        }
        return bitmapDrawable;
    }

    public int getVideoCount() {
        return this.flight != null ? this.flight.getFlightVideoSet().size() : 0;
    }
}
