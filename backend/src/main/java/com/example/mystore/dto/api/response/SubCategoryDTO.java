package com.example.mystore.dto.api.response;

import java.util.List;

/**
 * SubCategoryDTO represents a subcategory within a general category, including its specific categories.
 *
 * Fields:
 * - subCategory: The name of the subcategory.
 * - specificCategories: A list of specific category names under this subcategory.
 *
 * Used to structure deeper levels of product categorization for frontend browsing and filtering.
 */

public class SubCategoryDTO {
    private String subCategory;
    private List<String> specificCategories;

    public SubCategoryDTO() {
    }

    public SubCategoryDTO(String subCategory, List<String> specificCategories) {
        this.subCategory = subCategory;
        this.specificCategories = specificCategories;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public List<String> getSpecificCategories() {
        return specificCategories;
    }

    public void setSpecificCategories(List<String> specificCategories) {
        this.specificCategories = specificCategories;
    }
}