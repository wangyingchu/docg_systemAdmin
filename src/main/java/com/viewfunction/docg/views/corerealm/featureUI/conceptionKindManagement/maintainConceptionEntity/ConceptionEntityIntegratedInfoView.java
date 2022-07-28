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
        tabs.getElement().getStyle().set("width","100%");

        Tab tab0 = tabs.add("", new Label("1"),false);
        Span relationInfoSpan =new Span();
        Icon relationInfoIcon = new Icon(VaadinIcon.EXCHANGE);
        relationInfoIcon.setSize("20px");
        Label relationInfoLabel = new Label(" 实体数据关联信息");
        relationInfoSpan.add(relationInfoIcon,relationInfoLabel);
        tab0.add(relationInfoSpan);

        Tab tab1 = tabs.add("", new Label("2"),false);
        Span networkGraphSpan =new Span();
        Icon networkGraphIcon = new Icon(VaadinIcon.CLUSTER);
        networkGraphIcon.setSize("20px");
        Label networkGraphLabel = new Label(" 实体数据关联网络图");
        networkGraphSpan.add(networkGraphIcon,networkGraphLabel);
        tab1.add(networkGraphSpan);

        Tab tab2 = tabs.add("", new Label("3"),false);
        Span earthMapSpan =new Span();
        Icon earthMapIcon = new Icon(VaadinIcon.GLOBE);
        earthMapIcon.setSize("20px");
        Label earthMapLabel = new Label(" 实体地理空间相关信息");
        earthMapSpan.add(earthMapIcon,earthMapLabel);
        tab2.add(earthMapSpan);

        Tab tab3 = tabs.add("", new Label("4"),false);
        Span timeChartSpan =new Span();
        Icon timeChartIcon = new Icon(VaadinIcon.CALENDAR_CLOCK);
        timeChartIcon.setSize("20px");
        Label timeChartLabel = new Label(" 实体时间序列相关信息");
        timeChartSpan.add(timeChartIcon,timeChartLabel);
        tab3.add(timeChartSpan);

        add(tabs,container);
    }
}
