package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import org.vaadin.tabs.PagedTabs;

public class ConceptionEntityIntegratedInfoView extends VerticalLayout {

    private String conceptionKind;
    private String conceptionEntityUID;

    public ConceptionEntityIntegratedInfoView(String conceptionKind,String conceptionEntityUID){
        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.setPadding(false);
        this.setMargin(false);
        this.setSpacing(false);
        VerticalLayout container = new VerticalLayout();
        PagedTabs tabs = new PagedTabs(container);
        //tabs.

        Span coreRealmSpan =new Span();
        Icon coreRealmLogo = new Icon(VaadinIcon.CLUSTER);
        coreRealmLogo.setSize("20px");
        Label coreRealmLabel = new Label(" 实体数据关联信息");
        coreRealmSpan.add(coreRealmLogo,coreRealmLabel);
        Tab tab0 = tabs.add("", new Label("1"),false);
        tab0.add(coreRealmSpan);

        tabs.add("实体数据关联网络图", new Label("2"),false);
        tabs.add("实体数据地理空间信息", new Label("3"),false);
        tabs.add("实体时间序列相关信息", new Label("4"),false);

        add(tabs,container);
    }
}
