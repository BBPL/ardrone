package com.google.android.youtube.player.internal;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

public class C0384y extends Activity {
    private Activity f183a;

    public C0384y(Activity activity) {
        this.f183a = activity;
    }

    public void addContentView(View view, LayoutParams layoutParams) {
        this.f183a.addContentView(view, layoutParams);
    }

    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return this.f183a.bindService(intent, serviceConnection, i);
    }

    public int checkCallingOrSelfPermission(String str) {
        return this.f183a.checkCallingOrSelfPermission(str);
    }

    public int checkCallingOrSelfUriPermission(Uri uri, int i) {
        return this.f183a.checkCallingOrSelfUriPermission(uri, i);
    }

    public int checkCallingPermission(String str) {
        return this.f183a.checkCallingPermission(str);
    }

    public int checkCallingUriPermission(Uri uri, int i) {
        return this.f183a.checkCallingUriPermission(uri, i);
    }

    public int checkPermission(String str, int i, int i2) {
        return this.f183a.checkPermission(str, i, i2);
    }

    public int checkUriPermission(Uri uri, int i, int i2, int i3) {
        return this.f183a.checkUriPermission(uri, i, i2, i3);
    }

    public int checkUriPermission(Uri uri, String str, String str2, int i, int i2, int i3) {
        return this.f183a.checkUriPermission(uri, str, str2, i, i2, i3);
    }

    public void clearWallpaper() throws IOException {
        this.f183a.clearWallpaper();
    }

    public void closeContextMenu() {
        this.f183a.closeContextMenu();
    }

    public void closeOptionsMenu() {
        this.f183a.closeOptionsMenu();
    }

    public Context createPackageContext(String str, int i) throws NameNotFoundException {
        return this.f183a.createPackageContext(str, i);
    }

    public PendingIntent createPendingResult(int i, Intent intent, int i2) {
        return this.f183a.createPendingResult(i, intent, i2);
    }

    public String[] databaseList() {
        return this.f183a.databaseList();
    }

    public boolean deleteDatabase(String str) {
        return this.f183a.deleteDatabase(str);
    }

    public boolean deleteFile(String str) {
        return this.f183a.deleteFile(str);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        return this.f183a.dispatchGenericMotionEvent(motionEvent);
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return this.f183a.dispatchKeyEvent(keyEvent);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
        return this.f183a.dispatchKeyShortcutEvent(keyEvent);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        return this.f183a.dispatchPopulateAccessibilityEvent(accessibilityEvent);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return this.f183a.dispatchTouchEvent(motionEvent);
    }

    public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
        return this.f183a.dispatchTrackballEvent(motionEvent);
    }

    public void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.f183a.dump(str, fileDescriptor, printWriter, strArr);
    }

    public void enforceCallingOrSelfPermission(String str, String str2) {
        this.f183a.enforceCallingOrSelfPermission(str, str2);
    }

    public void enforceCallingOrSelfUriPermission(Uri uri, int i, String str) {
        this.f183a.enforceCallingOrSelfUriPermission(uri, i, str);
    }

    public void enforceCallingPermission(String str, String str2) {
        this.f183a.enforceCallingPermission(str, str2);
    }

    public void enforceCallingUriPermission(Uri uri, int i, String str) {
        this.f183a.enforceCallingUriPermission(uri, i, str);
    }

    public void enforcePermission(String str, int i, int i2, String str2) {
        this.f183a.enforcePermission(str, i, i2, str2);
    }

    public void enforceUriPermission(Uri uri, int i, int i2, int i3, String str) {
        this.f183a.enforceUriPermission(uri, i, i2, i3, str);
    }

    public void enforceUriPermission(Uri uri, String str, String str2, int i, int i2, int i3, String str3) {
        this.f183a.enforceUriPermission(uri, str, str2, i, i2, i3, str3);
    }

    public boolean equals(Object obj) {
        return this.f183a.equals(obj);
    }

    public String[] fileList() {
        return this.f183a.fileList();
    }

    public View findViewById(int i) {
        return this.f183a.findViewById(i);
    }

    public void finish() {
        this.f183a.finish();
    }

    public void finishActivity(int i) {
        this.f183a.finishActivity(i);
    }

    public void finishActivityFromChild(Activity activity, int i) {
        this.f183a.finishActivityFromChild(activity, i);
    }

    public void finishAffinity() {
        this.f183a.finishAffinity();
    }

    public void finishFromChild(Activity activity) {
        this.f183a.finishFromChild(activity);
    }

    public ActionBar getActionBar() {
        return this.f183a.getActionBar();
    }

    public Context getApplicationContext() {
        return this.f183a.getApplicationContext();
    }

    public ApplicationInfo getApplicationInfo() {
        return this.f183a.getApplicationInfo();
    }

    public AssetManager getAssets() {
        return this.f183a.getAssets();
    }

    public Context getBaseContext() {
        return this.f183a.getBaseContext();
    }

    public File getCacheDir() {
        return this.f183a.getCacheDir();
    }

    public ComponentName getCallingActivity() {
        return this.f183a.getCallingActivity();
    }

    public String getCallingPackage() {
        return this.f183a.getCallingPackage();
    }

    public int getChangingConfigurations() {
        return this.f183a.getChangingConfigurations();
    }

    public ClassLoader getClassLoader() {
        return this.f183a.getClassLoader();
    }

    public ComponentName getComponentName() {
        return this.f183a.getComponentName();
    }

    public ContentResolver getContentResolver() {
        return this.f183a.getContentResolver();
    }

    public View getCurrentFocus() {
        return this.f183a.getCurrentFocus();
    }

    public File getDatabasePath(String str) {
        return this.f183a.getDatabasePath(str);
    }

    public File getDir(String str, int i) {
        return this.f183a.getDir(str, i);
    }

    public File getExternalCacheDir() {
        return this.f183a.getExternalCacheDir();
    }

    public File getExternalFilesDir(String str) {
        return this.f183a.getExternalFilesDir(str);
    }

    public File getFileStreamPath(String str) {
        return this.f183a.getFileStreamPath(str);
    }

    public File getFilesDir() {
        return this.f183a.getFilesDir();
    }

    public FragmentManager getFragmentManager() {
        return this.f183a.getFragmentManager();
    }

    public Intent getIntent() {
        return this.f183a.getIntent();
    }

    public Object getLastNonConfigurationInstance() {
        return this.f183a.getLastNonConfigurationInstance();
    }

    public LayoutInflater getLayoutInflater() {
        return this.f183a.getLayoutInflater();
    }

    public LoaderManager getLoaderManager() {
        return this.f183a.getLoaderManager();
    }

    public String getLocalClassName() {
        return this.f183a.getLocalClassName();
    }

    public Looper getMainLooper() {
        return this.f183a.getMainLooper();
    }

    public MenuInflater getMenuInflater() {
        return this.f183a.getMenuInflater();
    }

    public File getObbDir() {
        return this.f183a.getObbDir();
    }

    public String getPackageCodePath() {
        return this.f183a.getPackageCodePath();
    }

    public PackageManager getPackageManager() {
        return this.f183a.getPackageManager();
    }

    public String getPackageName() {
        return this.f183a.getPackageName();
    }

    public String getPackageResourcePath() {
        return this.f183a.getPackageResourcePath();
    }

    public Intent getParentActivityIntent() {
        return this.f183a.getParentActivityIntent();
    }

    public SharedPreferences getPreferences(int i) {
        return this.f183a.getPreferences(i);
    }

    public int getRequestedOrientation() {
        return this.f183a.getRequestedOrientation();
    }

    public Resources getResources() {
        return this.f183a.getResources();
    }

    public SharedPreferences getSharedPreferences(String str, int i) {
        return this.f183a.getSharedPreferences(str, i);
    }

    public Object getSystemService(String str) {
        return this.f183a.getSystemService(str);
    }

    public int getTaskId() {
        return this.f183a.getTaskId();
    }

    public Theme getTheme() {
        return this.f183a.getTheme();
    }

    public Drawable getWallpaper() {
        return this.f183a.getWallpaper();
    }

    public int getWallpaperDesiredMinimumHeight() {
        return this.f183a.getWallpaperDesiredMinimumHeight();
    }

    public int getWallpaperDesiredMinimumWidth() {
        return this.f183a.getWallpaperDesiredMinimumWidth();
    }

    public Window getWindow() {
        return this.f183a.getWindow();
    }

    public WindowManager getWindowManager() {
        return this.f183a.getWindowManager();
    }

    public void grantUriPermission(String str, Uri uri, int i) {
        this.f183a.grantUriPermission(str, uri, i);
    }

    public boolean hasWindowFocus() {
        return this.f183a.hasWindowFocus();
    }

    public int hashCode() {
        return this.f183a.hashCode();
    }

    public void invalidateOptionsMenu() {
        this.f183a.invalidateOptionsMenu();
    }

    public boolean isChangingConfigurations() {
        return this.f183a.isChangingConfigurations();
    }

    public boolean isFinishing() {
        return this.f183a.isFinishing();
    }

    public boolean isRestricted() {
        return this.f183a.isRestricted();
    }

    public boolean isTaskRoot() {
        return this.f183a.isTaskRoot();
    }

    public boolean moveTaskToBack(boolean z) {
        return this.f183a.moveTaskToBack(z);
    }

    public boolean navigateUpTo(Intent intent) {
        return this.f183a.navigateUpTo(intent);
    }

    public boolean navigateUpToFromChild(Activity activity, Intent intent) {
        return this.f183a.navigateUpToFromChild(activity, intent);
    }

    public void onActionModeFinished(ActionMode actionMode) {
        this.f183a.onActionModeFinished(actionMode);
    }

    public void onActionModeStarted(ActionMode actionMode) {
        this.f183a.onActionModeStarted(actionMode);
    }

    public void onAttachFragment(Fragment fragment) {
        this.f183a.onAttachFragment(fragment);
    }

    public void onAttachedToWindow() {
        this.f183a.onAttachedToWindow();
    }

    public void onBackPressed() {
        this.f183a.onBackPressed();
    }

    public void onConfigurationChanged(Configuration configuration) {
        this.f183a.onConfigurationChanged(configuration);
    }

    public void onContentChanged() {
        this.f183a.onContentChanged();
    }

    public boolean onContextItemSelected(MenuItem menuItem) {
        return this.f183a.onContextItemSelected(menuItem);
    }

    public void onContextMenuClosed(Menu menu) {
        this.f183a.onContextMenuClosed(menu);
    }

    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenuInfo contextMenuInfo) {
        this.f183a.onCreateContextMenu(contextMenu, view, contextMenuInfo);
    }

    public CharSequence onCreateDescription() {
        return this.f183a.onCreateDescription();
    }

    public void onCreateNavigateUpTaskStack(TaskStackBuilder taskStackBuilder) {
        this.f183a.onCreateNavigateUpTaskStack(taskStackBuilder);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return this.f183a.onCreateOptionsMenu(menu);
    }

    public boolean onCreatePanelMenu(int i, Menu menu) {
        return this.f183a.onCreatePanelMenu(i, menu);
    }

    public View onCreatePanelView(int i) {
        return this.f183a.onCreatePanelView(i);
    }

    public boolean onCreateThumbnail(Bitmap bitmap, Canvas canvas) {
        return this.f183a.onCreateThumbnail(bitmap, canvas);
    }

    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return this.f183a.onCreateView(view, str, context, attributeSet);
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return this.f183a.onCreateView(str, context, attributeSet);
    }

    public void onDetachedFromWindow() {
        this.f183a.onDetachedFromWindow();
    }

    public boolean onGenericMotionEvent(MotionEvent motionEvent) {
        return this.f183a.onGenericMotionEvent(motionEvent);
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        return this.f183a.onKeyDown(i, keyEvent);
    }

    public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
        return this.f183a.onKeyLongPress(i, keyEvent);
    }

    public boolean onKeyMultiple(int i, int i2, KeyEvent keyEvent) {
        return this.f183a.onKeyMultiple(i, i2, keyEvent);
    }

    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        return this.f183a.onKeyShortcut(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        return this.f183a.onKeyUp(i, keyEvent);
    }

    public void onLowMemory() {
        this.f183a.onLowMemory();
    }

    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        return this.f183a.onMenuItemSelected(i, menuItem);
    }

    public boolean onMenuOpened(int i, Menu menu) {
        return this.f183a.onMenuOpened(i, menu);
    }

    public boolean onNavigateUp() {
        return this.f183a.onNavigateUp();
    }

    public boolean onNavigateUpFromChild(Activity activity) {
        return this.f183a.onNavigateUpFromChild(activity);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        return this.f183a.onOptionsItemSelected(menuItem);
    }

    public void onOptionsMenuClosed(Menu menu) {
        this.f183a.onOptionsMenuClosed(menu);
    }

    public void onPanelClosed(int i, Menu menu) {
        this.f183a.onPanelClosed(i, menu);
    }

    public void onPrepareNavigateUpTaskStack(TaskStackBuilder taskStackBuilder) {
        this.f183a.onPrepareNavigateUpTaskStack(taskStackBuilder);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return this.f183a.onPrepareOptionsMenu(menu);
    }

    public boolean onPreparePanel(int i, View view, Menu menu) {
        return this.f183a.onPreparePanel(i, view, menu);
    }

    public Object onRetainNonConfigurationInstance() {
        return this.f183a.onRetainNonConfigurationInstance();
    }

    public boolean onSearchRequested() {
        return this.f183a.onSearchRequested();
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.f183a.onTouchEvent(motionEvent);
    }

    public boolean onTrackballEvent(MotionEvent motionEvent) {
        return this.f183a.onTrackballEvent(motionEvent);
    }

    public void onTrimMemory(int i) {
        this.f183a.onTrimMemory(i);
    }

    public void onUserInteraction() {
        this.f183a.onUserInteraction();
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams layoutParams) {
        this.f183a.onWindowAttributesChanged(layoutParams);
    }

    public void onWindowFocusChanged(boolean z) {
        this.f183a.onWindowFocusChanged(z);
    }

    public ActionMode onWindowStartingActionMode(Callback callback) {
        return this.f183a.onWindowStartingActionMode(callback);
    }

    public void openContextMenu(View view) {
        this.f183a.openContextMenu(view);
    }

    public FileInputStream openFileInput(String str) throws FileNotFoundException {
        return this.f183a.openFileInput(str);
    }

    public FileOutputStream openFileOutput(String str, int i) throws FileNotFoundException {
        return this.f183a.openFileOutput(str, i);
    }

    public void openOptionsMenu() {
        this.f183a.openOptionsMenu();
    }

    public SQLiteDatabase openOrCreateDatabase(String str, int i, CursorFactory cursorFactory) {
        return this.f183a.openOrCreateDatabase(str, i, cursorFactory);
    }

    public SQLiteDatabase openOrCreateDatabase(String str, int i, CursorFactory cursorFactory, DatabaseErrorHandler databaseErrorHandler) {
        return this.f183a.openOrCreateDatabase(str, i, cursorFactory, databaseErrorHandler);
    }

    public void overridePendingTransition(int i, int i2) {
        this.f183a.overridePendingTransition(i, i2);
    }

    public Drawable peekWallpaper() {
        return this.f183a.peekWallpaper();
    }

    public void recreate() {
        this.f183a.recreate();
    }

    public void registerComponentCallbacks(ComponentCallbacks componentCallbacks) {
        this.f183a.registerComponentCallbacks(componentCallbacks);
    }

    public void registerForContextMenu(View view) {
        this.f183a.registerForContextMenu(view);
    }

    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        return this.f183a.registerReceiver(broadcastReceiver, intentFilter);
    }

    public Intent registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, String str, Handler handler) {
        return this.f183a.registerReceiver(broadcastReceiver, intentFilter, str, handler);
    }

    public void removeStickyBroadcast(Intent intent) {
        this.f183a.removeStickyBroadcast(intent);
    }

    public void revokeUriPermission(Uri uri, int i) {
        this.f183a.revokeUriPermission(uri, i);
    }

    public void sendBroadcast(Intent intent) {
        this.f183a.sendBroadcast(intent);
    }

    public void sendBroadcast(Intent intent, String str) {
        this.f183a.sendBroadcast(intent, str);
    }

    public void sendOrderedBroadcast(Intent intent, String str) {
        this.f183a.sendOrderedBroadcast(intent, str);
    }

    public void sendOrderedBroadcast(Intent intent, String str, BroadcastReceiver broadcastReceiver, Handler handler, int i, String str2, Bundle bundle) {
        this.f183a.sendOrderedBroadcast(intent, str, broadcastReceiver, handler, i, str2, bundle);
    }

    public void sendStickyBroadcast(Intent intent) {
        this.f183a.sendStickyBroadcast(intent);
    }

    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver broadcastReceiver, Handler handler, int i, String str, Bundle bundle) {
        this.f183a.sendStickyOrderedBroadcast(intent, broadcastReceiver, handler, i, str, bundle);
    }

    public void setContentView(int i) {
        this.f183a.setContentView(i);
    }

    public void setContentView(View view) {
        this.f183a.setContentView(view);
    }

    public void setContentView(View view, LayoutParams layoutParams) {
        this.f183a.setContentView(view, layoutParams);
    }

    public void setFinishOnTouchOutside(boolean z) {
        this.f183a.setFinishOnTouchOutside(z);
    }

    public void setIntent(Intent intent) {
        this.f183a.setIntent(intent);
    }

    public void setRequestedOrientation(int i) {
        this.f183a.setRequestedOrientation(i);
    }

    public void setTheme(int i) {
        this.f183a.setTheme(i);
    }

    public void setTitle(int i) {
        this.f183a.setTitle(i);
    }

    public void setTitle(CharSequence charSequence) {
        this.f183a.setTitle(charSequence);
    }

    public void setTitleColor(int i) {
        this.f183a.setTitleColor(i);
    }

    public void setVisible(boolean z) {
        this.f183a.setVisible(z);
    }

    public void setWallpaper(Bitmap bitmap) throws IOException {
        this.f183a.setWallpaper(bitmap);
    }

    public void setWallpaper(InputStream inputStream) throws IOException {
        this.f183a.setWallpaper(inputStream);
    }

    public boolean shouldUpRecreateTask(Intent intent) {
        return this.f183a.shouldUpRecreateTask(intent);
    }

    public ActionMode startActionMode(Callback callback) {
        return this.f183a.startActionMode(callback);
    }

    public void startActivities(Intent[] intentArr) {
        this.f183a.startActivities(intentArr);
    }

    public void startActivities(Intent[] intentArr, Bundle bundle) {
        this.f183a.startActivities(intentArr, bundle);
    }

    public void startActivity(Intent intent) {
        this.f183a.startActivity(intent);
    }

    public void startActivity(Intent intent, Bundle bundle) {
        this.f183a.startActivity(intent, bundle);
    }

    public void startActivityForResult(Intent intent, int i) {
        this.f183a.startActivityForResult(intent, i);
    }

    public void startActivityForResult(Intent intent, int i, Bundle bundle) {
        this.f183a.startActivityForResult(intent, i, bundle);
    }

    public void startActivityFromChild(Activity activity, Intent intent, int i) {
        this.f183a.startActivityFromChild(activity, intent, i);
    }

    public void startActivityFromChild(Activity activity, Intent intent, int i, Bundle bundle) {
        this.f183a.startActivityFromChild(activity, intent, i, bundle);
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int i) {
        this.f183a.startActivityFromFragment(fragment, intent, i);
    }

    public void startActivityFromFragment(Fragment fragment, Intent intent, int i, Bundle bundle) {
        this.f183a.startActivityFromFragment(fragment, intent, i, bundle);
    }

    public boolean startActivityIfNeeded(Intent intent, int i) {
        return this.f183a.startActivityIfNeeded(intent, i);
    }

    public boolean startActivityIfNeeded(Intent intent, int i, Bundle bundle) {
        return this.f183a.startActivityIfNeeded(intent, i, bundle);
    }

    public boolean startInstrumentation(ComponentName componentName, String str, Bundle bundle) {
        return this.f183a.startInstrumentation(componentName, str, bundle);
    }

    public void startIntentSender(IntentSender intentSender, Intent intent, int i, int i2, int i3) throws SendIntentException {
        this.f183a.startIntentSender(intentSender, intent, i, i2, i3);
    }

    public void startIntentSender(IntentSender intentSender, Intent intent, int i, int i2, int i3, Bundle bundle) throws SendIntentException {
        this.f183a.startIntentSender(intentSender, intent, i, i2, i3, bundle);
    }

    public void startIntentSenderForResult(IntentSender intentSender, int i, Intent intent, int i2, int i3, int i4) throws SendIntentException {
        this.f183a.startIntentSenderForResult(intentSender, i, intent, i2, i3, i4);
    }

    public void startIntentSenderForResult(IntentSender intentSender, int i, Intent intent, int i2, int i3, int i4, Bundle bundle) throws SendIntentException {
        this.f183a.startIntentSenderForResult(intentSender, i, intent, i2, i3, i4, bundle);
    }

    public void startIntentSenderFromChild(Activity activity, IntentSender intentSender, int i, Intent intent, int i2, int i3, int i4) throws SendIntentException {
        this.f183a.startIntentSenderFromChild(activity, intentSender, i, intent, i2, i3, i4);
    }

    public void startIntentSenderFromChild(Activity activity, IntentSender intentSender, int i, Intent intent, int i2, int i3, int i4, Bundle bundle) throws SendIntentException {
        this.f183a.startIntentSenderFromChild(activity, intentSender, i, intent, i2, i3, i4, bundle);
    }

    public void startManagingCursor(Cursor cursor) {
        this.f183a.startManagingCursor(cursor);
    }

    public boolean startNextMatchingActivity(Intent intent) {
        return this.f183a.startNextMatchingActivity(intent);
    }

    public boolean startNextMatchingActivity(Intent intent, Bundle bundle) {
        return this.f183a.startNextMatchingActivity(intent, bundle);
    }

    public void startSearch(String str, boolean z, Bundle bundle, boolean z2) {
        this.f183a.startSearch(str, z, bundle, z2);
    }

    public ComponentName startService(Intent intent) {
        return this.f183a.startService(intent);
    }

    public void stopManagingCursor(Cursor cursor) {
        this.f183a.stopManagingCursor(cursor);
    }

    public boolean stopService(Intent intent) {
        return this.f183a.stopService(intent);
    }

    public void takeKeyEvents(boolean z) {
        this.f183a.takeKeyEvents(z);
    }

    public String toString() {
        return this.f183a.toString();
    }

    public void triggerSearch(String str, Bundle bundle) {
        this.f183a.triggerSearch(str, bundle);
    }

    public void unbindService(ServiceConnection serviceConnection) {
        this.f183a.unbindService(serviceConnection);
    }

    public void unregisterComponentCallbacks(ComponentCallbacks componentCallbacks) {
        this.f183a.unregisterComponentCallbacks(componentCallbacks);
    }

    public void unregisterForContextMenu(View view) {
        this.f183a.unregisterForContextMenu(view);
    }

    public void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        this.f183a.unregisterReceiver(broadcastReceiver);
    }
}
