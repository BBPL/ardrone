package com.parrot.freeflight.updater.commands;

import android.content.Context;
import com.parrot.freeflight.service.listeners.DroneUpdaterListener.ArDroneToolError;
import com.parrot.freeflight.updater.UpdateManager;
import com.parrot.freeflight.updater.UpdaterCommand;
import com.parrot.freeflight.updater.UpdaterDelegate;

public abstract class UpdaterCommandBase implements UpdaterCommand {
    protected UpdateManager context;
    protected UpdaterDelegate delegate;
    protected ArDroneToolError error = ArDroneToolError.E_NONE;
    protected String errorMessage;
    protected String status;

    public UpdaterCommandBase(UpdateManager updateManager) {
        this.context = updateManager;
        this.delegate = updateManager.getDelegate();
    }

    public abstract void execute(Context context);

    public void executeInternal(Context context) {
        this.context.onPreExecuteCommand(this);
        execute(context);
        this.context.onPostExecuteCommand(this);
    }

    public String getCommandName() {
        return getClass().getSimpleName();
    }

    public ArDroneToolError getError() {
        return this.error;
    }

    protected void onUpdate() {
        this.context.notifyOnUpdateListeners(this);
    }

    protected void setError(ArDroneToolError arDroneToolError) {
        this.error = arDroneToolError;
    }

    protected void sleep(long j) {
        try {
            Thread.sleep(j);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
