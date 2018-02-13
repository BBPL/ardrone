package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ExecutionList {
    private static final Logger log = Logger.getLogger(ExecutionList.class.getName());
    private boolean executed = false;
    private final Queue<RunnableExecutorPair> runnables = Lists.newLinkedList();

    private static class RunnableExecutorPair {
        final Executor executor;
        final Runnable runnable;

        RunnableExecutorPair(Runnable runnable, Executor executor) {
            this.runnable = runnable;
            this.executor = executor;
        }

        void execute() {
            try {
                this.executor.execute(this.runnable);
            } catch (Throwable e) {
                ExecutionList.log.log(Level.SEVERE, "RuntimeException while executing runnable " + this.runnable + " with executor " + this.executor, e);
            }
        }
    }

    public void add(Runnable runnable, Executor executor) {
        Preconditions.checkNotNull(runnable, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");
        Object obj = null;
        synchronized (this.runnables) {
            if (this.executed) {
                obj = 1;
            } else {
                this.runnables.add(new RunnableExecutorPair(runnable, executor));
            }
        }
        if (obj != null) {
            new RunnableExecutorPair(runnable, executor).execute();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void execute() {
        /*
        r2 = this;
        r1 = r2.runnables;
        monitor-enter(r1);
        r0 = r2.executed;	 Catch:{ all -> 0x0021 }
        if (r0 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r1);	 Catch:{ all -> 0x0021 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = 1;
        r2.executed = r0;	 Catch:{ all -> 0x0021 }
        monitor-exit(r1);	 Catch:{ all -> 0x0021 }
    L_0x000d:
        r0 = r2.runnables;
        r0 = r0.isEmpty();
        if (r0 != 0) goto L_0x0008;
    L_0x0015:
        r0 = r2.runnables;
        r0 = r0.poll();
        r0 = (com.google.common.util.concurrent.ExecutionList.RunnableExecutorPair) r0;
        r0.execute();
        goto L_0x000d;
    L_0x0021:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0021 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.ExecutionList.execute():void");
    }
}
