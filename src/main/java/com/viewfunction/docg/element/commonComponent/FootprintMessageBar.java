package com.viewfunction.docg.element.commonComponent;

import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.List;

public class FootprintMessageBar extends HorizontalLayout {

    public static class FootprintMessageVO{
        private Icon messageIcon;
        private String messageContent;
        public FootprintMessageVO(Icon messageIcon,String messageContent){
            this.messageIcon = messageIcon;
            this.messageContent = messageContent;
        }
    }

    public FootprintMessageBar(List<FootprintMessageVO> messageFootprints){
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(false);

        Icon footPrintStartIcon = VaadinIcon.TERMINAL.create();
        footPrintStartIcon.setSize("22px");
        footPrintStartIcon.getStyle().set("padding-right","8px").set("color","var(--lumo-contrast-50pct)");
        this.add(footPrintStartIcon);

        if(messageFootprints != null){
            for(int i = 0; i< messageFootprints.size(); i++){
                FootprintMessageVO currentFootprintMessageVO = messageFootprints.get(i);
                this.add(currentFootprintMessageVO.messageIcon);
                this.setVerticalComponentAlignment(Alignment.CENTER,currentFootprintMessageVO.messageIcon);
                Label messageContentLabel = new Label(currentFootprintMessageVO.messageContent);
                this.add(messageContentLabel);
                this.setVerticalComponentAlignment(Alignment.CENTER,messageContentLabel);
                if(i<messageFootprints.size()-1){
                    Icon divIcon = VaadinIcon.ITALIC.create();
                    divIcon.setSize("12px");
                    divIcon.getStyle().set("padding-left","5px");
                    this.add(divIcon);
                    this.setVerticalComponentAlignment(Alignment.CENTER,divIcon);
                }
            }
        }
    }
}
