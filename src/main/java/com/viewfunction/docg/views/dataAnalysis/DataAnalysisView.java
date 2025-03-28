package com.viewfunction.docg.views.dataAnalysis;

import ch.carnet.kasparscherrer.VerticalScrollLayout;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.login.AbstractLogin;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.element.eventHandling.UserLockApplicationEvent;
import com.viewfunction.docg.util.ResourceHolder;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;
import com.viewfunction.docg.views.MainLayout;
import com.viewfunction.docg.views.dataAnalysis.featureUI.DataAnalysisCapacityManagementUI;

@PageTitle("数海云图 - 数据分析 [ Data Analysis ]")
@Route(value = "data-analysis", layout = MainLayout.class)
public class DataAnalysisView extends Div implements UserLockApplicationEvent.UserApplicationLogoutListener{

    private LoginOverlay loginOverlay;
    private Registration listener;
    private VerticalScrollLayout featureContainerLayout;
    private Tab dataAnalysisTab;
    private DataAnalysisCapacityManagementUI dataAnalysisCapacityManagementUI;

    public DataAnalysisView() {
        //addClassName("data-analysis-view");
        String enableUserLockApplicationStr = SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.ENABLE_USER_LOCK_APPLICATION);
        if(enableUserLockApplicationStr != null & Boolean.parseBoolean(enableUserLockApplicationStr)){
            showLoginUI();
        }

        this.setWidth(100, Unit.PERCENTAGE);
        //addClassName("compute-grid-view");

        Span dataAnalysisProviderSpan =new Span();
        Icon dataAnalysisProviderLogo = LineAwesomeIconsSvg.MEMORY_SOLID.create();
        dataAnalysisProviderLogo.setSize("20px");
        NativeLabel dataAnalysisProviderLabel = new NativeLabel(" AnalysisProviderManager-数据分析服务");
        dataAnalysisProviderLabel.getStyle().set("font-size","var(--lumo-font-size-l)");
        dataAnalysisProviderSpan.add(dataAnalysisProviderLogo,dataAnalysisProviderLabel);

        dataAnalysisTab = new Tab(dataAnalysisProviderSpan);
        dataAnalysisTab.setId("dataAnalysisTab");

        Tabs tabs = new Tabs(dataAnalysisTab);
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
        switchFeatureUI("dataAnalysisTab");
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
        if(this.dataAnalysisCapacityManagementUI != null){
            this.dataAnalysisCapacityManagementUI.setVisible(false);
        }
        if(activeFeatureID.equals("dataAnalysisTab")){
            if(this.dataAnalysisCapacityManagementUI == null){
                this.dataAnalysisCapacityManagementUI = new DataAnalysisCapacityManagementUI();
                this.featureContainerLayout.add(this.dataAnalysisCapacityManagementUI);
            }else{
                this.dataAnalysisCapacityManagementUI.setVisible(true);
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
        i18nErrorMessage.setMessage("请输入正确的 用户名称 与 用户密码 组合.");
        i18n.setErrorMessage(i18nErrorMessage);
        //i18n.setAdditionalInformation("Jos tarvitset lisätietoja käyttäjälle.");

        loginOverlay = new LoginOverlay();
        //<theme-editor-local-classname>
        loginOverlay.addClassName("core-realm-view-login-overlay-1");
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

        Paragraph text = new Paragraph("You grab my soda can, and you hold my sweating hand, I long to see the boring band, because I'm your super fan.");
        text.addClassName(LumoUtility.TextAlignment.CENTER);
        loginOverlay.getFooter().add(text);
        add(loginOverlay);
        loginOverlay.setOpened(true);
        loginOverlay.addLoginListener(new ComponentEventListener<AbstractLogin.LoginEvent>() {
            @Override
            public void onComponentEvent(AbstractLogin.LoginEvent loginEvent) {
                String inputUserName = loginEvent.getUsername();
                String inputUserPWD = loginEvent.getPassword();
                String _USER_NAME = SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.USER_NAME);
                String _USER_PWD = SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.USER_PWD);
                if(inputUserName.equals(_USER_NAME) & inputUserPWD.equals(_USER_PWD)){
                    loginOverlay.close();
                }else{
                    loginOverlay.setError(true);
                }
            }
        });
    }

    @Override
    public void receivedUserLockApplicationEvent(UserLockApplicationEvent event) {
        loginOverlay.setOpened(true);
    }

}
