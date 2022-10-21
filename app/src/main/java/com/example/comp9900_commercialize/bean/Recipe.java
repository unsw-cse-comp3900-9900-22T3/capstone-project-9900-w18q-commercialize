package com.example.comp9900_commercialize.bean;

import java.io.Serializable;
import java.util.List;

public class Recipe implements Serializable {
    public String recipeCover;
    public String recipeContributor;
    public String recipeName;
    public String recipeDescription;
    public List<Ingredient> recipeIngredientList;
    public String recipeDifficulty;
    public String recipeScheduledTime;
    public List<Step> recipeStepList;
    public int recipeLikesNum;
    public int recipeCommentsNum;
    public String recipePublishTime;

    public Recipe(){

    }

    public Recipe(String recipeCover, String recipeContributor, String recipeName, String recipeDescription, List<Ingredient> recipeIngredientList, String recipeDifficulty, String recipeScheduledTime, List<Step> recipeStepList, int recipeLikesNum, int recipeCommentsNum, String recipePublishTime) {
        this.recipeCover = recipeCover;
        this.recipeContributor = recipeContributor;
        this.recipeName = recipeName;
        this.recipeDescription = recipeDescription;
        this.recipeIngredientList = recipeIngredientList;
        this.recipeDifficulty = recipeDifficulty;
        this.recipeScheduledTime = recipeScheduledTime;
        this.recipeStepList = recipeStepList;
        this.recipeLikesNum = recipeLikesNum;
        this.recipeCommentsNum = recipeCommentsNum;
        this.recipePublishTime = recipePublishTime;
    }

    public String getRecipeCover() {
        return recipeCover;
    }

    public void setRecipeCover(String recipeCover) {
        this.recipeCover = recipeCover;
    }

    public String getRecipeContributor() {
        return recipeContributor;
    }

    public void setRecipeContributor(String recipeContributor) {
        this.recipeContributor = recipeContributor;
    }

    public int getRecipeLikesNum() {
        return recipeLikesNum;
    }

    public void setRecipeLikesNum(int recipeLikesNum) {
        this.recipeLikesNum = recipeLikesNum;
    }

    public int getRecipeCommentsNum() {
        return recipeCommentsNum;
    }

    public void setRecipeCommentsNum(int recipeCommentsNum) {
        this.recipeCommentsNum = recipeCommentsNum;
    }

    public String getRecipePublishTime() {
        return recipePublishTime;
    }

    public void setRecipePublishTime(String recipePublishTime) {
        this.recipePublishTime = recipePublishTime;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public List<Ingredient> getRecipeIngredientList() {
        return recipeIngredientList;
    }

    public void setRecipeIngredientList(List<Ingredient> recipeIngredientList) {
        this.recipeIngredientList = recipeIngredientList;
    }

    public String getRecipeDifficulty() {
        return recipeDifficulty;
    }

    public void setRecipeDifficulty(String recipeDifficulty) {
        this.recipeDifficulty = recipeDifficulty;
    }

    public String getRecipeScheduledTime() {
        return recipeScheduledTime;
    }

    public void setRecipeScheduledTime(String recipeScheduledTime) {
        this.recipeScheduledTime = recipeScheduledTime;
    }

    public List<Step> getRecipeStepList() {
        return recipeStepList;
    }

    public void setRecipeStepList(List<Step> recipeStepList) {
        this.recipeStepList = recipeStepList;
    }
}
