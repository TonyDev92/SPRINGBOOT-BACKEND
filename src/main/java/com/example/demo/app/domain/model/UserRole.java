package com.example.demo.app.domain.model;

import java.time.LocalDateTime;


public class UserRole {

    private Long userId;
    private Long roleId;
    private LocalDateTime assignedAt;
    private String roleName;

    public UserRole() {
    }

    public UserRole(Long userId, Long roleId, LocalDateTime assignedAt, String roleName) {
        this.userId = userId;
        this.roleId = roleId;
        this.assignedAt = assignedAt;
        this.roleName = roleName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                ", assignedAt=" + assignedAt +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}
