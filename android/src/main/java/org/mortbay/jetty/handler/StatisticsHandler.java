package org.mortbay.jetty.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Response;

public class StatisticsHandler extends AbstractStatisticsHandler {
    private transient long _maxRequestTime;
    private transient long _minRequestTime;
    private transient int _requests;
    private transient int _requestsActive;
    private transient int _requestsActiveMax;
    private transient int _responses1xx;
    private transient int _responses2xx;
    private transient int _responses3xx;
    private transient int _responses4xx;
    private transient int _responses5xx;
    private transient long _statsStartedAt;
    private transient long _totalRequestTime;

    public long getRequestTimeAverage() {
        long j;
        synchronized (this) {
            j = this._requests == 0 ? 0 : this._totalRequestTime / ((long) this._requests);
        }
        return j;
    }

    public long getRequestTimeMax() {
        long j;
        synchronized (this) {
            j = this._maxRequestTime;
        }
        return j;
    }

    public long getRequestTimeMin() {
        long j;
        synchronized (this) {
            j = this._minRequestTime;
        }
        return j;
    }

    public long getRequestTimeTotal() {
        long j;
        synchronized (this) {
            j = this._totalRequestTime;
        }
        return j;
    }

    public int getRequests() {
        int i;
        synchronized (this) {
            i = this._requests;
        }
        return i;
    }

    public int getRequestsActive() {
        int i;
        synchronized (this) {
            i = this._requestsActive;
        }
        return i;
    }

    public int getRequestsActiveMax() {
        int i;
        synchronized (this) {
            i = this._requestsActiveMax;
        }
        return i;
    }

    public int getResponses1xx() {
        int i;
        synchronized (this) {
            i = this._responses1xx;
        }
        return i;
    }

    public int getResponses2xx() {
        int i;
        synchronized (this) {
            i = this._responses2xx;
        }
        return i;
    }

    public int getResponses3xx() {
        int i;
        synchronized (this) {
            i = this._responses3xx;
        }
        return i;
    }

    public int getResponses4xx() {
        int i;
        synchronized (this) {
            i = this._responses4xx;
        }
        return i;
    }

    public int getResponses5xx() {
        int i;
        synchronized (this) {
            i = this._responses5xx;
        }
        return i;
    }

    public long getStatsOnMs() {
        long currentTimeMillis;
        long j;
        synchronized (this) {
            currentTimeMillis = System.currentTimeMillis();
            j = this._statsStartedAt;
        }
        return currentTimeMillis - j;
    }

    public void handle(String str, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, int i) throws IOException, ServletException {
        synchronized (this) {
            this._requests++;
            this._requestsActive++;
            if (this._requestsActive > this._requestsActiveMax) {
                this._requestsActiveMax = this._requestsActive;
            }
        }
        long currentTimeMillis = System.currentTimeMillis();
        try {
            super.handle(str, httpServletRequest, httpServletResponse, i);
            currentTimeMillis = System.currentTimeMillis() - currentTimeMillis;
            synchronized (this) {
                this._requestsActive--;
                if (this._requestsActive < 0) {
                    this._requestsActive = 0;
                }
                this._totalRequestTime += currentTimeMillis;
                if (currentTimeMillis < this._minRequestTime || this._minRequestTime == 0) {
                    this._minRequestTime = currentTimeMillis;
                }
                if (currentTimeMillis > this._maxRequestTime) {
                    this._maxRequestTime = currentTimeMillis;
                }
                switch ((httpServletResponse instanceof Response ? (Response) httpServletResponse : HttpConnection.getCurrentConnection().getResponse()).getStatus() / 100) {
                    case 1:
                        this._responses1xx++;
                        break;
                    case 2:
                        this._responses2xx++;
                        break;
                    case 3:
                        this._responses3xx++;
                        break;
                    case 4:
                        this._responses4xx++;
                        break;
                    case 5:
                        this._responses5xx++;
                        break;
                }
            }
        } catch (Throwable th) {
            currentTimeMillis = System.currentTimeMillis() - currentTimeMillis;
            synchronized (this) {
                this._requestsActive--;
                if (this._requestsActive < 0) {
                    this._requestsActive = 0;
                }
                this._totalRequestTime += currentTimeMillis;
                if (currentTimeMillis < this._minRequestTime || this._minRequestTime == 0) {
                    this._minRequestTime = currentTimeMillis;
                }
                if (currentTimeMillis > this._maxRequestTime) {
                    this._maxRequestTime = currentTimeMillis;
                }
                switch ((httpServletResponse instanceof Response ? (Response) httpServletResponse : HttpConnection.getCurrentConnection().getResponse()).getStatus() / 100) {
                    case 1:
                        this._responses1xx++;
                        break;
                    case 2:
                        this._responses2xx++;
                        break;
                    case 3:
                        this._responses3xx++;
                        break;
                    case 4:
                        this._responses4xx++;
                        break;
                    case 5:
                        this._responses5xx++;
                        break;
                }
            }
        }
    }

    public void statsReset() {
        synchronized (this) {
            this._statsStartedAt = System.currentTimeMillis();
            this._requests = 0;
            this._minRequestTime = 0;
            this._maxRequestTime = 0;
            this._totalRequestTime = 0;
            this._requestsActiveMax = this._requestsActive;
            this._requestsActive = 0;
            this._responses1xx = 0;
            this._responses2xx = 0;
            this._responses3xx = 0;
            this._responses4xx = 0;
            this._responses5xx = 0;
        }
    }
}
