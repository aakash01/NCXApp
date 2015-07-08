package com.anutanetworks.ncxapp.model;

/**
 * Created by Aakash on 7/7/2015.
 */
public class TasksSummary {

   private int tenants;
   private int vdcs;
   private int vms;
   private int tasksRunning;
   private int tasksWaiting;
   private int tasksError;

   public int getTenants() {
      return tenants;
   }

   public void setTenants(int tenants) {
      this.tenants = tenants;
   }

   public int getVdcs() {
      return vdcs;
   }

   public void setVdcs(int vdcs) {
      this.vdcs = vdcs;
   }

   public int getVms() {
      return vms;
   }

   public void setVms(int vms) {
      this.vms = vms;
   }

   public int getTasksRunning() {
      return tasksRunning;
   }

   public void setTasksRunning(int tasksRunning) {
      this.tasksRunning = tasksRunning;
   }

   public int getTasksWaiting() {
      return tasksWaiting;
   }

   public void setTasksWaiting(int tasksWaiting) {
      this.tasksWaiting = tasksWaiting;
   }

   public int getTasksError() {
      return tasksError;
   }

   public void setTasksError(int tasksError) {
      this.tasksError = tasksError;
   }
}
