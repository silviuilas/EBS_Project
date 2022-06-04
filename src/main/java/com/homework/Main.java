package com.homework;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        Field company = getCompany();
        Field value = getValue();
        Field drop = getDrop();
        Field variation = getVariation();
        Field date = getDate();

        Config config = new Config(10, 10);

        config.fields.add(company);
        config.fields.add(value);
        config.fields.add(drop);
        config.fields.add(variation);
        config.fields.add(date);

/*        System.out.println(config.generatePub());*/
        FileWritingService.writePubsToFile(config.generatePub());
        FileWritingService.writeSubsToFile(config.generateSub());
/*        for(List<SubSub> subSubs : config.generateSub())
            System.out.println(subSubs);*/
    }

    private static Field getValue() {
        Field value = new Field();
        value.setName("Value");
        value.setPond(0.0);
        value.setRangeInf(0.0);
        value.setRangeSup(100.0);
        Map<String, Double> opPond = new HashMap<>();
        opPond.put("=", 0.25);
        opPond.put(">", 0.50);
        opPond.put("<", 0.25);

        value.setOpPond(opPond);
        return value;
    }

    private static Field getDrop() {
        Field value = new Field();
        value.setName("Drop");
        value.setPond(0.2);
        value.setRangeInf(0.0);
        value.setRangeSup(20.0);
        Map<String, Double> opPond = new HashMap<>();
        opPond.put("=", 0.25);
        opPond.put(">", 0.25);
        opPond.put("<", 0.50);

        value.setOpPond(opPond);
        return value;
    }

    private static Field getVariation() {
        Field value = new Field();
        value.setName("Variation");
        value.setPond(0.3);
        value.setRangeInf(0.0);
        value.setRangeSup(1.0);
        Map<String, Double> opPond = new HashMap<>();
        opPond.put("=", 0.33);
        opPond.put(">=", 0.33);
        opPond.put("<=", 0.34);

        value.setOpPond(opPond);
        return value;
    }

    private static Field getCompany() {
        Field company = new Field();
        company.setName("Company");
        company.setPond(0.5);
        List<Object> companies = new ArrayList<>();
        companies.add("Google");
        companies.add("Facebook");
        companies.add("Amazon");
        companies.add("Netflix");
        company.setVals(companies);

        Map<String, Double> opPond = new HashMap<>();
        opPond.put("=", 0.5);
        opPond.put("!=", 0.25);
        opPond.put("<", 0.25);
        company.setOpPond(opPond);
        return company;
    }

    private static Field getDate() {
        Field datesField = new Field();
        datesField.setName("Date");
        datesField.setPond(0.0);
        String DEFAULT_PATTERN = "yyyy-MM-dd";

        DateFormat formatter = new SimpleDateFormat(DEFAULT_PATTERN);
        try {
            List<Object> dates = new ArrayList<>();
            dates.add(formatter.parse("2020-02-10"));
            dates.add(formatter.parse("2020-05-15"));
            dates.add(formatter.parse("2020-08-20"));
            dates.add(formatter.parse("2020-10-20"));
            dates.add(formatter.parse("2020-09-20"));
            dates.add(formatter.parse("2020-11-10"));
            datesField.setVals(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Map<String, Double> opPond = new HashMap<>();
        opPond.put("=", 0.5);
        opPond.put(">=", 0.25);
        opPond.put("<", 0.25);
        datesField.setOpPond(opPond);
        return datesField;
    }
}
