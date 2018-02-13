package com.parrot.freeflight.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.VideoCategories.List;
import com.google.api.services.youtube.model.VideoCategory;
import com.google.api.services.youtube.model.VideoCategoryListResponse;
import com.parrot.freeflight.utils.AsyncTaskResult;
import com.parrot.freeflight.vo.VideoCategoryVO;
import java.util.Locale;

public class GetYouTubeVideoCategoriesTask extends AsyncTask<Object, Void, AsyncTaskResult<VideoCategoryVO[]>> {
    public static final int REQUEST_AUTHORIZATION = 1;

    protected AsyncTaskResult<VideoCategoryVO[]> doInBackground(Object... objArr) {
        int i = 0;
        Activity activity = (Activity) objArr[0];
        YouTube youTube = (YouTube) objArr[1];
        try {
            Log.i("GetYouTubeVideoCategoriesTask", "inbackground");
            List list = youTube.videoCategories().list("id,snippet");
            list.setRegionCode(Locale.getDefault().getCountry());
            java.util.List items = ((VideoCategoryListResponse) list.execute()).getItems();
            Object obj = new VideoCategoryVO[items.size()];
            while (i < items.size()) {
                VideoCategory videoCategory = (VideoCategory) items.get(i);
                obj[i] = new VideoCategoryVO(videoCategory.getId(), videoCategory.getSnippet().getTitle());
                i++;
            }
            return new AsyncTaskResult(obj, null);
        } catch (Exception e) {
            return new AsyncTaskResult(null, e);
        }
    }
}
