package com.viewfunction.docg.views.corerealm.featureUI.conceptionKindManagement.maintainConceptionKind.exchangeConceptionEntities;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import com.flowingcode.vaadin.addons.fontawesome.FontAwesome;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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

public class DownloadCSVFormatConceptionEntitiesView extends VerticalLayout {
    private String conceptionKindName;
    private String TEMP_FILES_STORAGE_LOCATION =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.TEMP_FILES_STORAGE_LOCATION);
    private Dialog containerDialog;
    private Button cancelImportButton;
    private Button generateCsvButton;
    private Label csvFileName;
    private HorizontalLayout downloaderContainer;
    private String csvDataFileURI;
    private long conceptionEntitiesCount;

    public DownloadCSVFormatConceptionEntitiesView(String conceptionKindName, int viewWidth){
        this.setWidth(100, Unit.PERCENTAGE);
        this.conceptionKindName = conceptionKindName;

        Icon kindIcon = VaadinIcon.CUBE.create();
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");
        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, this.conceptionKindName));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

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

        generateCsvButton = new Button("生成 CSV 格式数据文件",new Icon(VaadinIcon.PLAY));
        generateCsvButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        generateCsvButton.setDisableOnClick(true);
        entitiesCountContainer.add(generateCsvButton);
        generateCsvButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                generateArrowDataFile();
            }
        });

        HorizontalLayout dataFileInfoLayout = new HorizontalLayout();
        dataFileInfoLayout.getStyle().set("padding-top","10px");
        Label messageContentLabel = new Label("CSV 数据文件: ");
        messageContentLabel.addClassNames("text-xs","text-secondary");

        csvFileName = new Label();
        csvFileName.addClassNames("text-xs");
        dataFileInfoLayout.add(messageContentLabel, csvFileName);
        add(dataFileInfoLayout);

        downloaderContainer = new HorizontalLayout();
        downloaderContainer.setHeight(40,Unit.PIXELS);
        add(downloaderContainer);

        HorizontalLayout spaceDivLayout2 = new HorizontalLayout();
        spaceDivLayout2.setWidthFull();
        spaceDivLayout2.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-20pct)");
        add(spaceDivLayout2);

        HorizontalLayout buttonbarLayout = new HorizontalLayout();
        add(buttonbarLayout);
        setHorizontalComponentAlignment(Alignment.END,buttonbarLayout);

        cancelImportButton = new Button("取消或结束导出 CSV 格式概念实体数据");
        cancelImportButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE,ButtonVariant.LUMO_SMALL);
        cancelImportButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                if(containerDialog != null){
                    containerDialog.close();
                }
                deleteArrowDataFile();
            }
        });
        buttonbarLayout.add(cancelImportButton);
    }

    public void setContainerDialog(Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }

    private void generateArrowDataFile(){
        CoreRealm coreRealm = RealmTermFactory.getDefaultCoreRealm();
        EntitiesExchangeOperator entitiesExchangeOperator = coreRealm.getEntitiesExchangeOperator();
        File fileFolder = new File(TEMP_FILES_STORAGE_LOCATION);
        if(!fileFolder.exists()){
            fileFolder.mkdirs();
        }

        String dataFileName = PinyinUtil.getPinyin(this.conceptionKindName,"")+"_"+System.currentTimeMillis()+"_EXPORT.csv";
        csvDataFileURI = fileFolder.getAbsolutePath()+"/"+ dataFileName;

        EntitiesOperationStatistics entitiesOperationStatistics = entitiesExchangeOperator.exportConceptionEntitiesToCSV(this.conceptionKindName, csvDataFileURI);
        csvFileName.setText(dataFileName);

        Button downloadButton = new Button("点击下载 CSV 数据文件");
        downloadButton.setIcon(VaadinIcon.DOWNLOAD_ALT.create());

        FileDownloadWrapper arrowFileDownloader = new FileDownloadWrapper(dataFileName,new File(TEMP_FILES_STORAGE_LOCATION));
        arrowFileDownloader.wrapComponent(downloadButton);
        downloaderContainer.add(arrowFileDownloader);
        arrowFileDownloader.setFile(new File(csvDataFileURI));

        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        Div text = new Div(new Text("概念类型 "+conceptionKindName+" 创建 CSV 数据文件操作成功"));
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
        notificationMessageContainer.add(new Div(new Text("CSV 数据文件: "+dataFileName)));
        notificationMessageContainer.add(new Div(new Text("当前概念实体总数: " + conceptionEntitiesCount)));
        notificationMessageContainer.add(new Div(new Text("导出成功实体数: "+entitiesOperationStatistics.getSuccessItemsCount())));
        notificationMessageContainer.add(new Div(new Text("导出失败实体数: "+entitiesOperationStatistics.getFailItemsCount())));
        notificationMessageContainer.add(new Div(new Text("操作开始时间: "+entitiesOperationStatistics.getStartTime())));
        notificationMessageContainer.add(new Div(new Text("操作结束时间: "+entitiesOperationStatistics.getFinishTime())));
        notification.add(notificationMessageContainer);
        notification.open();
    }

    private void deleteArrowDataFile(){
        if(csvDataFileURI != null){
            FileUtil.del(csvDataFileURI);
        }
    }
}
