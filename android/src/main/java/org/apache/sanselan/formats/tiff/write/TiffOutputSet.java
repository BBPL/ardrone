package org.apache.sanselan.formats.tiff.write;

import java.util.ArrayList;
import java.util.List;
import org.apache.sanselan.ImageWriteException;
import org.apache.sanselan.formats.tiff.constants.TagInfo;
import org.apache.sanselan.formats.tiff.constants.TiffConstants;
import org.apache.sanselan.util.Debug;
import org.mortbay.jetty.HttpVersions;

public final class TiffOutputSet implements TiffConstants {
    private static final String newline = System.getProperty("line.separator");
    public final int byteOrder;
    private final ArrayList directories;

    public TiffOutputSet() {
        this(73);
    }

    public TiffOutputSet(int i) {
        this.directories = new ArrayList();
        this.byteOrder = i;
    }

    public void addDirectory(TiffOutputDirectory tiffOutputDirectory) throws ImageWriteException {
        if (findDirectory(tiffOutputDirectory.type) != null) {
            throw new ImageWriteException("Output set already contains a directory of that type.");
        }
        this.directories.add(tiffOutputDirectory);
    }

    public TiffOutputDirectory addExifDirectory() throws ImageWriteException {
        TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(-2);
        addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }

    public TiffOutputDirectory addGPSDirectory() throws ImageWriteException {
        TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(-3);
        addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }

    public TiffOutputDirectory addInteroperabilityDirectory() throws ImageWriteException {
        getOrCreateExifDirectory();
        TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(-4);
        addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }

    public TiffOutputDirectory addRootDirectory() throws ImageWriteException {
        TiffOutputDirectory tiffOutputDirectory = new TiffOutputDirectory(0);
        addDirectory(tiffOutputDirectory);
        return tiffOutputDirectory;
    }

    public void dump() {
        Debug.debug(toString());
    }

    public TiffOutputDirectory findDirectory(int i) {
        for (int i2 = 0; i2 < this.directories.size(); i2++) {
            TiffOutputDirectory tiffOutputDirectory = (TiffOutputDirectory) this.directories.get(i2);
            if (tiffOutputDirectory.type == i) {
                return tiffOutputDirectory;
            }
        }
        return null;
    }

    public TiffOutputField findField(int i) {
        for (int i2 = 0; i2 < this.directories.size(); i2++) {
            TiffOutputField findField = ((TiffOutputDirectory) this.directories.get(i2)).findField(i);
            if (findField != null) {
                return findField;
            }
        }
        return null;
    }

    public TiffOutputField findField(TagInfo tagInfo) {
        return findField(tagInfo.tag);
    }

    public List getDirectories() {
        return new ArrayList(this.directories);
    }

    public TiffOutputDirectory getExifDirectory() {
        return findDirectory(-2);
    }

    public TiffOutputDirectory getGPSDirectory() {
        return findDirectory(-3);
    }

    public TiffOutputDirectory getInteroperabilityDirectory() {
        return findDirectory(-4);
    }

    public TiffOutputDirectory getOrCreateExifDirectory() throws ImageWriteException {
        getOrCreateRootDirectory();
        TiffOutputDirectory findDirectory = findDirectory(-2);
        return findDirectory != null ? findDirectory : addExifDirectory();
    }

    public TiffOutputDirectory getOrCreateGPSDirectory() throws ImageWriteException {
        getOrCreateExifDirectory();
        TiffOutputDirectory findDirectory = findDirectory(-3);
        return findDirectory != null ? findDirectory : addGPSDirectory();
    }

    public TiffOutputDirectory getOrCreateRootDirectory() throws ImageWriteException {
        TiffOutputDirectory findDirectory = findDirectory(0);
        return findDirectory != null ? findDirectory : addRootDirectory();
    }

    protected List getOutputItems(TiffOutputSummary tiffOutputSummary) throws ImageWriteException {
        List arrayList = new ArrayList();
        for (int i = 0; i < this.directories.size(); i++) {
            arrayList.addAll(((TiffOutputDirectory) this.directories.get(i)).getOutputItems(tiffOutputSummary));
        }
        return arrayList;
    }

    public TiffOutputDirectory getRootDirectory() {
        return findDirectory(0);
    }

    public void removeField(int i) {
        for (int i2 = 0; i2 < this.directories.size(); i2++) {
            ((TiffOutputDirectory) this.directories.get(i2)).removeField(i);
        }
    }

    public void removeField(TagInfo tagInfo) {
        removeField(tagInfo.tag);
    }

    public void setGPSInDegrees(double d, double d2) throws ImageWriteException {
        TiffOutputDirectory orCreateGPSDirectory = getOrCreateGPSDirectory();
        String str = d < 0.0d ? "W" : "E";
        double abs = Math.abs(d);
        String str2 = d2 < 0.0d ? "S" : "N";
        double abs2 = Math.abs(d2);
        TiffOutputField create = TiffOutputField.create(TiffConstants.GPS_TAG_GPS_LONGITUDE_REF, this.byteOrder, str);
        orCreateGPSDirectory.removeField(TiffConstants.GPS_TAG_GPS_LONGITUDE_REF);
        orCreateGPSDirectory.add(create);
        create = TiffOutputField.create(TiffConstants.GPS_TAG_GPS_LATITUDE_REF, this.byteOrder, str2);
        orCreateGPSDirectory.removeField(TiffConstants.GPS_TAG_GPS_LATITUDE_REF);
        orCreateGPSDirectory.add(create);
        abs = (abs % 1.0d) * 60.0d;
        double d3 = (double) ((long) abs);
        Double d4 = new Double((double) ((long) abs));
        Double d5 = new Double(d3);
        Double d6 = new Double((abs % 1.0d) * 60.0d);
        create = TiffOutputField.create(TiffConstants.GPS_TAG_GPS_LONGITUDE, this.byteOrder, new Double[]{d4, d5, d6});
        orCreateGPSDirectory.removeField(TiffConstants.GPS_TAG_GPS_LONGITUDE);
        orCreateGPSDirectory.add(create);
        abs = (abs2 % 1.0d) * 60.0d;
        abs2 = (double) ((long) abs);
        d4 = new Double((double) ((long) abs2));
        d5 = new Double(abs2);
        d6 = new Double((abs % 1.0d) * 60.0d);
        create = TiffOutputField.create(TiffConstants.GPS_TAG_GPS_LATITUDE, this.byteOrder, new Double[]{d4, d5, d6});
        orCreateGPSDirectory.removeField(TiffConstants.GPS_TAG_GPS_LATITUDE);
        orCreateGPSDirectory.add(create);
    }

    public String toString() {
        return toString(null);
    }

    public String toString(String str) {
        if (str == null) {
            str = HttpVersions.HTTP_0_9;
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(str);
        stringBuffer.append("TiffOutputSet {");
        stringBuffer.append(newline);
        stringBuffer.append(str);
        stringBuffer.append("byteOrder: " + this.byteOrder);
        stringBuffer.append(newline);
        for (int i = 0; i < this.directories.size(); i++) {
            TiffOutputDirectory tiffOutputDirectory = (TiffOutputDirectory) this.directories.get(i);
            stringBuffer.append(str);
            stringBuffer.append("\tdirectory " + i + ": " + tiffOutputDirectory.description() + " (" + tiffOutputDirectory.type + ")");
            stringBuffer.append(newline);
            ArrayList fields = tiffOutputDirectory.getFields();
            for (int i2 = 0; i2 < fields.size(); i2++) {
                TiffOutputField tiffOutputField = (TiffOutputField) fields.get(i2);
                stringBuffer.append(str);
                stringBuffer.append("\t\tfield " + i + ": " + tiffOutputField.tagInfo);
                stringBuffer.append(newline);
            }
        }
        stringBuffer.append(str);
        stringBuffer.append("}");
        stringBuffer.append(newline);
        return stringBuffer.toString();
    }
}
