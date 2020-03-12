package uk.co.admiralgroup.tmsdldata.model.subscribe;


import java.util.Date;

public interface Timestampable {

    Date getTimestamp();

    void setTimestamp(Date timestamp);
}
