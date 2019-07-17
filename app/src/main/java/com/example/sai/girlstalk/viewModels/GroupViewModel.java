package com.example.sai.girlstalk.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.sai.girlstalk.models.Group;
import com.example.sai.girlstalk.models.GroupMessage;
import com.example.sai.girlstalk.models.UserProfile;
import com.example.sai.girlstalk.repositories.GroupRepository;

import java.util.List;

public class GroupViewModel extends AndroidViewModel {
    private GroupRepository groupRepository;

    public GroupViewModel(@NonNull Application application) {
        super(application);
        groupRepository = GroupRepository.getInstance(application);
    }

    public LiveData<Boolean> createGroup(Group newGroup) {
        return groupRepository.createGroup(newGroup);
    }

    public LiveData<Boolean> addMember(UserProfile userProfile, String groupTitle) {
        return groupRepository.addMember(userProfile, groupTitle);
    }

    public LiveData<List<Group>> getAll() {
        return groupRepository.getAll();
    }

    public LiveData<Group> getGroup(String groupTitle) {
        return groupRepository.getGroup(groupTitle);
    }

    public LiveData<Boolean> addMessage(String groupTitle, GroupMessage message) {
        return groupRepository.addMessage(groupTitle, message);
    }

    public LiveData<List<GroupMessage>> getAllMessages(String groupTitle) {
        return groupRepository.getAllMessages(groupTitle);
    }

    public LiveData<List<UserProfile>> getAllMembers(String groupTitle) {
        return groupRepository.getAllMembers(groupTitle);
    }

    public LiveData<List<Group>> getUserGroups(UserProfile userProfile) {
        return groupRepository.getUserGroups(userProfile);
    }
}
