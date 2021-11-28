package com.example.myapplication.eventbus;

public class EventLoad {
    private boolean getData;

    public EventLoad(boolean getData) {
        this.getData = getData;
    }

    public boolean isGetData() {
        return getData;
    }

    public void setGetData(boolean getData) {
        this.getData = getData;
    }
}
