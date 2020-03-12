package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;
import io.realm.RealmObject;

public class EParkingBrakeStatusModel  extends RealmObject implements Timestampable {

    private String electronicParkBrakeStatus;
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    public String getElectronicParkBrakeStatus() {
        return electronicParkBrakeStatus;
    }

    public void setElectronicParkBrakeStatus(String electronicParkBrakeStatus) {
        electronicParkBrakeStatus = electronicParkBrakeStatus;
    }
}
