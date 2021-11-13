package au.edu.unsw.infs3634.unswgamifiedlearningapp;
//everything related to Cloud Firestore (Firebase database)

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.Batch;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.lang.reflect.Array;
import java.util.Map;

public class DbQuery {
    public static FirebaseFirestore g_firestore;
    //FirebaseFirestore.getInstance();

    public static void createUserData(String email,String name,MyCompleteListener comepleteListener){

        Map<String, Object> userData = new ArrayMap<>();
        userData.put("EMAIL",email);
        userData.put("NAME",name);
        userData.put("TOTAL_SCORE",0);

        DocumentReference userDoc = g_firestore.collection("USERS")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        WriteBatch batch = g_firestore.batch();
        batch.set(userDoc,userData);


        DocumentReference countDoc = g_firestore.collection("USERS")
                .document("TOTAL_USERS");
        batch.update(countDoc,"COUNT", FieldValue.increment(1));

        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                comepleteListener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        comepleteListener.onFailure();
                    }
                });







    }


}
