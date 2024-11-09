package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entitiesTopologyAnalysis;

public class ExpandParameters {

    private int expandPathResultMaxPathCount = 20;
    private int expandGraphResultMaxConceptionEntityCount = 50;
    private int expandSpanningTreeResultMaxConceptionEntityCount = 50;

    public int getExpandPathResultMaxPathCount() {
        return expandPathResultMaxPathCount;
    }

    public void setExpandPathResultMaxPathCount(int expandPathResultMaxPathCount) {
        this.expandPathResultMaxPathCount = expandPathResultMaxPathCount;
    }

    public int getExpandGraphResultMaxConceptionEntityCount() {
        return expandGraphResultMaxConceptionEntityCount;
    }

    public void setExpandGraphResultMaxConceptionEntityCount(int expandGraphResultMaxConceptionEntityCount) {
        this.expandGraphResultMaxConceptionEntityCount = expandGraphResultMaxConceptionEntityCount;
    }

    public int getExpandSpanningTreeResultMaxConceptionEntityCount() {
        return expandSpanningTreeResultMaxConceptionEntityCount;
    }

    public void setExpandSpanningTreeResultMaxConceptionEntityCount(int expandSpanningTreeResultMaxConceptionEntityCount) {
        this.expandSpanningTreeResultMaxConceptionEntityCount = expandSpanningTreeResultMaxConceptionEntityCount;
    }
}
