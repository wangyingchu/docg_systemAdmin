package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;

import com.viewfunction.docg.coreRealm.realmServiceCore.payload.ConceptionEntityValue;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindEntityAttributeRuntimeStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.AttributeDataType;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FixSizeWindow;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;
import com.viewfunction.docg.element.userInterfaceUtil.CommonUIOperationUtil;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AddCustomEntityAttributeUI;
import com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.entityMaintain.AttributeCreatorItemWidget;

import dev.mett.vaadin.tooltip.Tooltips;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;

public class AddConceptionEntityView extends VerticalLayout {

    private String conceptionKindName;
    private VerticalLayout criteriaItemsContainer;
    private List<String> resultAttributesList;
    private ComboBox<KindEntityAttributeRuntimeStatistics> kindExistingAttributeFilterSelect;
    private int viewWidth;
    private int viewHeight;

    public AddConceptionEntityView(String conceptionKindName,int viewWidth,int viewHeight){
        this.conceptionKindName = conceptionKindName;
        this.setMargin(false);
        this.setWidthFull();

        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;

        Icon kindIcon = VaadinIcon.CUBE.create();
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        Icon entityIcon = VaadinIcon.KEY_O.create();
        entityIcon.setSize("18px");
        entityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, this.conceptionKindName));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        VerticalLayout filterDropdownSelectorContainerLayout = new VerticalLayout();
        filterDropdownSelectorContainerLayout.setPadding(false);
        filterDropdownSelectorContainerLayout.setSpacing(false);
        filterDropdownSelectorContainerLayout.setMargin(false);
        add(filterDropdownSelectorContainerLayout);

        ThirdLevelIconTitle infoTitle2 = new ThirdLevelIconTitle(new Icon(VaadinIcon.COMBOBOX),"设定实体属性");
        filterDropdownSelectorContainerLayout.add(infoTitle2);

        HorizontalLayout buttonSpaceDivLayout = new HorizontalLayout();
        buttonSpaceDivLayout.setWidth(99, Unit.PERCENTAGE);

        resultAttributesList = new ArrayList<>();

        kindExistingAttributeFilterSelect = new ComboBox();
        kindExistingAttributeFilterSelect.setPageSize(30);
        kindExistingAttributeFilterSelect.setPlaceholder("选择要添加的实体属性");
        kindExistingAttributeFilterSelect.setMinWidth(220,Unit.PIXELS);
        kindExistingAttributeFilterSelect.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        buttonSpaceDivLayout.add(kindExistingAttributeFilterSelect);
        buttonSpaceDivLayout.setFlexGrow(1, kindExistingAttributeFilterSelect);

        kindExistingAttributeFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindEntityAttributeRuntimeStatistics>() {
            @Override
            public String apply(KindEntityAttributeRuntimeStatistics kindEntityAttributeRuntimeStatistics) {
                String itemLabelValue = kindEntityAttributeRuntimeStatistics.getAttributeName()+ " ("+
                        kindEntityAttributeRuntimeStatistics.getAttributeDataType()+")";
                return itemLabelValue;
            }
        });

        kindExistingAttributeFilterSelect.addValueChangeListener(new HasValue.
                ValueChangeListener<AbstractField.ComponentValueChangeEvent<ComboBox<KindEntityAttributeRuntimeStatistics>,
                KindEntityAttributeRuntimeStatistics>>() {
            @Override
            public void valueChanged(AbstractField.ComponentValueChangeEvent<ComboBox<KindEntityAttributeRuntimeStatistics>,
                    KindEntityAttributeRuntimeStatistics> comboBoxKindEntityAttributeRuntimeStatisticsComponentValueChangeEvent) {
                KindEntityAttributeRuntimeStatistics changedItem = comboBoxKindEntityAttributeRuntimeStatisticsComponentValueChangeEvent.getValue();
                if(changedItem != null){
                    kindExistingAttributeFilterSelect.setValue(null);
                    String selectedAttribute = changedItem.getAttributeName();
                    addAttributeCreatorItem(selectedAttribute,changedItem.getAttributeDataType());
                }
            }
        });
        kindExistingAttributeFilterSelect.setRenderer(createRenderer());
        kindExistingAttributeFilterSelect.getStyle().set("--vaadin-combo-box-overlay-width", "270px");

        Button addCustomQueryCriteriaButton = new Button();
        Tooltips.getCurrent().setTooltip(addCustomQueryCriteriaButton, "输入要添加的实体属性");
        addCustomQueryCriteriaButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        addCustomQueryCriteriaButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        addCustomQueryCriteriaButton.setIcon(VaadinIcon.KEYBOARD_O.create());
        addCustomQueryCriteriaButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                renderAddCustomAttributeUI();
            }
        });

        buttonSpaceDivLayout.add(addCustomQueryCriteriaButton);
        buttonSpaceDivLayout.setVerticalComponentAlignment(Alignment.CENTER, kindExistingAttributeFilterSelect);
        buttonSpaceDivLayout.setVerticalComponentAlignment(Alignment.CENTER,addCustomQueryCriteriaButton);

        filterDropdownSelectorContainerLayout.add(buttonSpaceDivLayout);

        criteriaItemsContainer = new VerticalLayout();
        criteriaItemsContainer.setMargin(false);
        criteriaItemsContainer.setSpacing(false);
        criteriaItemsContainer.setPadding(false);
        criteriaItemsContainer.setWidth(100,Unit.PERCENTAGE);

        Scroller queryConditionItemsScroller = new Scroller(criteriaItemsContainer);
        queryConditionItemsScroller.setScrollDirection(Scroller.ScrollDirection.VERTICAL);
        //scroller.getStyle().set("padding", "var(--lumo-space-m)");
        add(queryConditionItemsScroller);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidthFull();
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout2);

        Button confirmButton = new Button("确认添加概念类型实体",new Icon(VaadinIcon.CHECK_CIRCLE));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END,confirmButton);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                doAddConceptionEntity();
            }
        });
    }

    public void addAttributeCreatorItem(String attributeName, AttributeDataType attributeDataType){
        if(!resultAttributesList.contains(attributeName)){
            resultAttributesList.add(attributeName);
            AttributeCreatorItemWidget attributeCreatorItemWidget = new AttributeCreatorItemWidget(attributeName,attributeDataType,this.viewWidth);
            attributeCreatorItemWidget.setAddConceptionEntityView(this);
            criteriaItemsContainer.add(attributeCreatorItemWidget);
        }
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        criteriaItemsContainer.setHeight(this.viewHeight-210,Unit.PIXELS);
        loadKindExistingAttributeFilterComboBox();
    }

    private void loadKindExistingAttributeFilterComboBox(){
        int entityAttributesDistributionStatisticSampleRatio = 20000;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        coreRealm.openGlobalSession();
        ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(conceptionKindName);
        List<KindEntityAttributeRuntimeStatistics> kindEntityAttributeRuntimeStatisticsList =
                targetConceptionKind.statisticEntityAttributesDistribution(entityAttributesDistributionStatisticSampleRatio);
        coreRealm.closeGlobalSession();
        kindExistingAttributeFilterSelect.setItems(kindEntityAttributeRuntimeStatisticsList);
    }

    private Renderer<KindEntityAttributeRuntimeStatistics> createRenderer() {
        StringBuilder tpl = new StringBuilder();
        tpl.append("<div style=\"display: flex;\">");
        tpl.append("  <div>");
        tpl.append("    ${item.attributeName}");
        tpl.append("    <div style=\"font-size: var(--lumo-font-size-s); color: var(--lumo-secondary-text-color);\">${item.attributeDataType}</div>");
        tpl.append("  </div>");
        tpl.append("</div>");

        return LitRenderer.<KindEntityAttributeRuntimeStatistics>of(tpl.toString())
                .withProperty("attributeName", KindEntityAttributeRuntimeStatistics::getAttributeName)
                .withProperty("attributeDataType", KindEntityAttributeRuntimeStatistics::getAttributeDataType);
    }

    private void renderAddCustomAttributeUI(){
        AddCustomEntityAttributeUI addCustomEntityAttributeUI = new AddCustomEntityAttributeUI();
        addCustomEntityAttributeUI.setAddConceptionEntityView(this);
        FixSizeWindow fixSizeWindow = new FixSizeWindow(new Icon(VaadinIcon.COG),"输入要添加的实体属性",null,true,570,180,false);
        fixSizeWindow.setWindowContent(addCustomEntityAttributeUI);
        fixSizeWindow.setModel(true);
        addCustomEntityAttributeUI.setContainerDialog(fixSizeWindow);
        fixSizeWindow.show();
    }

    public void removeAttributeCreatorItemWidget(AttributeCreatorItemWidget attributeCreatorItemWidget){
        String attributeName = attributeCreatorItemWidget.getAttributeName();
        resultAttributesList.remove(attributeName);
        criteriaItemsContainer.remove(attributeCreatorItemWidget);
    }

    private boolean conceptionEntityAttributesValid = true;
    private void setConceptionEntityAttributesValid(boolean validFlag){
        conceptionEntityAttributesValid = validFlag;
    }

    private void doAddConceptionEntity(){
        Map<String,Object> entityAttributesMap = new HashMap<>();
        conceptionEntityAttributesValid = true;
        criteriaItemsContainer.getChildren().forEach(new Consumer<Component>() {

            @Override
            public void accept(Component component) {
                AttributeCreatorItemWidget currentAttributeCreatorItemWidget = (AttributeCreatorItemWidget)component;
                String attributeName = currentAttributeCreatorItemWidget.getAttributeName();
                Object attributeValue = currentAttributeCreatorItemWidget.getAttributeValue();
                AttributeDataType attributeDataType = currentAttributeCreatorItemWidget.getAttributeDataType();
                if(attributeValue == null){
                    setConceptionEntityAttributesValid(false);
                }else{
                    Object entityAttributeValue = getPropertyValue(attributeDataType,attributeValue);
                    if(entityAttributeValue != null){
                        entityAttributesMap.put(attributeName,entityAttributeValue);
                    }
                }
            }
        });
        if(conceptionEntityAttributesValid){
            CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
            ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
            ConceptionEntityValue conceptionEntityValue = new ConceptionEntityValue();
            conceptionEntityValue.setEntityAttributesValue(entityAttributesMap);
            ConceptionEntity newConceptionEntity = targetConceptionKind.newEntity(conceptionEntityValue,false);
            if(newConceptionEntity != null){
                CommonUIOperationUtil.showPopupNotification("添加概念类型实体 "+ conceptionKindName +" : "+newConceptionEntity.getConceptionEntityUID() +" 成功", NotificationVariant.LUMO_SUCCESS);

            }else{
                CommonUIOperationUtil.showPopupNotification("为概念类型实体 "+ conceptionKindName +" 添加实体失败", NotificationVariant.LUMO_ERROR);
            }
        }else{
            CommonUIOperationUtil.showPopupNotification("请为所有的概念实体属性输入非空并合法的属性值", NotificationVariant.LUMO_ERROR);
        }
    }

    private Object getPropertyValue(AttributeDataType attributeDataType,Object attributeValue){
        switch (attributeDataType) {
            case INT:
                attributeValue = Integer.valueOf(attributeValue.toString());
                break;
            case BYTE:
                attributeValue = Byte.valueOf(attributeValue.toString());
                break;
            case DATE:
                attributeValue = attributeValue;
                break;
            case LONG:
                attributeValue = Long.valueOf(attributeValue.toString());
                break;
            case FLOAT:
                attributeValue = Float.valueOf(attributeValue.toString());
                break;
            case SHORT:
                attributeValue = Short.valueOf(attributeValue.toString());
                break;
            case BINARY:
                break;
            case DOUBLE:
                attributeValue = Double.valueOf(attributeValue.toString());
                break;
            case STRING:
                attributeValue = attributeValue.toString();
                break;
            case BOOLEAN:
                attributeValue = Boolean.valueOf(attributeValue.toString());
                break;
            case DECIMAL:
                attributeValue =  new BigDecimal(attributeValue.toString());
                break;
            case TIMESTAMP:
                attributeValue = new Date(Long.valueOf(attributeValue.toString()));
                break;
            case TIME:
                attributeValue = attributeValue;
                break;
            case DATETIME:
                attributeValue = attributeValue;
        }
        return attributeValue;
    }
}
