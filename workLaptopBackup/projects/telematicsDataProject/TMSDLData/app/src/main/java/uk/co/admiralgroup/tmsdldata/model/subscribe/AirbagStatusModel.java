package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class AirbagStatusModel extends RealmObject implements Timestampable {

    private String driverAirbagDeployed;
    private String driverCurtainAirbagDeployed;
    private String driverKneeAirbagDeployed;
    private String driverSideAirbagDeployed;
    private String passengerAirbagDeployed;
    private String passengerCurtainAirbagDeployed;
    private String passengerKneeAirbagDeployed;
    private String passengerSideAirbagDeployed;
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDriverAirbagDeployed() {
        return driverAirbagDeployed;
    }

    public void setDriverAirbagDeployed(String driverAirbagDeployed) {
        this.driverAirbagDeployed = driverAirbagDeployed;
    }

    public String getDriverCurtainAirbagDeployed() {
        return driverCurtainAirbagDeployed;
    }

    public void setDriverCurtainAirbagDeployed(String driverCurtainAirbagDeployed) {
        this.driverCurtainAirbagDeployed = driverCurtainAirbagDeployed;
    }

    public String getDriverKneeAirbagDeployed() {
        return driverKneeAirbagDeployed;
    }

    public void setDriverKneeAirbagDeployed(String driverKneeAirbagDeployed) {
        this.driverKneeAirbagDeployed = driverKneeAirbagDeployed;
    }

    public String getDriverSideAirbagDeployed() {
        return driverSideAirbagDeployed;
    }

    public void setDriverSideAirbagDeployed(String driverSideAirbagDeployed) {
        this.driverSideAirbagDeployed = driverSideAirbagDeployed;
    }

    public String getPassengerAirbagDeployed() {
        return passengerAirbagDeployed;
    }

    public void setPassengerAirbagDeployed(String passengerAirbagDeployed) {
        this.passengerAirbagDeployed = passengerAirbagDeployed;
    }

    public String getPassengerCurtainAirbagDeployed() {
        return passengerCurtainAirbagDeployed;
    }

    public void setPassengerCurtainAirbagDeployed(String passengerCurtainAirbagDeployed) {
        this.passengerCurtainAirbagDeployed = passengerCurtainAirbagDeployed;
    }

    public String getPassengerKneeAirbagDeployed() {
        return passengerKneeAirbagDeployed;
    }

    public void setPassengerKneeAirbagDeployed(String passengerKneeAirbagDeployed) {
        this.passengerKneeAirbagDeployed = passengerKneeAirbagDeployed;
    }

    public String getPassengerSideAirbagDeployed() {
        return passengerSideAirbagDeployed;
    }

    public void setPassengerSideAirbagDeployed(String passengerSideAirbagDeployed) {
        this.passengerSideAirbagDeployed = passengerSideAirbagDeployed;
    }
}
