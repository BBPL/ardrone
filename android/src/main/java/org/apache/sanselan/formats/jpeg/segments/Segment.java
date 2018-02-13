package org.apache.sanselan.formats.jpeg.segments;

import java.io.PrintWriter;
import org.apache.sanselan.common.BinaryFileParser;
import org.apache.sanselan.formats.jpeg.JpegConstants;

public abstract class Segment extends BinaryFileParser {
    public final int length;
    public final int marker;

    public Segment(int i, int i2) {
        this.marker = i;
        this.length = i2;
    }

    public void dump(PrintWriter printWriter) {
    }

    public abstract String getDescription();

    public String getSegmentType() {
        switch (this.marker) {
            case 65281:
                return "For temporary private use in arithmetic coding";
            case JpegConstants.SOF0Marker /*65472*/:
                return "Start Of Frame, Baseline DCT, Huffman coding";
            case JpegConstants.SOF1Marker /*65473*/:
                return "Start Of Frame, Extended sequential DCT, Huffman coding";
            case JpegConstants.SOF2Marker /*65474*/:
                return "Start Of Frame, Progressive DCT, Huffman coding";
            case JpegConstants.SOF3Marker /*65475*/:
                return "Start Of Frame, Lossless (sequential), Huffman coding";
            case JpegConstants.SOF4Marker /*65476*/:
                return "Define Huffman table(s)";
            case JpegConstants.SOF5Marker /*65477*/:
                return "Start Of Frame, Differential sequential DCT, Huffman coding";
            case JpegConstants.SOF6Marker /*65478*/:
                return "Start Of Frame, Differential progressive DCT, Huffman coding";
            case JpegConstants.SOF7Marker /*65479*/:
                return "Start Of Frame, Differential lossless (sequential), Huffman coding";
            case JpegConstants.SOF8Marker /*65480*/:
                return "Start Of Frame, Reserved for JPEG extensions, arithmetic coding";
            case JpegConstants.SOF9Marker /*65481*/:
                return "Start Of Frame, Extended sequential DCT, arithmetic coding";
            case JpegConstants.SOF10Marker /*65482*/:
                return "Start Of Frame, Progressive DCT, arithmetic coding";
            case JpegConstants.SOF11Marker /*65483*/:
                return "Start Of Frame, Lossless (sequential), arithmetic coding";
            case JpegConstants.SOF12Marker /*65484*/:
                return "Define arithmetic coding conditioning(s)";
            case JpegConstants.SOF13Marker /*65485*/:
                return "Start Of Frame, Differential sequential DCT, arithmetic coding";
            case JpegConstants.SOF14Marker /*65486*/:
                return "Start Of Frame, Differential progressive DCT, arithmetic coding";
            case JpegConstants.SOF15Marker /*65487*/:
                return "Start Of Frame, Differential lossless (sequential), arithmetic coding";
            case 65488:
                return "Restart with modulo 8 count 0";
            case 65489:
                return "Restart with modulo 8 count 1";
            case 65490:
                return "Restart with modulo 8 count 2";
            case 65491:
                return "Restart with modulo 8 count 3";
            case 65492:
                return "Restart with modulo 8 count 4";
            case 65493:
                return "Restart with modulo 8 count 5";
            case 65494:
                return "Restart with modulo 8 count 6";
            case 65495:
                return "Restart with modulo 8 count 7";
            case 65496:
                return "Start of image";
            case 65497:
                return "End of image";
            case JpegConstants.SOS_Marker /*65498*/:
                return "Start of scan";
            case 65499:
                return "Define quantization table(s)";
            case 65500:
                return "Define number of lines";
            case 65501:
                return "Define restart interval";
            case 65502:
                return "Define hierarchical progression";
            case 65503:
                return "Expand reference component(s)";
            case 65534:
                return "Comment";
            default:
                return (this.marker < 65282 || this.marker > 65471) ? (this.marker < 65504 || this.marker > JpegConstants.JPEG_APP15_Marker) ? (this.marker < 65520 || this.marker > 65533) ? "Unknown" : "JPG" + (this.marker - 65504) : "APP" + (this.marker - 65504) : "Reserved";
        }
    }

    public String toString() {
        return "[Segment: " + getDescription() + "]";
    }
}
