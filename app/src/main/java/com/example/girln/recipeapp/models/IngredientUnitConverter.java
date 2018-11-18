package com.example.girln.recipeapp.models;

public class IngredientUnitConverter {
    public static IngredientUnit convert(String unit)
    {
        if(unit.equals(IngredientUnit.CUP.toString()))
            return IngredientUnit.CUP;
        else if(unit.equals(IngredientUnit.GRAM.toString()))
            return IngredientUnit.GRAM;
        else if(unit.equals(IngredientUnit.KILOGRAM.toString()))
            return IngredientUnit.KILOGRAM;
        else if(unit.equals(IngredientUnit.LITER.toString()))
            return IngredientUnit.LITER;
        else if(unit.equals(IngredientUnit.MILLILITER.toString()))
            return IngredientUnit.MILLILITER;
        else if(unit.equals(IngredientUnit.PINCH.toString()))
            return IngredientUnit.PINCH;
        else if(unit.equals(IngredientUnit.TABLESPOON.toString()))
            return IngredientUnit.TABLESPOON;
        else if(unit.equals(IngredientUnit.TEASPOON.toString()))
            return IngredientUnit.TEASPOON;
        else if(unit.equals(IngredientUnit.UNIT.toString()))
            return IngredientUnit.UNIT;
        else return null;
    }
}
