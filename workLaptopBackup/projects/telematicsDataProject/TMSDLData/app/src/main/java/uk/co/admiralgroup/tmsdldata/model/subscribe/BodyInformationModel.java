package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class BodyInformationModel extends RealmObject implements Timestampable {

    private String driverDoorAjar;
    private String ignitionStableStatus;
    private String ignitionStatus;
    private String parkBrakeActive;
    private String passengerDoorAjar;
    private String rearLeftDoorAjar;
    private String rearRightDoorAjar;
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDriverDoorAjar() {
        return driverDoorAjar;
    }

    public void setDriverDoorAjar(String driverDoorAjar) {
        this.driverDoorAjar = driverDoorAjar;
    }

    public String getIgnitionStableStatus() {
        return ignitionStableStatus;
    }

    public void setIgnitionStableStatus(String ignitionStableStatus) {
        this.ignitionStableStatus = ignitionStableStatus;
    }

    public String getIgnitionStatus() {
        return ignitionStatus;
    }

    public void setIgnitionStatus(String ignitionStatus) {
        this.ignitionStatus = ignitionStatus;
    }

    public String getParkBrakeActive() {
        return parkBrakeActive;
    }

    public void setParkBrakeActive(String parkBrakeActive) {
        this.parkBrakeActive = parkBrakeActive;
    }

    public String getPassengerDoorAjar() {
        return passengerDoorAjar;
    }

    public void setPassengerDoorAjar(String passengerDoorAjar) {
        this.passengerDoorAjar = passengerDoorAjar;
    }

    public String getRearLeftDoorAjar() {
        return rearLeftDoorAjar;
    }

    public void setRearLeftDoorAjar(String rearLeftDoorAjar) {
        this.rearLeftDoorAjar = rearLeftDoorAjar;
    }

    public String getRearRightDoorAjar() {
        return rearRightDoorAjar;
    }

    public void setRearRightDoorAjar(String rearRightDoorAjar) {
        this.rearRightDoorAjar = rearRightDoorAjar;
    }
}
