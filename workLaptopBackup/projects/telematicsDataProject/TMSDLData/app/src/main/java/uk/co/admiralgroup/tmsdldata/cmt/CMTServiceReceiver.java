package uk.co.admiralgroup.tmsdldata.cmt;

import static com.cmtelematics.drivewell.api.ServiceConstants.GOOGLE_PLAY_SERVICES_CONNECTION_SUSPENDED;
import static uk.co.admiralgroup.tmsdldata.cmt.CMTService.APPLICATION_ID;

import android.content.Intent;
import android.util.Log;

import com.cmtelematics.drivewell.api.ServiceNotificationType;
import com.cmtelematics.drivewell.model.ServiceNotificationReceiver;

import uk.co.admiralgroup.tmsdldata.R;

public class CMTServiceReceiver extends ServiceNotificationReceiver {

    // Tag must be <= 23 characters
    private static final String TAG = "CMTServiceReceiver";

    @Override
    protected NotificationDesc getDescription(ServiceNotificationType type, Integer code) {
        Log.v(TAG, "getDescription notificationType=" + type + ", code=" + code);

        final String appName = getString(R.string.app_name);
        final int warningIconResId = getWarningIconResId();

        switch (type) {
            case GPS_PERMISSION:
                return new NotificationDesc(
                        getString(R.string.gps_permission_required),
                        String.format(getString(R.string.service_requires_gps), appName), null,
                        CMT_WARNING_CHANNEL,
                        warningIconResId, true, GPS_PERMISSION_NOTIFICATION_ID
                );
            case GPS:
                return new NotificationDesc(
                        getString(R.string.location_service_required),
                        String.format(getString(R.string.service_requires_gps), appName), null,
                        CMT_WARNING_CHANNEL,
                        warningIconResId, true, GPS_ENABLED_NOTIFICATION_ID);
            case NETLOC:
                return new NotificationDesc(
                        getString(R.string.location_service_required),
                        String.format(getString(R.string.service_requires_netloc), appName), null,
                        CMT_WARNING_CHANNEL,
                        warningIconResId, true,
                        GOOGLE_LOCATION_ENABLED_NOTIFICATION_ID);

            case GOOGLE_PLAY_SERVICE:
                int titleResId = R.string.google_play_services_title_failure;
                if (code != null && code == GOOGLE_PLAY_SERVICES_CONNECTION_SUSPENDED) {
                    titleResId = R.string.google_play_services_title_suspended;
                }
                return new NotificationDesc(
                        getString(titleResId),
                        getString(R.string.google_play_services_message), null,
                        CMT_WARNING_CHANNEL,
                        warningIconResId, true,
                        GOOGLE_PLAY_ENABLED_NOTIFICATION_ID);

            case BTLE_DISABLED:
                return new NotificationDesc(
                        getString(R.string.btle_required),
                        String.format(getString(R.string.service_requires_btle), appName), null,
                        CMT_WARNING_CHANNEL,
                        warningIconResId, true, BTLE_DISABLED_NOTIFICATION_ID);
            case GPS_FAILURE:
                return new NotificationDesc(
                        getString(R.string.location_service_error_title),
                        getString(R.string.location_service_error_content),
                        null,
                        CMT_WARNING_CHANNEL,
                        warningIconResId, true, GPS_ERROR_NOTIFICATION_ID);
            case POWER_SAVE_MODE:
                return new NotificationDesc(
                        getString(R.string.power_save_enabled_title),
                        getString(R.string.power_save_enabled_content),
                        null,
                        CMT_WARNING_CHANNEL,
                        warningIconResId, true, POWER_SAVE_MODE_ID);
            case STANDBY_MODE:
            case ACCEL_FAILURE:
            case GYRO_FAILURE:
            case SVR_ALERT:
            case PANIC_BUTTON:
            case NETLOC_PERMISSION:
            case BTLE_TAG_IMPACT_ALERT:
            case LOW_BATTERY_GPS_DISABLED:
            default:
                // This set of notifications are not relevant to us.
                return null;
        }
    }

    @Override
    public Intent getStartMainActivityIntent() {
        final Intent intent = new Intent(APPLICATION_ID + ".action.START_MAIN_ACTIVITY");
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    private String getString(int resId) {
        return getContext().getString(resId);
    }

    private int getWarningIconResId() {
        return R.drawable.sdl_tray_icon;
    }
}
