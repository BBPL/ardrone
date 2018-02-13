package com.google.api.services.picasa.model;

import com.google.api.client.util.Key;
import java.util.List;

public class Link {
    @Key("@href")
    public String href;
    @Key("@rel")
    public String rel;

    public static String find(List<Link> list, String str) {
        if (list != null) {
            for (Link link : list) {
                if (str.equals(link.rel)) {
                    return link.href;
                }
            }
        }
        return null;
    }
}
