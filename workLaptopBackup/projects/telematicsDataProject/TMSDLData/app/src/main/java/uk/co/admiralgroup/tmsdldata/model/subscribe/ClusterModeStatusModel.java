package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class ClusterModeStatusModel extends RealmObject implements Timestampable {

    private String carModeStatus;
    private String powerModeActive;
    private String powerModeQualificationStatus;
    private String powerModeStatus;
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getCarModeStatus() {
        return carModeStatus;
    }

    public void setCarModeStatus(String carModeStatus) {
        this.carModeStatus = carModeStatus;
    }

    public String getPowerModeActive() {
        return powerModeActive;
    }

    public void setPowerModeActive(String powerModeActive) {
        this.powerModeActive = powerModeActive;
    }

    public String getPowerModeQualificationStatus() {
        return powerModeQualificationStatus;
    }

    public void setPowerModeQualificationStatus(String powerModeQualificationStatus) {
        this.powerModeQualificationStatus = powerModeQualificationStatus;
    }

    public String getPowerModeStatus() {
        return powerModeStatus;
    }

    public void setPowerModeStatus(String powerModeStatus) {
        this.powerModeStatus = powerModeStatus;
    }
}
