package com.example.broker.helper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

public class DateParserUsingDateFormat implements DateParser {
    private String dateFormat;

    public DateParserUsingDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    @Override
    public Optional<Date> parse(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            return Optional.ofNullable(sdf.parse(dateStr));
        } catch (ParseException e) {
            return Optional.empty();
        }
    }
}
