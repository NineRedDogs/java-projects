package uk.co.admiralgroup.tmsdldata.util;

import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.github.wnameless.json.flattener.JsonFlattener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;

import org.json.JSONException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FirebaseLogger {

    // Access a Cloud Fires store instance
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String deviceUUID;

    public enum LOG_LEVEL {
        ERROR,
        WARN,
        INFO,
        DEBUG
    }


    public static void setDeviceUUID(String uuid){
        deviceUUID = uuid;
    }

    public static void log(LOG_LEVEL level, final String tag, final String entry){

        // log at appropriate level then send remotely to firestore.
        switch (level){
            case INFO:
                Log.i(tag, entry);
                break;
            case WARN:
                Log.w(tag, entry);
                break;
            case DEBUG:
                Log.d(tag, entry);
                break;
            case ERROR:
                Log.e(tag, entry);
                break;
            default:
                Log.i(tag, entry);
                break;
        }



        // Create a new user with a first, middle, and last name
        Map<String, Object> logData = new HashMap<>();

        // Add user identifiers and timestamp
        logData.put("deviceUUID", deviceUUID);
        logData.put("level", level.toString());
        logData.put("tag", tag);
        logData.put("entry", entry);
        logData.put("timestamp", new Timestamp(new Date().getTime()));


        // Add a new document with a generated ID
        db.collection("logs")
                .add(logData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(tag, "Log entry added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(tag, "Error adding log", e);
                    }
                });

    }
}
