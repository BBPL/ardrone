package org.apache.sanselan.common;

import java.util.ArrayList;
import org.apache.sanselan.common.IImageMetadata.IImageMetadataItem;
import org.mortbay.jetty.HttpVersions;

public class ImageMetadata implements IImageMetadata {
    protected static final String newline = System.getProperty("line.separator");
    private final ArrayList items = new ArrayList();

    public static class Item implements IImageMetadataItem {
        private final String keyword;
        private final String text;

        public Item(String str, String str2) {
            this.keyword = str;
            this.text = str2;
        }

        public String getKeyword() {
            return this.keyword;
        }

        public String getText() {
            return this.text;
        }

        public String toString() {
            return toString(null);
        }

        public String toString(String str) {
            String str2 = this.keyword + ": " + this.text;
            return str != null ? new StringBuilder(String.valueOf(str)).append(str2).toString() : str2;
        }
    }

    public void add(String str, String str2) {
        add(new Item(str, str2));
    }

    public void add(IImageMetadataItem iImageMetadataItem) {
        this.items.add(iImageMetadataItem);
    }

    public ArrayList getItems() {
        return new ArrayList(this.items);
    }

    public String toString() {
        return toString(null);
    }

    public String toString(String str) {
        if (str == null) {
            Object obj = HttpVersions.HTTP_0_9;
        }
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < this.items.size(); i++) {
            if (i > 0) {
                stringBuffer.append(newline);
            }
            stringBuffer.append(((IImageMetadataItem) this.items.get(i)).toString(new StringBuilder(String.valueOf(obj)).append("\t").toString()));
        }
        return stringBuffer.toString();
    }
}
