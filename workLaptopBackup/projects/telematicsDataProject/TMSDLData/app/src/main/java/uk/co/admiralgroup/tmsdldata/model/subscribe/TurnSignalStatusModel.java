package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class TurnSignalStatusModel extends RealmObject implements Timestampable {
    private Date timestamp;
    private String turnSignal;

    public String getTurnSignal() {
        return turnSignal;
    }

    public void setTurnSignal(String turnSignal) {
        this.turnSignal = turnSignal;
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
