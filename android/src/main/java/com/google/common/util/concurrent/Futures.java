package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;

@Beta
public final class Futures {
    private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER = new C08675();
    private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf(new C08697()).reverse();

    static final class C08631 implements Function<Exception, X> {
        C08631() {
        }

        public X apply(Exception exception) {
            throw new AssertionError("impossible");
        }
    }

    static final class C08675 implements AsyncFunction<ListenableFuture<Object>, Object> {
        C08675() {
        }

        public ListenableFuture<Object> apply(ListenableFuture<Object> listenableFuture) {
            return listenableFuture;
        }
    }

    static final class C08697 implements Function<Constructor<?>, Boolean> {
        C08697() {
        }

        public Boolean apply(Constructor<?> constructor) {
            return Boolean.valueOf(Arrays.asList(constructor.getParameterTypes()).contains(String.class));
        }
    }

    private static class ChainingListenableFuture<I, O> extends AbstractFuture<O> implements Runnable {
        private AsyncFunction<? super I, ? extends O> function;
        private ListenableFuture<? extends I> inputFuture;
        private final BlockingQueue<Boolean> mayInterruptIfRunningChannel;
        private final CountDownLatch outputCreated;
        private volatile ListenableFuture<? extends O> outputFuture;

        private ChainingListenableFuture(AsyncFunction<? super I, ? extends O> asyncFunction, ListenableFuture<? extends I> listenableFuture) {
            this.mayInterruptIfRunningChannel = new LinkedBlockingQueue(1);
            this.outputCreated = new CountDownLatch(1);
            this.function = (AsyncFunction) Preconditions.checkNotNull(asyncFunction);
            this.inputFuture = (ListenableFuture) Preconditions.checkNotNull(listenableFuture);
        }

        private void cancel(@Nullable Future<?> future, boolean z) {
            if (future != null) {
                future.cancel(z);
            }
        }

        public boolean cancel(boolean z) {
            if (!super.cancel(z)) {
                return false;
            }
            Uninterruptibles.putUninterruptibly(this.mayInterruptIfRunningChannel, Boolean.valueOf(z));
            cancel(this.inputFuture, z);
            cancel(this.outputFuture, z);
            return true;
        }

        public void run() {
            try {
                try {
                    final ListenableFuture apply = this.function.apply(Uninterruptibles.getUninterruptibly(this.inputFuture));
                    this.outputFuture = apply;
                    if (isCancelled()) {
                        apply.cancel(((Boolean) Uninterruptibles.takeUninterruptibly(this.mayInterruptIfRunningChannel)).booleanValue());
                        this.outputFuture = null;
                        return;
                    }
                    apply.addListener(new Runnable() {
                        public void run() {
                            try {
                                ChainingListenableFuture.this.set(Uninterruptibles.getUninterruptibly(apply));
                            } catch (CancellationException e) {
                                ChainingListenableFuture.this.cancel(false);
                            } catch (ExecutionException e2) {
                                ChainingListenableFuture.this.setException(e2.getCause());
                            } finally {
                                ChainingListenableFuture.this.outputFuture = null;
                            }
                        }
                    }, MoreExecutors.sameThreadExecutor());
                    this.function = null;
                    this.inputFuture = null;
                    this.outputCreated.countDown();
                } catch (UndeclaredThrowableException e) {
                    setException(e.getCause());
                } catch (Throwable e2) {
                    setException(e2);
                } catch (Throwable e22) {
                    setException(e22);
                } finally {
                    this.function = null;
                    this.inputFuture = null;
                    this.outputCreated.countDown();
                }
            } catch (CancellationException e3) {
                cancel(false);
                this.function = null;
                this.inputFuture = null;
                this.outputCreated.countDown();
            } catch (ExecutionException e4) {
                setException(e4.getCause());
                this.function = null;
                this.inputFuture = null;
                this.outputCreated.countDown();
            }
        }
    }

    private static class ListFuture<V> extends AbstractFuture<List<V>> {
        final boolean allMustSucceed;
        ImmutableList<? extends ListenableFuture<? extends V>> futures;
        final AtomicInteger remaining;
        List<V> values;

        class C08711 implements Runnable {
            C08711() {
            }

            public void run() {
                ListFuture.this.values = null;
                ListFuture.this.futures = null;
            }
        }

        ListFuture(ImmutableList<? extends ListenableFuture<? extends V>> immutableList, boolean z, Executor executor) {
            this.futures = immutableList;
            this.values = Lists.newArrayListWithCapacity(immutableList.size());
            this.allMustSucceed = z;
            this.remaining = new AtomicInteger(immutableList.size());
            init(executor);
        }

        private void init(Executor executor) {
            int i = 0;
            addListener(new C08711(), MoreExecutors.sameThreadExecutor());
            if (this.futures.isEmpty()) {
                set(Lists.newArrayList(this.values));
                return;
            }
            for (int i2 = 0; i2 < this.futures.size(); i2++) {
                this.values.add(null);
            }
            ImmutableList immutableList = this.futures;
            while (i < immutableList.size()) {
                final ListenableFuture listenableFuture = (ListenableFuture) immutableList.get(i);
                listenableFuture.addListener(new Runnable() {
                    public void run() {
                        ListFuture.this.setOneValue(i, listenableFuture);
                    }
                }, executor);
                i++;
            }
        }

        private void setOneValue(int r6, java.util.concurrent.Future<? extends V> r7) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0013 in list [B:24:0x0060]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
            /*
            r5 = this;
            r1 = 1;
            r0 = 0;
            r2 = r5.values;
            r3 = r5.isDone();
            if (r3 != 0) goto L_0x000c;
        L_0x000a:
            if (r2 != 0) goto L_0x0014;
        L_0x000c:
            r0 = r5.allMustSucceed;
            r1 = "Future was done before all dependencies completed";
            com.google.common.base.Preconditions.checkState(r0, r1);
        L_0x0013:
            return;
        L_0x0014:
            r3 = r7.isDone();	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
            r4 = "Tried to set value from future which is not done";	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
            com.google.common.base.Preconditions.checkState(r3, r4);	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
            r3 = com.google.common.util.concurrent.Uninterruptibles.getUninterruptibly(r7);	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
            r2.set(r6, r3);	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
            r2 = r5.remaining;
            r2 = r2.decrementAndGet();
            if (r2 < 0) goto L_0x002d;
        L_0x002c:
            r0 = r1;
        L_0x002d:
            r1 = "Less than 0 remaining futures";
            com.google.common.base.Preconditions.checkState(r0, r1);
            if (r2 != 0) goto L_0x0013;
        L_0x0034:
            r0 = r5.values;
            if (r0 == 0) goto L_0x0040;
        L_0x0038:
            r0 = com.google.common.collect.Lists.newArrayList(r0);
            r5.set(r0);
            goto L_0x0013;
        L_0x0040:
            r0 = r5.isDone();
            com.google.common.base.Preconditions.checkState(r0);
            goto L_0x0013;
        L_0x0048:
            r2 = move-exception;
            r2 = r5.allMustSucceed;	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
            if (r2 == 0) goto L_0x0051;	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
        L_0x004d:
            r2 = 0;	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
            r5.cancel(r2);	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
        L_0x0051:
            r2 = r5.remaining;
            r2 = r2.decrementAndGet();
            if (r2 < 0) goto L_0x006c;
        L_0x0059:
            r0 = "Less than 0 remaining futures";
            com.google.common.base.Preconditions.checkState(r1, r0);
            if (r2 != 0) goto L_0x0013;
        L_0x0060:
            r0 = r5.values;
            if (r0 == 0) goto L_0x006e;
        L_0x0064:
            r0 = com.google.common.collect.Lists.newArrayList(r0);
            r5.set(r0);
            goto L_0x0013;
        L_0x006c:
            r1 = r0;
            goto L_0x0059;
        L_0x006e:
            r0 = r5.isDone();
            com.google.common.base.Preconditions.checkState(r0);
            goto L_0x0013;
        L_0x0076:
            r2 = move-exception;
            r3 = r5.allMustSucceed;	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
            if (r3 == 0) goto L_0x0082;	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
        L_0x007b:
            r2 = r2.getCause();	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
            r5.setException(r2);	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
        L_0x0082:
            r2 = r5.remaining;
            r2 = r2.decrementAndGet();
            if (r2 < 0) goto L_0x009e;
        L_0x008a:
            r0 = "Less than 0 remaining futures";
            com.google.common.base.Preconditions.checkState(r1, r0);
            if (r2 != 0) goto L_0x0013;
        L_0x0091:
            r0 = r5.values;
            if (r0 == 0) goto L_0x00a0;
        L_0x0095:
            r0 = com.google.common.collect.Lists.newArrayList(r0);
            r5.set(r0);
            goto L_0x0013;
        L_0x009e:
            r1 = r0;
            goto L_0x008a;
        L_0x00a0:
            r0 = r5.isDone();
            com.google.common.base.Preconditions.checkState(r0);
            goto L_0x0013;
        L_0x00a9:
            r2 = move-exception;
            r3 = r5.allMustSucceed;	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
            if (r3 == 0) goto L_0x00b1;	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
        L_0x00ae:
            r5.setException(r2);	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
        L_0x00b1:
            r2 = r5.remaining;
            r2 = r2.decrementAndGet();
            if (r2 < 0) goto L_0x00cd;
        L_0x00b9:
            r0 = "Less than 0 remaining futures";
            com.google.common.base.Preconditions.checkState(r1, r0);
            if (r2 != 0) goto L_0x0013;
        L_0x00c0:
            r0 = r5.values;
            if (r0 == 0) goto L_0x00cf;
        L_0x00c4:
            r0 = com.google.common.collect.Lists.newArrayList(r0);
            r5.set(r0);
            goto L_0x0013;
        L_0x00cd:
            r1 = r0;
            goto L_0x00b9;
        L_0x00cf:
            r0 = r5.isDone();
            com.google.common.base.Preconditions.checkState(r0);
            goto L_0x0013;
        L_0x00d8:
            r2 = move-exception;
            r5.setException(r2);	 Catch:{ CancellationException -> 0x0048, ExecutionException -> 0x0076, RuntimeException -> 0x00a9, Error -> 0x00d8, all -> 0x0103 }
            r2 = r5.remaining;
            r2 = r2.decrementAndGet();
            if (r2 < 0) goto L_0x00f8;
        L_0x00e4:
            r0 = "Less than 0 remaining futures";
            com.google.common.base.Preconditions.checkState(r1, r0);
            if (r2 != 0) goto L_0x0013;
        L_0x00eb:
            r0 = r5.values;
            if (r0 == 0) goto L_0x00fa;
        L_0x00ef:
            r0 = com.google.common.collect.Lists.newArrayList(r0);
            r5.set(r0);
            goto L_0x0013;
        L_0x00f8:
            r1 = r0;
            goto L_0x00e4;
        L_0x00fa:
            r0 = r5.isDone();
            com.google.common.base.Preconditions.checkState(r0);
            goto L_0x0013;
        L_0x0103:
            r2 = move-exception;
            r3 = r5.remaining;
            r3 = r3.decrementAndGet();
            if (r3 < 0) goto L_0x011f;
        L_0x010c:
            r0 = "Less than 0 remaining futures";
            com.google.common.base.Preconditions.checkState(r1, r0);
            if (r3 != 0) goto L_0x011e;
        L_0x0113:
            r0 = r5.values;
            if (r0 == 0) goto L_0x0121;
        L_0x0117:
            r0 = com.google.common.collect.Lists.newArrayList(r0);
            r5.set(r0);
        L_0x011e:
            throw r2;
        L_0x011f:
            r1 = r0;
            goto L_0x010c;
        L_0x0121:
            r0 = r5.isDone();
            com.google.common.base.Preconditions.checkState(r0);
            goto L_0x011e;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.Futures.ListFuture.setOneValue(int, java.util.concurrent.Future):void");
        }
    }

    private static class MappingCheckedFuture<V, X extends Exception> extends AbstractCheckedFuture<V, X> {
        final Function<Exception, X> mapper;

        MappingCheckedFuture(ListenableFuture<V> listenableFuture, Function<Exception, X> function) {
            super(listenableFuture);
            this.mapper = (Function) Preconditions.checkNotNull(function);
        }

        protected X mapException(Exception exception) {
            return (Exception) this.mapper.apply(exception);
        }
    }

    private Futures() {
    }

    public static <V> void addCallback(ListenableFuture<V> listenableFuture, FutureCallback<? super V> futureCallback) {
        addCallback(listenableFuture, futureCallback, MoreExecutors.sameThreadExecutor());
    }

    public static <V> void addCallback(final ListenableFuture<V> listenableFuture, final FutureCallback<? super V> futureCallback, Executor executor) {
        Preconditions.checkNotNull(futureCallback);
        listenableFuture.addListener(new Runnable() {
            public void run() {
                try {
                    futureCallback.onSuccess(Uninterruptibles.getUninterruptibly(listenableFuture));
                } catch (ExecutionException e) {
                    futureCallback.onFailure(e.getCause());
                } catch (Throwable e2) {
                    futureCallback.onFailure(e2);
                } catch (Throwable e22) {
                    futureCallback.onFailure(e22);
                }
            }
        }, executor);
    }

    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(Iterable<? extends ListenableFuture<? extends V>> iterable) {
        return new ListFuture(ImmutableList.copyOf((Iterable) iterable), true, MoreExecutors.sameThreadExecutor());
    }

    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(ListenableFuture<? extends V>... listenableFutureArr) {
        return new ListFuture(ImmutableList.copyOf((Object[]) listenableFutureArr), true, MoreExecutors.sameThreadExecutor());
    }

    @Beta
    public static <V> ListenableFuture<V> dereference(ListenableFuture<? extends ListenableFuture<? extends V>> listenableFuture) {
        return transform((ListenableFuture) listenableFuture, DEREFERENCER);
    }

    @Beta
    public static <V, X extends Exception> V get(Future<V> future, long j, TimeUnit timeUnit, Class<X> cls) throws Exception {
        Preconditions.checkNotNull(future);
        Preconditions.checkNotNull(timeUnit);
        Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(cls), "Futures.get exception type (%s) must not be a RuntimeException", cls);
        try {
            return future.get(j, timeUnit);
        } catch (Throwable e) {
            Thread.currentThread().interrupt();
            throw newWithCause(cls, e);
        } catch (Throwable e2) {
            throw newWithCause(cls, e2);
        } catch (ExecutionException e3) {
            wrapAndThrowExceptionOrError(e3.getCause(), cls);
            throw new AssertionError();
        }
    }

    @Beta
    public static <V, X extends Exception> V get(Future<V> future, Class<X> cls) throws Exception {
        Preconditions.checkNotNull(future);
        Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(cls), "Futures.get exception type (%s) must not be a RuntimeException", cls);
        try {
            return future.get();
        } catch (Throwable e) {
            Thread.currentThread().interrupt();
            throw newWithCause(cls, e);
        } catch (ExecutionException e2) {
            wrapAndThrowExceptionOrError(e2.getCause(), cls);
            throw new AssertionError();
        }
    }

    @Beta
    public static <V> V getUnchecked(Future<V> future) {
        Preconditions.checkNotNull(future);
        try {
            return Uninterruptibles.getUninterruptibly(future);
        } catch (ExecutionException e) {
            wrapAndThrowUnchecked(e.getCause());
            throw new AssertionError();
        }
    }

    public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable V v) {
        ListenableFuture create = SettableFuture.create();
        create.set(v);
        return makeChecked(create, new C08631());
    }

    public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(final X x) {
        Preconditions.checkNotNull(x);
        return makeChecked(immediateFailedFuture(x), new Function<Exception, X>() {
            public X apply(Exception exception) {
                return x;
            }
        });
    }

    public static <V> ListenableFuture<V> immediateFailedFuture(Throwable th) {
        Preconditions.checkNotNull(th);
        ListenableFuture create = SettableFuture.create();
        create.setException(th);
        return create;
    }

    public static <V> ListenableFuture<V> immediateFuture(@Nullable V v) {
        ListenableFuture create = SettableFuture.create();
        create.set(v);
        return create;
    }

    @Beta
    public static <I, O> Future<O> lazyTransform(final Future<I> future, final Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(future);
        Preconditions.checkNotNull(function);
        return new Future<O>() {
            private O applyTransformation(I i) throws ExecutionException {
                try {
                    return function.apply(i);
                } catch (Throwable th) {
                    ExecutionException executionException = new ExecutionException(th);
                }
            }

            public boolean cancel(boolean z) {
                return future.cancel(z);
            }

            public O get() throws InterruptedException, ExecutionException {
                return applyTransformation(future.get());
            }

            public O get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
                return applyTransformation(future.get(j, timeUnit));
            }

            public boolean isCancelled() {
                return future.isCancelled();
            }

            public boolean isDone() {
                return future.isDone();
            }
        };
    }

    public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(ListenableFuture<V> listenableFuture, Function<Exception, X> function) {
        return new MappingCheckedFuture((ListenableFuture) Preconditions.checkNotNull(listenableFuture), function);
    }

    @Nullable
    private static <X> X newFromConstructor(Constructor<X> constructor, Throwable th) {
        Class[] parameterTypes = constructor.getParameterTypes();
        Object[] objArr = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            Object obj = parameterTypes[i];
            if (!obj.equals(String.class)) {
                if (!obj.equals(Throwable.class)) {
                    X x = null;
                    break;
                }
                objArr[i] = th;
            } else {
                objArr[i] = th.toString();
            }
        }
        try {
            x = constructor.newInstance(objArr);
            return x;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (InstantiationException e2) {
            return null;
        } catch (IllegalAccessException e3) {
            return null;
        } catch (InvocationTargetException e4) {
            return null;
        }
    }

    private static <X extends Exception> X newWithCause(Class<X> cls, Throwable th) {
        for (Constructor newFromConstructor : preferringStrings(Arrays.asList(cls.getConstructors()))) {
            Exception exception = (Exception) newFromConstructor(newFromConstructor, th);
            if (exception != null) {
                if (exception.getCause() == null) {
                    exception.initCause(th);
                }
                return exception;
            }
        }
        throw new IllegalArgumentException("No appropriate constructor for exception of type " + cls + " in response to chained exception", th);
    }

    private static <X extends Exception> List<Constructor<X>> preferringStrings(List<Constructor<X>> list) {
        return WITH_STRING_PARAM_FIRST.sortedCopy(list);
    }

    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(Iterable<? extends ListenableFuture<? extends V>> iterable) {
        return new ListFuture(ImmutableList.copyOf((Iterable) iterable), false, MoreExecutors.sameThreadExecutor());
    }

    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(ListenableFuture<? extends V>... listenableFutureArr) {
        return new ListFuture(ImmutableList.copyOf((Object[]) listenableFutureArr), false, MoreExecutors.sameThreadExecutor());
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> listenableFuture, Function<? super I, ? extends O> function) {
        return transform((ListenableFuture) listenableFuture, (Function) function, MoreExecutors.sameThreadExecutor());
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> listenableFuture, final Function<? super I, ? extends O> function, Executor executor) {
        Preconditions.checkNotNull(function);
        return transform((ListenableFuture) listenableFuture, new AsyncFunction<I, O>() {
            public ListenableFuture<O> apply(I i) {
                return Futures.immediateFuture(function.apply(i));
            }
        }, executor);
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> listenableFuture, AsyncFunction<? super I, ? extends O> asyncFunction) {
        return transform((ListenableFuture) listenableFuture, (AsyncFunction) asyncFunction, MoreExecutors.sameThreadExecutor());
    }

    public static <I, O> ListenableFuture<O> transform(ListenableFuture<I> listenableFuture, AsyncFunction<? super I, ? extends O> asyncFunction, Executor executor) {
        Object chainingListenableFuture = new ChainingListenableFuture(asyncFunction, listenableFuture);
        listenableFuture.addListener(chainingListenableFuture, executor);
        return chainingListenableFuture;
    }

    private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable th, Class<X> cls) throws Exception {
        if (th instanceof Error) {
            throw new ExecutionError((Error) th);
        } else if (th instanceof RuntimeException) {
            throw new UncheckedExecutionException(th);
        } else {
            throw newWithCause(cls, th);
        }
    }

    private static void wrapAndThrowUnchecked(Throwable th) {
        if (th instanceof Error) {
            throw new ExecutionError((Error) th);
        }
        throw new UncheckedExecutionException(th);
    }
}
