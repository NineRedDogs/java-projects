package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class GPSModel extends RealmObject implements Timestampable {

    private String actual;
    private String altitude;
    private String compassDirection;
    private String dimension;
    private String hdop;
    private String heading;
    private String latitudeDegrees;
    private String longitudeDegrees;
    private String pdop;
    private String satellites;
    private String speed;
    private String utcDay;
    private String utcHours;
    private String utcMinutes;
    private String utcMonth;
    private String utcSeconds;
    private String utcYear;
    private String vdop;
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getActual() {
        return actual;
    }

    public void setActual(String actual) {
        this.actual = actual;
    }

    public String getAltitude() {
        return altitude;
    }

    public void setAltitude(String altitude) {
        this.altitude = altitude;
    }

    public String getCompassDirection() {
        return compassDirection;
    }

    public void setCompassDirection(String compassDirection) {
        this.compassDirection = compassDirection;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getHdop() {
        return hdop;
    }

    public void setHdop(String hdop) {
        this.hdop = hdop;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getLatitudeDegrees() {
        return latitudeDegrees;
    }

    public void setLatitudeDegrees(String latitudeDegrees) {
        this.latitudeDegrees = latitudeDegrees;
    }

    public String getLongitudeDegrees() {
        return longitudeDegrees;
    }

    public void setLongitudeDegrees(String longitudeDegrees) {
        this.longitudeDegrees = longitudeDegrees;
    }

    public String getPdop() {
        return pdop;
    }

    public void setPdop(String pdop) {
        this.pdop = pdop;
    }

    public String getSatellites() {
        return satellites;
    }

    public void setSatellites(String satellites) {
        this.satellites = satellites;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getUtcDay() {
        return utcDay;
    }

    public void setUtcDay(String utcDay) {
        this.utcDay = utcDay;
    }

    public String getUtcHours() {
        return utcHours;
    }

    public void setUtcHours(String utcHours) {
        this.utcHours = utcHours;
    }

    public String getUtcMinutes() {
        return utcMinutes;
    }

    public void setUtcMinutes(String utcMinutes) {
        this.utcMinutes = utcMinutes;
    }

    public String getUtcMonth() {
        return utcMonth;
    }

    public void setUtcMonth(String utcMonth) {
        this.utcMonth = utcMonth;
    }

    public String getUtcSeconds() {
        return utcSeconds;
    }

    public void setUtcSeconds(String utcSeconds) {
        this.utcSeconds = utcSeconds;
    }

    public String getUtcYear() {
        return utcYear;
    }

    public void setUtcYear(String utcYear) {
        this.utcYear = utcYear;
    }

    public String getVdop() {
        return vdop;
    }

    public void setVdop(String vdop) {
        this.vdop = vdop;
    }
}
