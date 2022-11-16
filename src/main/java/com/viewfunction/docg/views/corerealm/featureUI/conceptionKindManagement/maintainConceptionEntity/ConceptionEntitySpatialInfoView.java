package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ConceptionEntitySpatialInfoView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;
    private int conceptionEntityTemporalInfoViewHeightOffset;
    private HorizontalLayout doesNotContainsTemporalInfoMessage;
    public ConceptionEntitySpatialInfoView(String conceptionKind,String conceptionEntityUID,int conceptionEntityTemporalInfoViewHeightOffset){

        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);
        this.setWidthFull();

        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntityTemporalInfoViewHeightOffset = conceptionEntityTemporalInfoViewHeightOffset+106;

        doesNotContainsTemporalInfoMessage = new HorizontalLayout();
        doesNotContainsTemporalInfoMessage.setSpacing(true);
        doesNotContainsTemporalInfoMessage.setPadding(true);
        doesNotContainsTemporalInfoMessage.setMargin(true);
        doesNotContainsTemporalInfoMessage.setWidth(100, Unit.PERCENTAGE);
        doesNotContainsTemporalInfoMessage.setHeight(300,Unit.PIXELS);
        Icon messageLogo = new Icon(VaadinIcon.MAILBOX);
        messageLogo.getStyle()
                .set("color","#2e4e7e").set("padding-right", "5px");
        messageLogo.setSize("30px");
        Label messageLabel = new Label(" 当前概念实体中不包含地理空间相关信息");
        messageLabel.getStyle().set("font-size","var(--lumo-font-size-xl)").set("color","#2e4e7e");
        doesNotContainsTemporalInfoMessage.add(messageLogo,messageLabel);
        add(doesNotContainsTemporalInfoMessage);

    }
}
