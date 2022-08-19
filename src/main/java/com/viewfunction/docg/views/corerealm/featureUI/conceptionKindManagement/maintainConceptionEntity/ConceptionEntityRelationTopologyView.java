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
import dev.mett.vaadin.tooltip.Tooltips;

import java.util.ArrayList;
import java.util.List;

public class ConceptionEntityRelationTopologyView extends VerticalLayout {
    private String conceptionKind;
    private String conceptionEntityUID;
    private SecondaryKeyValueDisplayItem relationCountDisplayItem;
    private int conceptionEntityRelationInfoViewHeightOffset;
    private ConceptionEntityRelationsChart conceptionEntityRelationsChart;
    private Registration listener;
    private Button deleteSingleEntityButton;
    private Button deleteEntitiesButton;

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
        graphExploreActionButtonContainer.setPadding(false);
        graphExploreActionButtonContainer.setSpacing(false);
        graphExploreActionButtonContainer.setMargin(false);

        Button reloadConceptionEntitiesInfoButton = new Button();
        reloadConceptionEntitiesInfoButton.setIcon(VaadinIcon.REFRESH.create());
        Tooltips.getCurrent().setTooltip(reloadConceptionEntitiesInfoButton, "重新加载关系图");
        reloadConceptionEntitiesInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                conceptionEntityRelationsChart.reload();
            }
        });
        reloadConceptionEntitiesInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        graphExploreActionButtonContainer.add(reloadConceptionEntitiesInfoButton);

        deleteSingleEntityButton = new Button();
        deleteSingleEntityButton.setIcon(VaadinIcon.DEL_A.create());
        Tooltips.getCurrent().setTooltip(deleteSingleEntityButton, "隐藏选中的概念实体");
        deleteSingleEntityButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        graphExploreActionButtonContainer.add(deleteSingleEntityButton);
        deleteSingleEntityButton.setEnabled(false);

        deleteEntitiesButton = new Button();
        deleteEntitiesButton.setIcon(VaadinIcon.DEL.create());
        Tooltips.getCurrent().setTooltip(deleteEntitiesButton, "隐藏选中的以及与其一度关联的所有概念实体");
        deleteEntitiesButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        graphExploreActionButtonContainer.add(deleteEntitiesButton);
        deleteEntitiesButton.setEnabled(false);

        Label selectMethodMessage = new Label("单击选中实体，双击概念实体获取其一度关联信息");
        selectMethodMessage.getStyle().set("font-size","10px").set("padding-right","30px");
        selectMethodMessage.addClassNames("text-tertiary");
        graphExploreActionButtonContainer.add(selectMethodMessage);
        graphExploreActionButtonContainer.setVerticalComponentAlignment(Alignment.CENTER,selectMethodMessage);

        secondaryTitleComponentsList.add(graphExploreActionButtonContainer);

        HorizontalLayout titleLayout = new HorizontalLayout();
        secondaryTitleComponentsList.add(titleLayout);
        relationCountDisplayItem = new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.EXPAND.create(), "当前显示关系实体总量", "-");
        new SecondaryKeyValueDisplayItem(titleLayout, VaadinIcon.CIRCLE_THIN.create(), "当显示概念实体总量", "-");


        Button conceptionEntitiesStaticInfoButton = new Button("关联概念实体类型分布");
        conceptionEntitiesStaticInfoButton.setIcon(VaadinIcon.PIE_CHART.create());
        conceptionEntitiesStaticInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        conceptionEntitiesStaticInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

            }
        });
        actionComponentsList.add(conceptionEntitiesStaticInfoButton);

        Button fullRelationsInfoButton = new Button("关联实体全量星云图");
        fullRelationsInfoButton.setIcon(VaadinIcon.ASTERISK.create());
        fullRelationsInfoButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_TERTIARY);
        fullRelationsInfoButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {

            }
        });
        actionComponentsList.add(fullRelationsInfoButton);

        Icon relationsIcon = VaadinIcon.AIRPLANE.create();
        SecondaryTitleActionBar secondaryTitleActionBar = new SecondaryTitleActionBar(relationsIcon, "关联关系探索: ", secondaryTitleComponentsList, actionComponentsList);
        add(secondaryTitleActionBar);

        HorizontalLayout relationEntitiesDetailLayout = new HorizontalLayout();
        relationEntitiesDetailLayout.setWidthFull();
        add(relationEntitiesDetailLayout);

        this.conceptionEntityRelationsChart = new ConceptionEntityRelationsChart(this.conceptionKind,this.conceptionEntityUID);
        this.conceptionEntityRelationsChart.setContainerConceptionEntityRelationTopologyView(this);
        relationEntitiesDetailLayout.add(this.conceptionEntityRelationsChart);

        VerticalLayout selectedEntityInfoContainerLayout = new VerticalLayout();
        selectedEntityInfoContainerLayout.setSpacing(false);
        selectedEntityInfoContainerLayout.setMargin(false);

        selectedEntityInfoContainerLayout.setWidth(350,Unit.PIXELS);
        selectedEntityInfoContainerLayout.getStyle()
                .set("border-left", "1px solid var(--lumo-contrast-20pct)");
        relationEntitiesDetailLayout.add(selectedEntityInfoContainerLayout);
        relationEntitiesDetailLayout.setFlexGrow(1,this.conceptionEntityRelationsChart);

        ThirdLevelIconTitle infoTitle1 = new ThirdLevelIconTitle(new Icon(VaadinIcon.POINTER)," 已选中实体综合信息");
        selectedEntityInfoContainerLayout.add(infoTitle1);
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

    public void disableControlActionButtons() {
        deleteSingleEntityButton.setEnabled(false);
        deleteEntitiesButton.setEnabled(false);
    }

    public void enableControlActionButtons() {
        deleteSingleEntityButton.setEnabled(true);
        deleteEntitiesButton.setEnabled(true);
    }
}
