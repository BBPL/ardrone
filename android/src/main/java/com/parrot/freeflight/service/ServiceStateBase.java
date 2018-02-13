package com.parrot.freeflight.service;

import com.parrot.freeflight.service.commands.DroneServiceCommand;

public abstract class ServiceStateBase {
    protected DroneControlService context;
    private EServiceStateResult result;

    public enum EServiceStateResult {
        SUCCESS,
        FAILED
    }

    public ServiceStateBase(DroneControlService droneControlService) {
        this.context = droneControlService;
    }

    public abstract void connect();

    public abstract void disconnect();

    public EServiceStateResult getResult() {
        return this.result;
    }

    public String getStateName() {
        return getClass().getSimpleName();
    }

    public abstract void onCommandFinished(DroneServiceCommand droneServiceCommand);

    protected void onConnected() {
        this.context.onConnected();
    }

    protected void onDisconnected() {
        this.context.onDisconnected();
    }

    protected void onFinalize() {
    }

    protected void onPaused() {
        this.context.onPaused();
    }

    protected void onPrepare() {
    }

    protected void onResumed() {
        this.context.onResumed();
    }

    public abstract void pause();

    public abstract void resume();

    protected void setResult(EServiceStateResult eServiceStateResult) {
        this.result = eServiceStateResult;
    }

    protected void setState(ServiceStateBase serviceStateBase) {
        this.context.setState(serviceStateBase);
    }

    protected void startCommand(DroneServiceCommand droneServiceCommand) {
        this.context.startCommand(droneServiceCommand);
    }
}
