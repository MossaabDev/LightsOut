package com.proglobby.lightsout;

import java.util.List;

public class Page {
    private List<Driver> driverList;

    public boolean isReady = true;

    public Page(List<Driver> driverList) {
        this.driverList = driverList;
    }

    public List<Driver> getDriverList() {
        return driverList;
    }

    public void setDriverList(List<Driver> driverList) {
        this.driverList = driverList;
    }
}
