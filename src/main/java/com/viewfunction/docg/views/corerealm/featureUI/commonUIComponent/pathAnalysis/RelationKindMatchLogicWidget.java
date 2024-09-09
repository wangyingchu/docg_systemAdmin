package com.viewfunction.docg.views.corerealm.featureUI.commonUIComponent.pathAnalysis;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.RelationDirection;

import java.util.ArrayList;
import java.util.List;

public class RelationKindMatchLogicWidget extends HorizontalLayout {

    private String relationKindName;
    private RelationDirection relationDirection;

    public RelationKindMatchLogicWidget(){
        setSizeFull();

        NativeLabel defaultRelationDirectionFilterText = new NativeLabel("默认全局关系方向:");
        defaultRelationDirectionFilterText.getStyle().set("font-size","0.7rem").set("color","var(--lumo-contrast-80pct)");
        defaultRelationDirectionFilterText.addClassNames("text-xs","font-semibold","text-secondary");
        add(defaultRelationDirectionFilterText);
        this.setVerticalComponentAlignment(Alignment.CENTER,defaultRelationDirectionFilterText);

        ComboBox<RelationDirection> provinceValueTextField = new ComboBox();
        provinceValueTextField.addThemeVariants(ComboBoxVariant.LUMO_SMALL,ComboBoxVariant.LUMO_ALIGN_RIGHT);
        List<RelationDirection> relationDirectionList = new ArrayList<>();
        relationDirectionList.add(RelationDirection.FROM);
        relationDirectionList.add(RelationDirection.TO);
        relationDirectionList.add(RelationDirection.TWO_WAY);
        provinceValueTextField.setItems(relationDirectionList);




        provinceValueTextField.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        provinceValueTextField.setWidth(100, Unit.PIXELS);

        add(provinceValueTextField);

    }

    public String getRelationKindName() {
        return relationKindName;
    }

    public void setRelationKindName(String relationKindName) {
        this.relationKindName = relationKindName;
    }

    public RelationDirection getRelationDirection() {
        return relationDirection;
    }

    public void setRelationDirection(RelationDirection relationDirection) {
        this.relationDirection = relationDirection;
    }
}
