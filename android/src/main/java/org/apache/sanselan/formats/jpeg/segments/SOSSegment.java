package org.apache.sanselan.formats.jpeg.segments;

import java.io.IOException;
import java.io.InputStream;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.util.Debug;
import org.mortbay.jetty.HttpVersions;

public class SOSSegment extends Segment {
    public SOSSegment(int i, int i2, InputStream inputStream) throws ImageReadException, IOException {
        super(i, i2);
        if (getDebug()) {
            System.out.println("SOSSegment marker_length: " + i2);
        }
        Debug.debug("SOS", i2);
        int readByte = readByte("number_of_components_in_scan", inputStream, "Not a Valid JPEG File");
        Debug.debug("number_of_components_in_scan", readByte);
        for (int i3 = 0; i3 < readByte; i3++) {
            Debug.debug("scan_component_selector", readByte("scan_component_selector", inputStream, "Not a Valid JPEG File"));
            Debug.debug("ac_dc_entrooy_coding_table_selector", readByte("ac_dc_entrooy_coding_table_selector", inputStream, "Not a Valid JPEG File"));
        }
        Debug.debug("start_of_spectral_selection", readByte("start_of_spectral_selection", inputStream, "Not a Valid JPEG File"));
        Debug.debug("end_of_spectral_selection", readByte("end_of_spectral_selection", inputStream, "Not a Valid JPEG File"));
        Debug.debug("successive_approximation_bit_position", readByte("successive_approximation_bit_position", inputStream, "Not a Valid JPEG File"));
        if (getDebug()) {
            System.out.println(HttpVersions.HTTP_0_9);
        }
    }

    public String getDescription() {
        return "SOS (" + getSegmentType() + ")";
    }
}
