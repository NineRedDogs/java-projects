package uk.co.admiralgroup.tmsdldata.cmt;

import com.cmtelematics.drivewell.model.Model;
import com.cmtelematics.drivewell.model.types.MapDataReader;

/**
 * Provides access to CMT objects.
 */
public interface CmtProvider {

    /**
     * Get a CMT MapDataReader object.
     *
     * @return a MapDataReader or null if if it doesn't exist.
     */
    MapDataReader getMapDataReader();

    /**
     * Get the CMT Model object, which is the centrepiece of the API.
     *
     * @return the model or null if a model cannot be found.
     */
    Model getModel();
}
