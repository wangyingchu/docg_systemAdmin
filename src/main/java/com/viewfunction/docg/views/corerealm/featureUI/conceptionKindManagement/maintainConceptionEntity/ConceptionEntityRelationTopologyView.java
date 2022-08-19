package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.element.commonComponent.SecondaryKeyValueDisplayItem;
import com.viewfunction.docg.element.commonComponent.SecondaryTitleActionBar;
import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;

import java.util.ArrayList;
import java.util.List;

public class ConceptionEntityRelationTopologyView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    private SecondaryKeyValueDisplayItem relationCountDisplayItem;
    private SecondaryKeyValueDisplayItem inDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem outDegreeDisplayItem;
    private SecondaryKeyValueDisplayItem isDenseDisplayItem;
    private int conceptionEntityRelationInfoViewHeightOffset;
    private ConceptionEntityRelationsChart conceptionEntityRelationsChart;
    private Registration listener;

    public ConceptionEntityRelationTopologyView(String conceptionKind,String conceptionEntityUID,int conceptionEntityIntegratedInfoViewHeightOffset) {
        this.setPadding(false);
        this.setSpacing(false);
        this.setMargin(false);

        this.conceptionKind = conceptionKind;
        this.conceptionEntityUID = conceptionEntityUID;
        this.conceptionEntityRelationInfoViewHeightOffset = conceptionEntityIntegratedInfoViewHeightOffset+100;
        List<Component> secondaryTitleComponentsList = new ArrayList<>();
        List<Component> actionComponentsList = new ArrayList<>();



        HorizontalLayout graphExploreActionButtonContainer = new HorizontalLayout();

        Button reloadConceptionEntitiesInfoButton0 = new Button();
        reloadConceptionEntitiesInfoButton0.setIcon(VaadinIcon.PIE_CHART.create());
        reloadConceptionEntitiesInfoButton0.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        graphExploreActionButtonContainer.add(reloadConceptionEntitiesInfoButton0);

        Label ss = new Label("单击选中实体，双击概念实体获取其一度关联信息");
        ss.getStyle().set("font-size","10px").set("padding-left","5px").set("padding-right","15px");
        ss.addClassNames("text-tertiary");
        graphExploreActionButtonContainer.add(ss);
        graphExploreActionButtonContainer.setVerticalComponentAlignment(Alignment.CENTER,ss);


        secondaryTitleComponentsList.add(graphExploreActionButtonContainer);

        HorizontalLayout titleLayout = new HorizontalLayout();
        secondaryTitleComponentsList.add(titleLayout);
        relationCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.LIST_OL.create(), "显示关联关系总量", "-");
        new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.LIST_OL.create(), "显示概念实体总量", "-");
        //inDegreeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.ANGLE_DOUBLE_LEFT.create(), "关系入度", "-");
        //outDegreeDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.ANGLE_DOUBLE_RIGHT.create(), "关系出度", "-");
        //isDenseDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.BULLSEYE.create(), "是否稠密实体", "-");



        //H6 ss = new H6("单击选中实体，双击概念实体获取其一度关联信息");
        //ss.getStyle().set("font-size","8px").set("padding-left","20px");
        //ss.addClassNames("text-tertiary");

        //titleLayout.add(ss);

        //titleLayout.setVerticalComponentAlignment(Alignment.START,ss);


        Button reloadConceptionEntitiesInfoButton = new Button("关联概念实体类型分布");
        reloadConceptionEntitiesInfoButton.setIcon(VaadinIcon.PIE_CHART.create());
        reloadConceptionEntitiesInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        reloadConceptionEntitiesInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

            }
        });
        actionComponentsList.add(reloadConceptionEntitiesInfoButton);

        Button reloadRelationInfoButton = new Button("重新获取数据");
        reloadRelationInfoButton.setIcon(VaadinIcon.REFRESH.create());
        reloadRelationInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        reloadRelationInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                conceptionEntityRelationsChart.reload();
            }
        });
        actionComponentsList.add(reloadRelationInfoButton);

        Icon relationsIcon = VaadinIcon.AIRPLANE.create();
        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(relationsIcon, "关联关系探索: ", secondaryTitleComponentsList, actionComponentsList);
        add(secondaryTitleActionBar);

        HorizontalLayout relationEntitiesDetailLayout = new HorizontalLayout();
        relationEntitiesDetailLayout.setWidthFull();
        add(relationEntitiesDetailLayout);

        this.conceptionEntityRelationsChart = new ConceptionEntityRelationsChart(this.conceptionKind,this.conceptionEntityUID);
        relationEntitiesDetailLayout.add(this.conceptionEntityRelationsChart);

        VerticalLayout selectedEntityInfoContainerLayout = new VerticalLayout();
        //selectedEntityInfoContainerLayout.setPadding(false);
        selectedEntityInfoContainerLayout.setSpacing(false);
        selectedEntityInfoContainerLayout.setMargin(false);

        selectedEntityInfoContainerLayout.setWidth(350,Unit.PIXELS);
        selectedEntityInfoContainerLayout.getStyle()
                .set("border-left", "1px solid var(--lumo-contrast-20pct)");
        relationEntitiesDetailLayout.add(selectedEntityInfoContainerLayout);
        relationEntitiesDetailLayout.setFlexGrow(1,this.conceptionEntityRelationsChart);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.POINTER)," 已选中实体综合信息");
        selectedEntityInfoContainerLayout.add(infoTitle1);


        //Label titleTextLabel = new Label("单击选中实体，双击概念实体获取其一度关联信息");
        //titleTextLabel.addClassNames("text-xs","text-tertiary");
        ///selectedEntityInfoContainerLayout.add(ss);

    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        // Add browser window listener to observe size change
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            conceptionEntityRelationsChart.setHeight((event.getHeight()-this.conceptionEntityRelationInfoViewHeightOffset)+40, Unit.PIXELS);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            conceptionEntityRelationsChart.setHeight((browserHeight-this.conceptionEntityRelationInfoViewHeightOffset+40),Unit.PIXELS);
        }));
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    public void loadEntityRelationNetworks(){
        this.conceptionEntityRelationsChart.initLoadTargetConceptionEntityRelationData();
    }
}
