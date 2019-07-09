package com.example.sai.girlstalk.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtils
{
    private static FirebaseUtils firebaseUtils;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private PhoneAuthProvider phoneAuthProvider;

    private FirebaseUtils(){}

    public static FirebaseUtils getInstance()
    {
        if (firebaseUtils == null) firebaseUtils = new FirebaseUtils();
        return firebaseUtils;
    }

    public FirebaseAuth getAuthInstance()
    {
        if (auth == null) auth = FirebaseAuth.getInstance();
        return auth;
    }

    public FirebaseFirestore getDbInstance()
    {
        if (db == null) db = FirebaseFirestore.getInstance();
        return db;
    }

    public PhoneAuthProvider getPhoneAuthInstance()
    {
        if (phoneAuthProvider == null) phoneAuthProvider = PhoneAuthProvider.getInstance();
        return phoneAuthProvider;
    }


}
