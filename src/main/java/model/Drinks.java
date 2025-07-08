package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Drinks {
    private String name;
    private String imgSrc;
    private String color;
    private Map<String, Double> sizes; // Size -> Price mapping
    private String description;
    private String category;
    private int drinkId; // DB primary key
    private String brand;
    private double unitPrice;

    public Drinks() {
        this.sizes = new HashMap<>();
    }

    // Constructor with parameters for easier object creation
    public Drinks(String name, String imgSrc, String color, String category, String description) {
        this();
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
        this.sizes = new HashMap<>();
    }

    // Constructor for DB insert
    public Drinks(String name, String brand, double unitPrice) {
        this.name = name;
        this.brand = brand;
        this.unitPrice = unitPrice;
        this.sizes = new HashMap<>();
    }

    // Getters and Setters
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

    public Map<String, Double> getSizes() {
        return new HashMap<>(sizes); // Return defensive copy
    }

    public void setSizes(Map<String, Double> sizes) {
        this.sizes = new HashMap<>(sizes);
    }

    public void addSize(String size, Double price) {
        if (size != null && price != null && price > 0) {
            this.sizes.put(size, price);
        }
    }

    public Double getPrice(String size) {
        return sizes.getOrDefault(size, 0.0);
    }

    public Double getBasePrice() {
        return sizes.values().stream()
                .min(Double::compareTo)
                .orElse(0.0);
    }

    public Double getMaxPrice() {
        return sizes.values().stream()
                .max(Double::compareTo)
                .orElse(0.0);
    }

    public Set<String> getAvailableSizes() {
        return sizes.keySet();
    }

    public boolean hasSize(String size) {
        return sizes.containsKey(size);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getDrinkId() { return drinkId; }
    public void setDrinkId(int drinkId) { this.drinkId = drinkId; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    // Utility methods
    public boolean isAvailable() {
        return !sizes.isEmpty();
    }

    public String getPriceRange() {
        if (sizes.isEmpty()) return "N/A";
        if (sizes.size() == 1) return String.format("KSh %.2f", getBasePrice());
        return String.format("KSh %.2f - %.2f", getBasePrice(), getMaxPrice());
    }

    @Override
    public String toString() {
        return name + " (" + category + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Drinks drinks = (Drinks) obj;
        return Objects.equals(name, drinks.name) &&
                Objects.equals(category, drinks.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category);
    }

    // Builder pattern for easier object creation
    public static class Builder {
        private Drinks drink;

        public Builder(String name) {
            drink = new Drinks();
            drink.name = name;
        }

        public Builder imgSrc(String imgSrc) {
            drink.imgSrc = imgSrc;
            return this;
        }

        public Builder color(String color) {
            drink.color = color;
            return this;
        }

        public Builder category(String category) {
            drink.category = category;
            return this;
        }

        public Builder description(String description) {
            drink.description = description;
            return this;
        }

        public Builder addSize(String size, Double price) {
            drink.addSize(size, price);
            return this;
        }

        public Drinks build() {
            return drink;
        }
    }
}