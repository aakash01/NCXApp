package com.anutanetworks.ncxapp.model;

/**
 * Created by Aakash on 7/6/2015.
 */
public class Capacity {

   private String type;
   private String explanation;
   private String typeName;
   private String units;
   private String unitsName;
   private long total;
   private long reserved;
   private long used;
   private int usedPercentage;
   private String status;
   private String componentType;
   private String componentId;
   private String componentName;
   private String id;

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getExplanation() {
      return explanation;
   }

   public void setExplanation(String explanation) {
      this.explanation = explanation;
   }

   public String getTypeName() {
      return typeName;
   }

   public void setTypeName(String typeName) {
      this.typeName = typeName;
   }

   public String getUnits() {
      return units;
   }

   public void setUnits(String units) {
      this.units = units;
   }

   public String getUnitsName() {
      return unitsName;
   }

   public void setUnitsName(String unitsName) {
      this.unitsName = unitsName;
   }

   public long getTotal() {
      return total;
   }

   public void setTotal(long total) {
      this.total = total;
   }

   public long getReserved() {
      return reserved;
   }

   public void setReserved(long reserved) {
      this.reserved = reserved;
   }

   public long getUsed() {
      return used;
   }

   public void setUsed(long used) {
      this.used = used;
   }

   public int getUsedPercentage() {
      return usedPercentage;
   }

   public void setUsedPercentage(int usedPercentage) {
      this.usedPercentage = usedPercentage;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getComponentType() {
      return componentType;
   }

   public void setComponentType(String componentType) {
      this.componentType = componentType;
   }

   public String getComponentId() {
      return componentId;
   }

   public void setComponentId(String componentId) {
      this.componentId = componentId;
   }

   public String getComponentName() {
      return componentName;
   }

   public void setComponentName(String componentName) {
      this.componentName = componentName;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }
}
