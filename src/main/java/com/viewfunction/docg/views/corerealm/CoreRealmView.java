package com.viewfunction.docg.views.corerealm;

import ch.carnet.kasparscherrer.VerticalScrollLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.views.MainLayout;
import com.viewfunction.docg.views.corerealm.featureUI.*;

@PageTitle("数海云图 - 核心领域模型 [ Core Realm ]")
@Route(value = "core-realm", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class CoreRealmView extends Div {

    //https://vaadin.com/directory/component/scrolllayout
    //https://vaadin.com/forum/thread/17192835/scrollable-layout
    private VerticalScrollLayout featureContainerLayout;

    private Registration listener;

    private CoreRealmDataUI coreRealmDataUI;
    private ConceptionKindManagementUI conceptionKindManagementUI;
    private RelationKindManagementUI relationKindManagementUI;
    private AttributeKindManagementUI attributeKindManagementUI;
    private AttributesViewKindManagementUI attributesViewKindManagementUI;
    private ClassificationManagementUI classificationManagementUI;
    private GeospatialRegionManagementUI geospatialRegionManagementUI;
    private TimeFlowManagementUI timeFlowManagementUI;

    public CoreRealmView() {

        this.setWidth(100, Unit.PERCENTAGE);

        Span coreRealmSpan =new Span();
        Icon coreRealmLogo = new Icon(VaadinIcon.CLUSTER);
        coreRealmLogo.setSize("20px");
        Label coreRealmLabel = new Label(" CoreRealm-领域数据");
        coreRealmLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        coreRealmSpan.add(coreRealmLogo,coreRealmLabel);
        Tab coreRealmTab = new Tab(coreRealmSpan);
        coreRealmTab.setId("coreRealmTab");

        Span conceptionKindSpan =new Span();
        Icon conceptionKindLogo = new Icon(VaadinIcon.CUBE);
        conceptionKindLogo.setSize("20px");
        Label conceptionKindLabel = new Label(" ConceptionKind-概念类型");
        conceptionKindLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        conceptionKindSpan.add(conceptionKindLogo,conceptionKindLabel);
        Tab conceptionKindTab = new Tab(conceptionKindSpan);
        conceptionKindTab.setId("conceptionKindTab");

        Span relationKindSpan =new Span();
        Icon relationKindLogo = new Icon(VaadinIcon.CONNECT_O);
        relationKindLogo.setSize("20px");
        Label relationKindLabel = new Label(" RelationKind-关系类型");
        relationKindLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        relationKindSpan.add(relationKindLogo,relationKindLabel);
        Tab relationKindTab = new Tab(relationKindSpan);
        relationKindTab.setId("relationKindTab");

        Span attributeKindSpan =new Span();
        Icon attributeKindLogo = new Icon(VaadinIcon.INPUT);
        attributeKindLogo.setSize("20px");
        Label attributeKindLabel = new Label(" AttributeKind-属性类型");
        attributeKindLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        attributeKindSpan.add(attributeKindLogo,attributeKindLabel);
        Tab attributeKindTab = new Tab(attributeKindSpan);
        attributeKindTab.setId("attributeKindTab");

        Span attributesViewKindSpan =new Span();
        Icon attributesViewKindLogo = new Icon(VaadinIcon.TASKS);
        attributesViewKindLogo.setSize("20px");
        Label attributesViewKindLabel = new Label(" AttributesViewKind-属性视图类型");
        attributesViewKindLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        attributesViewKindSpan.add(attributesViewKindLogo,attributesViewKindLabel);
        Tab attributesViewKindTab = new Tab(attributesViewKindSpan);
        attributesViewKindTab.setId("attributesViewKindTab");

        Span classificationSpan =new Span();
        Icon classificationLogo = new Icon(VaadinIcon.TAGS);
        classificationLogo.setSize("20px");
        Label classificationLabel = new Label(" Classification-分类");
        classificationLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        classificationSpan.add(classificationLogo,classificationLabel);
        Tab classificationTab = new Tab(classificationSpan);
        classificationTab.setId("classificationTab");

        Span geospatialRegionSpan =new Span();
        Icon geospatialRegionLogo = new Icon(VaadinIcon.GLOBE_WIRE);
        geospatialRegionLogo.setSize("20px");
        Label geospatialRegionLabel = new Label(" GeospatialRegion-地理空间区域");
        geospatialRegionLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        geospatialRegionSpan.add(geospatialRegionLogo,geospatialRegionLabel);
        Tab geospatialRegionTab = new Tab(geospatialRegionSpan);
        geospatialRegionTab.setId("geospatialRegionTab");

        Span timeFlowSpan =new Span();
        Icon timeFlowLogo = new Icon(VaadinIcon.TIMER);
        timeFlowLogo.setSize("20px");
        Label timeFlowLabel = new Label(" TimeFlow-时间流");
        timeFlowLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        timeFlowSpan.add(timeFlowLogo,timeFlowLabel);
        Tab timeFlowTab = new Tab(timeFlowSpan);
        timeFlowTab.setId("timeFlowTab");

        Tabs tabs = new Tabs(coreRealmTab, conceptionKindTab, relationKindTab, attributeKindTab, attributesViewKindTab,
                classificationTab, geospatialRegionTab, timeFlowTab);
        add(tabs);
        tabs.addSelectedChangeListener(event -> switchFeatureUI(event.getSelectedTab().getId().get()));

        this.featureContainerLayout = new VerticalScrollLayout();
        this.featureContainerLayout.setPadding(false);
        this.featureContainerLayout.setSpacing(false);
        //this.featureContainerLayout.getStyle().set("border", "1px solid #9E9E9E");
        add(this.featureContainerLayout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            this.featureContainerLayout.setHeight(event.getHeight()-100,Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserWidth = receiver.getBodyClientWidth();
            int browserHeight = receiver.getBodyClientHeight();
            this.featureContainerLayout.setHeight(browserHeight-100,Unit.PIXELS);

        }));
        switchFeatureUI("coreRealmTab");
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private void switchFeatureUI(String activeFeatureID){
        if(this.coreRealmDataUI != null){
            this.coreRealmDataUI.setVisible(false);
        }
        if(this.conceptionKindManagementUI != null){
            this.conceptionKindManagementUI.setVisible(false);
        }
        if(this.relationKindManagementUI != null){
            this.relationKindManagementUI.setVisible(false);
        }
        if(this.attributeKindManagementUI != null){
            this.attributeKindManagementUI.setVisible(false);
        }
        if(this.attributesViewKindManagementUI != null){
            this.attributesViewKindManagementUI.setVisible(false);
        }
        if(this.classificationManagementUI != null){
            this.classificationManagementUI.setVisible(false);
        }
        if(this.geospatialRegionManagementUI != null){
            this.geospatialRegionManagementUI.setVisible(false);
        }
        if(this.timeFlowManagementUI != null){
            this.timeFlowManagementUI.setVisible(false);
        }
        if(activeFeatureID.equals("coreRealmTab")){
            if(this.coreRealmDataUI == null){
                this.coreRealmDataUI = new CoreRealmDataUI();
                this.featureContainerLayout.add(this.coreRealmDataUI);
            }else{
                this.coreRealmDataUI.setVisible(true);
            }
        }
        if(activeFeatureID.equals("conceptionKindTab")){
            if(this.conceptionKindManagementUI == null){
                this.conceptionKindManagementUI = new ConceptionKindManagementUI();
                this.featureContainerLayout.add(this.conceptionKindManagementUI);
            }else{
                this.conceptionKindManagementUI.setVisible(true);
            }
        }
        if(activeFeatureID.equals("relationKindTab")){
            if(this.relationKindManagementUI == null){
                this.relationKindManagementUI = new RelationKindManagementUI();
                this.featureContainerLayout.add(this.relationKindManagementUI);
            }else{
                this.relationKindManagementUI.setVisible(true);
            }
        }
        if(activeFeatureID.equals("attributeKindTab")){
            if(this.attributeKindManagementUI == null){
                this.attributeKindManagementUI = new AttributeKindManagementUI();
                this.featureContainerLayout.add(this.attributeKindManagementUI);
            }else{
                this.attributeKindManagementUI.setVisible(true);
            }
        }
        if(activeFeatureID.equals("attributesViewKindTab")){
            if(this.attributesViewKindManagementUI == null){
                this.attributesViewKindManagementUI = new AttributesViewKindManagementUI();
                this.featureContainerLayout.add(this.attributesViewKindManagementUI);
            }else{
                this.attributesViewKindManagementUI.setVisible(true);
            }
        }
        if(activeFeatureID.equals("classificationTab")){
            if(this.classificationManagementUI == null){
                this.classificationManagementUI = new ClassificationManagementUI();
                this.featureContainerLayout.add(this.classificationManagementUI);
            }else{
                this.classificationManagementUI.setVisible(true);
            }
        }
        if(activeFeatureID.equals("geospatialRegionTab")){
            if(this.geospatialRegionManagementUI == null){
                this.geospatialRegionManagementUI = new GeospatialRegionManagementUI();
                this.featureContainerLayout.add(this.geospatialRegionManagementUI);
            }else{
                this.geospatialRegionManagementUI.setVisible(true);
            }
        }
        if(activeFeatureID.equals("timeFlowTab")){
            if(this.timeFlowManagementUI == null){
                this.timeFlowManagementUI = new TimeFlowManagementUI();
                this.featureContainerLayout.add(this.timeFlowManagementUI);
            }else{
                this.timeFlowManagementUI.setVisible(true);
            }
        }
    }
}
