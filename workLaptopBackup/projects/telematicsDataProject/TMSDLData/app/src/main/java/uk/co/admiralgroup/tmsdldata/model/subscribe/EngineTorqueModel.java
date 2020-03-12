package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;


public class EngineTorqueModel extends RealmObject implements  Timestampable {

    private Date timestamp;
    private String engineTorque;

    public String getEngineTorque() {
        return engineTorque;
    }

    public void setEngineTorque(String engineTorque) {
        this.engineTorque = engineTorque;
    }

    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
