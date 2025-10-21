package com.example.mystore.api;

import com.example.mystore.services.apiServices.CategoryService;
import com.example.mystore.dto.api.response.GeneralCategoryDTO;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * CategoryResource handles API requests related to retrieving available item categories.
 *
 * Base path: /categories
 * Produces: application/json
 */
@Component
@Path("/categories")
public class CategoryResource {

    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

/**
 * Retrieves a hierarchical list of all item categories.
 *
 * Each general category contains a list of subcategories,
 * and each subcategory contains a list of specific categories.
 *
 * Structure:
 * - GeneralCategoryDTO
 *    - List<SubCategoryDTO>
 *        - List<String> of SpecificCategory
 */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<GeneralCategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }
}