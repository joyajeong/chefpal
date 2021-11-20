package au.edu.unsw.infs3634.unswgamifiedlearningapp;
//everything related to Cloud Firestore (Firebase database)

import android.util.ArrayMap;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.edu.unsw.infs3634.unswgamifiedlearningapp.Model.CategoryQuiz;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.Model.Profile;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.Model.Question;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.Model.Rank;
import au.edu.unsw.infs3634.unswgamifiedlearningapp.Model.TestQuestion;

public class DbQuery {
    public static FirebaseFirestore g_firestore;
    //FirebaseFirestore.getInstance();
    public static List<CategoryQuiz> g_categoryList = new ArrayList<>();
    public static int g_selected_cat_index = 0;
    public static List<TestQuestion> g_testList = new ArrayList<>();
    public static int g_selected_test_index = 0;
    public static List <Question> g_quesList = new ArrayList<>();
    public static Rank myPerformance = new Rank(null,0,-1);
    public static boolean isMeOnTopList = false;

    public static Profile myProfile = new Profile("NA",null);

    public static List<Rank> g_userList = new ArrayList<>();
    public static int g_usersCount = 0;

    public static final int NOT_VISITED =0;
    public static final int UNANSWERED =1;
    public static final int ANSWERED =2;
    public static final int REVIEW =3;



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

    public static void loadQuestions(final MyCompleteListener completeListener){
        g_quesList.clear();
        //fetch questions
        g_firestore.collection("Questions")
                .whereEqualTo("CATEGORY",g_categoryList.get(g_selected_cat_index).getDocID())
                .whereEqualTo("TEST",g_testList.get(g_selected_test_index).getTestID())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc: queryDocumentSnapshots){
                            g_quesList.add(new Question (
                                    doc.getString("QUESTION"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getLong("ANSWER").intValue(),
                                    doc.getString("FEEDBACK"),
                                    -1,
                                    NOT_VISITED
                                    ));
                            }
                            completeListener.onSuccess();

                        }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadMyScores(MyCompleteListener completeListener){
        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_SCORES")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for(int i=0; i<g_testList.size();i++){
                            int top = 0;
                            if(documentSnapshot.get(g_testList.get(i).getTestID())!=null){
                                //change top score
                                top = documentSnapshot.getLong(g_testList.get(i).getTestID()).intValue();
                            }
                            g_testList.get(i).setTopScore(top);
                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }


    //fetch top 10 users only
    public static void getTopUsers(MyCompleteListener completeListener){
        g_userList.clear();
        String myUID = FirebaseAuth.getInstance().getUid();

        g_firestore.collection("USERS")
                .whereGreaterThan("TOTAL_SCORE",0)
                .orderBy("TOTAL_SCORE", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int rank = 1;
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                            g_userList.add(new Rank(
                                    doc.getString("NAME"),
                                    doc.getLong("TOTAL_SCORE").intValue(),
                                    rank
                            ));

                            //match with ids on Top List?
                            if(myUID.compareTo(doc.getId()) ==0){
                                isMeOnTopList = true;
                                myPerformance.setRank(rank);
                            }
                            rank++;
                        }
                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();

                    }
                });


    }

    public static void getUsersCount(MyCompleteListener completeListener){
        g_firestore.collection("USERS").document("TOTAL_USERS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        g_usersCount = documentSnapshot.getLong("COUNT"). intValue();
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();

                    }
                });

    }

    public static void saveResult(int score, MyCompleteListener completeListener){
        WriteBatch batch = g_firestore.batch();
        DocumentReference userDoc = g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid());
        batch.update(userDoc, "TOTAL_SCORE", score);

        if(score>g_testList.get(g_selected_test_index).getTopScore()){
            DocumentReference scoreDoc = userDoc.collection("USER_DATA").document("MY_SCORES");
            Map<String, Object> testData = new ArrayMap<>();
            testData.put(g_testList.get(g_selected_test_index).getTestID(),score);
            batch.set(scoreDoc,testData, SetOptions.merge());
        }
        batch.commit()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if(score > g_testList.get(g_selected_test_index).getTopScore()){
                            g_testList.get(g_selected_test_index).setTopScore(score);
                            myPerformance.setScore(score);
                            completeListener.onSuccess();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();

                    }
                });

    }



    public static void loadCategories(final MyCompleteListener completeListener){
        //fetch data from DB
        g_categoryList.clear();
        g_firestore.collection("QUIZ").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        //get all docs
                        Map<String, QueryDocumentSnapshot> docList = new ArrayMap<>();
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                            //store docs one by one to map
                            docList.put(doc.getId(),doc);
                        }
                        QueryDocumentSnapshot catListDoc = docList.get("CATEGORIES");
                        long catCount = catListDoc.getLong("COUNT");
                        for(int i = 1; i <=catCount; i++){
                            String catID = catListDoc.getString("CAT"+ String.valueOf(i)+"_ID");

                            QueryDocumentSnapshot catDoc = docList.get(catID);

                            int testNo = catDoc.getLong("TEST_NO").intValue();

                            String catName = catDoc.getString("NAME");

                            g_categoryList.add(new CategoryQuiz(catID, catName,testNo));
                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();

                    }
                });

    }

    public static void loadTestData(final MyCompleteListener completeListener){
        g_testList.clear();

        g_firestore.collection("QUIZ").document(g_categoryList.get(g_selected_cat_index).getDocID())
                .collection("TEST_LIST").document("TESTS_INFO")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int testNo = g_categoryList.get(g_selected_cat_index).getNumberOfTests();
                        for(int i =1; i<=testNo; i++){
                            g_testList.add(new TestQuestion(
                                    documentSnapshot.getString("TEST"+String.valueOf(i)+"_ID"),
                                            0,
                                            documentSnapshot.getLong("TEST"+String.valueOf(i)+"_TIME").intValue()

                            ));
                        }


                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void geUserData(final MyCompleteListener completeListener){
        g_firestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        myProfile.setName(documentSnapshot.getString("NAME"));
                        myProfile.setEmail(documentSnapshot.getString("EMAIL"));
                        myPerformance.setScore(documentSnapshot.getLong("TOTAL_SCORE").intValue());
                        completeListener.onSuccess();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

    public static void loadData (final MyCompleteListener completeListener){
        loadCategories(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                geUserData(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        getTopUsers(completeListener);


                    }

                    @Override
                    public void onFailure() {
                        completeListener.onFailure();

                    }
                });

            }


            @Override
            public void onFailure() {
                completeListener.onFailure();

            }
        });
    }





}
