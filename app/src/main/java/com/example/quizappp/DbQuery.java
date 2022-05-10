package com.example.quizappp;

import android.content.Context;
import android.util.ArrayMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DbQuery {
   public static  FirebaseFirestore g_Firestore;
   public static List<CategoryModel> g_catList=new ArrayList<>();
   public static List<TestModel> g_testList=new ArrayList<>();
   public static int g_selected_cat_index=0;
   public static int g_selected_test_index=0;
   public static List<QuestionModel> g_quesList=new ArrayList<>();

   public static void createUserData(String email, String name, MyCompleteListener completeListener)
   {
      Map<String,Object> userData=new ArrayMap<>();
      userData.put("EMAIL",email);
      userData.put("NAME",name);
      userData.put("TOTAL_SCORE",0);
      DocumentReference userDoc=g_Firestore.collection("USERS").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
      WriteBatch batch=g_Firestore.batch();
      batch.set(userDoc,userData);

      DocumentReference countDoc=g_Firestore.collection("USERS").document("TOTAL_USERS");
      batch.update(countDoc,"COUNT", FieldValue.increment(1));
      batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
         @Override
         public void onSuccess(Void unused) {
            completeListener.onSuccess();
         }
      }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
            completeListener.onFailure();
         }
      });
   }
   public static void loadCategories(MyCompleteListener completeListener)
   {
      g_catList.clear();
      g_Firestore.collection("QUIZ").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
         @Override
         public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            Map<String, QueryDocumentSnapshot> docList=new ArrayMap<>();
            for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
            {
               docList.put(doc.getId(),doc);
            }
            QueryDocumentSnapshot catListDooc=docList.get("Categories");
            long catCount=catListDooc.getLong("COUNT");
            for(int i=1;i<=catCount;i++)
            {
               String catID=catListDooc.getString("CAT"+String.valueOf(i)+"_ID");
               QueryDocumentSnapshot catDoc=docList.get(catID);
               int noOfTest=catDoc.getLong("NO_OF_TESTS").intValue();
               String catName=catDoc.getString("NAME");
               g_catList.add(new CategoryModel(catID,catName,noOfTest));

            }
            completeListener.onSuccess();
         }
      }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
               completeListener.onFailure();
         }
      });
   }
   public static void loadTestData(MyCompleteListener completeListener)
   {
      g_testList.clear();
      g_Firestore.collection("QUIZ").document(g_catList.get(g_selected_cat_index).getDocID())
              .collection("TESTS_LIST").document("TESTS_INFO").get()
              .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                  @Override
                  public void onSuccess(DocumentSnapshot documentSnapshot) {
                     int noOfTests=g_catList.get(g_selected_cat_index).getNoOfTests();
                     for(int i=1;i<=noOfTests;i++)
                     {
                        g_testList.add(new TestModel(documentSnapshot.getString("TEST" + String.valueOf(i)+"_ID"),
                                0,documentSnapshot.getLong("TEST"+String.valueOf(i)+"_TIME").intValue()));
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
   public static void loadQuestions(MyCompleteListener completeListener)
   {
      g_quesList.clear();
      g_Firestore.collection("Questions")
              .whereEqualTo("CATEGORY",g_catList.get(g_selected_cat_index).getDocID())
              .whereEqualTo("TEST",g_testList.get(g_selected_test_index).getTestID())
              .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
         @Override
         public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
            for(DocumentSnapshot doc :queryDocumentSnapshots)
            {
               g_quesList.add(new QuestionModel(
                       doc.getString("QUESTION"),
                       doc.getString("A"),
                       doc.getString("B"),
                       doc.getString("C"),
                       doc.getString("D"),
                       doc.getLong("ANSWER").intValue()
               ));
            }
            completeListener.onSuccess();
         }
      }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {
            completeListener.onFailure();
         }
      });
   }
}
