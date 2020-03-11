package uk.co.admiralgroup.tmsdldata.cmt;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cmtelematics.drivewell.api.CmtService;
import com.cmtelematics.drivewell.api.CmtServiceListener;
import com.cmtelematics.drivewell.api.ServiceState;

import uk.co.admiralgroup.tmsdldata.R;

import static uk.co.admiralgroup.tmsdldata.BuildConfig.APPLICATION_ID;
import static uk.co.admiralgroup.tmsdldata.cmt.CMTService.NOTIFICATION_CHANNEL_SERVICE;


/**
 * Listener that is returned by the {@link CMTService}
 */
class CMTServiceListener extends CmtServiceListener {

    // Tag must be <= 23 characters
    private static final String TAG = "CMTServiceListener";

    CMTServiceListener(CmtService cmtService) {
        super(cmtService);
    }

    @Override
    public Notification getFleetNotification(ServiceState state) {
        return getNotification(state);
    }

    @Override
    public Notification getNotification(ServiceState state) {
        Log.v(TAG, "getNotification invoked " + state);

        int subtitleResId;

        switch (state) {
            case LOW_BATTERY:
                subtitleResId = R.string.service_notification_low_battery;
                break;
            case RECORDING:
                subtitleResId = R.string.service_notification_recording;
                break;
            case WAITING_FOR_TRIP:
                subtitleResId = R.string.service_notification_waiting_for_trip;
                break;
            case IMPACT:
                // The CMT documentation recommends using the same text for impact and recording.
                // However impact alert is only relevant were a Tag is used, we have no plans to
                // support it.
            case STANDBY:
                // We have no standby function.
            case ON_DESTROY:
            case NO_AUTH:
            default:
                // I don't think we can return null for states that are not really relevant to us.
                // The CMT documentation, CMT DriveWell app and CMT sample application all fallback
                // to showing the "Waiting to record trip" text for unsuported states. We're
                // following their approach but perhaps there is something better we can do here.
                subtitleResId = R.string.service_notification_waiting_for_trip;
                break;
        }
        return getNotification(getString(R.string.app_name), getString(subtitleResId));
    }

    @Override
    public int getNotificationId() {
        return 1231234123;
    }

    private Notification getNotification(String title, String subtitle) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(cmtService, NOTIFICATION_CHANNEL_SERVICE)
                .setContentTitle(title)
                .setContentText(subtitle)
                .setSmallIcon(getNotificationIcon())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(subtitle));

        final PendingIntent startPendingIntent = getNotificationStartIntent();
        builder.setContentIntent(startPendingIntent);
        return builder.build();
    }

    private int getNotificationIcon() {
        return R.drawable.sdl_tray_icon;
    }

    private PendingIntent getNotificationStartIntent() {
        final Intent intent = new Intent(APPLICATION_ID + ".action.START_MAIN_ACTIVITY");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return PendingIntent.getActivity(cmtService, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private String getString(int resId) {
        return cmtService.getString(resId);
    }
}
