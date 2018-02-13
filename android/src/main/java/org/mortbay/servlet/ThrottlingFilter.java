package org.mortbay.servlet;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.log.Log;
import org.mortbay.util.ajax.Continuation;

public class ThrottlingFilter implements Filter {
    private int _current = 0;
    private final Object _lock = new Object();
    private int _maximum;
    private final List _queue = new LinkedList();
    private long _queueSize;
    private long _queueTimeout;

    private boolean acceptRequest() {
        synchronized (this._lock) {
            if (this._current < this._maximum) {
                this._current++;
                return true;
            }
            return false;
        }
    }

    private void dropFromQueue(Continuation continuation) {
        this._queue.remove(continuation);
        continuation.reset();
    }

    private Continuation getContinuation(ServletRequest servletRequest) {
        return (Continuation) servletRequest.getAttribute("org.mortbay.jetty.ajax.Continuation");
    }

    private int getIntegerParameter(FilterConfig filterConfig, String str, int i) throws ServletException {
        String initParameter = filterConfig.getInitParameter(str);
        if (initParameter != null) {
            try {
                i = Integer.parseInt(initParameter);
            } catch (NumberFormatException e) {
                throw new ServletException(new StringBuffer().append("Parameter ").append(str).append(" must be a number (was ").append(initParameter).append(" instead)").toString());
            }
        }
        return i;
    }

    private void popQueue() {
        synchronized (this._queue) {
            if (this._queue.isEmpty()) {
                return;
            }
            Continuation continuation = (Continuation) this._queue.remove(0);
            Log.debug("Resuming continuation {}", continuation, null);
            continuation.resume();
        }
    }

    private boolean queueRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Continuation continuation) throws IOException, ServletException {
        synchronized (this._queue) {
            if (((long) this._queue.size()) >= this._queueSize) {
                Log.debug("Queue is full, rejecting request {}", httpServletRequest.getRequestURI(), null);
                return false;
            }
            Log.debug("Queuing request {} / {}", httpServletRequest.getRequestURI(), continuation);
            this._queue.add(continuation);
            continuation.suspend(this._queueTimeout);
            Log.debug("Resuming blocking continuation for request {}", httpServletRequest.getRequestURI(), null);
            return true;
        }
    }

    private void releaseRequest() {
        synchronized (this._lock) {
            this._current--;
        }
    }

    public void destroy() {
        this._queue.clear();
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
    }

    public void doFilter(javax.servlet.http.HttpServletRequest r5, javax.servlet.http.HttpServletResponse r6, javax.servlet.FilterChain r7) throws java.io.IOException, javax.servlet.ServletException {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x002d in list [B:10:0x0027]
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r4 = this;
        r0 = r4.getContinuation(r5);
        r1 = 0;
        r1 = r4.acceptRequest();	 Catch:{ all -> 0x003d }
        if (r1 != 0) goto L_0x0020;	 Catch:{ all -> 0x003d }
    L_0x000b:
        r2 = r0.isPending();	 Catch:{ all -> 0x003d }
        if (r2 == 0) goto L_0x002e;	 Catch:{ all -> 0x003d }
    L_0x0011:
        r2 = "Request {} / {} was already queued, rejecting";	 Catch:{ all -> 0x003d }
        r3 = r5.getRequestURI();	 Catch:{ all -> 0x003d }
        org.mortbay.log.Log.debug(r2, r3, r0);	 Catch:{ all -> 0x003d }
        r4.dropFromQueue(r0);	 Catch:{ all -> 0x003d }
        r0.reset();	 Catch:{ all -> 0x003d }
    L_0x0020:
        if (r1 == 0) goto L_0x0039;	 Catch:{ all -> 0x003d }
    L_0x0022:
        r7.doFilter(r5, r6);	 Catch:{ all -> 0x003d }
    L_0x0025:
        if (r1 == 0) goto L_0x002d;
    L_0x0027:
        r4.releaseRequest();
        r4.popQueue();
    L_0x002d:
        return;
    L_0x002e:
        r0 = r4.queueRequest(r5, r6, r0);	 Catch:{ all -> 0x003d }
        if (r0 == 0) goto L_0x0020;	 Catch:{ all -> 0x003d }
    L_0x0034:
        r1 = r4.acceptRequest();	 Catch:{ all -> 0x003d }
        goto L_0x0020;	 Catch:{ all -> 0x003d }
    L_0x0039:
        r4.rejectRequest(r5, r6);	 Catch:{ all -> 0x003d }
        goto L_0x0025;
    L_0x003d:
        r0 = move-exception;
        if (r1 == 0) goto L_0x0046;
    L_0x0040:
        r4.releaseRequest();
        r4.popQueue();
    L_0x0046:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.mortbay.servlet.ThrottlingFilter.doFilter(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain):void");
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this._maximum = getIntegerParameter(filterConfig, "maximum", 10);
        this._queueTimeout = (long) getIntegerParameter(filterConfig, "block", 5000);
        this._queueSize = (long) getIntegerParameter(filterConfig, "queue", 500);
        if (this._queueTimeout == -1) {
            this._queueTimeout = 2147483647L;
        }
        Log.debug(new StringBuffer().append("Config{maximum:").append(this._maximum).append(", block:").append(this._queueTimeout).append(", queue:").append(this._queueSize).append("}").toString(), null, null);
    }

    protected void rejectRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendError(503, new StringBuffer().append("Too many active connections to resource ").append(httpServletRequest.getRequestURI()).toString());
    }
}
