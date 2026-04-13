package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity.spatial;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.viewfunction.docg.coreRealm.realmServiceCore.feature.GeospatialScaleCalculable;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionEntity;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;

import java.util.ArrayList;
import java.util.List;

public class UpdateConceptionEntitySpatialInfoView extends VerticalLayout {

    private ConceptionEntity conceptionEntity;
    private GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel;
    private boolean isInited = false;
    private TextField  _CRSAIDField;

    public UpdateConceptionEntitySpatialInfoView(){}

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        if(!isInited){
            Icon conceptionKindIcon = VaadinIcon.CUBE.create();
            conceptionKindIcon.setSize("12px");
            conceptionKindIcon.getStyle().set("padding-right","3px");
            Icon conceptionEntityIcon = VaadinIcon.KEY_O.create();
            conceptionEntityIcon.setSize("18px");
            conceptionEntityIcon.getStyle().set("padding-right","3px").set("padding-left","5px");
            List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
            footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionKindIcon, this.getConceptionEntity().getConceptionKindName()));
            footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(conceptionEntityIcon, this.getConceptionEntity().getConceptionEntityUID()));
            FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
            add(entityInfoFootprintMessageBar);
            HorizontalLayout spaceDivLayout = new HorizontalLayout();
            spaceDivLayout.setWidthFull();
            spaceDivLayout.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
            add(spaceDivLayout);

            _CRSAIDField = new TextField("CRSAID");
            _CRSAIDField.setWidth(200, Unit.PIXELS);

            switch (this.spatialScaleLevel){
                case GeospatialScaleCalculable.SpatialScaleLevel.Global:
                    Span globalInfoSpan =new Span();
                    Icon globalInfoIcon = new Icon(VaadinIcon.GLOBE_WIRE);
                    globalInfoIcon.setSize("14px");
                    NativeLabel globalInfoLabel = new NativeLabel(" 全球坐标系地理空间信息");
                    globalInfoSpan.add(globalInfoIcon,globalInfoLabel,_CRSAIDField);
                    add(globalInfoSpan);
                    break;
                case GeospatialScaleCalculable.SpatialScaleLevel.Country:
                    Span countryInfoSpan =new Span();
                    Icon countryInfoIcon = new Icon(VaadinIcon.LOCATION_ARROW_CIRCLE);
                    countryInfoIcon.setSize("14px");
                    NativeLabel countryInfoLabel = new NativeLabel(" 国家坐标系地理空间信息");
                    countryInfoSpan.add(countryInfoIcon,countryInfoLabel,_CRSAIDField);
                    add(countryInfoSpan);
                    break;
                case GeospatialScaleCalculable.SpatialScaleLevel.Local:
                    Span localInfoSpan =new Span();
                    Icon localInfoIcon = new Icon(VaadinIcon.HOME);
                    localInfoIcon.setSize("14px");
                    NativeLabel localInfoLabel = new NativeLabel(" 本地坐标系地理空间信息");
                    localInfoSpan.add(localInfoIcon,localInfoLabel,_CRSAIDField);
                    add(localInfoSpan);
                    break;
            }
            isInited = true;
        }
    }

    private Dialog containerDialog;

    public Dialog getContainerDialog() {
        return containerDialog;
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    public ConceptionEntity getConceptionEntity() {
        return conceptionEntity;
    }

    public void setConceptionEntity(ConceptionEntity conceptionEntity) {
        this.conceptionEntity = conceptionEntity;
    }

    public GeospatialScaleCalculable.SpatialScaleLevel getSpatialScaleLevel() {
        return spatialScaleLevel;
    }

    public void setSpatialScaleLevel(GeospatialScaleCalculable.SpatialScaleLevel spatialScaleLevel) {
        this.spatialScaleLevel = spatialScaleLevel;
    }
}
