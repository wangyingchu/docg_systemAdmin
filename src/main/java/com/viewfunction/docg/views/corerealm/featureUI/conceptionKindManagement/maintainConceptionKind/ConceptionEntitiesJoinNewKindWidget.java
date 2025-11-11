package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.QueryParameters;
import com.viewfunction.docg.coreRealm.realmServiceCore.analysis.query.filteringItem.EqualFilteringItem;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.CrossKindDataOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntitiesRetrieveResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationResult;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.eventHandling.ConceptionEntitiesCountRefreshEvent;
import com.viewfunction.docg.util.ResourceHolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConceptionEntitiesJoinNewKindWidget extends VerticalLayout {

    private List<KindMetaInfo> conceptionKindsInfoList;
    private String conceptionKind;
    private String attributeName;
    private Checkbox exitCurrentConceptionKindCheckbox;
    private Object entityAttributeValue;
    private MultiSelectComboBox<KindMetaInfo> conceptionKindFilterSelect;
    private Button confirmButton;

    public ConceptionEntitiesJoinNewKindWidget(String conceptionKind,String attributeName,Object entityAttributeValue,
                                               Number matchedEntitiesCount,List<KindMetaInfo> conceptionKindsMetaInfoList){
        this.conceptionKind = conceptionKind;
        this.attributeName = attributeName;
        this.entityAttributeValue = entityAttributeValue;
        this.setWidthFull();
        this.setSpacing(true);

        HorizontalLayout valueTitleContainer = new HorizontalLayout();
        this.add(valueTitleContainer);

        Icon valueIcon = VaadinIcon.EYE.create();
        valueIcon.setSize("12px");
        valueIcon.getStyle().set("padding-right","3px");
        valueTitleContainer.add(valueIcon);
        valueTitleContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);

        Span matchedEntitiesCountSpan = new Span(matchedEntitiesCount.toString());
        matchedEntitiesCountSpan.getElement().getThemeList().add("badge contrast");
        valueTitleContainer.add(matchedEntitiesCountSpan);

        H6 valueContentHeader = new H6(this.entityAttributeValue.toString());
        valueTitleContainer.add(valueContentHeader);

        HorizontalLayout newConceptionKindsContainer = new HorizontalLayout();
        newConceptionKindsContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        this.add(newConceptionKindsContainer);

        Icon conceptionKindIcon = VaadinIcon.CUBE.create();
        conceptionKindIcon.setSize("10px");
        conceptionKindIcon.getStyle().set("padding-right","2px");
        conceptionKindIcon.setColor("var(--lumo-contrast-50pct)");
        newConceptionKindsContainer.add(conceptionKindIcon);

        conceptionKindFilterSelect = new MultiSelectComboBox();
        conceptionKindFilterSelect.setAutoExpand(MultiSelectComboBox.AutoExpandMode.VERTICAL);
        conceptionKindFilterSelect.setSelectedItemsOnTop(true);
        conceptionKindFilterSelect.setPageSize(30);
        conceptionKindFilterSelect.setPlaceholder("选择要加入的概念类型");
        conceptionKindFilterSelect.setWidth(500, Unit.PIXELS);
        conceptionKindFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo attributeKindMetaInfo) {
                String itemLabelValue = attributeKindMetaInfo.getKindName()+ " ("+
                        attributeKindMetaInfo.getKindDesc()+")";
                return itemLabelValue;
            }
        });
        conceptionKindFilterSelect.setRenderer(createRenderer());
        newConceptionKindsContainer.add(conceptionKindFilterSelect);

        this.conceptionKindsInfoList = new ArrayList<>();
        if(conceptionKindsMetaInfoList != null){
            conceptionKindsMetaInfoList.forEach(kindMetaInfo -> {
                if(!kindMetaInfo.getKindName().equals(conceptionKind)){
                    KindMetaInfo currentKindMetaInfo =
                            new KindMetaInfo(kindMetaInfo.getKindName(),kindMetaInfo.getKindDesc(),null,null,null,null,null);
                    conceptionKindsInfoList.add(currentKindMetaInfo);
                }
            });
        }
        conceptionKindFilterSelect.setItems(this.conceptionKindsInfoList);

        confirmButton = new Button("加入新类型",new Icon(VaadinIcon.CHECK));
        confirmButton.setWidth(100,Unit.PIXELS);
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newConceptionKindsContainer.add(confirmButton);
        confirmButton.addClickListener(buttonClickEvent -> {
            addInNewConceptionKinds();
        });
        exitCurrentConceptionKindCheckbox = new Checkbox("同时退出当前概念类型");
        newConceptionKindsContainer.add(exitCurrentConceptionKindCheckbox);

        this.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-s)");
    }

    private Renderer<KindMetaInfo> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    <span style=\"font-size: var(--lumo-font-size-xl); color: var(--lumo-primary-text-color);\">${item.attributeKindName}</span>");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.attributeKindDesc}</div>");
        tpl.append("  </div>");
        tpl.append("</div>");

        return LitRenderer.<KindMetaInfo>of(tpl.toString())
                .withProperty("attributeKindName", KindMetaInfo::getKindName)
                .withProperty("attributeKindDesc", KindMetaInfo::getKindDesc);
    }

    private void addInNewConceptionKinds(){
        if(conceptionKindFilterSelect.getSelectedItems().size() ==0){
            return;
        }

        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认加入",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        String currentAttributeValueFullStr = this.entityAttributeValue.toString();
        String currentAttributeValueShortStr = "";
        if(currentAttributeValueFullStr.length() > 20){
            currentAttributeValueShortStr = currentAttributeValueFullStr.substring(0,20)+"...";
        }else{
            currentAttributeValueShortStr =currentAttributeValueFullStr;
        }

        String confirmMessage = "请确认将概念类型 "+conceptionKind+" 的全部属性 "+attributeName +" 值为 “"+currentAttributeValueShortStr +"” 的概念实体加入到选定的新概念类型中。";
        if(exitCurrentConceptionKindCheckbox.getValue()){
            confirmMessage = "请确认将概念类型 "+conceptionKind+" 的全部属性 "+attributeName +" 值为 “"+currentAttributeValueShortStr +"” 的概念实体加入到选定的新概念类型中，并从当前概念类型中退出。";
        }

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作", confirmMessage,actionButtonList,600,200);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doAddIntoNewConceptionKinds();
                confirmWindow.closeConfirmWindow();
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doAddIntoNewConceptionKinds(){
        Set<String> joinedConceptionKindNameSet = new HashSet<>();
        conceptionKindFilterSelect.getSelectedItems().forEach(kindMetaInfo -> {
            joinedConceptionKindNameSet.add(kindMetaInfo.getKindName());
        });

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            coreRealm.openGlobalSession();
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKind);
            if(targetConceptionKind != null){
                QueryParameters queryParameters = new QueryParameters();
                queryParameters.setResultNumber(1000000000);
                queryParameters.setDefaultFilteringItem(new EqualFilteringItem(this.attributeName,this.entityAttributeValue));
                ConceptionEntitiesRetrieveResult conceptionEntitiesRetrieveResult = targetConceptionKind.getEntities(queryParameters);

                Set<String> targetEntityUIDsSet = new HashSet<>();
                if(conceptionEntitiesRetrieveResult.getConceptionEntities() != null &&
                        !conceptionEntitiesRetrieveResult.getConceptionEntities().isEmpty()){
                    conceptionEntitiesRetrieveResult.getConceptionEntities().forEach(conceptionEntity -> {
                        targetEntityUIDsSet.add(conceptionEntity.getConceptionEntityUID());
                    });
                }

                if(!targetEntityUIDsSet.isEmpty()){
                    CrossKindDataOperator crossKindDataOperator = coreRealm.getCrossKindDataOperator();
                    EntitiesOperationResult entitiesOperationResult =
                            crossKindDataOperator.joinEntitiesToConceptionKinds(targetEntityUIDsSet,joinedConceptionKindNameSet.toArray(new String[0]));

                    Notification notification0 = new Notification();
                    notification0.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    Div text0 = new Div(new Text("将概念类型实体加入新概念类型操作成功"));
                    Button closeButton0 = new Button(new Icon("lumo", "cross"));
                    closeButton0.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                    closeButton0.addClickListener(event -> {
                        notification0.close();
                    });
                    HorizontalLayout layout0 = new HorizontalLayout(text0, closeButton0);
                    layout0.setWidth(100, Unit.PERCENTAGE);
                    layout0.setFlexGrow(1,text0);
                    notification0.add(layout0);

                    VerticalLayout notificationMessageContainer0 = new VerticalLayout();
                    notificationMessageContainer0.add(new Div(new Text("操作成功实体数: "+entitiesOperationResult.getOperationStatistics().getSuccessItemsCount())));
                    notificationMessageContainer0.add(new Div(new Text("执行开始时间: "+entitiesOperationResult.getOperationStatistics().getStartTime())));
                    notificationMessageContainer0.add(new Div(new Text("执行结束时间: "+entitiesOperationResult.getOperationStatistics().getFinishTime())));
                    notification0.add(notificationMessageContainer0);
                    notification0.setDuration(5000);
                    notification0.open();

                    if(exitCurrentConceptionKindCheckbox.getValue()){
                        entitiesOperationResult =
                                crossKindDataOperator.retreatEntitiesFromConceptionKind(targetEntityUIDsSet,this.conceptionKind);
                        Notification notification = new Notification();
                        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        Div text = new Div(new Text("将概念类型实体退出原有概念类型操作成功"));
                        Button closeButton = new Button(new Icon("lumo", "cross"));
                        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
                        closeButton.addClickListener(event -> {
                            notification.close();
                        });
                        HorizontalLayout layout = new HorizontalLayout(text, closeButton);
                        layout.setWidth(100, Unit.PERCENTAGE);
                        layout.setFlexGrow(1,text);
                        notification.add(layout);

                        VerticalLayout notificationMessageContainer = new VerticalLayout();
                        notificationMessageContainer.add(new Div(new Text("操作成功实体数: "+entitiesOperationResult.getOperationStatistics().getSuccessItemsCount())));
                        notificationMessageContainer.add(new Div(new Text("执行开始时间: "+entitiesOperationResult.getOperationStatistics().getStartTime())));
                        notificationMessageContainer.add(new Div(new Text("执行结束时间: "+entitiesOperationResult.getOperationStatistics().getFinishTime())));
                        notification.add(notificationMessageContainer);
                        notification.setDuration(5000);
                        notification.open();

                        long conceptionEntitiesCount = targetConceptionKind.countConceptionEntities();
                        ConceptionEntitiesCountRefreshEvent conceptionEntitiesCountRefreshEvent = new ConceptionEntitiesCountRefreshEvent();
                        conceptionEntitiesCountRefreshEvent.setConceptionEntitiesCount(conceptionEntitiesCount);
                        conceptionEntitiesCountRefreshEvent.setConceptionKindName(this.conceptionKind);
                        ResourceHolder.getApplicationBlackboard().fire(conceptionEntitiesCountRefreshEvent);
                    }

                    if(exitCurrentConceptionKindCheckbox.getValue()){
                        conceptionKindFilterSelect.setEnabled(false);
                        exitCurrentConceptionKindCheckbox.setEnabled(false);
                        confirmButton.setEnabled(false);
                    }
                    exitCurrentConceptionKindCheckbox.setValue(false);
                    conceptionKindsInfoList.removeIf(currentkindMetaInfo -> joinedConceptionKindNameSet.contains(currentkindMetaInfo.getKindName()));
                    conceptionKindFilterSelect.setItems(this.conceptionKindsInfoList);
                }
            }
        } catch (CoreRealmServiceEntityExploreException | CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }finally {
            coreRealm.closeGlobalSession();
        }
    }
}
