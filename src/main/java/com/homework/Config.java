package com.homework;

import java.util.*;

public class Config {
    Integer nrSub;
    Integer nrPub;


    public Config(Integer nrSub, Integer nrPub) {
        this.nrSub = nrSub;
        this.nrPub = nrPub;
    }

    public List<Field> fields = new ArrayList<>();

    public List<List<SubPub>> generatePub() {
        Random random = new Random();
        List<List<SubPub>> generatedPub = new ArrayList<>();

        for (int i = 0; i < nrPub; i++) {
            List<SubPub> subPubList = new ArrayList<>();
            for (Field field : fields) {
                Object val = getObject(random, field);
                SubPub subPub = new SubPub(field.name, val);
                subPubList.add(subPub);
            }
            generatedPub.add(subPubList);
        }
        return generatedPub;
    }

    public List<List<SubSub>> generateSub() {
        Random random = new Random();
        List<List<SubSub>> generatedSub = new ArrayList<>();

        for (int i = 0; i < nrSub; i++) {
            generatedSub.add(new ArrayList<>());
        }

        for (Field field : fields) {
            Collections.shuffle(generatedSub);
            int size = (int) (field.pond * nrSub);

            List<List<SubSub>> subList = generatedSub.subList(0, size);

            for (List<SubSub> subSubs : subList) {
                Object val = getObject(random, field);
                SubSub subSub = new SubSub(field.name, val);
                subSubs.add(subSub);
            }

            List<String> keys = new ArrayList<>(field.getOp_pond().keySet());
            List<Double> valsSum = new ArrayList<>();
            Double sum = 0.0;
            for (String key : keys) {
                sum += field.getOp_pond().get(key);
                valsSum.add(sum);
            }

            int k = 0;
            for (int i = 0; i < subList.size(); i++) {
                double percent = (double) (i + 1) / subList.size();
                if (percent > valsSum.get(k))
                    k += 1;
                List<SubSub> subSubs = subList.get(i);
                subSubs.get(subSubs.size() - 1).op = keys.get(k);
            }

        }

        return generatedSub;
    }


    private Object getObject(Random random, Field field) {
        Object val;
        if (field.vals != null) {
            val = field.vals.get(random.nextInt(field.vals.size()));
        } else {
            double dif = field.rangeSup - field.rangeInf;
            val = (random.nextDouble() * dif) + field.rangeInf;
        }
        return val;
    }
}
