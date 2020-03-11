package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class InstantFuelConsumptionModel extends RealmObject implements Timestampable{

    private String instantFuelConsumption;
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getInstantFuelConsumption() {
        return instantFuelConsumption;
    }

    public void setInstantFuelConsumption(String instantFuelConsumption) {
        this.instantFuelConsumption = instantFuelConsumption;
    }
}
