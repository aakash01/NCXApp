package com.anutanetworks.ncxapp.services;

import com.anutanetworks.ncxapp.model.Alarm;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Aakash on 7/14/2015.
 */
public class SampleDataGenerator {


    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static String getRandomString(int length){
        StringBuffer buffer = new StringBuffer();
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int charactersLength = characters.length();

        for (int i = 0; i < randInt(0,length); i++) {
            double index = Math.random() * charactersLength;
            buffer.append(characters.charAt((int) index));
        }
        return buffer.toString();
    }

    public static ArrayList<Alarm> getAlarmData(){
        String[] alarmSeverityTypes = {"CRITICAL","MAJOR","MINOR","INFO","WARNING"};
        String[] alarmSpecType = {"INVALID_POD", "SERVICE_UNAVAILABLE","DEVICE_OFFLINE","INVALID_RESOURCE_POOL","INTERFACE_DOWN"};
        String[] componentType={"POD","POLICY","DEVICE", "RESOURCE_POOL","INTERFACE"};
        String[] alarmState = {"ACTIVE","CLEARED"};
        boolean[] isAck = {true,false};
        ArrayList<Alarm> alarms = new ArrayList<>();
        Alarm alarm;
        for(int i=0;i<30;i++){
            alarm = new Alarm();
            int j = randInt(0,4);
            int k = randInt(0,1);
            alarm.setAcknowledged(isAck[k]);
            alarm.setAlarmSpecType(alarmSpecType[j]);
            alarm.setSeverity(alarmSeverityTypes[j]);
            alarm.setComponentType(componentType[j]);
            alarm.setAlarmState(alarmState[k]);
            alarm.setComponent(getRandomString(15));
            alarm.setDescription(getRandomString(30));
            alarm.setMessage(getRandomString(30));
            alarms.add(alarm);
        }
        return alarms;
    }

}
