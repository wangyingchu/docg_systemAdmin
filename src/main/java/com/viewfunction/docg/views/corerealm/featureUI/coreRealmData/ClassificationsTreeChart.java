package com.viewfunction.docg.views.corerealm.featureUI.coreRealmData;

import com.storedobject.chart.*;

import java.util.Random;

public class ClassificationsTreeChart extends SOChart{

    public ClassificationsTreeChart(){

        this.setSize("360px", "400px");
        // Tree chart
        // (By default it assumes circular shape. Otherwise, we can set orientation)
        // All values are randomly generated
        TreeChart tc = new TreeChart();
        TreeData td = new TreeData("Root", 1000);
        tc.setTreeData(td);
        Random r = new Random();
        for(int i = 1; i < 21; i++) {
            td.add(new TreeData("Node " + i, r.nextInt(500)));
        }
        TreeData td1 = td.get(13);
        td = td.get(9);
        for(int i = 50; i < 56; i++) {
            td.add(new TreeData("Node " + i, r.nextInt(500)));
        }
        for(int i = 30; i < 34; i++) {
            td1.add(new TreeData("Node " + i, r.nextInt(500)));
        }
        tc.setName("Classification Root");

        // Add to the chart display area with a simple title
        //add(tc, new Title("A Circular Tree Chart"));
        add(tc);




    }
}
