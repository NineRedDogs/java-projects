package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class PrndlModel extends RealmObject implements Timestampable {

    private String prndl;

    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setPrndl(String prndl) {
        this.prndl = prndl;
    }
    public String getPrndl() {
        return prndl;
    }
}
