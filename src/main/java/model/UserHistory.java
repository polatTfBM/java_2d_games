package model;

import java.time.LocalDateTime;

/**
 * Represents a user action log entry.
 */
public class UserHistory {
    private int id;
    private int userId;
    private String action;
    private LocalDateTime actionTime;

    public UserHistory() {
    }

    public UserHistory(int id, int userId, String action, LocalDateTime actionTime) {
        this.id = id;
        this.userId = userId;
        this.action = action;
        this.actionTime = actionTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public LocalDateTime getActionTime() {
        return actionTime;
    }

    public void setActionTime(LocalDateTime actionTime) {
        this.actionTime = actionTime;
    }

    @Override
    public String toString() {
        return "UserHistory{" +
                "id=" + id +
                ", userId=" + userId +
                ", action='" + action + '\'' +
                ", actionTime=" + actionTime +
                '}';
    }
}
