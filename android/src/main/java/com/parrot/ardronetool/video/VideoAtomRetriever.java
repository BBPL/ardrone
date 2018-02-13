package com.parrot.ardronetool.video;

public final class VideoAtomRetriever {

    public enum AtomType {
        ARDT("ardt"),
        PRRT("prrt");
        
        private String name;

        private AtomType(String str) {
            this.name = str;
        }

        public String toString() {
            return this.name;
        }
    }

    private native String getAtomData(String str, String str2);

    public ArdtAtom getArdtAtom(String str) {
        String atomData = getAtomData(str, AtomType.ARDT);
        return atomData != null ? new ArdtAtom(atomData) : null;
    }

    public String getAtomData(String str, AtomType atomType) {
        return getAtomData(str, atomType.toString());
    }
}
