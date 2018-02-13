package org.mortbay.util.ajax;

import javax.servlet.http.HttpServletRequest;

public class ContinuationSupport {
    public static Continuation getContinuation(HttpServletRequest httpServletRequest, Object obj) {
        Continuation continuation = (Continuation) httpServletRequest.getAttribute("org.mortbay.jetty.ajax.Continuation");
        if (continuation == null) {
            return new WaitingContinuation(obj);
        }
        if (!(continuation instanceof WaitingContinuation) || obj == null) {
            return continuation;
        }
        ((WaitingContinuation) continuation).setMutex(obj);
        return continuation;
    }
}
