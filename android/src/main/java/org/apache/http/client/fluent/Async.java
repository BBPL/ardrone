package org.apache.http.client.fluent;

import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import org.apache.http.client.ResponseHandler;
import org.apache.http.concurrent.BasicFuture;
import org.apache.http.concurrent.FutureCallback;

public class Async {
    private Executor concurrentExec;
    private Executor executor;

    static class ExecRunnable<T> implements Runnable {
        private final Executor executor;
        private final BasicFuture<T> future;
        private final ResponseHandler<T> handler;
        private final Request request;

        ExecRunnable(BasicFuture<T> basicFuture, Request request, Executor executor, ResponseHandler<T> responseHandler) {
            this.future = basicFuture;
            this.request = request;
            this.executor = executor;
            this.handler = responseHandler;
        }

        public void run() {
            try {
                this.future.completed(this.executor.execute(this.request).handleResponse(this.handler));
            } catch (Exception e) {
                this.future.failed(e);
            }
        }
    }

    Async() {
    }

    public static Async newInstance() {
        return new Async();
    }

    public Future<Content> execute(Request request) {
        return execute(request, new ContentResponseHandler(), null);
    }

    public <T> Future<T> execute(Request request, ResponseHandler<T> responseHandler) {
        return execute(request, responseHandler, null);
    }

    public <T> Future<T> execute(Request request, ResponseHandler<T> responseHandler, FutureCallback<T> futureCallback) {
        BasicFuture basicFuture = new BasicFuture(futureCallback);
        Runnable execRunnable = new ExecRunnable(basicFuture, request, this.executor != null ? this.executor : Executor.newInstance(), responseHandler);
        if (this.concurrentExec != null) {
            this.concurrentExec.execute(execRunnable);
            return basicFuture;
        }
        Thread thread = new Thread(execRunnable);
        thread.setDaemon(true);
        thread.start();
        return basicFuture;
    }

    public Future<Content> execute(Request request, FutureCallback<Content> futureCallback) {
        return execute(request, new ContentResponseHandler(), futureCallback);
    }

    public Async use(Executor executor) {
        this.concurrentExec = executor;
        return this;
    }

    public Async use(Executor executor) {
        this.executor = executor;
        return this;
    }
}
