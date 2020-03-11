package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class WiperStatusModel extends RealmObject implements Timestampable {

    private String wiperStatus;
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getWiperStatus() {
        return wiperStatus;
    }

    public void setWiperStatus(String wiperStatus) {
        this.wiperStatus = wiperStatus;
    }
}
