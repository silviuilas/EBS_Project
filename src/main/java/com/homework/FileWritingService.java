package com.homework;

import java.io.*;
import java.util.List;

public class FileWritingService {

    public static void writePubsToFile(List<List<SubPub>> subPubs) {
        try{
            String pubsFilePath = "pubs.txt";
            new FileOutputStream(pubsFilePath).close();
            File file = new File(pubsFilePath);
            FileOutputStream fw = new FileOutputStream(file, true);
            for (List<SubPub> subPub: subPubs) {
                fw.write((subPub.toString() + "\n").getBytes());
            }
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void writeSubsToFile(List<List<SubSub>> subSubs) {
        try{
            String pubsFilePath = "subs.txt";
            new FileOutputStream(pubsFilePath).close();
            File file = new File(pubsFilePath);
            FileOutputStream fw = new FileOutputStream(file, true);
            for (List<SubSub> subSub: subSubs) {
                fw.write((subSub.toString() + "\n").getBytes());
            }
            fw.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
