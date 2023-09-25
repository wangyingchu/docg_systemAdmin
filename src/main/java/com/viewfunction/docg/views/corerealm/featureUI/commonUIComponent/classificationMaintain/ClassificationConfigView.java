package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.classificationMaintain;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.ClassificationAttachable;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ClassificationAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.RelationAttachInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.spi.common.payloadImpl.ClassificationMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.RealmConstant;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.ConfirmWindow;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.LightGridColumnHeader;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;

import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;

import dev.mett.vaadin.tooltip.Tooltips;

import java.util.*;

public class ClassificationConfigView extends VerticalLayout {

    private class ClassificationConfigItemValueObject {
        private String classificationName;
        private String attachRelationKindName;
        private RelationDirection attachRelationDirection;
        private Object attachRelationDataValue;

        public ClassificationConfigItemValueObject(String classificationName,String attachRelationKindName,
                                                   RelationDirection attachRelationDirection,Object attachRelationDataValue){
            this.classificationName = classificationName;
            this.attachRelationKindName = attachRelationKindName;
            this.attachRelationDirection = attachRelationDirection;
            this.attachRelationDataValue = attachRelationDataValue;
        }

        public String getClassificationName() {
            return classificationName;
        }

        public void setClassificationName(String classificationName) {
            this.classificationName = classificationName;
        }

        public String getAttachRelationKindName() {
            return attachRelationKindName;
        }

        public void setAttachRelationKindName(String attachRelationKindName) {
            this.attachRelationKindName = attachRelationKindName;
        }

        public RelationDirection getAttachRelationDirection() {
            return attachRelationDirection;
        }

        public void setAttachRelationDirection(RelationDirection attachRelationDirection) {
            this.attachRelationDirection = attachRelationDirection;
        }

        public Object getAttachRelationDataValue() {
            return attachRelationDataValue;
        }

        public void setAttachRelationDataValue(Object attachRelationDataValue) {
            this.attachRelationDataValue = attachRelationDataValue;
        }
    }

    public enum ClassificationRelatedObjectType {
        ConceptionKind,RelationKind,AttributeKind,AttributesViewKind,ConceptionEntity
    }

    private ClassificationConfigView.ClassificationRelatedObjectType classificationRelatedObjectType;
    private String relatedObjectID;
    private Grid<ClassificationConfigItemValueObject> classificationConfigItemValueObjectGrid;

    public ClassificationConfigView(ClassificationConfigView.ClassificationRelatedObjectType
                                            classificationRelatedObjectType,String relatedObjectID){
        this.classificationRelatedObjectType = classificationRelatedObjectType;
        this.relatedObjectID = relatedObjectID;
        this.setSpacing(false);
        this.setMargin(false);
        this.setPadding(false);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        renderClassificationConfigUI();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
    }

    private void renderClassificationConfigUI(){
        this.setWidth(100, Unit.PERCENTAGE);
        List<Component> secTitleElementsList = new ArrayList<>();
        List<Component> buttonList = new ArrayList<>();

        Button relateToClassificationButton= new Button("关联分类");
        relateToClassificationButton.setIcon(VaadinIcon.BOOKMARK.create());
        relateToClassificationButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        buttonList.add(relateToClassificationButton);
        relateToClassificationButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderRelateClassificationViewUI();
            }
        });

        Button refreshRelatedClassificationsInfoButton = new Button("刷新分类信息",new Icon(VaadinIcon.REFRESH));
        refreshRelatedClassificationsInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON,ButtonVariant.LUMO_SMALL,ButtonVariant.LUMO_TERTIARY);
        refreshRelatedClassificationsInfoButton.addClickListener((ClickEvent<Button> click) ->{
            refreshClassifications();
        });
        buttonList.add(refreshRelatedClassificationsInfoButton);

        SecondaryTitleActionBar relatedClassificationConfigActionBar = new SecondaryTitleActionBar(new Icon(VaadinIcon.TAGS),"分类配置管理 ",secTitleElementsList,buttonList);
        add(relatedClassificationConfigActionBar);

        ComponentRenderer _toolBarComponentRenderer = new ComponentRenderer<>(classificationConfigItemValueObjectInfo -> {
            Icon removeIcon = new Icon(VaadinIcon.UNLINK);
            removeIcon.setSize("20px");
            Button removeItemButton = new Button(removeIcon, event -> {
                if(classificationConfigItemValueObjectInfo instanceof ClassificationConfigItemValueObject){
                    renderDeleteConfigItemUI((ClassificationConfigItemValueObject)classificationConfigItemValueObjectInfo);
                }
            });
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_SMALL);
            removeItemButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            Tooltips.getCurrent().setTooltip(removeItemButton, "删除分类关联");

            HorizontalLayout buttons = new HorizontalLayout(removeItemButton);
            buttons.setPadding(false);
            buttons.setSpacing(false);
            buttons.setMargin(false);
            buttons.setDefaultVerticalComponentAlignment(Alignment.CENTER);
            buttons.setHeight(10,Unit.PIXELS);
            buttons.setWidth(80,Unit.PIXELS);
            return new VerticalLayout(buttons);
        });

        classificationConfigItemValueObjectGrid = new Grid<>();

        classificationConfigItemValueObjectGrid.setWidth(100, Unit.PERCENTAGE);
        classificationConfigItemValueObjectGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        classificationConfigItemValueObjectGrid.addThemeVariants(GridVariant.LUMO_COMPACT,GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_ROW_STRIPES);
        classificationConfigItemValueObjectGrid.addColumn(ClassificationConfigItemValueObject::getClassificationName).setHeader("分类名称").setKey("idx_0").
                setFlexGrow(0).setWidth("250px").setResizable(true).setTooltipGenerator(ClassificationConfigItemValueObject::getClassificationName);
        classificationConfigItemValueObjectGrid.addColumn(ClassificationConfigItemValueObject::getAttachRelationKindName).setHeader("关系类型").setKey("idx_1").
                setFlexGrow(0).setWidth("250px").setTooltipGenerator(ClassificationConfigItemValueObject::getAttachRelationKindName);
        classificationConfigItemValueObjectGrid.addColumn(ClassificationConfigItemValueObject::getAttachRelationDirection).setHeader("关系方向").setKey("idx_2").
                setFlexGrow(0).setWidth("80px").setResizable(false);
        classificationConfigItemValueObjectGrid.addColumn(ClassificationConfigItemValueObject::getAttachRelationDataValue).setHeader("关系属性").setKey("idx_3").
                setFlexGrow(1).setResizable(false).setTooltipGenerator( classificationConfigItemValueObject -> classificationConfigItemValueObject.getAttachRelationDataValue().toString());
        classificationConfigItemValueObjectGrid.addColumn(_toolBarComponentRenderer).setHeader("操作").setKey("idx_4").setFlexGrow(0).setWidth("100px").setResizable(false);

        LightGridColumnHeader gridColumnHeader_1_idx0 = new LightGridColumnHeader(VaadinIcon.TAG,"分类名称");
        classificationConfigItemValueObjectGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_1_idx0).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx1 = new LightGridColumnHeader(VaadinIcon.CONNECT_O,"关系类型");
        classificationConfigItemValueObjectGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_1_idx1).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx2 = new LightGridColumnHeader(VaadinIcon.EXCHANGE,"关系方向");
        classificationConfigItemValueObjectGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_1_idx2).setSortable(true);
        LightGridColumnHeader gridColumnHeader_1_idx3 = new LightGridColumnHeader(VaadinIcon.INPUT,"关系属性");
        classificationConfigItemValueObjectGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_1_idx3).setSortable(true);
        LightGridColumnHeader gridColumnHeader_idx4 = new LightGridColumnHeader(VaadinIcon.TOOLS,"操作");
        classificationConfigItemValueObjectGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4);
        classificationConfigItemValueObjectGrid.setHeight(150,Unit.PIXELS);
        add(classificationConfigItemValueObjectGrid);
        loadAttachedClassifications();
    }

    private void renderRelateClassificationViewUI(){
        RelateClassificationView relateClassificationView = new RelateClassificationView(this.classificationRelatedObjectType,this.relatedObjectID);
        relateClassificationView.setContainerClassificationConfigView(this);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.BOOKMARK),"关联分类",null,true,1140,670,false);
        fixSizeWindow.setWindowContent(relateClassificationView);
        fixSizeWindow.setModel(true);
        relateClassificationView.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    public void setViewHeight(int viewHeight){
        this.setHeight(viewHeight,Unit.PIXELS);
    }

    public void refreshClassificationConfigInfo(){}

    private void loadAttachedClassifications(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();

        ClassificationAttachable classificationAttachable= null;
        switch (this.classificationRelatedObjectType){
            case ConceptionKind :
                classificationAttachable = coreRealm.getConceptionKind(relatedObjectID);
                break;
            case RelationKind :
                classificationAttachable = coreRealm.getRelationKind(relatedObjectID);
                break;
            case AttributeKind:
                classificationAttachable = coreRealm.getAttributeKind(relatedObjectID);
                break;
            case AttributesViewKind:
                classificationAttachable = coreRealm.getAttributesViewKind(relatedObjectID);
                break;
            case ConceptionEntity:
                break;
        }
        List<ClassificationConfigItemValueObject> classificationConfigItemValueObjectList = new ArrayList<>();
        List<ClassificationAttachInfo> classificationAttachInfoList = null;
        if(classificationAttachable != null){
            classificationAttachInfoList = classificationAttachable.getAllAttachedClassificationsInfo();
        }
        coreRealm.closeGlobalSession();

        if(classificationAttachInfoList != null){
            for(ClassificationAttachInfo currentClassificationAttachInfo:classificationAttachInfoList){
                if(currentClassificationAttachInfo.getAttachedClassification() != null &&
                        currentClassificationAttachInfo.getRelationAttachInfo()!= null){
                    currentClassificationAttachInfo.getAttachedClassification().getClassificationName();
                    currentClassificationAttachInfo.getRelationAttachInfo().getRelationKind();
                    currentClassificationAttachInfo.getRelationAttachInfo().getRelationDirection();
                    currentClassificationAttachInfo.getRelationAttachInfo().getRelationData();
                    ClassificationConfigItemValueObject currentClassificationConfigItemValueObject = new ClassificationConfigItemValueObject(
                            currentClassificationAttachInfo.getAttachedClassification().getClassificationName(),
                            currentClassificationAttachInfo.getRelationAttachInfo().getRelationKind(),
                            currentClassificationAttachInfo.getRelationAttachInfo().getRelationDirection(),
                            currentClassificationAttachInfo.getRelationAttachInfo().getRelationData()
                    );
                    classificationConfigItemValueObjectList.add(currentClassificationConfigItemValueObject);
                }
            }
        }
        classificationConfigItemValueObjectGrid.setItems(classificationConfigItemValueObjectList);
    }

    private void refreshClassifications(){
        ListDataProvider dtaProvider=(ListDataProvider)classificationConfigItemValueObjectGrid.getDataProvider();
        dtaProvider.getItems().clear();
        dtaProvider.refreshAll();
        loadAttachedClassifications();
    }

    public void attachClassificationSuccessCallback(Set<ClassificationMetaInfo> classificationInfoSet, RelationAttachInfo relationAttachInfo){
        ListDataProvider dtaProvider=(ListDataProvider)classificationConfigItemValueObjectGrid.getDataProvider();

        Map<String,Object> relationData = new HashMap<>();
        relationData.putAll(relationAttachInfo.getRelationData());
        relationData.remove(RealmConstant._createDateProperty);
        relationData.remove(RealmConstant._lastModifyDateProperty);
        relationData.remove(RealmConstant._creatorIdProperty);
        relationData.remove(RealmConstant._dataOriginProperty);

        for(ClassificationMetaInfo currentClassificationMetaInfo:classificationInfoSet){
            ClassificationConfigItemValueObject currentClassificationConfigItemValueObject = new ClassificationConfigItemValueObject(
                    currentClassificationMetaInfo.getClassificationName(),
                    relationAttachInfo.getRelationKind(),
                    relationAttachInfo.getRelationDirection(),
                    relationData
            );
            dtaProvider.getItems().add(currentClassificationConfigItemValueObject);
        }
        dtaProvider.refreshAll();
    }

    private void renderDeleteConfigItemUI(ClassificationConfigItemValueObject classificationConfigItemValueObject){
        List<Button> actionButtonList = new ArrayList<>();
        Button confirmButton = new Button("确认删除分类关联",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        Button cancelButton = new Button("取消操作");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        actionButtonList.add(confirmButton);
        actionButtonList.add(cancelButton);

        ConfirmWindow confirmWindow = new ConfirmWindow(new Icon(VaadinIcon.INFO),"确认操作","请确认执行删除分类关联 "+ classificationConfigItemValueObject.classificationName+" - "+classificationConfigItemValueObject.attachRelationKindName+" 的操作",actionButtonList,400,180);
        confirmWindow.open();

        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doDeleteConfigItem(classificationConfigItemValueObject,confirmWindow);
            }
        });
        cancelButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                confirmWindow.closeConfirmWindow();
            }
        });
    }

    private void doDeleteConfigItem(ClassificationConfigItemValueObject classificationConfigItemValueObject,ConfirmWindow confirmWindow){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();

        ClassificationAttachable classificationAttachable= null;
        switch (this.classificationRelatedObjectType){
            case ConceptionKind :
                classificationAttachable = coreRealm.getConceptionKind(relatedObjectID);
                break;
            case RelationKind :
                classificationAttachable = coreRealm.getRelationKind(relatedObjectID);
                break;
            case AttributeKind:
                classificationAttachable = coreRealm.getAttributeKind(relatedObjectID);
                break;
            case AttributesViewKind:
                classificationAttachable = coreRealm.getAttributesViewKind(relatedObjectID);
                break;
            case ConceptionEntity:
                break;
        }

        if(classificationAttachable != null){
            try {
                boolean detachResult = classificationAttachable.detachClassification(classificationConfigItemValueObject.getClassificationName(),
                        classificationConfigItemValueObject.getAttachRelationKindName(),classificationConfigItemValueObject.getAttachRelationDirection());
                if(detachResult){
                    CommonUIOperationUtil.showPopupNotification("删除分类关联 "+ classificationConfigItemValueObject.classificationName+" - "+classificationConfigItemValueObject.attachRelationKindName +" 成功", NotificationVariant.LUMO_SUCCESS);
                    confirmWindow.closeConfirmWindow();
                    ListDataProvider dtaProvider=(ListDataProvider)classificationConfigItemValueObjectGrid.getDataProvider();
                    dtaProvider.getItems().remove(classificationConfigItemValueObject);
                    dtaProvider.refreshAll();
                }else{
                    CommonUIOperationUtil.showPopupNotification("删除分类关联 "+ classificationConfigItemValueObject.classificationName+" - "+classificationConfigItemValueObject.attachRelationKindName +" 失败", NotificationVariant.LUMO_ERROR);
                }
            } catch (CoreRealmServiceRuntimeException e) {
                throw new RuntimeException(e);
            }
        }
        coreRealm.closeGlobalSession();
    }
}
