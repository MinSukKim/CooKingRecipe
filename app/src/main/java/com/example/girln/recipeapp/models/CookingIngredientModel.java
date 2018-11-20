package com.example.girln.recipeapp.models;

import static com.example.girln.recipeapp.models.IngredientUnitConverter.convert;

public class CookingIngredientModel {
    private String ingredientName;
    private IngredientUnit unit;
    private double amount;

    public CookingIngredientModel() {
    }

    public CookingIngredientModel(CookingIngredientModel i) {
        ingredientName=i.getIngredientName();
        unit=i.getUnit();
        amount=i.getAmount();
    }

    public CookingIngredientModel(String ingredientName, String ingredientUnit, String ingredientAmount) {
        this.ingredientName=ingredientName;
        this.unit=convert(ingredientUnit);
        amount=Double.parseDouble(ingredientAmount);
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public IngredientUnit getUnit() {
        return unit;
    }

    public void setUnit(IngredientUnit unit) {
        this.unit = unit;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
