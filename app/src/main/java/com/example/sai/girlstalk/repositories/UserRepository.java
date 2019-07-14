package com.example.sai.girlstalk.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.widget.Toast;

import com.example.sai.girlstalk.models.User;
import com.example.sai.girlstalk.models.UserProfile;
import com.example.sai.girlstalk.utils.FirebaseUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository {
    private FirebaseUtils firebaseUtils;
    private static UserRepository userRepository;
    private Application application;

    private UserRepository(Application application) {
        firebaseUtils = FirebaseUtils.getInstance();
        this.application = application;
    }

    public static UserRepository getInstance(Application application) {
        if (userRepository == null) userRepository = new UserRepository(application);
        return userRepository;
    }

    public LiveData<Boolean> googleLogin(GoogleSignInAccount account) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseUtils.getAuthInstance().signInWithCredential(credential).addOnCompleteListener(task ->
                result.setValue(task.isSuccessful()));
        return result;
    }

    public LiveData<Boolean> logIn(String email, String password) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        firebaseUtils.getAuthInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
                if (Objects.requireNonNull(firebaseUtils.getAuthInstance().getCurrentUser()).isEmailVerified())
                    result.setValue(true);
                else {
                    firebaseUtils.getAuthInstance().signOut();
                    result.setValue(false);
                }
            else {
                result.setValue(false);
                Toast.makeText(application, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return result;
    }

    public LiveData<Boolean> signUp(User newUser) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        firebaseUtils.getAuthInstance().createUserWithEmailAndPassword(newUser.getEmmail(), newUser.getPassword()).addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
                Objects.requireNonNull(firebaseUtils.getAuthInstance().getCurrentUser()).sendEmailVerification().addOnCompleteListener(emailResult ->
                {
                    if (emailResult.isSuccessful()) {
                        firebaseUtils.getDbInstance().collection("Users").add(newUser).addOnCompleteListener(addResult ->
                        {
                            if (addResult.isSuccessful()) {
                                firebaseUtils.getAuthInstance().signOut();
                                result.setValue(true);
                            } else {
                                Objects.requireNonNull(firebaseUtils.getAuthInstance().getCurrentUser()).delete();
                                firebaseUtils.getAuthInstance().signOut();
                                result.setValue(false);
                            }
                        });
                    } else {
                        Objects.requireNonNull(firebaseUtils.getAuthInstance().getCurrentUser()).delete();
                        firebaseUtils.getAuthInstance().signOut();
                        result.setValue(false);
                    }
                });
            else result.setValue(false);
        });
        return result;
    }

    public LiveData<Boolean> resetPassword(String email) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        firebaseUtils.getAuthInstance().sendPasswordResetEmail(email).addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) result.setValue(true);
            else {
                Toast.makeText(application, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                result.setValue(false);
            }
        });
        return result;
    }

    public LiveData<User> getUser(String email) {
        MutableLiveData<User> result = new MutableLiveData<>();

        firebaseUtils.getDbInstance().collection("Users").whereEqualTo("profile .email", email)
                .get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) {
                QuerySnapshot userSnapshot = task.getResult();
                if (userSnapshot != null && !userSnapshot.getDocuments().isEmpty())
                    result.setValue(userSnapshot.getDocuments().get(0).toObject(User.class));
                else result.setValue(null);
            } else result.setValue(null);
        });
        return result;
    }

    public LiveData<List<User>> getUsers(String username) {
        MutableLiveData<List<User>> result = new MutableLiveData<>();

        firebaseUtils.getDbInstance().collection("Users").whereEqualTo("username", username)
                .get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) {
                QuerySnapshot userSnapshot = task.getResult();
                if (userSnapshot != null && !userSnapshot.getDocuments().isEmpty()) {
                    List<User> users = new ArrayList<>();
                    for (DocumentSnapshot snapshot : userSnapshot)
                        users.add(snapshot.toObject(User.class));
                    result.setValue(users);
                } else result.setValue(null);
            } else result.setValue(null);
        });

        return result;
    }

    public LiveData<List<User>> getFriends(String email) {
        MutableLiveData<List<User>> result = new MutableLiveData<>();

        firebaseUtils.getDbInstance().collection("Users").whereEqualTo("profile .email", email)
                .get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) {
                QuerySnapshot userSnapshot = task.getResult();
                if (userSnapshot != null && !userSnapshot.getDocuments().isEmpty()) {
                    DocumentReference userReference = userSnapshot.getDocuments().get(0).getReference();
                    userReference.collection("friends").get().addOnCompleteListener(friendsTask ->
                    {
                        if (friendsTask.isSuccessful()) {
                            QuerySnapshot friendsSnapshot = task.getResult();
                            if (friendsSnapshot != null) {
                                List<User> friends = new ArrayList<>();
                                for (DocumentSnapshot snapshot : friendsSnapshot)
                                    friends.add(snapshot.toObject(User.class));
                            } else result.setValue(null);
                        } else result.setValue(null);
                    });
                } else result.setValue(null);
            } else result.setValue(null);
        });

        return result;
    }

    public LiveData<Boolean> sendFriendRequest(String userEmail, UserProfile currentUser) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        firebaseUtils.getDbInstance().collection("Users").whereEqualTo("profile .email", userEmail)
                .get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    DocumentReference userReference = task.getResult().getDocuments().get(0).getReference();
                    userReference.collection("friendRequest").add(currentUser).addOnCompleteListener(requestTask ->
                            result.setValue(requestTask.isSuccessful()));
                } else result.setValue(false);
            } else result.setValue(false);
        });
        return result;
    }

    public LiveData<List<UserProfile>> getFriendRequests(String userEmail) {
        MutableLiveData<List<UserProfile>> result = new MutableLiveData<>();

        firebaseUtils.getDbInstance().collection("Users").whereEqualTo("profile .email", userEmail)
                .get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    DocumentReference userReference = task.getResult().getDocuments().get(0).getReference();
                    userReference.collection("friendRequest").get().addOnCompleteListener(requestTask ->
                    {
                        if (requestTask.isSuccessful()) {
                            QuerySnapshot requestSanpshot = requestTask.getResult();
                            if (requestSanpshot != null) {
                                List<UserProfile> requests = new ArrayList<>();
                                for (DocumentSnapshot snapshot : requestSanpshot)
                                    requests.add(snapshot.toObject(UserProfile.class));
                            }
                        } else result.setValue(null);
                    });
                } else result.setValue(null);
            } else result.setValue(null);
        });
        return result;
    }

    public LiveData<Boolean> acceptFriendRequest(String email, UserProfile friend) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        firebaseUtils.getDbInstance().collection("Users").whereEqualTo("profile .email", email)
                .get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    DocumentReference userReference = task.getResult().getDocuments().get(0).getReference();
                    userReference.collection("friends").add(friend).addOnCompleteListener(addTask ->
                    {
                        if (addTask.isSuccessful()) {
                            userReference.collection("friendRequest").whereEqualTo("email", friend.getEmail())
                                    .get().addOnCompleteListener(getTask ->
                            {
                                if (getTask.isSuccessful()) {
                                    QuerySnapshot snapshot = getTask.getResult();
                                    if (snapshot != null) {
                                        snapshot.getDocuments().get(0).getReference().delete();
                                        result.setValue(true);
                                    } else result.setValue(false);
                                } else result.setValue(false);
                            });
                        } else result.setValue(false);
                    });
                } else result.setValue(false);
            } else result.setValue(false);
        });
        return result;
    }

}
