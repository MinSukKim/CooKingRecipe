package com.example.girln.recipeapp.models;

public enum IngredientUnit {
    GRAM("gr"),KILOGRAM("Kg"),LITER("l"),
    MILLILITER("ml"),TEASPOON("tsp"),
    TABLESPOON("tbsp"),CUP("cup"),PINCH("pinch");
    private String text;
    IngredientUnit(String text){
        this.text=text;
    }
    @Override
    public String toString(){
        return text;
    }
}
