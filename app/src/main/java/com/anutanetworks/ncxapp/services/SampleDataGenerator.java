package com.anutanetworks.ncxapp.services;

import com.anutanetworks.ncxapp.model.Alarm;
import com.anutanetworks.ncxapp.model.AlarmsSummary;
import com.anutanetworks.ncxapp.model.Approval;
import com.anutanetworks.ncxapp.model.Capacity;
import com.anutanetworks.ncxapp.model.TasksSummary;

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

    public static String getRandomString(int min, int max){
        StringBuffer buffer = new StringBuffer();
        String characters = "abcdefghijklmnopqrstuvwxyz";
        String capital = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int charactersLength = characters.length();

        for (int i = 0; i < randInt(min,max); i++) {
            double index = Math.random() * charactersLength;
            buffer.append(i == 0 ? capital.charAt((int) index) : characters.charAt((int) index));
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
        for(int i=0;i<15;i++){
            alarm = new Alarm();
            int j = randInt(0,4);
            int k = randInt(0,1);
            alarm.setAcknowledged(isAck[k]);
            alarm.setAlarmSpecType(alarmSpecType[j]);
            alarm.setSeverity(alarmSeverityTypes[j]);
            alarm.setComponentType(componentType[j]);
            alarm.setAlarmState(alarmState[k]);
            alarm.setComponent(getRandomString(5,15));
            alarm.setDescription(getRandomString(5, 30));
            alarm.setMessage(getRandomString(5, 30));
            alarms.add(alarm);
        }
        return alarms;
    }

    public static ArrayList<Capacity> getDeviceCapacities(){
        ArrayList<Capacity> capacities = new ArrayList<>();

        for(int i=0;i<randInt(2,20);i++){
            Capacity c = new Capacity();
            c.setUsed(randInt(1, 100));
            c.setComponentName(getRandomString(5, 10));
            capacities.add(c);
        }
        return capacities;
    }

    public static ArrayList<Capacity> getRiskData(){
        ArrayList<Capacity> capacities = new ArrayList<>();

        for(int i=0;i<5;i++){
            Capacity c = new Capacity();
            c.setUsed(randInt(1, 100));
            c.setComponentName(getRandomString(5, 10));
            capacities.add(c);
        }
        return capacities;
    }

    public static ArrayList<Approval> getApprovalData(){
        String[] approvalOriginator = {"admin","user"};
        String[] approvalStatus = {"Approved","Rejected"};
        String[] componentType={"POD","POLICY","DEVICE", "RESOURCE_POOL","INTERFACE"};
        String[] alarmState = {"ACTIVE","CLEARED"};
        boolean[] isAck = {true,false};
        ArrayList<Approval> approvals = new ArrayList<>();
        Approval approval;
        for(int i=0;i<30;i++){
            approval = new Approval();
            int j = randInt(0,1);
            int k = randInt(0,1);
            approval.setOriginator(approvalOriginator[k]);
            approval.setApproved(approvalStatus[j]);
            String desc = getRandomString(5,15)+'\n'+getRandomString(15,30);
            approval.setDescription(desc);
            approvals.add(approval);
        }
        return approvals;
    }
    public static ArrayList<Capacity> getServiceSummary(){
        ArrayList<Capacity> capacities = new ArrayList<>();

        for(int i=0;i<randInt(2,20);i++){
            Capacity c = new Capacity();
            c.setUsed(randInt(1, 10));
            c.setComponentName(getRandomString(5, 10));
            capacities.add(c);
        }
        return capacities;
    }
     public static TasksSummary getSystemSummary()
     {
         TasksSummary summary = new TasksSummary();
         summary.setTasksError(randInt(0, 30));
         summary.setTasksRunning(randInt(0, 35));
         summary.setTasksWaiting(randInt(0, 30));
         summary.setTenants(randInt(0, 30));
         summary.setVdcs(randInt(0, 30));
         summary.setVms(randInt(0,30));
         return summary;
     }

    public static AlarmsSummary getAlarmSummary() {
        AlarmsSummary alarmsSummary = new AlarmsSummary();
        alarmsSummary.setCritical(randInt(0,30));
        alarmsSummary.setInfo(randInt(0, 30));
        alarmsSummary.setMajor(randInt(0, 30));
        alarmsSummary.setMinor(randInt(0, 30));
        alarmsSummary.setWarning(randInt(0,30));
        return alarmsSummary;
    }
}
