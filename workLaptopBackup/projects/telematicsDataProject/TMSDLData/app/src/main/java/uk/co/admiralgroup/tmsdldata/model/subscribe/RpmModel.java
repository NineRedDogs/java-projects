package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;
import io.realm.RealmObject;

public class RpmModel extends RealmObject implements Timestampable {

    private String rpm;
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getRpm() {
        return rpm;
    }
    public void setRpm(String rpm) {
        this.rpm = rpm;
    }
}
