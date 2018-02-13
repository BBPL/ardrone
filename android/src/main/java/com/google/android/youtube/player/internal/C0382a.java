package com.google.android.youtube.player.internal;

import android.graphics.Bitmap;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailLoader.ErrorReason;
import com.google.android.youtube.player.YouTubeThumbnailLoader.OnThumbnailLoadedListener;
import com.google.android.youtube.player.YouTubeThumbnailView;
import java.util.NoSuchElementException;

public abstract class C0382a implements YouTubeThumbnailLoader {
    private final YouTubeThumbnailView f178a;
    private OnThumbnailLoadedListener f179b;
    private boolean f180c;
    private boolean f181d;

    public C0382a(YouTubeThumbnailView youTubeThumbnailView) {
        this.f178a = (YouTubeThumbnailView) ac.m1483a((Object) youTubeThumbnailView, (Object) "thumbnailView cannot be null");
    }

    private void m1464h() {
        if (!mo1327a()) {
            throw new IllegalStateException("This YouTubeThumbnailLoader has been released");
        }
    }

    public final void m1465a(Bitmap bitmap, String str) {
        if (mo1327a()) {
            this.f178a.setImageBitmap(bitmap);
            if (this.f179b != null) {
                this.f179b.onThumbnailLoaded(this.f178a, str);
            }
        }
    }

    public abstract void mo1325a(String str);

    public abstract void mo1326a(String str, int i);

    protected boolean mo1327a() {
        return !this.f181d;
    }

    public abstract void mo1328b();

    public final void m1470b(String str) {
        if (mo1327a() && this.f179b != null) {
            ErrorReason valueOf;
            try {
                valueOf = ErrorReason.valueOf(str);
            } catch (IllegalArgumentException e) {
                valueOf = ErrorReason.UNKNOWN;
            } catch (NullPointerException e2) {
                valueOf = ErrorReason.UNKNOWN;
            }
            this.f179b.onThumbnailError(this.f178a, valueOf);
        }
    }

    public abstract void mo1329c();

    public abstract void mo1330d();

    public abstract boolean mo1331e();

    public abstract boolean mo1332f();

    public final void first() {
        m1464h();
        if (this.f180c) {
            mo1330d();
            return;
        }
        throw new IllegalStateException("Must call setPlaylist first");
    }

    public abstract void mo1333g();

    public final boolean hasNext() {
        m1464h();
        return mo1331e();
    }

    public final boolean hasPrevious() {
        m1464h();
        return mo1332f();
    }

    public final void next() {
        m1464h();
        if (!this.f180c) {
            throw new IllegalStateException("Must call setPlaylist first");
        } else if (mo1331e()) {
            mo1328b();
        } else {
            throw new NoSuchElementException("Called next at end of playlist");
        }
    }

    public final void previous() {
        m1464h();
        if (!this.f180c) {
            throw new IllegalStateException("Must call setPlaylist first");
        } else if (mo1332f()) {
            mo1329c();
        } else {
            throw new NoSuchElementException("Called previous at start of playlist");
        }
    }

    public final void release() {
        if (mo1327a()) {
            this.f181d = true;
            this.f179b = null;
            mo1333g();
        }
    }

    public final void setOnThumbnailLoadedListener(OnThumbnailLoadedListener onThumbnailLoadedListener) {
        m1464h();
        this.f179b = onThumbnailLoadedListener;
    }

    public final void setPlaylist(String str) {
        setPlaylist(str, 0);
    }

    public final void setPlaylist(String str, int i) {
        m1464h();
        this.f180c = true;
        mo1326a(str, i);
    }

    public final void setVideo(String str) {
        m1464h();
        this.f180c = false;
        mo1325a(str);
    }
}
