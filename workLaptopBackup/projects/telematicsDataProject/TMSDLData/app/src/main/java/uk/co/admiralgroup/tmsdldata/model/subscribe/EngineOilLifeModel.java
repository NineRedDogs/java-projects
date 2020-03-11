package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class EngineOilLifeModel extends RealmObject implements Timestampable {
    private Date timestamp;
    private String engineOilLife;

    public String getEngineOilLife() {
        return engineOilLife;
    }
    public void setEngineOilLife(String engineOilLife) {
        this.engineOilLife = engineOilLife;
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
