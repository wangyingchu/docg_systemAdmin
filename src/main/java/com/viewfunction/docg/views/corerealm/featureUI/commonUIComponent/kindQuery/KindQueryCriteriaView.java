package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.kindQuery;

import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.queryConceptionKind.QueryConditionItemWidget;

public interface KindQueryCriteriaView {

    public void addQueryConditionItem(String attributeName, AttributeDataType attributeDataType);

    public void removeCriteriaFilterItem(QueryConditionItemWidget queryConditionItemWidget);
}
