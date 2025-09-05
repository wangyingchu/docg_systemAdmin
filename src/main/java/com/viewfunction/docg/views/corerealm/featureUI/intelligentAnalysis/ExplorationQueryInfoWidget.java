package com.viewfunction.docg.views.corerealm.featureUI.intelligentAnalysis;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import com.viewfunction.docg.element.commonComponent.ThirdLevelIconTitle;

public class ExplorationQueryInfoWidget extends VerticalLayout {

    public ExplorationQueryInfoWidget(String explorationQuery){
        ThirdLevelIconTitle infoTitle = new ThirdLevelIconTitle(new Icon(VaadinIcon.ALIGN_LEFT),"探索查询语句");
        infoTitle.setWidthFull();
        infoTitle.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-10pct)");
        add(infoTitle);

        Scroller insightContentScroller = new Scroller();
        insightContentScroller.setWidthFull();
        insightContentScroller.setHeight(230, Unit.PIXELS);
        add(insightContentScroller);

        Span explorationQuerySpan = new Span(explorationQuery);
        explorationQuerySpan.getElement().getThemeList().add("badge contrast");
        insightContentScroller.setContent(explorationQuerySpan);
    }
}
