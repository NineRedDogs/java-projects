package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class BeltStatusModel extends RealmObject implements Timestampable {

    private String driverBeltDeployed;
    private String driverBuckleBelted;
    private String leftRearInflatableBelted;
    private String leftRow2BuckleBelted;
    private String leftRow3BuckleBelted;
    private String middleRow1BeltDeployed;
    private String middleRow1BuckleBelted;
    private String middleRow2BuckleBelted;
    private String middleRow3BuckleBelted;
    private String passengerBeltDeployed;
    private String passengerBuckleBelted;
    private String passengerChildDetected;
    private String rightRearInflatableBelted;
    private String rightRow2BuckleBelted;
    private String rightRow3BuckleBelted;
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getDriverBeltDeployed() {
        return driverBeltDeployed;
    }

    public void setDriverBeltDeployed(String driverBeltDeployed) {
        this.driverBeltDeployed = driverBeltDeployed;
    }

    public String getDriverBuckleBelted() {
        return driverBuckleBelted;
    }

    public void setDriverBuckleBelted(String driverBuckleBelted) {
        this.driverBuckleBelted = driverBuckleBelted;
    }

    public String getLeftRearInflatableBelted() {
        return leftRearInflatableBelted;
    }

    public void setLeftRearInflatableBelted(String leftRearInflatableBelted) {
        this.leftRearInflatableBelted = leftRearInflatableBelted;
    }

    public String getLeftRow2BuckleBelted() {
        return leftRow2BuckleBelted;
    }

    public void setLeftRow2BuckleBelted(String leftRow2BuckleBelted) {
        this.leftRow2BuckleBelted = leftRow2BuckleBelted;
    }

    public String getLeftRow3BuckleBelted() {
        return leftRow3BuckleBelted;
    }

    public void setLeftRow3BuckleBelted(String leftRow3BuckleBelted) {
        this.leftRow3BuckleBelted = leftRow3BuckleBelted;
    }

    public String getMiddleRow1BeltDeployed() {
        return middleRow1BeltDeployed;
    }

    public void setMiddleRow1BeltDeployed(String middleRow1BeltDeployed) {
        this.middleRow1BeltDeployed = middleRow1BeltDeployed;
    }

    public String getMiddleRow1BuckleBelted() {
        return middleRow1BuckleBelted;
    }

    public void setMiddleRow1BuckleBelted(String middleRow1BuckleBelted) {
        this.middleRow1BuckleBelted = middleRow1BuckleBelted;
    }

    public String getMiddleRow2BuckleBelted() {
        return middleRow2BuckleBelted;
    }

    public void setMiddleRow2BuckleBelted(String middleRow2BuckleBelted) {
        this.middleRow2BuckleBelted = middleRow2BuckleBelted;
    }

    public String getMiddleRow3BuckleBelted() {
        return middleRow3BuckleBelted;
    }

    public void setMiddleRow3BuckleBelted(String middleRow3BuckleBelted) {
        this.middleRow3BuckleBelted = middleRow3BuckleBelted;
    }

    public String getPassengerBeltDeployed() {
        return passengerBeltDeployed;
    }

    public void setPassengerBeltDeployed(String passengerBeltDeployed) {
        this.passengerBeltDeployed = passengerBeltDeployed;
    }

    public String getPassengerBuckleBelted() {
        return passengerBuckleBelted;
    }

    public void setPassengerBuckleBelted(String passengerBuckleBelted) {
        this.passengerBuckleBelted = passengerBuckleBelted;
    }

    public String getPassengerChildDetected() {
        return passengerChildDetected;
    }

    public void setPassengerChildDetected(String passengerChildDetected) {
        this.passengerChildDetected = passengerChildDetected;
    }

    public String getRightRearInflatableBelted() {
        return rightRearInflatableBelted;
    }

    public void setRightRearInflatableBelted(String rightRearInflatableBelted) {
        this.rightRearInflatableBelted = rightRearInflatableBelted;
    }

    public String getRightRow2BuckleBelted() {
        return rightRow2BuckleBelted;
    }

    public void setRightRow2BuckleBelted(String rightRow2BuckleBelted) {
        this.rightRow2BuckleBelted = rightRow2BuckleBelted;
    }

    public String getRightRow3BuckleBelted() {
        return rightRow3BuckleBelted;
    }

    public void setRightRow3BuckleBelted(String rightRow3BuckleBelted) {
        this.rightRow3BuckleBelted = rightRow3BuckleBelted;
    }
}
