package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class MykeyStatusModel extends RealmObject implements Timestampable {

    private String e911Override;

    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getE911Override() {
        return e911Override;
    }

    public void setE911Override(String e911Override) {
        this.e911Override = e911Override;
    }
}
