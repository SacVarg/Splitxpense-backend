package com.main.splitxpense.controller;

import com.main.splitxpense.dto.GroupRequest;
import com.main.splitxpense.dto.GroupResponse;
import com.main.splitxpense.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@CrossOrigin(origins = "http://localhost:3000")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@Valid @RequestBody GroupRequest request) {
        GroupResponse group = groupService.createGroup(request);
        return ResponseEntity.ok(group);
    }

    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        List<GroupResponse> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable Long id) {
        GroupResponse group = groupService.getGroupById(id);  // ← Direct call, no .map()
        return ResponseEntity.ok(group);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<GroupResponse>> getGroupsByUserId(@PathVariable Long userId) {
        List<GroupResponse> groups = groupService.getGroupsByUserId(userId);
        return ResponseEntity.ok(groups);
    }

    @PostMapping("/{groupId}/members/{userId}")
    public ResponseEntity<GroupResponse> addMemberToGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        GroupResponse group = groupService.addMemberToGroup(groupId, userId);
        return ResponseEntity.ok(group);
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<Void> removeMemberFromGroup(@PathVariable Long groupId, @PathVariable Long userId) {
        groupService.removeMemberFromGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }
}
