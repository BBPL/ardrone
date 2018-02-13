package com.parrot.freeflight.academy.utils;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class AcademyLocationUtils {

    public static class ARSexaCoordinate {
        private long degrees = 0;
        private char direction;
        private long minutes = 0;
        private long seconds = 0;

        public ARSexaCoordinate(long j, long j2, long j3, char c) {
            this.degrees = j;
            this.minutes = j2;
            this.seconds = j3;
            this.direction = c;
        }

        public long getDegrees() {
            return this.degrees;
        }

        public char getDirection() {
            return this.direction;
        }

        public long getMinutes() {
            return this.minutes;
        }

        public long getSeconds() {
            return this.seconds;
        }

        public void setDegrees(long j) {
            this.degrees = j;
        }

        public void setDirection(char c) {
            this.direction = c;
        }

        public void setMinutes(long j) {
            this.minutes = j;
        }

        public void setSeconds(long j) {
            this.seconds = j;
        }
    }

    public static ARSexaCoordinate ARConvertCLLocationDegreesToSexaCoordinate(double d, boolean z) {
        ARSexaCoordinate aRSexaCoordinate = new ARSexaCoordinate();
        double abs = Math.abs(d);
        aRSexaCoordinate.degrees = (long) abs;
        abs -= (double) aRSexaCoordinate.degrees;
        aRSexaCoordinate.minutes = (long) (abs * 60.0d);
        aRSexaCoordinate.seconds = (long) (((abs * 60.0d) - ((double) aRSexaCoordinate.minutes)) * 60.0d);
        if (z) {
            aRSexaCoordinate.direction = d < 0.0d ? 'S' : 'N';
            return aRSexaCoordinate;
        }
        aRSexaCoordinate.direction = d < 0.0d ? 'O' : 'E';
        return aRSexaCoordinate;
    }

    public static double ARConvertSexaCoordinateToCLLocationDegrees(ARSexaCoordinate aRSexaCoordinate) {
        double access$000 = (double) (((float) aRSexaCoordinate.degrees) + (((((float) aRSexaCoordinate.seconds) / BitmapDescriptorFactory.HUE_YELLOW) + ((float) aRSexaCoordinate.minutes)) / BitmapDescriptorFactory.HUE_YELLOW));
        return (aRSexaCoordinate.direction == 'S' || aRSexaCoordinate.direction == 's' || aRSexaCoordinate.direction == 'O' || aRSexaCoordinate.direction == 'o') ? -access$000 : access$000;
    }
}
