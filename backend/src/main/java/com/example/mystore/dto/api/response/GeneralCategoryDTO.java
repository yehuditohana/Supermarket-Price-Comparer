package com.example.mystore.dto.api.response;

import java.util.List;
/**
 * GeneralCategoryDTO represents a general category and its associated subcategories.
 *
 * Fields:
 * - generalCategory: The name of the general category
 * - subCategories: A list of SubCategoryDTOs representing subcategories under this general category.
 *
 * Used to structure the category hierarchy for items search and navigation on the frontend.
 */
public class GeneralCategoryDTO {
    private String generalCategory;
    private List<SubCategoryDTO> subCategories;

    public GeneralCategoryDTO() {
    }

    public GeneralCategoryDTO(String generalCategory, List<SubCategoryDTO> subCategories) {
        this.generalCategory = generalCategory;
        this.subCategories = subCategories;
    }

    public String getGeneralCategory() {
        return generalCategory;
    }

    public void setGeneralCategory(String generalCategory) {
        this.generalCategory = generalCategory;
    }

    public List<SubCategoryDTO> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategoryDTO> subCategories) {
        this.subCategories = subCategories;
    }
}