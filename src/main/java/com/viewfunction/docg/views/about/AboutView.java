package com.viewfunction.docg.views.about;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import com.viewfunction.docg.element.externalTechFeature.rgbColorPicker.RgbaColorPicker;
import com.viewfunction.docg.views.MainLayout;

@PageTitle("数海云图 - 关于 [ About ]")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends Div {

    public AboutView() {
        addClassName("about-view");
        add(new Text("Content placeholder"));


        RgbaColorPicker rgbaColorPicker = new RgbaColorPicker();
        add(rgbaColorPicker);
    }

}
