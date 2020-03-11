package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class DeviceStatusModel extends RealmObject implements Timestampable {
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
