package com.viewfunction.docg.views.computegrid.featureUI.dataComputeGridManagement;

public class GridComputeUnitVO {

    private String unitID;
    private String hostName;
    private String IP;
    private String unitType;

    public GridComputeUnitVO(){}

    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
}
