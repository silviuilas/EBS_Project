package com.example.broker.helper;

import java.util.Date;
import java.util.Optional;

public interface DateParser {
    Optional<Date> parse(String dateStr);
}