package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData.exchangeCoreRealmEntities;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.pinyin.PinyinUtil;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.coreRealm.realmServiceCore.operator.EntitiesExchangeOperator;
import com.viewfunction.docg.coreRealm.realmServiceCore.payload.EntitiesOperationStatistics;
import com.viewfunction.docg.coreRealm.realmServiceCore.term.CoreRealm;
import com.viewfunction.docg.coreRealm.realmServiceCore.util.factory.RealmTermFactory;
import com.viewfunction.docg.element.commonComponent.FootprintMessageBar;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;

import org.vaadin.olli.FileDownloadWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownloadARROWFormatCoreRealmEntitiesView extends VerticalLayout {

    private String TEMP_FILES_STORAGE_LOCATION =
            SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.TEMP_FILES_STORAGE_LOCATION);
    private Dialog containerDialog;
    private Button cancelImportButton;
    private Button generateArrowButton;
    private NativeLabel arrowFileName;
    private HorizontalLayout downloaderContainer;
    private String arrowDataFileURI;

    public DownloadARROWFormatCoreRealmEntitiesView(){
        this.setWidth(100, Unit.PERCENTAGE);

        Icon kindIcon = VaadinIcon.CLUSTER.create();
        kindIcon.setSize("12px");
        kindIcon.getStyle().set("padding-right","3px");

        List<FootprintMessageBar.FootprintMessageVO> footprintMessageVOList = new ArrayList<>();
        footprintMessageVOList.add(new FootprintMessageBar.FootprintMessageVO(kindIcon, "Default CoreRealm"));
        FootprintMessageBar entityInfoFootprintMessageBar = new FootprintMessageBar(footprintMessageVOList);
        add(entityInfoFootprintMessageBar);

        generateArrowButton = new Button("生成 ARROW 格式数据文件",new Icon(VaadinIcon.PLAY));
        generateArrowButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        generateArrowButton.setDisableOnClick(true);
        add(generateArrowButton);
        generateArrowButton.addClickListener(new ComponentEventListener<ClickEvent<Button>>() {
            @Override
            public void onComponentEvent(ClickEvent<Button> buttonClickEvent) {
                generateArrowDataFile();
            }
        });

        HorizontalLayout dataFileInfoLayout = new HorizontalLayout();
        dataFileInfoLayout.getStyle().set("padding-top","10px");
        NativeLabel messageContentLabel = new NativeLabel("ARROW 数据文件: ");
        messageContentLabel.addClassNames("text-xs","text-secondary");

        arrowFileName = new NativeLabel();
        arrowFileName.addClassNames("text-xs");
        dataFileInfoLayout.add(messageContentLabel,arrowFileName);
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

        cancelImportButton = new Button("取消或结束导出 ARROW 格式领域模型全量数据");
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

        String dataFileName = PinyinUtil.getPinyin("CoreRealm","")+"_"+System.currentTimeMillis()+"_EXPORT.arrow";
        arrowDataFileURI = fileFolder.getAbsolutePath()+"/"+ dataFileName;

        EntitiesOperationStatistics entitiesOperationStatistics = entitiesExchangeOperator.exportCoreRealmEntitiesToArrow(arrowDataFileURI);
        arrowFileName.setText(dataFileName);

        Button downloadButton = new Button("点击下载 ARROW 数据文件");
        downloadButton.setIcon(VaadinIcon.DOWNLOAD_ALT.create());

        FileDownloadWrapper arrowFileDownloader = new FileDownloadWrapper(dataFileName,new File(TEMP_FILES_STORAGE_LOCATION));
        arrowFileDownloader.wrapComponent(downloadButton);
        downloaderContainer.add(arrowFileDownloader);
        arrowFileDownloader.setFile(new File(arrowDataFileURI));

        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        Div text = new Div(new Text("领域模型 创建 ARROW 数据文件操作成功"));
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
        notificationMessageContainer.add(new Div(new Text("Arrow 数据文件: "+dataFileName)));
        notificationMessageContainer.add(new Div(new Text("导出成功实体数: "+entitiesOperationStatistics.getSuccessItemsCount())));
        notificationMessageContainer.add(new Div(new Text("导出失败实体数: "+entitiesOperationStatistics.getFailItemsCount())));
        notificationMessageContainer.add(new Div(new Text("操作开始时间: "+entitiesOperationStatistics.getStartTime())));
        notificationMessageContainer.add(new Div(new Text("操作结束时间: "+entitiesOperationStatistics.getFinishTime())));
        notification.add(notificationMessageContainer);
        notification.open();
    }

    private void deleteArrowDataFile(){
        if(arrowDataFileURI != null){
            FileUtil.del(arrowDataFileURI);
        }
    }
}
