package com.mywings.messmanagementsystem.model;

import java.util.List;

public class UserHolder {

    public static UserHolder getInstance() {
        return UserHolderHelper.INSTANCE;
    }

    private User user;

    private List<Mess> messes;

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


    private static class UserHolderHelper {
        final static UserHolder INSTANCE = new UserHolder();
    }
}
