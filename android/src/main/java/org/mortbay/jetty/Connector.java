package org.mortbay.jetty;

import java.io.IOException;
import org.mortbay.component.LifeCycle;
import org.mortbay.io.Buffers;
import org.mortbay.io.EndPoint;
import org.mortbay.util.ajax.Continuation;

public interface Connector extends LifeCycle, Buffers {
    void close() throws IOException;

    void customize(EndPoint endPoint, Request request) throws IOException;

    int getConfidentialPort();

    String getConfidentialScheme();

    Object getConnection();

    int getConnections();

    long getConnectionsDurationAve();

    long getConnectionsDurationMax();

    long getConnectionsDurationMin();

    long getConnectionsDurationTotal();

    int getConnectionsOpen();

    int getConnectionsOpenMax();

    int getConnectionsOpenMin();

    int getConnectionsRequestsAve();

    int getConnectionsRequestsMax();

    int getConnectionsRequestsMin();

    int getHeaderBufferSize();

    String getHost();

    int getIntegralPort();

    String getIntegralScheme();

    int getLocalPort();

    int getLowResourceMaxIdleTime();

    int getMaxIdleTime();

    String getName();

    int getPort();

    int getRequestBufferSize();

    int getRequests();

    boolean getResolveNames();

    int getResponseBufferSize();

    Server getServer();

    boolean getStatsOn();

    long getStatsOnMs();

    boolean isConfidential(Request request);

    boolean isIntegral(Request request);

    Continuation newContinuation();

    void open() throws IOException;

    void persist(EndPoint endPoint) throws IOException;

    void setHeaderBufferSize(int i);

    void setHost(String str);

    void setLowResourceMaxIdleTime(int i);

    void setMaxIdleTime(int i);

    void setPort(int i);

    void setRequestBufferSize(int i);

    void setResponseBufferSize(int i);

    void setServer(Server server);

    void setStatsOn(boolean z);

    void statsReset();
}
