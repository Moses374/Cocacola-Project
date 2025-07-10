package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Drinks {
    private String name;
    private String imgSrc;
    private String color;
    private String category;
    private String description;
    private Map<String, Double> sizes = new HashMap<>();
    private int drinkId; // DB primary key
    private String brand;
    private double unitPrice;
    
    public Drinks() {
    }
    
    public Drinks(String name, String imgSrc, String color, String category, String description) {
        this.name = name;
        this.imgSrc = imgSrc;
        this.color = color;
        this.category = category;
        this.description = description;
    }
    
    // Constructor for DB mapping
    public Drinks(int drinkId, String name, String brand, double unitPrice) {
        this.drinkId = drinkId;
        this.name = name;
        this.brand = brand;
        this.unitPrice = unitPrice;
    }
    
    // Constructor for DB insert
    public Drinks(String name, String brand, double unitPrice) {
        this.name = name;
        this.brand = brand;
        this.unitPrice = unitPrice;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getImgSrc() {
        return imgSrc;
    }
    
    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Map<String, Double> getSizes() {
        return sizes;
    }
    
    public void setSizes(Map<String, Double> sizes) {
        this.sizes = sizes;
    }
    
    public void addSize(String size, double price) {
        sizes.put(size, price);
    }
    
    public double getPrice(String size) {
        return sizes.getOrDefault(size, 0.0);
    }
    
    public int getDrinkId() { 
        return drinkId; 
    }
    
    public void setDrinkId(int drinkId) { 
        this.drinkId = drinkId; 
    }
    
    public String getBrand() { 
        return brand; 
    }
    
    public void setBrand(String brand) { 
        this.brand = brand; 
    }
    
    public double getUnitPrice() { 
        return unitPrice; 
    }
    
    public void setUnitPrice(double unitPrice) { 
        this.unitPrice = unitPrice; 
    }
    
    public Set<String> getAvailableSizes() {
        return sizes.keySet();
    }
    
    public boolean hasSize(String size) {
        return sizes.containsKey(size);
    }
    
    public double getBasePrice() {
        return sizes.values().stream()
                .min(Double::compareTo)
                .orElse(0.0);
    }
    
    public double getMaxPrice() {
        return sizes.values().stream()
                .max(Double::compareTo)
                .orElse(0.0);
    }
    
    public String getPriceRange() {
        if (sizes.isEmpty()) return "N/A";
        if (sizes.size() == 1) return String.format("KSh %.2f", getBasePrice());
        return String.format("KSh %.2f - %.2f", getBasePrice(), getMaxPrice());
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Drinks drink = (Drinks) o;
        return name != null ? name.equals(drink.name) : drink.name == null;
    }
    
    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
    
    @Override
    public String toString() {
        return "Drink{" +
                "name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", sizes=" + sizes +
                '}';
    }
}