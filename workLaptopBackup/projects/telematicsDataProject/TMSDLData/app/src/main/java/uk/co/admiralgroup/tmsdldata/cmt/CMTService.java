package uk.co.admiralgroup.tmsdldata.cmt;

import android.support.annotation.NonNull;

import com.cmtelematics.drivewell.api.CmtService;
import com.cmtelematics.drivewell.api.CmtServiceListener;

/**
 * Service that manages trip recording, uploading, and other telematics functionality.
 */
public class CMTService extends CmtService {

    public static final String APPLICATION_ID = "uk.co.admiralgroup.tmsdldata";
    public static final String NOTIFICATION_CHANNEL_SERVICE = "CHANNEL_SERVICE";

    @NonNull
    @Override
    public CmtServiceListener createListener(CmtService context) {
        return new CMTServiceListener(context);
    }
}