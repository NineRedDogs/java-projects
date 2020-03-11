package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class HeadLampStatusModel extends RealmObject implements Timestampable {

    private String ambientLightSensorStatus;
    private String highBeamsOn;
    private String lowBeamsOn;
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public String getAmbientLightSensorStatus() {
        return ambientLightSensorStatus;
    }

    public void setAmbientLightSensorStatus(String ambientLightSensorStatus) {
        this.ambientLightSensorStatus = ambientLightSensorStatus;
    }

    public String getHighBeamsOn() {
        return highBeamsOn;
    }

    public void setHighBeamsOn(String highBeamsOn) {
        this.highBeamsOn = highBeamsOn;
    }

    public String getLowBeamsOn() {
        return lowBeamsOn;
    }

    public void setLowBeamsOn(String lowBeamsOn) {
        this.lowBeamsOn = lowBeamsOn;
    }
}
