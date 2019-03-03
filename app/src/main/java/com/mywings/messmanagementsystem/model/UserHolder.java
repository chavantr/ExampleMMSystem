package com.mywings.messmanagementsystem.model;

import java.util.List;

public class UserHolder {

    public static UserHolder getInstance() {
        return UserHolderHelper.INSTANCE;
    }

    private User user;

    private List<Mess> messes;

    private int id;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Mess> getMesses() {
        return messes;
    }

    public void setMesses(List<Mess> messes) {
        this.messes = messes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    private static class UserHolderHelper {
        final static UserHolder INSTANCE = new UserHolder();
    }
}
