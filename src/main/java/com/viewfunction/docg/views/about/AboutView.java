package com.viewfunction.docg.views.about;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.server.streams.DownloadHandler;
import com.vaadin.flow.server.streams.DownloadResponse;
import com.vaadin.flow.shared.Registration;
import com.viewfunction.docg.util.config.SystemAdminCfgPropertiesHandler;
import com.viewfunction.docg.views.MainLayout;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

//@PageTitle("数海云图 - 关于 [ About ]")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends Div implements HasDynamicTitle {

    private Registration listener;
    int browserWidth;
    int browserHeight;

    public AboutView() {
        addClassName("about-view");
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        getUI().ifPresent(ui -> listener = ui.getPage().addBrowserWindowResizeListener(event -> {
            browserWidth = event.getWidth();
            browserHeight = event.getHeight();
        }));
        // Adjust size according to initial width of the screen
        getUI().ifPresent(ui -> ui.getPage().retrieveExtendedClientDetails(receiver -> {
            browserWidth = receiver.getBodyClientWidth();
            browserHeight = receiver.getBodyClientHeight();
        }));

        IFrame pdfViewer = new IFrame();
        // 使用 DownloadHandler 提供数据，这是推荐的新方式
        pdfViewer.setSrc(DownloadHandler.fromInputStream(event -> {
            try {
                byte[] reportBytes = readResourceBytes("documents/about.pdf");
                return new DownloadResponse(
                        new ByteArrayInputStream(reportBytes),
                        "report.pdf",
                        "application/pdf",
                        reportBytes.length
                );
            } catch (Exception e) {
                return DownloadResponse.error(500);
            }
        }));

        //IFrame 不会自动撑开高度，必须设置固定高度
        pdfViewer.setWidth(browserWidth-10, Unit.PIXELS);
        pdfViewer.setHeight(browserHeight-60, Unit.PIXELS);
        add(pdfViewer);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Listener needs to be eventually removed in order to avoid resource leak
        listener.remove();
        super.onDetach(detachEvent);
    }

    private final String SYSTEM_TITLE_PREFIX = SystemAdminCfgPropertiesHandler.getPropertyValue(SystemAdminCfgPropertiesHandler.SYSTEM_TITLE_PREFIX);
    @Override
    public String getPageTitle() {
        if(SYSTEM_TITLE_PREFIX != null){
            return SYSTEM_TITLE_PREFIX+" - 关于 [ About ]";
        }else{
            return "数海云图 - 关于 [ About ]";
        }
    }

    private byte[] readResourceBytes(String resourceName) throws IOException {
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream(resourceName)) {
            if (is == null) {
                throw new IllegalArgumentException("资源不存在: " + resourceName);
            }
            return is.readAllBytes();
        }
    }
}
