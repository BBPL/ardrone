package org.apache.sanselan.formats.tiff;

import java.util.ArrayList;
import java.util.Collections;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.util.Debug;

public class TiffContents {
    public final ArrayList directories;
    public final TiffHeader header;

    public TiffContents(TiffHeader tiffHeader, ArrayList arrayList) {
        this.header = tiffHeader;
        this.directories = arrayList;
    }

    public void dissect(boolean z) throws ImageReadException {
        ArrayList elements = getElements();
        Collections.sort(elements, TiffElement.COMPARATOR);
        int i = 0;
        int i2 = 0;
        while (i2 < elements.size()) {
            TiffElement tiffElement = (TiffElement) elements.get(i2);
            if (tiffElement.offset > i) {
                Debug.debug("\tgap: " + (tiffElement.offset - i));
            }
            if (tiffElement.offset < i) {
                Debug.debug("\toverlap");
            }
            Debug.debug("element, start: " + tiffElement.offset + ", length: " + tiffElement.length + ", end: " + (tiffElement.offset + tiffElement.length) + ": " + tiffElement.getElementDescription(false));
            if (z) {
                String elementDescription = tiffElement.getElementDescription(true);
                if (elementDescription != null) {
                    Debug.debug(elementDescription);
                }
            }
            i2++;
            i = tiffElement.length + tiffElement.offset;
        }
        Debug.debug("end: " + i);
        Debug.debug();
    }

    public TiffField findField(TagInfo tagInfo) throws ImageReadException {
        for (int i = 0; i < this.directories.size(); i++) {
            TiffField findField = ((TiffDirectory) this.directories.get(i)).findField(tagInfo);
            if (findField != null) {
                return findField;
            }
        }
        return null;
    }

    public ArrayList getElements() throws ImageReadException {
        ArrayList arrayList = new ArrayList();
        arrayList.add(this.header);
        for (int i = 0; i < this.directories.size(); i++) {
            TiffDirectory tiffDirectory = (TiffDirectory) this.directories.get(i);
            arrayList.add(tiffDirectory);
            ArrayList arrayList2 = tiffDirectory.entries;
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                TiffElement oversizeValueElement = ((TiffField) arrayList2.get(i2)).getOversizeValueElement();
                if (oversizeValueElement != null) {
                    arrayList.add(oversizeValueElement);
                }
            }
            if (tiffDirectory.hasTiffImageData()) {
                arrayList.addAll(tiffDirectory.getTiffRawImageDataElements());
            }
            if (tiffDirectory.hasJpegImageData()) {
                arrayList.add(tiffDirectory.getJpegRawImageDataElement());
            }
        }
        return arrayList;
    }
}
