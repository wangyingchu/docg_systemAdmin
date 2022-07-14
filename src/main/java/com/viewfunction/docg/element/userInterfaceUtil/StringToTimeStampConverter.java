package com.viewfunction.docg.element.userInterfaceUtil;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.util.Date;

public class StringToTimeStampConverter implements Converter<String, Date> {

    @Override
    public Result<Date> convertToModel(String s, ValueContext valueContext) {
        return null;
    }

    @Override
    public String convertToPresentation(Date date, ValueContext valueContext) {
        return null;
    }
}
