package com.google.api.client.googleapis.extensions.servlet.subscriptions;

import com.google.api.client.googleapis.subscriptions.NotificationHeaders;
import com.google.api.client.googleapis.subscriptions.SubscriptionStore;
import com.google.api.client.googleapis.subscriptions.UnparsedNotification;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractWebHookServlet extends HttpServlet {
    public static final String UNSUBSCRIBE_HEADER = "X-Goog-Unsubscribe";
    private static SubscriptionStore subscriptionStore;

    protected abstract SubscriptionStore createSubscriptionStore();

    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        httpServletResponse.sendError(405);
    }

    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String header = httpServletRequest.getHeader(NotificationHeaders.SUBSCRIPTION_ID);
        String header2 = httpServletRequest.getHeader(NotificationHeaders.TOPIC_ID);
        String header3 = httpServletRequest.getHeader(NotificationHeaders.TOPIC_URI);
        String header4 = httpServletRequest.getHeader(NotificationHeaders.EVENT_TYPE_HEADER);
        String header5 = httpServletRequest.getHeader(NotificationHeaders.CLIENT_TOKEN);
        String header6 = httpServletRequest.getHeader(NotificationHeaders.MESSAGE_NUMBER_HEADER);
        String header7 = httpServletRequest.getHeader(NotificationHeaders.CHANGED_HEADER);
        if (header == null || header2 == null || header3 == null || header4 == null || header6 == null) {
            httpServletResponse.sendError(400, "Notification did not contain all required information.");
            return;
        }
        ServletInputStream inputStream = httpServletRequest.getInputStream();
        try {
            UnparsedNotification unparsedNotification = new UnparsedNotification(header, header2, header3, header5, Long.valueOf(header6).longValue(), header4, header7, httpServletRequest.getContentType(), inputStream);
            if (!unparsedNotification.deliverNotification(getSubscriptionStore())) {
                sendUnsubscribeResponse(httpServletResponse, unparsedNotification);
            }
            inputStream.close();
        } catch (Throwable th) {
            inputStream.close();
        }
    }

    public final SubscriptionStore getSubscriptionStore() {
        SubscriptionStore subscriptionStore;
        synchronized (this) {
            if (subscriptionStore == null) {
                subscriptionStore = createSubscriptionStore();
            }
            subscriptionStore = subscriptionStore;
        }
        return subscriptionStore;
    }

    protected void sendUnsubscribeResponse(HttpServletResponse httpServletResponse, UnparsedNotification unparsedNotification) {
        httpServletResponse.setStatus(200);
        httpServletResponse.setHeader(UNSUBSCRIBE_HEADER, unparsedNotification.getSubscriptionId());
    }
}
