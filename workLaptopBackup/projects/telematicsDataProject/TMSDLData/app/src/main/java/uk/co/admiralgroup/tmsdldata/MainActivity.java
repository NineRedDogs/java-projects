package uk.co.admiralgroup.tmsdldata;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.cmtelematics.drivewell.api.CmtService;
import com.cmtelematics.drivewell.api.ServiceConfiguration;
import com.cmtelematics.drivewell.model.AppModel;
import com.cmtelematics.drivewell.model.Model;
import com.cmtelematics.drivewell.model.types.MapDataReader;
import com.cmtelematics.drivewell.model.types.ModelConfiguration;
import com.crashlytics.android.Crashlytics;
import com.smartdevicelink.util.DebugTool;

import java.util.UUID;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.admiralgroup.tmsdldata.cmt.CMTService;
import uk.co.admiralgroup.tmsdldata.cmt.CMTServiceReceiver;
import uk.co.admiralgroup.tmsdldata.sdl.SdlReceiver;
import uk.co.admiralgroup.tmsdldata.sdl.SdlService;
import uk.co.admiralgroup.tmsdldata.util.FirebaseLogger;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.cmtelematics.drivewell.api.ServiceConstants.ACTION_GPS_PERMISSION_GRANTED;
import static com.cmtelematics.drivewell.model.DataModelConstants.REQUEST_CODE_ASK_GPS_PERMISSION;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TMSDLData Main Activity";
    private int PERMISSIONS_REQUEST= 1213;


    private Context context;
    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;



        // Set up Fabric Crashlytics
        Fabric.with(this, new Crashlytics());

        // make sure permissions have been granted
        checkPermissions();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //If we are connected to a module we want to start our SdlService
            if (BuildConfig.TRANSPORT.equals("MULTI") || BuildConfig.TRANSPORT.equals("MULTI_HB")) {
                SdlReceiver.queryForConnectedService(this);
            } else if (BuildConfig.TRANSPORT.equals("TCP")) {
                Intent proxyIntent = new Intent(this, SdlService.class);
                startService(proxyIntent);
            }
        }

        // initialise the CMT service
        CmtService.init(this, TAG);

        model = new AppModel(getApplicationContext(),
                (new ModelConfiguration()).setServiceClass(CMTService.class),
                new TestDriveServiceConfiguration());

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        // If we have been granted the
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                    FirebaseLogger.setDeviceUUID(Settings.Secure.getString(this.getContentResolver(),
                            Settings.Secure.ANDROID_ID) + "-" + mngr.getDeviceId());

                    if (BuildConfig.TRANSPORT.equals("MULTI") || BuildConfig.TRANSPORT.equals("MULTI_HB")) {
                        SdlReceiver.queryForConnectedService(this);
                    } else if (BuildConfig.TRANSPORT.equals("TCP")) {
                        Intent proxyIntent = new Intent(this, SdlService.class);
                        startService(proxyIntent);
                    }
                }
            }

            // Have we just been granted location permissions, if so, alert the CMT SDK
            if (grantResults.length > 0 && grantResults[1] == PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: ACCESS_LOCATION permission granted");

                // This is needed to inform CMT that the permission has been granted.
                final Intent permissionIntent = new Intent(ACTION_GPS_PERMISSION_GRANTED);
                LocalBroadcastManager.getInstance(this).sendBroadcast(permissionIntent);
            } else {
                Log.w(TAG, "onRequestPermissionsResult: READ_PHONE_STATE permission denied");
            }

        }
    }

    public void checkPermissions() {
        // Check if the READ_PHONE_STATE permission is already available.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                != PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST);

            FirebaseLogger.setDeviceUUID("Unknown - Awaiting permissions");
        } else {

            TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            FirebaseLogger.setDeviceUUID(Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID) + "-" + mngr.getDeviceId());
        }
    }

    // expose the model
    public Model getModel() {
        return model;
    }

    // Configuration for the CMT Android Service e.g. CMT backend, API Key,
    private class TestDriveServiceConfiguration extends ServiceConfiguration {

        @Override
        public String getCmtApiKey() {
            return getString(R.string.cmt_api_key);
        }

        @Override
        public String getEndpoint() {
            return getString(R.string.cmt_endpoint);
        }

        @Override
        public boolean isReleaseMode() {
            return getString(R.string.cmt_release_mode).equals("release");
        }

        @Override
        public ComponentName getTripRecordingService() {
            return new ComponentName(context, CMTService.class);
        }

        @Override
        public ComponentName getAnomalyReceiver() {
            return new ComponentName(context, CMTServiceReceiver.class);
        }
    }


}
