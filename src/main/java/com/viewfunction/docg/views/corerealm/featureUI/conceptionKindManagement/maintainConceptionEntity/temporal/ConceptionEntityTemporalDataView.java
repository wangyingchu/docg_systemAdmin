package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.temporal;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.TimeScaleDataPair;
import com.viewfunction.docg.element.commonComponent.SecondaryIconTitle;

import java.util.List;

public class ConceptionEntityTemporalDataView extends VerticalLayout {
    private String conceptionKindName;
    private String conceptionEntityUID;
    private List<TimeScaleDataPair> timeScaleDataPairList;

    public ConceptionEntityTemporalDataView(){
        SecondaryIconTitle secondaryIconTitle2 = new SecondaryIconTitle(VaadinIcon.LIST_SELECT.create(), "时间序列关联信息");
        add(secondaryIconTitle2);

        Accordion accordion = new Accordion();
        accordion.setWidth(95,Unit.PERCENTAGE);

        Span name = new Span("Sophia Williams");
        Span email = new Span("sophia.williams@company.com");
        Span phone = new Span("(501) 555-9128");

        VerticalLayout personalInformationLayout = new VerticalLayout(name,email, phone);
        personalInformationLayout.setSpacing(false);
        personalInformationLayout.setPadding(false);
        personalInformationLayout.setWidth(100, Unit.PERCENTAGE);
        //personalInformationLayout.getStyle()
            //    .set("border-bottom", "1px solid var(--lumo-contrast-20pct)");

        accordion.add("Personal information", personalInformationLayout);

        VerticalLayout personalInformationLayout2 = new VerticalLayout(new Span("aaa"),new Span("aaa"), new Span("aaa"));
        personalInformationLayout2.setSpacing(false);
        personalInformationLayout2.setPadding(false);
        accordion.add("Personal information2", personalInformationLayout2);
        add(accordion);
    }

    public void renderTemporalDataInfo(List<TimeScaleDataPair> timeScaleDataPairList, String conceptionKindName, String conceptionEntityUID){
        this.conceptionKindName = conceptionKindName;
        this.conceptionEntityUID = conceptionEntityUID;
        this.timeScaleDataPairList = timeScaleDataPairList;
    }
}
