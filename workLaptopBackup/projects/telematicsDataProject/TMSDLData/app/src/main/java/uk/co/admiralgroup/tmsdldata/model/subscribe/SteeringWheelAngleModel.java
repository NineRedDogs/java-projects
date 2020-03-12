package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class SteeringWheelAngleModel extends RealmObject implements  Timestampable {

    private String steeringWheelAngle;
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public String getSteeringWheelAngle() {
        return steeringWheelAngle;
    }

    public void setSteeringWheelAngle(String steeringWheelAngle) {
        this.steeringWheelAngle = steeringWheelAngle;
    }
}
