package com.viewfunction.docg.views.corerealm.featureUI.attributesViewKindManagement.maintainAttributesViewKind;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.AttributeKindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.GridColumnHeader;
import com.viewfunction.docg.element.commonComponent.lineAwesomeIcon.LineAwesomeIconsSvg;
import com.viewfunction.docg.util.ResourceHolder;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BatchAttachNewAttributeKindsView extends VerticalLayout {

    private String attributesViewKindUID;
    private Dialog containerDialog;
    private ComboBox<AttributeKindMetaInfo> attributeKindFilterSelect;
    private NativeLabel errorMessage;
    private Grid<AttributeKindMetaInfo> attributeKindMetaInfoGrid;

    public BatchAttachNewAttributeKindsView(String attributesViewKindUID){
        this.attributesViewKindUID = attributesViewKindUID;

        ComponentRenderer _createDateComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            if(entityStatisticsInfo instanceof AttributeKindMetaInfo && ((AttributeKindMetaInfo)entityStatisticsInfo).getCreateDate() != null){
                ZonedDateTime createZonedDateTime = ((AttributeKindMetaInfo)entityStatisticsInfo).getCreateDate();
                return new NativeLabel(createZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
            }else{
                return new NativeLabel("-");
            }
        });

        Comparator createDateComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(((AttributeKindMetaInfo)o1).getCreateDate()!= null && ((AttributeKindMetaInfo)o2).getCreateDate()!= null &&
                        ((AttributeKindMetaInfo)o1).getCreateDate() instanceof ZonedDateTime &&
                        ((AttributeKindMetaInfo)o2).getCreateDate() instanceof ZonedDateTime){
                    if(((AttributeKindMetaInfo)o1).getCreateDate().isBefore(((AttributeKindMetaInfo)o2).getCreateDate())){
                        return -1;
                    }if(((AttributeKindMetaInfo)o1).getCreateDate().isAfter(((AttributeKindMetaInfo)o2).getCreateDate())){
                        return 1;
                    }
                }
                return 0;
            }
            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };

        ComponentRenderer _lastUpdateDateComponentRenderer = new ComponentRenderer<>(entityStatisticsInfo -> {
            if(entityStatisticsInfo instanceof AttributeKindMetaInfo && ((AttributeKindMetaInfo)entityStatisticsInfo).getLastModifyDate() != null){
                ZonedDateTime createZonedDateTime = ((AttributeKindMetaInfo)entityStatisticsInfo).getLastModifyDate();
                return new NativeLabel(createZonedDateTime.format(DateTimeFormatter.ofLocalizedDateTime((FormatStyle.MEDIUM))));
            }else{
                return new NativeLabel("-");
            }
        });

        Comparator lastUpdateDateComparator = new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(((AttributeKindMetaInfo)o1).getLastModifyDate()!= null && ((AttributeKindMetaInfo)o2).getLastModifyDate()!= null &&
                        ((AttributeKindMetaInfo)o1).getLastModifyDate() instanceof ZonedDateTime &&
                        ((AttributeKindMetaInfo)o2).getLastModifyDate() instanceof ZonedDateTime){
                    if(((AttributeKindMetaInfo)o1).getLastModifyDate().isBefore(((AttributeKindMetaInfo)o2).getLastModifyDate())){
                        return -1;
                    }if(((AttributeKindMetaInfo)o1).getLastModifyDate().isAfter(((AttributeKindMetaInfo)o2).getLastModifyDate())){
                        return 1;
                    }
                }
                return 0;
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        };

        attributeKindMetaInfoGrid = new Grid<>();
        attributeKindMetaInfoGrid.setWidth(1300, Unit.PIXELS);
        attributeKindMetaInfoGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        attributeKindMetaInfoGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
        attributeKindMetaInfoGrid.addColumn(AttributeKindMetaInfo::getKindName).setHeader("属性类型名称").setKey("idx_0").setFlexGrow(1);
        attributeKindMetaInfoGrid.addColumn(AttributeKindMetaInfo::getKindDesc).setHeader("属性类型描述").setKey("idx_1").setFlexGrow(1)
                .setTooltipGenerator(attributeKindMetaInfoData -> attributeKindMetaInfoData.getKindDesc());
        attributeKindMetaInfoGrid.addColumn(AttributeKindMetaInfo::getAttributeDataType).setHeader("属性数据类型").setKey("idx_2")
                .setFlexGrow(0).setWidth("130px").setResizable(false);
        attributeKindMetaInfoGrid.addColumn(AttributeKindMetaInfo::getKindUID).setHeader("属性类型 UID").setKey("idx_3")
                .setFlexGrow(0).setWidth("150px").setResizable(false);
        attributeKindMetaInfoGrid.addColumn(_createDateComponentRenderer).setHeader("类型创建时间").setKey("idx_4")
                .setComparator(createDateComparator)
                .setFlexGrow(0).setWidth("210px").setResizable(false);
        attributeKindMetaInfoGrid.addColumn(_lastUpdateDateComponentRenderer).setHeader("类型最后更新时间").setKey("idx_5")
                .setComparator(lastUpdateDateComparator)
                .setFlexGrow(0).setWidth("210px").setResizable(false);

        GridColumnHeader gridColumnHeader_idx0 = new GridColumnHeader(VaadinIcon.INFO_CIRCLE_O,"属性类型名称");
        attributeKindMetaInfoGrid.getColumnByKey("idx_0").setHeader(gridColumnHeader_idx0).setSortable(true);
        GridColumnHeader gridColumnHeader_idx1 = new GridColumnHeader(VaadinIcon.DESKTOP,"属性类型描述");
        attributeKindMetaInfoGrid.getColumnByKey("idx_1").setHeader(gridColumnHeader_idx1).setSortable(true);
        GridColumnHeader gridColumnHeader_idx2 = new GridColumnHeader(LineAwesomeIconsSvg.FIRSTDRAFT.create(),"属性数据类型");
        attributeKindMetaInfoGrid.getColumnByKey("idx_2").setHeader(gridColumnHeader_idx2).setSortable(true);
        GridColumnHeader gridColumnHeader_idx3 = new GridColumnHeader(VaadinIcon.KEY_O,"属性类型 UID");
        attributeKindMetaInfoGrid.getColumnByKey("idx_3").setHeader(gridColumnHeader_idx3).setSortable(true);
        GridColumnHeader gridColumnHeader_idx4 = new GridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"类型创建时间");
        attributeKindMetaInfoGrid.getColumnByKey("idx_4").setHeader(gridColumnHeader_idx4).setSortable(true);
        GridColumnHeader gridColumnHeader_idx5 = new GridColumnHeader(VaadinIcon.CALENDAR_CLOCK,"类型最后更新时间");
        attributeKindMetaInfoGrid.getColumnByKey("idx_5").setHeader(gridColumnHeader_idx5).setSortable(true);
        attributeKindMetaInfoGrid.appendFooterRow();
        add(attributeKindMetaInfoGrid);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        loadAttributeKindsInfo();
        // Add browser window listener to observe size change


        /*
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            int browserHeight = event.getHeight();
            int browserWidth = event.getWidth();
            int chartWidth = browserWidth-1300-80;
            int chartHeight = browserHeight-590;
            attributeKindMetaInfoGrid.setHeight(event.getHeight()-250,Unit.PIXELS);
            attributeInConceptionKindDistributionInfoChart.setChartSize(chartWidth,chartHeight);
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            int browserHeight = receiver.getBodyClientHeight();
            int browserWidth = receiver.getBodyClientWidth();
            int chartWidth = browserWidth-1300-80;
            int chartHeight = browserHeight-590;
            attributeKindMetaInfoGrid.setHeight(browserHeight-250,Unit.PIXELS);
            attributeInConceptionKindDistributionInfoChart.setChartSize(chartWidth,chartHeight);
        }));
        */


    }



    private void loadAttributeKindsInfo(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        List<AttributeKindMetaInfo> gridAttributeKindMetaInfoList = new ArrayList<>();
        try {
            List<AttributeKindMetaInfo> runtimeAttributeKindMetaInfoList = coreRealm.getAttributeKindsMetaInfo();
            gridAttributeKindMetaInfoList.addAll(runtimeAttributeKindMetaInfoList);
            //this.attributeKindNameFilterField.setValue("");
            //this.attributeKindDescFilterField.setValue("");
           // this.attributeDataTypeFilterSelect.setValue(null);
           // this.attributeKindsMetaInfoView =
            attributeKindMetaInfoGrid.setItems(gridAttributeKindMetaInfoList);
            /*
            //logic to filter AttributeKinds already loaded from server
            this.attributeKindsMetaInfoView.addFilter(item->{
                String entityKindName = item.getKindName().toUpperCase();
                String entityKindDesc = item.getKindDesc().toUpperCase();
                String entityDataType = item.getAttributeDataType();
                boolean attributeKindNameFilterResult = true;
                if(!attributeKindNameFilterField.getValue().trim().equals("")){
                    if(entityKindName.contains(attributeKindNameFilterField.getValue().trim().toUpperCase())){
                        attributeKindNameFilterResult = true;
                    }else{
                        attributeKindNameFilterResult = false;
                    }
                }

                boolean attributeKindDescFilterResult = true;
                if(!attributeKindDescFilterField.getValue().trim().equals("")){
                    if(entityKindDesc.contains(attributeKindDescFilterField.getValue().trim().toUpperCase())){
                        attributeKindDescFilterResult = true;
                    }else{
                        attributeKindDescFilterResult = false;
                    }
                }

                boolean attributeDataTypeFilterResult = true;
                if(attributeDataTypeFilterSelect.getValue() != null){
                    if(entityDataType.equals(attributeDataTypeFilterSelect.getValue().toString())){
                        attributeDataTypeFilterResult = true;
                    }else{
                        attributeDataTypeFilterResult = false;
                    }
                }
                return attributeKindNameFilterResult & attributeKindDescFilterResult & attributeDataTypeFilterResult;
            });

            */
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }




    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }



}
