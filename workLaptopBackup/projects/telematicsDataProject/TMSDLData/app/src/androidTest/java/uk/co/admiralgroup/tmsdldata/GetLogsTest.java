package uk.co.admiralgroup.tmsdldata;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class GetLogsTest {
    @Test
    public void getLogs() {

        // Access a Cloud Fires store instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a reference to the cities collection
        db.collection("logs").whereEqualTo("deviceUUID", "f2d869a3a28116c6-865800021689465").orderBy("timestamp", Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Timestamp ts = (Timestamp) document.get("timestamp");

                        System.out.println("LOG::" + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(ts.toDate()) + " - " + document.get("tag") + "::" + document.get("entry") );
                    }
                } else {
                    Log.i("getLogsTest", "Error getting documents: ", task.getException());
                }
            }
        });
        try {

            Thread.sleep(10000);
        } catch (Exception e){

        }

    }
}
