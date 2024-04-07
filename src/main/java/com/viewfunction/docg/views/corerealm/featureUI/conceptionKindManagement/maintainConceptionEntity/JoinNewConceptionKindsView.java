package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionEntity;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ItemLabelGenerator;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.renderer.Renderer;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceEntityExploreException;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.KindMetaInfo;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;

import java.util.List;

public class JoinNewConceptionKindsView extends VerticalLayout {
    private Dialog containerDialog;
    private ComboBox<KindMetaInfo> conceptionKindFilterSelect;
    private NativeLabel errorMessage;

    public JoinNewConceptionKindsView(String conceptionKind,String conceptionEntityUID) {
        HorizontalLayout errorMessageContainer = new HorizontalLayout();
        errorMessageContainer.setSpacing(false);
        errorMessageContainer.setPadding(false);
        errorMessageContainer.setMargin(false);
        errorMessageContainer.getStyle().set("padding-top", "3px").set("padding-bottom", "3px");

        NativeLabel viewTitle = new NativeLabel("新附加概念类型信息:");
        viewTitle.getStyle().set("color", "var(--lumo-contrast-50pct)").set("font-size", "0.8rem");
        errorMessageContainer.add(viewTitle);
        errorMessage = new NativeLabel("-");
        errorMessage.getStyle().set("color", "var(--lumo-error-text-color)").set("font-size", "0.8rem");
        errorMessage.setVisible(false);
        errorMessageContainer.add(errorMessage);
        add(errorMessageContainer);

        conceptionKindFilterSelect = new ComboBox();
        conceptionKindFilterSelect.setPageSize(30);
        conceptionKindFilterSelect.setPlaceholder("选择要附加的概念类型");
        conceptionKindFilterSelect.setWidth(100, Unit.PERCENTAGE);
        conceptionKindFilterSelect.setItemLabelGenerator(new ItemLabelGenerator<KindMetaInfo>() {
            @Override
            public String apply(KindMetaInfo attributeKindMetaInfo) {

                String itemLabelValue = attributeKindMetaInfo.getKindName() + " (" +
                        attributeKindMetaInfo.getKindDesc() + ")";
                return itemLabelValue;
            }
        });
        conceptionKindFilterSelect.setRenderer(createRenderer());
        add(conceptionKindFilterSelect);

        HorizontalLayout spaceDivLayout = new HorizontalLayout();
        spaceDivLayout.setWidthFull();
        spaceDivLayout.getStyle()
                .set("border-bottom", "1px solid var(--lumo-contrast-20pct)")
                .set("padding-bottom", "var(--lumo-space-m)");
        add(spaceDivLayout);

        Button confirmButton = new Button("确定附加概念类型", new Icon(VaadinIcon.CHECK));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                errorMessage.setVisible(false);
                if (conceptionKindFilterSelect.getValue() == null) {
                    errorMessage.setText("请选择概念类型");
                    errorMessage.setVisible(true);
                } else {
                    //doAttachConceptionType(conceptionKindFilterSelect.getValue());
                }
            }
        });
        add(confirmButton);
        setHorizontalComponentAlignment(Alignment.END, confirmButton);

        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        try {
            List<KindMetaInfo> runtimeAttributeKindMetaInfoList = coreRealm.getConceptionKindsMetaInfo();
            conceptionKindFilterSelect.setItems(runtimeAttributeKindMetaInfoList);
        } catch (CoreRealmServiceEntityExploreException e) {
            throw new RuntimeException(e);
        }
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
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



}
