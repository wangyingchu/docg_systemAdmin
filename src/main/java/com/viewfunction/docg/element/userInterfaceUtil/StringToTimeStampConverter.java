package com.viewfunction.docg.element.userInterfaceUtil;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class StringToTimeStampConverter implements Converter<String, Date> {

    @Override
    public Result<Date> convertToModel(String s, ValueContext valueContext) {
        if (s == null) {
            return Result.ok(null);
        } else {
            try{
                long timestampLongValue = Long.parseLong(s);
                Date timeStampDate = new Date(timestampLongValue);
                return Result.ok(timeStampDate);
            }catch(NumberFormatException e){
                return Result.error("Could not convert '" + s);
            }
        }
    }

    @Override
    public String convertToPresentation(Date date, ValueContext valueContext) {
        return date == null ? null : this.getFormat((Locale)valueContext.getLocale().orElse(null)).format(date);
    }

    protected DateFormat getFormat(Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }

        DateFormat format = DateFormat.getDateTimeInstance(2, 2, locale);
        format.setLenient(false);
        return format;
    }
}
