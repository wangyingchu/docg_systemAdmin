package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.loadConceptionEntities;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.viewfunction.docg.coreRealm.realmServiceCore.exception.CoreRealmServiceRuntimeException;
import com.viewfunction.docg.coreRealm.realmServiceCore.operator.EntitiesExchangeOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.element.commonComponent.PrimaryKeyValueDisplayItem;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class DownloadARROWFormatConceptionEntitiesView extends VerticalLayout {
    private String conceptionKindName;
    private String TEMP_FILES_STORAGE_LOCATION =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.TEMP_FILES_STORAGE_LOCATION);
    private Dialog containerDialog;
    private Button cancelImportButton;
    private Button generateArrowButton;
    private FileDownloadWrapper arrowFileDownloader;

    public DownloadARROWFormatConceptionEntitiesView(String conceptionKindName, int viewWidth){
        this.setWidth(100, Unit.PERCENTAGE);
        this.conceptionKindName = conceptionKindName;

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

        long conceptionEntitiesCount = 0;
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        com.viewfunction.docg.coreRealm.realmServiceCore.term.ConceptionKind targetConceptionKind = coreRealm.getConceptionKind(this.conceptionKindName);
        try {
            conceptionEntitiesCount = targetConceptionKind.countConceptionEntities();
        } catch (CoreRealmServiceRuntimeException e) {
            throw new RuntimeException(e);
        }

        NumberFormat numberFormat = NumberFormat.getInstance();
        HorizontalLayout entitiesCountContainer = new HorizontalLayout();
        entitiesCountContainer.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        add(entitiesCountContainer);
        new PrimaryKeyValueDisplayItem(entitiesCountContainer, FontAwesome.Solid.CIRCLE.create(),"概念实体数量:",numberFormat.format(conceptionEntitiesCount));

        HorizontalLayout spaceDiv = new HorizontalLayout();
        spaceDiv.setWidth(20,Unit.PIXELS);
        entitiesCountContainer.add(spaceDiv);

        generateArrowButton = new Button("生成 ARROW 格式数据文件",new Icon(VaadinIcon.PLAY));
        generateArrowButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        generateArrowButton.setDisableOnClick(true);
        entitiesCountContainer.add(generateArrowButton);
        generateArrowButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                generateArrowDataFile();
            }
        });


        TextField textField = new TextField("Enter file contents");
        FileDownloadWrapper link = new FileDownloadWrapper("textfield.txt", () -> textField.getValue().getBytes());
        link.setText("Download textfield.txt that has contents of the TextField");
        add(link);

        Button button = new Button("Click to download");

        arrowFileDownloader = new FileDownloadWrapper("data.arrow",new File("/home/wangychu/Desktop/tess/entinfo.arrow"));
        arrowFileDownloader.wrapComponent(button);
        add(arrowFileDownloader);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidthFull();
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout2);

        HorizontalLayout buttonbarLayout = new HorizontalLayout();
        add(buttonbarLayout);
        setHorizontalComponentAlignment(Alignment.END,buttonbarLayout);

        cancelImportButton = new Button("取消导出 ARROW 格式概念实体数据");
        cancelImportButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        cancelImportButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(containerDialog != null){
                    containerDialog.close();
                }
                //cancelImportUploadedFile();
            }
        });

        buttonbarLayout.add(cancelImportButton);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private EntitiesOperationStatistics generateArrowDataFile(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        EntitiesExchangeOperator entitiesExchangeOperator = coreRealm.getEntitiesExchangeOperator();
        String dataFileName = PinyinUtil.getPinyin(this.conceptionKindName,"")+"_"+System.currentTimeMillis()+"_EXPORT.arrow";
        String arrowDataFileURI = TEMP_FILES_STORAGE_LOCATION+"/"+ dataFileName;

        EntitiesOperationStatistics entitiesOperationStatistics = entitiesExchangeOperator.exportConceptionEntitiesToArrow(this.conceptionKindName,arrowDataFileURI);
        arrowFileDownloader.setText(dataFileName);
        arrowFileDownloader.setFile(new File(arrowDataFileURI));
        return entitiesOperationStatistics;
    }
}
