package org.apache.http.impl;

import java.util.Locale;
import org.apache.http.ReasonPhraseCatalog;
import org.apache.http.annotation.Immutable;
import org.mortbay.jetty.HttpStatus;

@Immutable
public class EnglishReasonPhraseCatalog implements ReasonPhraseCatalog {
    public static final EnglishReasonPhraseCatalog INSTANCE = new EnglishReasonPhraseCatalog();
    private static final String[][] REASON_PHRASES = new String[][]{null, new String[3], new String[8], new String[8], new String[25], new String[8]};

    static {
        setReason(200, HttpStatus.OK);
        setReason(201, HttpStatus.Created);
        setReason(202, HttpStatus.Accepted);
        setReason(204, HttpStatus.No_Content);
        setReason(301, HttpStatus.Moved_Permanently);
        setReason(302, HttpStatus.Moved_Temporarily);
        setReason(304, HttpStatus.Not_Modified);
        setReason(400, HttpStatus.Bad_Request);
        setReason(401, HttpStatus.Unauthorized);
        setReason(403, HttpStatus.Forbidden);
        setReason(404, HttpStatus.Not_Found);
        setReason(500, HttpStatus.Internal_Server_Error);
        setReason(501, HttpStatus.Not_Implemented);
        setReason(502, HttpStatus.Bad_Gateway);
        setReason(503, HttpStatus.Service_Unavailable);
        setReason(100, HttpStatus.Continue);
        setReason(307, "Temporary Redirect");
        setReason(405, HttpStatus.Method_Not_Allowed);
        setReason(409, HttpStatus.Conflict);
        setReason(412, HttpStatus.Precondition_Failed);
        setReason(413, "Request Too Long");
        setReason(414, "Request-URI Too Long");
        setReason(415, HttpStatus.Unsupported_Media_Type);
        setReason(300, HttpStatus.Multiple_Choices);
        setReason(303, HttpStatus.See_Other);
        setReason(305, HttpStatus.Use_Proxy);
        setReason(402, HttpStatus.Payment_Required);
        setReason(406, HttpStatus.Not_Acceptable);
        setReason(407, HttpStatus.Proxy_Authentication_Required);
        setReason(408, HttpStatus.Request_Timeout);
        setReason(101, HttpStatus.Switching_Protocols);
        setReason(203, HttpStatus.Non_Authoritative_Information);
        setReason(205, HttpStatus.Reset_Content);
        setReason(206, HttpStatus.Partial_Content);
        setReason(504, HttpStatus.Gateway_Timeout);
        setReason(505, "Http Version Not Supported");
        setReason(410, HttpStatus.Gone);
        setReason(411, HttpStatus.Length_Required);
        setReason(416, HttpStatus.Requested_Range_Not_Satisfiable);
        setReason(417, HttpStatus.Expectation_Failed);
        setReason(102, HttpStatus.Processing);
        setReason(207, "Multi-Status");
        setReason(422, HttpStatus.Unprocessable_Entity);
        setReason(org.apache.http.HttpStatus.SC_INSUFFICIENT_SPACE_ON_RESOURCE, "Insufficient Space On Resource");
        setReason(org.apache.http.HttpStatus.SC_METHOD_FAILURE, "Method Failure");
        setReason(423, HttpStatus.Locked);
        setReason(507, HttpStatus.Insufficient_Storage);
        setReason(424, HttpStatus.Failed_Dependency);
    }

    protected EnglishReasonPhraseCatalog() {
    }

    private static void setReason(int i, String str) {
        int i2 = i / 100;
        REASON_PHRASES[i2][i - (i2 * 100)] = str;
    }

    public String getReason(int i, Locale locale) {
        if (i < 100 || i >= 600) {
            throw new IllegalArgumentException("Unknown category for status code " + i + ".");
        }
        int i2 = i / 100;
        int i3 = i - (i2 * 100);
        return REASON_PHRASES[i2].length > i3 ? REASON_PHRASES[i2][i3] : null;
    }
}
