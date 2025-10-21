package com.example.mystore.database.cleaning;

import com.example.mystore.database.entities.Item;
import com.example.mystore.dto.xml.ItemXmlDTO;
import org.springframework.stereotype.Component;

/**
 * ItemCleaner standardizes and cleans product data imported from external XML sources.
 *
 * Responsibilities:
 * - Cleans unit quantities, units of measure, and manufacturing country names.
 * - Copies basic item fields from the XML DTO to the database entity.
 * - Ensures consistent and unified formatting of item data for storage and further processing.
 */

@Component
public class ItemCleaner {


    public void clean(ItemXmlDTO original , Item cleaned) {
        copyBasicFields(original, cleaned);
        cleanUnitQty(original , cleaned);
        cleanUnitOfMeasure(original, cleaned);
        cleanManufactureCountry(original, cleaned);
    }

    private void cleanManufactureCountry(ItemXmlDTO original, Item cleaned) {
            String raw = original.getManufactureCountry();
            if (raw == null) {
                cleaned.setManufactureCountry(null);
                return;
            }
        if (raw == null) {
            cleaned.setManufactureCountry(null);
            return;
        }

        switch (raw) {
            case "RU": cleaned.setManufactureCountry("רוסיה"); break;
            case "PE": cleaned.setManufactureCountry("פרו"); break;
            case "DK": cleaned.setManufactureCountry("דנמרק"); break;
            case "PY": cleaned.setManufactureCountry("פרגוואי"); break;
            case "CZ":
            case "צכיה": cleaned.setManufactureCountry("צ'כיה"); break;
            case "US":
            case "ארצות הברית": cleaned.setManufactureCountry("ארצות הברית"); break;
            case "KR": cleaned.setManufactureCountry("דרום קוריאה"); break;
            case "NZ": cleaned.setManufactureCountry("ניו זילנד"); break;
            case "IL":
            case "ישראל": cleaned.setManufactureCountry("ישראל"); break;
            case "UY": cleaned.setManufactureCountry("אורוגוואי"); break;
            case "AE": cleaned.setManufactureCountry("איחוד האמירויות"); break;
            case "AU":
            case "אוסטרליה": cleaned.setManufactureCountry("אוסטרליה"); break;
            case "CL":
            case "צילה": cleaned.setManufactureCountry("צ'ילה"); break;
            case "NL":
            case "הולנד": cleaned.setManufactureCountry("הולנד"); break;
            case "IN": cleaned.setManufactureCountry("הודו"); break;
            case "CN":
            case "סין": cleaned.setManufactureCountry("סין"); break;
            case "AT": cleaned.setManufactureCountry("אוסטריה"); break;
            case "CI": cleaned.setManufactureCountry("חוף השנהב"); break;
            case "VN": cleaned.setManufactureCountry("וייטנאם"); break;
            case "AR": cleaned.setManufactureCountry("ארגנטינה"); break;
            case "פולין":
            case "PL": cleaned.setManufactureCountry("פולין"); break;
            case "מלזיה": cleaned.setManufactureCountry("מלזיה"); break;
            case "BR": cleaned.setManufactureCountry("ברזיל"); break;
            case "מקסיקו":
            case "MX": cleaned.setManufactureCountry("מקסיקו"); break;
            case "בריטניה":
            case "GB":
            case "אנגליה": cleaned.setManufactureCountry("בריטניה"); break;
            case "BY": cleaned.setManufactureCountry("בלארוס"); break;
            case "XX":
            case "לא ידוע": cleaned.setManufactureCountry("לא ידוע"); break;
            case "EG": cleaned.setManufactureCountry("מצרים"); break;
            case "הונגריה":
            case "HU": cleaned.setManufactureCountry("הונגריה"); break;
            case "GH": cleaned.setManufactureCountry("גאנה"); break;
            case "CH": cleaned.setManufactureCountry("שווייץ"); break;
            case "קנדה":
            case "CA": cleaned.setManufactureCountry("קנדה"); break;
            case "LK": cleaned.setManufactureCountry("סרי לנקה"); break;
            case "DE":
            case "גרמניה": cleaned.setManufactureCountry("גרמניה"); break;
            case "ספרד":
            case "ES": cleaned.setManufactureCountry("ספרד"); break;
            case "TW": cleaned.setManufactureCountry("טאיוואן"); break;
            case "לטביה":
            case "LV": cleaned.setManufactureCountry("לטביה"); break;
            case "RO": cleaned.setManufactureCountry("רומניה"); break;
            case "FI": cleaned.setManufactureCountry("פינלנד"); break;
            case "PH": cleaned.setManufactureCountry("הפיליפינים"); break;
            case "RS": cleaned.setManufactureCountry("סרביה"); break;
            case "EC": cleaned.setManufactureCountry("אקוודור"); break;
            case "סלובקיה":
            case "SK": cleaned.setManufactureCountry("סלובקיה"); break;
            case "LT": cleaned.setManufactureCountry("ליטא"); break;
            case "FR":
            case "צרפת": cleaned.setManufactureCountry("צרפת"); break;
            case "איטליה":
            case "IT": cleaned.setManufactureCountry("איטליה"); break;
            case "IE": cleaned.setManufactureCountry("אירלנד"); break;
            case "ID": cleaned.setManufactureCountry("אינדונזיה"); break;
            case "ZA": cleaned.setManufactureCountry("דרום אפריקה"); break;
            case "UA": cleaned.setManufactureCountry("אוקראינה"); break;
            case "PT": cleaned.setManufactureCountry("פורטוגל"); break;
            case "NO": cleaned.setManufactureCountry("נורווגיה"); break;
            case "TH":
            case "תאילנד": cleaned.setManufactureCountry("תאילנד"); break;
            case "BG": cleaned.setManufactureCountry("בולגריה"); break;
            case "TR": cleaned.setManufactureCountry("טורקיה"); break;
            case "CR": cleaned.setManufactureCountry("קוסטה ריקה"); break;
            case "בלגיה":
            case "BE": cleaned.setManufactureCountry("בלגיה"); break;
            case "MK": cleaned.setManufactureCountry("מקדוניה"); break;
            case "GR": cleaned.setManufactureCountry("יוון"); break;
            case "CO": cleaned.setManufactureCountry("קולומביה"); break;
            case "AZ": cleaned.setManufactureCountry("אזרבייג'ן"); break;

            default:
                cleaned.setManufactureCountry(raw);
                break;
        }
    }


    private void copyBasicFields(ItemXmlDTO original, Item cleaned) {
        cleaned.setItemID(original.getItemID());
        cleaned.setItemName(original.getItemName());
        cleaned.setManufacturerName(original.getManufacturerName());
        cleaned.setManufactureCountry(original.getManufactureCountry());
        cleaned.setManufacturerItemDescription(original.getManufacturerItemDescription());
        cleaned.setQuantity(original.getQuantity());
        cleaned.setBIsWeighted(original.getWeighted());
        cleaned.setUnitOfMeasure(original.getUnitOfMeasure());
    }
    private void cleanUnitOfMeasure(ItemXmlDTO original, Item cleaned) {
        String raw = original.getUnitOfMeasure();
        if (raw == null) {
            cleaned.setUnitOfMeasure(null);
            return;
        }
        switch (raw) {
            case "100 מטר":
                cleaned.setUnitOfMeasure("100 מטר");
                break;

            case "יח'": case "יחידה": case "יח":
                cleaned.setUnitOfMeasure("יחידה");
                break;

            case "100 ק\"\"ג":
                cleaned.setUnitOfMeasure("100 ק\"ג");
                break;

            case "100 ליטר":
                cleaned.setUnitOfMeasure("100 ליטר");
                break;

            case "גרם":
                cleaned.setUnitOfMeasure("גרם");
                break;

            case "100 יח":
                cleaned.setUnitOfMeasure("100 יחידות");
                break;

            case "מ'ל":
                cleaned.setUnitOfMeasure("מ\"ל");
                break;

            case "ליטר":
                cleaned.setUnitOfMeasure("ליטר");
                break;

            case "לק\"ג": case "ק\"ג":
                cleaned.setUnitOfMeasure("ק\"ג");
                break;

            case "100 גרם":
                cleaned.setUnitOfMeasure("100 גרם");
                break;

            case "100 מ'ל": case "100 מ\"ל": case "100 מל":
                cleaned.setUnitOfMeasure("100 מ\"ל");
                break;
            case "מטר":
                cleaned.setUnitOfMeasure("מטר");
                break;
            default:
                cleaned.setUnitOfMeasure(raw);
                break;
        }
    }



    private void cleanName() {

    }

    private void cleanUnitQty(ItemXmlDTO original, Item cleaned) {
        String raw = original.getUnitQty();
        if(raw == null){
            cleaned.setUnitQty(null);
            return;
        }
        switch (raw){
            case "100 יח":
            case "יח":
            case "יח'":
            case "יח`":
                cleaned.setUnitQty("יחידה");
                break;

            case "100 מל":
            case "מיליליטרים":
            case "מטרים":
                cleaned.setUnitQty("מ\"ל");
                break;

            case "גרמים":
                cleaned.setUnitQty("גרם");
                break;

            case "יחידה":
                cleaned.setUnitQty("ליטר");
                break;

            case "ליטרים":
                cleaned.setUnitQty("מטר");
                break;

            case "ק\"\"ג":
            case "קילוגרמים":
                cleaned.setUnitQty("ק\"ג");
                break;

            default:
                cleaned.setUnitQty(raw);
                break;
        }
}
    /* private void detectAndCleanRamiLevy(ItemXmlDTO original, Item cleaned) {
        String itemName = original.getItemName();
        String manufacturerName = original.getManufacturerName();
        String description = original.getManufacturerItemDescription();

        Pattern pattern = Pattern.compile("(?<=\\s|^)רמי לוי(?=\\s|$)|(?<=\\s|^)רמי לו(?=\\s|$)|(?<=\\s|^)רמי ל(?=\\s|$)|(?<=\\s|^)רמי(?=\\s|$)");

        if (itemName != null && pattern.matcher(itemName).find()) {
            cleaned.setItemName(replacePartialRami(itemName));
            found = true;
        }
        if (manufacturerName != null && pattern.matcher(manufacturerName).find()) {
            cleaned.setManufacturerName(replacePartialRami(manufacturerName));
            found = true;
        }
        if (description != null && pattern.matcher(description).find()) {
            cleaned.setManufacturerItemDescription(replacePartialRami(description));
            found = true;
        }

    }*/


    // replace to "רמי לוי"
   /* private String replacePartialRami(String text) {
        //If it already exists 'רמי לוי' - we will return it as it is.
        if (text.contains("רמי לוי")) {
            return text;
        }
        if (text.contains("רמי לו")) {
            text = text.replaceFirst("(?<=\\s|^)רמי לו(?=\\s|$)", "רמי לוי");
        }
        if (text.contains("רמי ל") && !text.contains("רמי לוי")) {
            text = text.replaceFirst("(?<=\\s|^)רמי ל(?=\\s|$)", "רמי לוי");
        }
        if (text.contains("רמי") && !text.contains("רמי לוי") && !text.contains("רמי לו") && !text.contains("רמי ל")) {
            text = text.replaceFirst("(?<=\\s|^)רמי(?=\\s|$)", "רמי לוי");
        }
        return text.trim();
    }*/

    /*
    private void detectAndCleanShufersal(ItemXmlDTO original, Item cleaned) {
        String itemName = original.getItemName();
        String manufacturerName = original.getManufacturerName();
        String description = original.getManufacturerItemDescription();

        if ((itemName != null && itemName.contains("שופרסל")) ||
                (manufacturerName != null && manufacturerName.contains("שופרסל")) ||
                (description != null && description.contains("שופרסל"))) {

            cleaned.setAvailableInChain("שופרסל");
        }
    }*/

}


