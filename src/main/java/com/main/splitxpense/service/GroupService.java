package com.main.splitxpense.service;

import com.main.splitxpense.dto.GroupRequest;
import com.main.splitxpense.dto.GroupResponse;
import com.main.splitxpense.exception.GroupNotFoundException;
import com.main.splitxpense.exception.UserNotFoundException;
import com.main.splitxpense.mapper.DTOMapper;
import com.main.splitxpense.model.Group;
import com.main.splitxpense.model.GroupMember;
import com.main.splitxpense.model.User;
import com.main.splitxpense.repository.GroupRepository;
import com.main.splitxpense.repository.GroupMemberRepository;
import com.main.splitxpense.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupMemberRepository groupMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DTOMapper dtoMapper;

    public GroupResponse createGroup(GroupRequest request) {
        // Validate creator exists
        if (!userRepository.existsById(request.getCreatedBy())) {
            throw new UserNotFoundException(request.getCreatedBy());
        }

        Group group = new Group();
        group.setName(request.getName());
        group.setCreatedBy(request.getCreatedBy());

        Group savedGroup = groupRepository.save(group);

        // Add members to the group
        if (request.getMemberIds() != null) {
            for (Long userId : request.getMemberIds()) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(userId));

                GroupMember member = new GroupMember(savedGroup, user);
                groupMemberRepository.save(member);
            }

            // Reload group with members
            savedGroup = groupRepository.findById(savedGroup.getId()).orElse(savedGroup);
        }

        return dtoMapper.toGroupResponse(savedGroup);
    }

    public List<GroupResponse> getAllGroups() {
        List<Group> groups = groupRepository.findAll();
        return dtoMapper.toGroupResponseList(groups);
    }

    public GroupResponse getGroupById(Long id) {  // ← Changed to return GroupResponse directly
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new GroupNotFoundException(id));
        return dtoMapper.toGroupResponse(group);
    }

    public List<GroupResponse> getGroupsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        List<Group> groups = groupRepository.findByCreatedBy(userId);
        return dtoMapper.toGroupResponseList(groups);
    }

    public GroupResponse addMemberToGroup(Long groupId, Long userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        GroupMember member = new GroupMember(group, user);
        groupMemberRepository.save(member);

        // Reload group with updated members
        Group updatedGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(groupId));
        return dtoMapper.toGroupResponse(updatedGroup);
    }

    public void removeMemberFromGroup(Long groupId, Long userId) {
        // Validate group exists
        if (!groupRepository.existsById(groupId)) {
            throw new GroupNotFoundException(groupId);
        }

        // Validate user exists
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(userId);
        }

        List<GroupMember> members = groupMemberRepository.findByGroupId(groupId);
        members.stream()
                .filter(member -> member.getUser().getId().equals(userId))
                .findFirst()
                .ifPresent(groupMemberRepository::delete);
    }
}
