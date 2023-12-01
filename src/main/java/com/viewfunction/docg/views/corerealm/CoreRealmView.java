package com.viewfunction.docg.views.corerealm;

import ch.carnet.kasparscherrer.VerticalScrollLayout;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.shared.Registration;

import com.vaadin.flow.theme.lumo.LumoUtility;
import com.viewfunction.docg.element.eventHandling.UserLockApplicationEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.views.MainLayout;
import com.viewfunction.docg.views.corerealm.featureUI.*;

@PageTitle("数海云图 - 核心领域模型 [ Core Realm ]")
@Route(value = "core-realm", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class CoreRealmView extends Div implements UserLockApplicationEvent.UserApplicationLogoutListener{

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
    private LoginOverlay loginOverlay;
    public CoreRealmView(){

        showLoginUI();

        this.setWidth(100, Unit.PERCENTAGE);

        Span coreRealmSpan =new Span();
        Icon coreRealmLogo = new Icon(VaadinIcon.CLUSTER);
        coreRealmLogo.setSize("20px");
        NativeLabel coreRealmLabel = new NativeLabel(" CoreRealm-领域数据");
        coreRealmLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        coreRealmSpan.add(coreRealmLogo,coreRealmLabel);
        Tab coreRealmTab = new Tab(coreRealmSpan);
        coreRealmTab.setId("coreRealmTab");

        Span conceptionKindSpan =new Span();
        Icon conceptionKindLogo = new Icon(VaadinIcon.CUBE);
        conceptionKindLogo.setSize("20px");
        NativeLabel conceptionKindLabel = new NativeLabel(" ConceptionKind-概念类型");
        conceptionKindLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        conceptionKindSpan.add(conceptionKindLogo,conceptionKindLabel);
        Tab conceptionKindTab = new Tab(conceptionKindSpan);
        conceptionKindTab.setId("conceptionKindTab");

        Span relationKindSpan =new Span();
        Icon relationKindLogo = new Icon(VaadinIcon.CONNECT_O);
        relationKindLogo.setSize("20px");
        NativeLabel relationKindLabel = new NativeLabel(" RelationKind-关系类型");
        relationKindLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        relationKindSpan.add(relationKindLogo,relationKindLabel);
        Tab relationKindTab = new Tab(relationKindSpan);
        relationKindTab.setId("relationKindTab");

        Span attributeKindSpan =new Span();
        Icon attributeKindLogo = new Icon(VaadinIcon.INPUT);
        attributeKindLogo.setSize("20px");
        NativeLabel attributeKindLabel = new NativeLabel(" AttributeKind-属性类型");
        attributeKindLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        attributeKindSpan.add(attributeKindLogo,attributeKindLabel);
        Tab attributeKindTab = new Tab(attributeKindSpan);
        attributeKindTab.setId("attributeKindTab");

        Span attributesViewKindSpan =new Span();
        Icon attributesViewKindLogo = new Icon(VaadinIcon.TASKS);
        attributesViewKindLogo.setSize("20px");
        NativeLabel attributesViewKindLabel = new NativeLabel(" AttributesViewKind-属性视图类型");
        attributesViewKindLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        attributesViewKindSpan.add(attributesViewKindLogo,attributesViewKindLabel);
        Tab attributesViewKindTab = new Tab(attributesViewKindSpan);
        attributesViewKindTab.setId("attributesViewKindTab");

        Span classificationSpan =new Span();
        Icon classificationLogo = new Icon(VaadinIcon.TAGS);
        classificationLogo.setSize("20px");
        NativeLabel classificationLabel = new NativeLabel(" Classification-分类");
        classificationLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        classificationSpan.add(classificationLogo,classificationLabel);
        Tab classificationTab = new Tab(classificationSpan);
        classificationTab.setId("classificationTab");

        Span geospatialRegionSpan =new Span();
        Icon geospatialRegionLogo = new Icon(VaadinIcon.GLOBE_WIRE);
        geospatialRegionLogo.setSize("20px");
        NativeLabel geospatialRegionLabel = new NativeLabel(" GeospatialRegion-地理空间区域");
        geospatialRegionLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        geospatialRegionSpan.add(geospatialRegionLogo,geospatialRegionLabel);
        Tab geospatialRegionTab = new Tab(geospatialRegionSpan);
        geospatialRegionTab.setId("geospatialRegionTab");

        Span timeFlowSpan =new Span();
        Icon timeFlowLogo = new Icon(VaadinIcon.TIMER);
        timeFlowLogo.setSize("20px");
        NativeLabel timeFlowLabel = new NativeLabel(" TimeFlow-时间流");
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
        ResourceHolder.getApplicationBlackboard().addListener(this);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
        ResourceHolder.getApplicationBlackboard().removeListener(this);
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

    private void showLoginUI(){
        LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("应用登录");
        i18nForm.setUsername("用户名称");
        i18nForm.setPassword("用户密码");
        i18nForm.setSubmit("确认登录");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("用户名称或密码错误");
        i18nErrorMessage.setMessage("请输入正确的 用户名称 与 用户密码组合.");
        i18n.setErrorMessage(i18nErrorMessage);
        //i18n.setAdditionalInformation("Jos tarvitset lisätietoja käyttäjälle.");

        loginOverlay = new LoginOverlay();
        loginOverlay.setI18n(i18n);

        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        Image image = new Image("images/logo.png","DOCG-SAC logo");
        image.setHeight(39, Unit.PIXELS);
        image.setWidth(128, Unit.PIXELS);
        logoLayout.add(image);

        loginOverlay.setTitle(logoLayout);
        loginOverlay.setDescription("D.O.C.G 领域模型数据分析平台");
        loginOverlay.setForgotPasswordButtonVisible(false);

        Paragraph text = new Paragraph("Create your own style.");
        text.addClassName(LumoUtility.TextAlignment.CENTER);
        loginOverlay.getFooter().add(text);
        add(loginOverlay);
        loginOverlay.setOpened(true);
        loginOverlay.addLoginListener(new ComponentEventListener<AbstractLogin.LoginEvent>() {
            @Override
            public void onComponentEvent(AbstractLogin.LoginEvent loginEvent) {
                System.out.println(loginEvent.getUsername());
                System.out.println(loginEvent.getPassword());

                loginOverlay.close();
            }
        });
    }

    @Override
    public void receivedUserLockApplicationEvent(UserLockApplicationEvent event) {
        loginOverlay.setOpened(true);
    }
}
