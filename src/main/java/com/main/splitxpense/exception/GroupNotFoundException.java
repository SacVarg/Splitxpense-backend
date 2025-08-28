package com.main.splitxpense.exception;

public class GroupNotFoundException extends SplitxpenseException {
    public GroupNotFoundException(Long groupId) {
        super("Group not found with ID: " + groupId, "GROUP_NOT_FOUND");
    }
}
