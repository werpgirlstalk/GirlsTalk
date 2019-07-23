package com.example.sai.girlstalk.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.sai.girlstalk.models.Group;
import com.example.sai.girlstalk.models.GroupMessage;
import com.example.sai.girlstalk.models.UserProfile;
import com.example.sai.girlstalk.utils.FirebaseUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupRepository {
    private static GroupRepository groupRepository;
    private final String GROUP_COLLECTION_NAME = "Groups";
    private final String GROUP_COLLECTION_TITLE = "title";
    private final String GROUP_COLLECTION_MESSAGE = "messages";
    private FirebaseUtils firebaseUtils;
    private Application application;
    //private final String GROUP_COLLECTION_MEMBERS= "members";

    private GroupRepository(Application application) {
        firebaseUtils = FirebaseUtils.getInstance();
        this.application = application;
    }

    public static GroupRepository getInstance(Application application) {
        if (groupRepository == null) groupRepository = new GroupRepository(application);
        return groupRepository;
    }

    public LiveData<Boolean> createGroup(Group newGroup) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        firebaseUtils.getDbInstance().collection(GROUP_COLLECTION_NAME).add(newGroup).addOnCompleteListener(task ->
                result.setValue(task.isSuccessful()));
        return result;
    }

    public LiveData<List<Group>> getAll() {
        MutableLiveData<List<Group>> result = new MutableLiveData<>();

        firebaseUtils.getDbInstance().collection(GROUP_COLLECTION_NAME).get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) {
                List<Group> groups = new ArrayList<>();
                for (DocumentSnapshot snapshot : task.getResult())
                    groups.add(snapshot.toObject(Group.class));
                result.setValue(groups);
            } else result.setValue(null);
        });
        return result;
    }

    public LiveData<Group> getGroup(String groupTitle) {
        MutableLiveData<Group> result = new MutableLiveData<>();

        firebaseUtils.getDbInstance().collection(GROUP_COLLECTION_NAME).whereEqualTo("title", groupTitle).get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful())
                result.setValue(task.getResult().getDocuments().get(0).toObject(Group.class));
            else result.setValue(null);
        });
        return result;
    }

    public LiveData<Boolean> addMember(UserProfile userProfile, String groupTitle) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        firebaseUtils.getDbInstance().collection(GROUP_COLLECTION_NAME).whereEqualTo(GROUP_COLLECTION_TITLE, groupTitle).get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                Group group = documentSnapshot.toObject(Group.class);
                group.getMembers().add(userProfile);

                task.getResult().getDocuments().get(0).getReference().delete().addOnCompleteListener(deleteTask ->
                {
                    if (deleteTask.isSuccessful()) {
                        firebaseUtils.getDbInstance().collection(GROUP_COLLECTION_NAME).add(group).addOnCompleteListener(addTask ->
                                result.setValue(addTask.isSuccessful()));
                    } else result.setValue(false);
                });
            } else result.setValue(false);
        });
        return result;
    }

    public LiveData<Boolean> addMessage(String groupTitle, GroupMessage message) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();

        firebaseUtils.getDbInstance().collection(GROUP_COLLECTION_NAME).whereEqualTo(GROUP_COLLECTION_TITLE, groupTitle).get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) {
                CollectionReference groupMessages = Objects.requireNonNull(task.getResult())
                        .getDocuments().get(0).getReference().collection(GROUP_COLLECTION_MESSAGE);

                groupMessages.add(message).addOnCompleteListener(addTask -> result.setValue(addTask.isSuccessful()));
            } else result.setValue(false);
        });

        return result;
    }

    public LiveData<List<GroupMessage>> getAllMessages(String groupTitle) {
        MutableLiveData<List<GroupMessage>> result = new MutableLiveData<>();

        firebaseUtils.getDbInstance().collection(GROUP_COLLECTION_NAME).whereEqualTo(GROUP_COLLECTION_TITLE, groupTitle).get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) {
                CollectionReference groupMessages = Objects.requireNonNull(task.getResult())
                        .getDocuments().get(0).getReference().collection(GROUP_COLLECTION_MESSAGE);

                groupMessages.get().addOnCompleteListener(getMessagesTask ->
                {
                    if (getMessagesTask.isSuccessful()) {
                        List<GroupMessage> groupMessageList = new ArrayList<>();
                        if (getMessagesTask.getResult() != null) {
                            for (DocumentSnapshot snapshot : getMessagesTask.getResult())
                                groupMessageList.add(snapshot.toObject(GroupMessage.class));
                            result.setValue(groupMessageList);
                        } else result.setValue(null);
                    } else result.setValue(null);
                });
            } else result.setValue(null);
        });
        return result;
    }

    public LiveData<List<UserProfile>> getAllMembers(String groupTitle) {
        MutableLiveData<List<UserProfile>> result = new MutableLiveData<>();

        firebaseUtils.getDbInstance().collection(GROUP_COLLECTION_NAME).whereEqualTo(GROUP_COLLECTION_TITLE, groupTitle).get()
                .addOnCompleteListener(task ->
                {
                    if (task.isSuccessful()) {
                        Group group = task.getResult().getDocuments().get(0).toObject(Group.class);
                        result.setValue(group.getMembers());
                    } else result.setValue(null);
                });
        return result;
    }

    public LiveData<List<Group>> getUserGroups(UserProfile userProfile) {
        MutableLiveData<List<Group>> result = new MutableLiveData<>();
        firebaseUtils.getDbInstance().collection(GROUP_COLLECTION_NAME).whereArrayContains("members", userProfile).get().addOnCompleteListener(task ->
        {
            if (task.isSuccessful()) {
                List<Group> groups = new ArrayList<>();
                for (DocumentSnapshot snapshot : task.getResult())
                    groups.add(snapshot.toObject(Group.class));

                result.setValue(groups);
            }
        });
        return result;
    }
}
