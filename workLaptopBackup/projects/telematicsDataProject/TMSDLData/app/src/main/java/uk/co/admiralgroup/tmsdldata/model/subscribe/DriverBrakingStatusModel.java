package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class DriverBrakingStatusModel extends RealmObject implements Timestampable {

    private String driverBraking;
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDriverBraking() {
        return driverBraking;
    }

    public void setDriverBraking(String driverBraking) {
        this.driverBraking = driverBraking;
    }

}
