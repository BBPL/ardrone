package org.apache.sanselan.formats.tiff.write;

import org.apache.sanselan.formats.tiff.TiffElement.DataElement;
import org.apache.sanselan.formats.tiff.write.TiffOutputItem.Value;

class ImageDataOffsets {
    public final int[] imageDataOffsets;
    public final TiffOutputField imageDataOffsetsField;
    public final TiffOutputItem[] outputItems;

    public ImageDataOffsets(DataElement[] dataElementArr, int[] iArr, TiffOutputField tiffOutputField) {
        this.imageDataOffsets = iArr;
        this.imageDataOffsetsField = tiffOutputField;
        this.outputItems = new TiffOutputItem[dataElementArr.length];
        for (int i = 0; i < dataElementArr.length; i++) {
            this.outputItems[i] = new Value("TIFF image data", dataElementArr[i].data);
        }
    }
}
