package uk.co.admiralgroup.tmsdldata.model.subscribe;

import java.util.Date;

import io.realm.RealmObject;

public class TirePressureModel extends RealmObject implements Timestampable {


    private String innerLeftRear_status;
    private String innerRightRear_status;
    private String leftFront_status;
    private String leftRear_status;
    private String pressureTelltale;
    private String rightRear_status;
    private String rightFront_status;
    private Date timestamp;


    @Override
    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getInnerLeftRear_status() {
        return innerLeftRear_status;
    }

    public void setInnerLeftRear_status(String innerLeftRear_status) {
        this.innerLeftRear_status = innerLeftRear_status;
    }

    public String getInnerRightRear_status() {
        return innerRightRear_status;
    }

    public void setInnerRightRear_status(String innerRightRear_status) {
        this.innerRightRear_status = innerRightRear_status;
    }

    public String getLeftFront_status() {
        return leftFront_status;
    }

    public void setLeftFront_status(String leftFront_status) {
        this.leftFront_status = leftFront_status;
    }

    public String getLeftRear_status() {
        return leftRear_status;
    }

    public void setLeftRear_status(String leftRear_status) {
        this.leftRear_status = leftRear_status;
    }

    public String getPressureTelltale() {
        return pressureTelltale;
    }

    public void setPressureTelltale(String pressureTelltale) {
        this.pressureTelltale = pressureTelltale;
    }

    public String getRightRear_status() {
        return rightRear_status;
    }

    public void setRightRear_status(String rightRear_status) {
        this.rightRear_status = rightRear_status;
    }

    public String getRightFront_status() {
        return rightFront_status;
    }

    public void setRightFront_status(String rightFront_status) {
        this.rightFront_status = rightFront_status;
    }
}
