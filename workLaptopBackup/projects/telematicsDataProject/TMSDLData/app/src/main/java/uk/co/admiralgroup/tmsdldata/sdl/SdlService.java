package uk.co.admiralgroup.tmsdldata.sdl;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.smartdevicelink.managers.CompletionListener;
import com.smartdevicelink.managers.SdlManager;
import com.smartdevicelink.managers.SdlManagerListener;
import com.smartdevicelink.managers.file.filetypes.SdlArtwork;
import com.smartdevicelink.managers.permission.OnPermissionChangeListener;
import com.smartdevicelink.managers.permission.PermissionElement;
import com.smartdevicelink.managers.permission.PermissionManager;
import com.smartdevicelink.managers.permission.PermissionStatus;
import com.smartdevicelink.protocol.enums.FunctionID;
import com.smartdevicelink.proxy.RPCNotification;
import com.smartdevicelink.proxy.RPCResponse;
import com.smartdevicelink.proxy.TTSChunkFactory;
import com.smartdevicelink.proxy.rpc.AirbagStatus;
import com.smartdevicelink.proxy.rpc.GetVehicleData;
import com.smartdevicelink.proxy.rpc.OnHMIStatus;
import com.smartdevicelink.proxy.rpc.OnVehicleData;
import com.smartdevicelink.proxy.rpc.Speak;
import com.smartdevicelink.proxy.rpc.SubscribeVehicleData;
import com.smartdevicelink.proxy.rpc.enums.AppHMIType;
import com.smartdevicelink.proxy.rpc.enums.FileType;
import com.smartdevicelink.proxy.rpc.enums.HMILevel;
import com.smartdevicelink.proxy.rpc.listeners.OnRPCNotificationListener;
import com.smartdevicelink.proxy.rpc.listeners.OnRPCResponseListener;
import com.smartdevicelink.transport.BaseTransportConfig;
import com.smartdevicelink.transport.MultiplexTransportConfig;
import com.smartdevicelink.transport.TCPTransportConfig;

import org.json.JSONException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import uk.co.admiralgroup.tmsdldata.BuildConfig;
import uk.co.admiralgroup.tmsdldata.R;
import uk.co.admiralgroup.tmsdldata.model.subscribe.AcceleratorPedalPosModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.BeltStatusModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.BodyInformationModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.ClusterModeStatusModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.DeviceStatusModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.DriverBrakingStatusModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.ECallStatusModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.EParkingBrakeStatusModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.EmergencyEventStatusModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.EngineOilLifeModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.EngineTorqueModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.ExternalTemperatureStatusModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.GPSModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.HeadLampStatusModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.InstantFuelConsumptionModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.MykeyStatusModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.OdometerModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.PrndlModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.RpmModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.SpeedModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.SteeringWheelAngleModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.Timestampable;
import uk.co.admiralgroup.tmsdldata.model.subscribe.TirePressureModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.TurnSignalStatusModel;
import uk.co.admiralgroup.tmsdldata.model.subscribe.WiperStatusModel;
import uk.co.admiralgroup.tmsdldata.util.FirebaseLogger;

public class SdlService extends Service {

    private static final String TAG = "SDLService";

    private static final String APP_NAME = "Admiral";
    private static final String APP_ID = "2301155069";

    private static final String ICON_FILENAME = "hello_sdl_icon.png";
    private static final String SDL_IMAGE_FILENAME = "sdl_full_image.png";

    private static final String WELCOME_SHOW = "TM Data Test";
    private static final String WELCOME_SPEAK = "Admiral Telematics App connected";


    private static final int FOREGROUND_SERVICE_ID = 112;

    // TCP/IP transport config
    // The default port is 12345
    // The IP is of the machine that is running SDL Core
    private static final String DEV_MACHINE_IP_ADDRESS = "m.sdl.tools";  // VirtualBox IP on home wifi
    private static final int TCP_PORT = 13998;

    // Are we watching for ignition status or just starting the recording process on connection?
    boolean watchForIgnition = false;
    // controls whether we are using realm or firestore for persistence
    boolean usingFireStore = false;

    // Access a Cloud Fires store instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    // variable to create and call functions of the SyncProxy
    private SdlManager sdlManager = null;
    private boolean _recordingStatus = false;
    private int _errorCount = 0;


    // get vehicle data flags:
    private boolean KEY_ACC_PEDAL_POSITION = false;
    private boolean KEY_AIRBAG_STATUS = false;
    private boolean KEY_BELT_STATUS = false;
    private boolean KEY_BODY_INFORMATION = false;
    private boolean KEY_CLUSTER_MODE_STATUS = false;
    private boolean KEY_DEVICE_STATUS = false;
    private boolean KEY_DRIVER_BRAKING = false;
    private boolean KEY_E_CALL_INFO = false;
    private boolean KEY_ELECTRONIC_PARK_BRAKE_STATUS = false;
    private boolean KEY_EMERGENCY_EVENT = false;
    private boolean KEY_ENGINE_OIL_LIFE = false;
    private boolean KEY_EXTERNAL_TEMPERATURE = false;
    private boolean KEY_ENGINE_TORQUE = false;
    private boolean KEY_GPS = false;
    private boolean KEY_HEAD_LAMP_STATUS = false;
    private boolean KEY_INSTANT_FUEL_CONSUMPTION = false;
    private boolean KEY_MY_KEY = false;
    private boolean KEY_PRNDL = false;
    private boolean KEY_ODOMETER = false;
    private boolean KEY_RPM = false;
    private boolean KEY_SPEED = false;
    private boolean KEY_STEERING_WHEEL_ANGLE = false;
    private boolean KEY_TIRE_PRESSURE = false;
    private boolean KEY_TURN_SIGNAL = false;
    private boolean KEY_VIN = false;
    private boolean KEY_WIPER_STATUS = false;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            enterForeground();
        }
    }

    // Helper method to let the service enter foreground mode
    @SuppressLint("NewApi")
    public void enterForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(APP_ID, "SdlService", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                Notification serviceNotification = new Notification.Builder(this, channel.getId())
                        .setContentTitle("Admiral TM Data Test Running.")
                        .setSmallIcon(R.drawable.ic_sdl)
                        .build();
                startForeground(FOREGROUND_SERVICE_ID, serviceNotification);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startProxy();
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true);
        }

        if (sdlManager != null) {
            sdlManager.dispose();
        }

        super.onDestroy();
    }

    /**
     * Performs set up of the SDL Manager (aka proxy)
     */
    private void startProxy() {

        // This logic is to select the correct transport and security levels defined in the selected build flavor
        if (sdlManager == null) {
            BaseTransportConfig transport = null;
            if (BuildConfig.TRANSPORT.equals("MULTI")) {
                int securityLevel;
                if (BuildConfig.SECURITY.equals("HIGH")) {
                    securityLevel = MultiplexTransportConfig.FLAG_MULTI_SECURITY_HIGH;
                } else if (BuildConfig.SECURITY.equals("MED")) {
                    securityLevel = MultiplexTransportConfig.FLAG_MULTI_SECURITY_MED;
                } else if (BuildConfig.SECURITY.equals("LOW")) {
                    securityLevel = MultiplexTransportConfig.FLAG_MULTI_SECURITY_LOW;
                } else {
                    securityLevel = MultiplexTransportConfig.FLAG_MULTI_SECURITY_OFF;
                }
                transport = new MultiplexTransportConfig(this, APP_ID, securityLevel);
            } else if (BuildConfig.TRANSPORT.equals("TCP")) {
                transport = new TCPTransportConfig(TCP_PORT, DEV_MACHINE_IP_ADDRESS, true);
            } else if (BuildConfig.TRANSPORT.equals("MULTI_HB")) {
                MultiplexTransportConfig mtc = new MultiplexTransportConfig(this, APP_ID, MultiplexTransportConfig.FLAG_MULTI_SECURITY_OFF);
                mtc.setRequiresHighBandwidth(true);
                transport = mtc;
            }

            // The app type to be used
            Vector<AppHMIType> appType = new Vector<>();
            appType.add(AppHMIType.BACKGROUND_PROCESS);


            // The manager listener understands when certain events that pertain to the SDL Manager happen
            // Here we will listen for ON_HMI_STATUS and ON_VEHICLE_DATA notifications
            // the listeners onStart method is called once the SDL manager is fully set up.
            SdlManagerListener listener = new SdlManagerListener() {

                @Override
                public void onStart() {

                    // individually check each permission and enable the appropriate flag accordingly
                    KEY_ACC_PEDAL_POSITION = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_ACC_PEDAL_POSITION);
                    KEY_AIRBAG_STATUS = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_AIRBAG_STATUS);
                    KEY_BELT_STATUS = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_BELT_STATUS);
                    KEY_BODY_INFORMATION = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_BODY_INFORMATION);
                    KEY_CLUSTER_MODE_STATUS = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_CLUSTER_MODE_STATUS);
                    KEY_DEVICE_STATUS = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_DEVICE_STATUS);
                    KEY_DRIVER_BRAKING = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_DRIVER_BRAKING);
                    KEY_E_CALL_INFO = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_E_CALL_INFO);
                    KEY_ELECTRONIC_PARK_BRAKE_STATUS = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_ELECTRONIC_PARK_BRAKE_STATUS);
                    KEY_EMERGENCY_EVENT = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_EMERGENCY_EVENT);
                    KEY_ENGINE_OIL_LIFE = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_ENGINE_OIL_LIFE);
                    KEY_EXTERNAL_TEMPERATURE = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_EXTERNAL_TEMPERATURE);
                    KEY_ENGINE_TORQUE = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_ENGINE_TORQUE);
                    KEY_GPS = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_GPS);
                    KEY_HEAD_LAMP_STATUS = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_HEAD_LAMP_STATUS);
                    KEY_INSTANT_FUEL_CONSUMPTION = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_INSTANT_FUEL_CONSUMPTION);
                    KEY_MY_KEY = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_MY_KEY);
                    KEY_PRNDL = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_PRNDL);
                    KEY_ODOMETER = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_ODOMETER);
                    KEY_RPM = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_RPM);
                    KEY_SPEED = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_SPEED);
                    KEY_STEERING_WHEEL_ANGLE = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_STEERING_WHEEL_ANGLE);
                    KEY_TIRE_PRESSURE = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_TIRE_PRESSURE);
                    KEY_TURN_SIGNAL = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_TURN_SIGNAL);
                    KEY_WIPER_STATUS = sdlManager.getPermissionManager().isPermissionParameterAllowed(FunctionID.SUBSCRIBE_VEHICLE_DATA, SubscribeVehicleData.KEY_WIPER_STATUS);


                    // Register for any changes in permission status so that we can act accordingly
                    List<PermissionElement> permissionElements = new ArrayList<>();
                    permissionElements.add(new PermissionElement(FunctionID.SUBSCRIBE_VEHICLE_DATA,

                            Arrays.asList(SubscribeVehicleData.KEY_ACC_PEDAL_POSITION,
                                    SubscribeVehicleData.KEY_AIRBAG_STATUS,
                                    SubscribeVehicleData.KEY_BELT_STATUS,
                                    SubscribeVehicleData.KEY_BODY_INFORMATION,
                                    SubscribeVehicleData.KEY_CLUSTER_MODE_STATUS,
                                    SubscribeVehicleData.KEY_DEVICE_STATUS,
                                    SubscribeVehicleData.KEY_DRIVER_BRAKING,
                                    SubscribeVehicleData.KEY_E_CALL_INFO,
                                    SubscribeVehicleData.KEY_ELECTRONIC_PARK_BRAKE_STATUS,
                                    SubscribeVehicleData.KEY_EMERGENCY_EVENT,
                                    SubscribeVehicleData.KEY_ENGINE_OIL_LIFE,
                                    SubscribeVehicleData.KEY_EXTERNAL_TEMPERATURE,
                                    SubscribeVehicleData.KEY_ENGINE_TORQUE,
                                    SubscribeVehicleData.KEY_GPS,
                                    SubscribeVehicleData.KEY_HEAD_LAMP_STATUS,
                                    SubscribeVehicleData.KEY_INSTANT_FUEL_CONSUMPTION,
                                    SubscribeVehicleData.KEY_MY_KEY,
                                    SubscribeVehicleData.KEY_PRNDL,
                                    SubscribeVehicleData.KEY_ODOMETER,
                                    SubscribeVehicleData.KEY_RPM,
                                    SubscribeVehicleData.KEY_SPEED,
                                    SubscribeVehicleData.KEY_STEERING_WHEEL_ANGLE,
                                    SubscribeVehicleData.KEY_TIRE_PRESSURE,
                                    SubscribeVehicleData.KEY_TURN_SIGNAL,
                                    SubscribeVehicleData.KEY_WIPER_STATUS)));

                    // Listen to any changes in the above permissions
                    sdlManager.getPermissionManager().addListener(permissionElements, PermissionManager.PERMISSION_GROUP_TYPE_ANY, new OnPermissionChangeListener() {
                        @Override
                        public void onPermissionsChange(@NonNull Map<FunctionID, PermissionStatus> allowedPermissions, @NonNull int permissionGroupStatus) {

                            // toggle all vehicle data elements according to new permission
                            if (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA) != null) {
                                if (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters() != null) {
                                    KEY_ACC_PEDAL_POSITION = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_ACC_PEDAL_POSITION) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_ACC_PEDAL_POSITION) : KEY_ACC_PEDAL_POSITION;
                                    KEY_AIRBAG_STATUS = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_AIRBAG_STATUS) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_AIRBAG_STATUS) : KEY_AIRBAG_STATUS;
                                    KEY_BELT_STATUS = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_BELT_STATUS) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_BELT_STATUS) : KEY_BELT_STATUS;
                                    KEY_BODY_INFORMATION = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_BODY_INFORMATION) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_BODY_INFORMATION) : KEY_BODY_INFORMATION;
                                    KEY_CLUSTER_MODE_STATUS = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_CLUSTER_MODE_STATUS) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_CLUSTER_MODE_STATUS) : KEY_CLUSTER_MODE_STATUS;
                                    KEY_DEVICE_STATUS = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(GetVehicleData.KEY_DEVICE_STATUS) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_DEVICE_STATUS) : KEY_DEVICE_STATUS;
                                    KEY_DRIVER_BRAKING = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_DRIVER_BRAKING) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_DRIVER_BRAKING) : KEY_DRIVER_BRAKING;
                                    KEY_E_CALL_INFO = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_E_CALL_INFO) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_E_CALL_INFO) : KEY_E_CALL_INFO;
                                    KEY_ELECTRONIC_PARK_BRAKE_STATUS = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_ELECTRONIC_PARK_BRAKE_STATUS) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_ELECTRONIC_PARK_BRAKE_STATUS) : KEY_ELECTRONIC_PARK_BRAKE_STATUS;
                                    KEY_EMERGENCY_EVENT = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_EMERGENCY_EVENT) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_EMERGENCY_EVENT) : KEY_EMERGENCY_EVENT;
                                    KEY_ENGINE_OIL_LIFE = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_ENGINE_OIL_LIFE) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_ENGINE_OIL_LIFE) : KEY_ENGINE_OIL_LIFE;
                                    KEY_EXTERNAL_TEMPERATURE = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_EXTERNAL_TEMPERATURE) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_EXTERNAL_TEMPERATURE) : KEY_EXTERNAL_TEMPERATURE;
                                    KEY_ENGINE_TORQUE = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_ENGINE_TORQUE) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_ENGINE_TORQUE) : KEY_ENGINE_TORQUE;
                                    KEY_GPS = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_GPS) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_GPS) : KEY_GPS;
                                    KEY_HEAD_LAMP_STATUS = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_HEAD_LAMP_STATUS) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_HEAD_LAMP_STATUS) : KEY_HEAD_LAMP_STATUS;
                                    KEY_INSTANT_FUEL_CONSUMPTION = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_INSTANT_FUEL_CONSUMPTION) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_INSTANT_FUEL_CONSUMPTION) : KEY_INSTANT_FUEL_CONSUMPTION;
                                    KEY_MY_KEY = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_MY_KEY) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_MY_KEY) : KEY_MY_KEY;
                                    KEY_RPM = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_RPM) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_RPM) : KEY_RPM;
                                    KEY_PRNDL = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_PRNDL) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_PRNDL) : KEY_PRNDL;
                                    KEY_SPEED = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_RPM) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_RPM) : KEY_RPM;
                                    KEY_ODOMETER = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_RPM) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_ODOMETER) : KEY_ODOMETER;
                                    KEY_STEERING_WHEEL_ANGLE = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_STEERING_WHEEL_ANGLE) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_STEERING_WHEEL_ANGLE) : KEY_STEERING_WHEEL_ANGLE;
                                    KEY_TIRE_PRESSURE = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_TIRE_PRESSURE) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_TIRE_PRESSURE) : KEY_TIRE_PRESSURE;
                                    KEY_TURN_SIGNAL = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_TURN_SIGNAL) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_TURN_SIGNAL) : KEY_TURN_SIGNAL;
                                    KEY_WIPER_STATUS = (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_WIPER_STATUS) != null) ? allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getAllowedParameters().get(SubscribeVehicleData.KEY_WIPER_STATUS) : KEY_WIPER_STATUS;

                                }

                                // if GetVehicleData RPC is allowed
                                if (allowedPermissions.get(FunctionID.SUBSCRIBE_VEHICLE_DATA).getIsRPCAllowed()) {
                                    FirebaseLogger.log(FirebaseLogger.LOG_LEVEL.INFO, TAG, "PERMISSIONS:: Vehicle Data permission granted!");

                                    // Subscribe to changes in vehicle data
                                    startWatchingForVehicleData();
                                } else {
                                    // stop vehicle recording if it is currently running
                                    stopVehicleRecording();
                                    FirebaseLogger.log(FirebaseLogger.LOG_LEVEL.INFO, TAG, "PERMISSIONS::: We're not unable to call getVehicleData");
                                }
                            }
                        }

                    });


                    // Once the HMI connection state status has changed....
                    sdlManager.addOnRPCNotificationListener(FunctionID.ON_HMI_STATUS, new OnRPCNotificationListener() {
                        @Override
                        public void onNotified(RPCNotification notification) {
                            OnHMIStatus status = (OnHMIStatus) notification;
                            FirebaseLogger.log(FirebaseLogger.LOG_LEVEL.INFO, TAG, "HMI Status just changed to: " + status.getHmiLevel());

                            // if we're in any HMI state other than none then..
                            if ((status.getHmiLevel() == HMILevel.HMI_FULL) || (status.getHmiLevel() == HMILevel.HMI_LIMITED) || (status.getHmiLevel() == HMILevel.HMI_BACKGROUND)) {

                                // if HMI State full then..
                                if (status.getHmiLevel() == HMILevel.HMI_FULL) {
                                    // if this is the first run of the app then perform welcome show and speech.
                                    if (((OnHMIStatus) notification).getFirstRun()) {
                                        performWelcomeShow();
                                    }
                                }
                                startWatchingForVehicleData();
                            }
                        }
                    });

                    // we need to handle any vehicle data notifications that come in..
                    sdlManager.addOnRPCNotificationListener(FunctionID.ON_VEHICLE_DATA, new OnRPCNotificationListener() {
                        @Override
                        public void onNotified(RPCNotification notification) {

                            try {
                                OnVehicleData onVehicleDataNotification = (OnVehicleData) notification;

                                // have we just received a notification for accel position?
                                if (onVehicleDataNotification.getAccPedalPosition() != null) {
                                    Log.i(TAG, "Acc pedal event received.");
                                    writeDataToLocalDB(AcceleratorPedalPosModel.class, onVehicleDataNotification.getAccPedalPosition().toString(), "accPedalPosition");
                                }

                                // Airbag event handling
                                if (onVehicleDataNotification.getAirbagStatus() != null) {
                                    Log.i(TAG, "Airbag event received.");
                                    writeDataToLocalDB(AirbagStatus.class, onVehicleDataNotification.getAirbagStatus().serializeJSON().toString());
                                }

                                // process belt status update
                                if (onVehicleDataNotification.getBeltStatus() != null) {
                                    Log.i(TAG, "Belt event received.");
                                    writeDataToLocalDB(BeltStatusModel.class, onVehicleDataNotification.getBeltStatus().serializeJSON().toString());
                                }

                                // process body information status update
                                if (onVehicleDataNotification.getBodyInformation() != null) {
                                    Log.i(TAG, "Body Info event received.");
                                    writeDataToLocalDB(BodyInformationModel.class, onVehicleDataNotification.getBodyInformation().serializeJSON().toString());
                                }

                                // process cluster mode status update
                                if (onVehicleDataNotification.getClusterModeStatus() != null) {
                                    Log.i(TAG, "Cluster mode event received.");
                                    writeDataToLocalDB(ClusterModeStatusModel.class, onVehicleDataNotification.getClusterModeStatus().serializeJSON().toString());
                                }

                                // process device status update
                                if (onVehicleDataNotification.getDeviceStatus() != null) {
                                    Log.i(TAG, "Device event received.");
                                    writeDataToLocalDB(DeviceStatusModel.class, onVehicleDataNotification.getDeviceStatus().serializeJSON().toString());
                                }

                                //Driver braking status
                                if (onVehicleDataNotification.getDriverBraking() != null) {
                                    Log.i(TAG, "Driver braking event received.");
                                    writeDataToLocalDB(DriverBrakingStatusModel.class, onVehicleDataNotification.getDriverBraking().toString(), "driverBraking");
                                }

                                // process E Call information status update
                                if (onVehicleDataNotification.getECallInfo() != null) {
                                    Log.i(TAG, "E-Call event received.");
                                    writeDataToLocalDB(ECallStatusModel.class, onVehicleDataNotification.getECallInfo().serializeJSON().toString());
                                }

                                // process e parking brake status changes
                                if (onVehicleDataNotification.getElectronicParkBrakeStatus() != null) {
                                    Log.i(TAG, "Electronic parking brake event received.");
                                    writeDataToLocalDB(EParkingBrakeStatusModel.class, onVehicleDataNotification.getElectronicParkBrakeStatus().toString(), "electronicParkBrakeStatus");
                                }

                                // write emergency event status information
                                if (onVehicleDataNotification.getEmergencyEvent() != null) {
                                    Log.i(TAG, "Emergency event received.");
                                    writeDataToLocalDB(EmergencyEventStatusModel.class, onVehicleDataNotification.getEmergencyEvent().serializeJSON().toString());
                                }

                                // engine oil life status information update
                                if (onVehicleDataNotification.getEngineOilLife() != null) {
                                    Log.i(TAG, "Engine Oil life event received.");
                                    writeDataToLocalDB(EngineOilLifeModel.class, onVehicleDataNotification.getEngineOilLife().toString(), "engineOilLife");
                                }

                                // External temperature status update
                                if (onVehicleDataNotification.getExternalTemperature() != null) {
                                    Log.i(TAG, "External temperature event received.");
                                    writeDataToLocalDB(ExternalTemperatureStatusModel.class, onVehicleDataNotification.getExternalTemperature().toString(), "externalTemperature");
                                }

                                //EngineTorque status update
                                if (onVehicleDataNotification.getEngineTorque() != null) {
                                    Log.i(TAG, "Engine torque event received.");
                                    writeDataToLocalDB(EngineTorqueModel.class, onVehicleDataNotification.getEngineTorque().toString(), "engineTorque");
                                }

                                // GPS Status update
                                if (onVehicleDataNotification.getGps() != null) {
                                    Log.i(TAG, "GPS event received.");
                                    writeDataToLocalDB(GPSModel.class, onVehicleDataNotification.getGps().serializeJSON().toString());
                                }

                                // Head lamp status update
                                if (onVehicleDataNotification.getHeadLampStatus() != null) {
                                    Log.i(TAG, "Head lamp event received.");
                                    writeDataToLocalDB(HeadLampStatusModel.class, onVehicleDataNotification.getHeadLampStatus().serializeJSON().toString());
                                }

                                // instant fuel consumption update
                                if (onVehicleDataNotification.getInstantFuelConsumption() != null) {
                                    Log.i(TAG, "Instant fuel consumption event received.");
                                    writeDataToLocalDB(InstantFuelConsumptionModel.class, onVehicleDataNotification.getInstantFuelConsumption().toString(), "instantFuelConsumption");
                                }

                                // key status update
                                if (onVehicleDataNotification.getMyKey() != null) {
                                    Log.i(TAG, "Mykey event received.");
                                    writeDataToLocalDB(MykeyStatusModel.class, onVehicleDataNotification.getMyKey().serializeJSON().toString());
                                }

                                // PRNDL status update
                                if (onVehicleDataNotification.getPrndl() != null) {
                                    Log.i(TAG, "prndl event received.");
                                    writeDataToLocalDB(PrndlModel.class, onVehicleDataNotification.getPrndl().toString(), "prndl");
                                }

                                // Odometer status update
                                if (onVehicleDataNotification.getOdometer() != null) {
                                    Log.i(TAG, "Odometer event received.");
                                    writeDataToLocalDB(OdometerModel.class, onVehicleDataNotification.getOdometer().toString(), "odometer");
                                }

                                // RPM Status update
                                if (onVehicleDataNotification.getRpm() != null) {
                                    Log.i(TAG, "Rpm event received.");
                                    writeDataToLocalDB(RpmModel.class, onVehicleDataNotification.getRpm().toString(), "rpm");
                                }

                                // Speed Update
                                if (onVehicleDataNotification.getSpeed() != null) {
                                    Log.i(TAG, "Speed event received.");
                                    writeDataToLocalDB(SpeedModel.class, onVehicleDataNotification.getSpeed().toString(), "speed");
                                }

                                // Steering wheel angle status update
                                if (onVehicleDataNotification.getSteeringWheelAngle() != null) {
                                    Log.i(TAG, "Steering wheel angle event received.");
                                    writeDataToLocalDB(SteeringWheelAngleModel.class, onVehicleDataNotification.getSteeringWheelAngle().toString(), "steeringWheelAngle");
                                }

                                // Tire Pressure status update
                                if (onVehicleDataNotification.getTirePressure() != null) {
                                    Log.i(TAG, "Tire pressure event received.");
                                    writeDataToLocalDB(TirePressureModel.class, onVehicleDataNotification.getTirePressure().serializeJSON().toString());
                                }

                                // Turn Signal status update
                                if (onVehicleDataNotification.getTurnSignal() != null) {
                                    Log.i(TAG, "Turn signal event received.");
                                    writeDataToLocalDB(TurnSignalStatusModel.class, onVehicleDataNotification.getTurnSignal().toString(), "turnSignal");
                                }

                                // Wiper status
                                if (onVehicleDataNotification.getWiperStatus() != null) {
                                    Log.i(TAG, "Wiper event received.");
                                    writeDataToLocalDB(WiperStatusModel.class, onVehicleDataNotification.getWiperStatus().toString(), "wiperStatus");
                                }

                            } catch (JSONException ex) {
                                FirebaseLogger.log(FirebaseLogger.LOG_LEVEL.ERROR, TAG, "Issue serialising JSON");
                            }
                        }
                    });
                }


                @Override
                public void onDestroy() {
                    // If we are shutting down the SDL manager, stop the vehicle data thread.
                    stopVehicleRecording();
                    SdlService.this.stopSelf();
                    // log system start
                    FirebaseLogger.log(FirebaseLogger.LOG_LEVEL.INFO, TAG, "SDLManager stopped.");
                }

                @Override
                public void onError(String info, Exception e) {
                    // log system start
                    FirebaseLogger.log(FirebaseLogger.LOG_LEVEL.ERROR, TAG, "SDL Manager error thrown - " + e.getMessage());
                    e.printStackTrace();

                }
            };


            // Create App Icon, this is set in the SdlManager builder
            SdlArtwork appIcon = new SdlArtwork(ICON_FILENAME, FileType.GRAPHIC_PNG, R.mipmap.ic_launcher, true);

            // The manager builder sets options for your session
            SdlManager.Builder builder = new SdlManager.Builder(this, APP_ID, APP_NAME, listener);
            builder.setAppTypes(appType);
            builder.setTransportType(transport);
            builder.setAppIcon(appIcon);

            sdlManager = builder.build();
            sdlManager.start();

            // log system start
            FirebaseLogger.log(FirebaseLogger.LOG_LEVEL.INFO, TAG, "Service Started.");

            // initialise the local Realm DB for use later
            Realm.init(this);
            RealmConfiguration config = new RealmConfiguration.Builder().name("tmsdldata.realm").build();
            Realm.setDefaultConfiguration(config);

        }
    }


    /**
     * Add functionality for watching subscribing to vehicle data changes
     */
    private void startWatchingForVehicleData() {

        // Here we would pull down vehicle information to see if the ignition had in fact already been started prior to the connection being started.
        SubscribeVehicleData subscribeRequest = new SubscribeVehicleData();
        subscribeRequest.setAccPedalPosition(KEY_ACC_PEDAL_POSITION);
        subscribeRequest.setAirbagStatus(KEY_AIRBAG_STATUS);
        subscribeRequest.setBeltStatus(KEY_BELT_STATUS);
        subscribeRequest.setBodyInformation(KEY_BODY_INFORMATION);
        subscribeRequest.setClusterModeStatus(KEY_CLUSTER_MODE_STATUS);
        subscribeRequest.setDeviceStatus(KEY_DEVICE_STATUS);
        subscribeRequest.setDriverBraking(KEY_DRIVER_BRAKING);
        subscribeRequest.setElectronicParkBrakeStatus(KEY_ELECTRONIC_PARK_BRAKE_STATUS);
        subscribeRequest.setEmergencyEvent(KEY_EMERGENCY_EVENT);
        subscribeRequest.setECallInfo(KEY_E_CALL_INFO);
        subscribeRequest.setEngineOilLife(KEY_ENGINE_OIL_LIFE);
        subscribeRequest.setExternalTemperature(KEY_EXTERNAL_TEMPERATURE);
        subscribeRequest.setEngineTorque(KEY_ENGINE_TORQUE);
        subscribeRequest.setGps(KEY_GPS);
        subscribeRequest.setHeadLampStatus(KEY_HEAD_LAMP_STATUS);
        subscribeRequest.setInstantFuelConsumption(KEY_INSTANT_FUEL_CONSUMPTION);
        subscribeRequest.setMyKey(KEY_MY_KEY);
        subscribeRequest.setOdometer(KEY_ODOMETER);
        subscribeRequest.setPrndl(KEY_PRNDL);
        subscribeRequest.setRpm(KEY_RPM);
        subscribeRequest.setSpeed(KEY_SPEED);
        subscribeRequest.setSteeringWheelAngle(KEY_STEERING_WHEEL_ANGLE);
        subscribeRequest.setTirePressure(KEY_TIRE_PRESSURE);
        subscribeRequest.setTurnSignal(KEY_TURN_SIGNAL);
        subscribeRequest.setWiperStatus(KEY_WIPER_STATUS);


        // starting to subscribe for vehicle data
        subscribeRequest.setOnRPCResponseListener(new OnRPCResponseListener() {
            @Override
            public void onResponse(int correlationId, RPCResponse response) {
                if (response.getSuccess()) {
                    // notify driver that we've connected and are watching for vehicle data
                    FirebaseLogger.log(FirebaseLogger.LOG_LEVEL.INFO, TAG, "Successfully subscribed to vehicle data change events");
                } else {
                    FirebaseLogger.log(FirebaseLogger.LOG_LEVEL.INFO, TAG, "Data change subscription rejected.:" + response.getInfo());
                }
            }
        });
        sdlManager.sendRPC(subscribeRequest);
    }


    /**
     * Stops any currently running vehicle recording.
     */
    private void stopVehicleRecording() {
        Log.i(TAG, "Stopping vehicle recording");
        _recordingStatus = false;
    }


    /**
     * @param objectclass
     * @param JSON
     */
    private void writeDataToLocalDB(final Class objectclass, final String JSON) {
        writeDataToLocalDB(objectclass, JSON, "");
    }

    /**
     * @param objectclass
     * @param JSONValue
     * @param singleValueTitle
     */
    private void writeDataToLocalDB(final Class objectclass, String JSONValue, String singleValueTitle) {

        // if we're dealing with a single value, quickly convert to JSON for ease, this would need to be tidied up in a final product
        if (!singleValueTitle.equals("")) {
            JSONValue = "{ " + singleValueTitle + " : " + JSONValue + "}";
        }

        final String JSON = JSONValue;

        try {

            // Get a Realm instance for this thread
            Realm realm = Realm.getDefaultInstance();

            // Insert from a string
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(@NonNull Realm realm) {
                    Object ro = realm.createObjectFromJson(objectclass, JSON);
                    if (ro != null)
                        ((Timestampable) ro).setTimestamp(new Timestamp(new Date().getTime()));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            FirebaseLogger.log(FirebaseLogger.LOG_LEVEL.ERROR, TAG, "Realm Exception Thrown " + e.getMessage());

        }
    }


    /**
     * Will speak a simple welcome message
     */
    private void performWelcomeSpeak() {
        sdlManager.sendRPC(new Speak(TTSChunkFactory.createSimpleTTSChunks(WELCOME_SPEAK)));
    }


    /**
     * Use the Screen Manager to set the initial screen text and set the image.
     * Because we are setting multiple items, we will call beginTransaction() first,
     * and finish with commit() when we are done.
     */
    private void performWelcomeShow() {
        sdlManager.getScreenManager().beginTransaction();
        sdlManager.getScreenManager().setTextField1(APP_NAME);
        sdlManager.getScreenManager().setTextField2(WELCOME_SHOW);
        sdlManager.getScreenManager().setPrimaryGraphic(new SdlArtwork(SDL_IMAGE_FILENAME, FileType.GRAPHIC_PNG, R.drawable.sdl, true));
        sdlManager.getScreenManager().commit(new CompletionListener() {
            @Override
            public void onComplete(boolean success) {
                if (success) {
                    FirebaseLogger.log(FirebaseLogger.LOG_LEVEL.INFO, TAG, "welcome show successful");
                }
            }
        });
    }


}
