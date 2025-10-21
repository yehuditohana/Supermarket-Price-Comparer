import React, { useState, useEffect, useRef } from "react";
import { fetchCategories } from "../api/categoryAPI";
import "./HoverTopCategoryBar.css";

const HoverTopCategoryBar = ({ onSelectCategory, onCategoriesLoaded  }) => {
  const [categories, setCategories] = useState([]);// all category data
  const [hoveredGeneral, setHoveredGeneral] = useState(null);// currently hovered main category
  const timeoutRef = useRef(null); // for delayed collapse

  useEffect(() => {
      // load categories once on mount
    const fetchAll = async () => {
      try {
        const data = await fetchCategories();
        setCategories(data);
        if (onCategoriesLoaded) {
          onCategoriesLoaded(data); 
        }
      } catch (error) {
        console.error(" Error fetching categories:", error);
      }
    };
    fetchAll();
  }, []);
// when user enters a main category, show its sub-sections immediately
  const handleGeneralMouseEnter = (generalCategory) => {
    clearTimeout(timeoutRef.current);
    setHoveredGeneral(generalCategory);
  };
 
  const handleMouseLeave = () => {
    timeoutRef.current = setTimeout(() => {
      setHoveredGeneral(null);
    }, 300); 
  };
//subcategory was selected
  const handleSubCategoryClick = (generalCategory, subCategory) => {
    onSelectCategory({ generalCategory, subCategory });
  };
  //specific category was selected
  const handleSpecificClick = (generalCategory, subCategory, specificCategory) => {
    onSelectCategory({ generalCategory, subCategory, specificCategory });
  };

  return (
    <div className="hover-category-bar" onMouseLeave={handleMouseLeave}>
     {/* main categories row */}
      <div className="main-categories">
        {categories.map((cat, idx) => (
          <div
            key={`${cat.generalCategory}-${idx}`}
            className="main-category"
            onMouseEnter={() => handleGeneralMouseEnter(cat.generalCategory)}
            onClick={() => onSelectCategory({ generalCategory: cat.generalCategory })} 
          >
            {cat.generalCategory}
          </div>
        ))}
      </div>

    {/* subcategories and specific categories panel */}
      {hoveredGeneral && (
        <div className="sub-sections">
          {categories
            .find((cat) => cat.generalCategory === hoveredGeneral)
            ?.subCategories.map((sub, idx) => (
              <div key={`${sub.subCategory}-${idx}`} className="subcategory-block">
                <h4
                  className="subcategory-title"
                  onClick={() => handleSubCategoryClick(hoveredGeneral, sub.subCategory)}
                >
                  {sub.subCategory}
                </h4>
                <ul className="specific-list">
                  {sub.specificCategories.map((spec, i) => (
                    <li
                      key={`${spec}-${i}`}
                      className="specific-item"
                      onClick={() =>
                        handleSpecificClick(hoveredGeneral, sub.subCategory, spec)
                      }
                    >
                      {spec}
                    </li>
                  ))}
                </ul>
              </div>
            ))}
        </div>
      )}
    </div>
  );
};

export default HoverTopCategoryBar;