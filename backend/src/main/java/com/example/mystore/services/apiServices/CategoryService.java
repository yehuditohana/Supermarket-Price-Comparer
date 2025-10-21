package com.example.mystore.services.apiServices;

import com.example.mystore.dto.api.response.GeneralCategoryDTO;
import com.example.mystore.dto.api.response.SubCategoryDTO;
import com.example.mystore.utils.CategoryLoader;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    private final CategoryLoader categoryLoader;

    public CategoryService(CategoryLoader categoryLoader) {
        this.categoryLoader = categoryLoader;
    }
    /**
     * Retrieves all general categories along with their subcategories and specific categories.
     *
     * @return a list of GeneralCategoryDTO containing subcategories and specific categories
     */
    public List<GeneralCategoryDTO> getAllCategories() {
        List<GeneralCategoryDTO> generalCategoryDTOList = new ArrayList<>();
// Iterate over each general category entry
        for (Map.Entry<String, Map<String, List<String>>> generalEntry : categoryLoader.getCategoriesMap().entrySet()) {
            String generalCategoryName = generalEntry.getKey();
            Map<String, List<String>> subCategoriesMap = generalEntry.getValue();

            List<SubCategoryDTO> subCategoryDTOList = mapSubCategories(subCategoriesMap);
            GeneralCategoryDTO generalCategoryDTO = new GeneralCategoryDTO(generalCategoryName, subCategoryDTOList);
            generalCategoryDTOList.add(generalCategoryDTO);
        }

        return generalCategoryDTOList;
    }
    /**
     * Maps subcategories and their specific categories into SubCategoryDTO objects.
     *
     * @param subCategoriesMap the map of subcategory names to lists of specific category names
     * @return a list of SubCategoryDTO
     */

    private List<SubCategoryDTO> mapSubCategories(Map<String, List<String>> subCategoriesMap) {
        List<SubCategoryDTO> subCategoryDTOList = new ArrayList<>();
        // Iterate over each subcategory entry

        for (Map.Entry<String, List<String>> subEntry : subCategoriesMap.entrySet()) {
            String subCategoryName = subEntry.getKey();
            List<String> specificCategories = subEntry.getValue();

            SubCategoryDTO subCategoryDTO = new SubCategoryDTO(subCategoryName, specificCategories);
            subCategoryDTOList.add(subCategoryDTO);
        }

        return subCategoryDTOList;
    }
}
