package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.RealmClass;

@RealmClass
public class AcceleratorPedalPosModel extends RealmObject implements Timestampable {

    private String accPedalPosition;
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getAccPedalPosition() {
        return accPedalPosition;
    }

    public void setAccPedalPosition(String accPedalPosition) {
        this.accPedalPosition = accPedalPosition;
    }


}
