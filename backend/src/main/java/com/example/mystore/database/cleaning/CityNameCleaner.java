package com.example.mystore.database.cleaning;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

/**
 * CityNameCleaner standardizes the storeCity field values for consistent city matching and searching.
 *
 * Responsibilities:
 * - Maps various variations and spellings of city names to a standard form.
 * - Converts known numeric city codes to standard city names.
 * - Returns null if the input is invalid or unknown.
 *
 * This ensures easier and more accurate store search operations by city name.
 */
@Component
public class CityNameCleaner {

        private static final Map<String, String> cityNameMap = new HashMap<>();
        private static final Map<String, String> cityCodeMap = new HashMap<>();

        static {
            // Mapping city names
            addCityVariations("תל אביב", "תל אביב", "תל-אביב", "תל אביב-יפו", "תל-אביב-יפו", "תל אביב יפו",
                    "רמת אביב א", "ת\"א", "תא", "תל אבית יפה", "תל-אביב יפה", "תל אביב יפה");


            addCityVariations("ירושלים", "ירושלים", "י-ם", "י\"ם");


            addCityVariations("חיפה", "חיפה");


            addCityVariations("באר שבע", "באר שבע", "באר-שבע", "ב\"ש", "בש", "ב.ש");


            addCityVariations("ראשון לציון", "ראשון לציון", "ראשל\"צ", "ראשלצ", "ראשון", "ראשל\"צ");


            addCityVariations("פתח תקווה", "פתח תקווה", "פתח תקוה", "פתח-תקווה", "פתח-תקוה", "פ\"ת", "פת");


            addCityVariations("אשדוד", "אשדוד");


            addCityVariations("נתניה", "נתניה");


            addCityVariations("רמת גן", "רמת גן", "רמת-גן", "ר\"ג", "רג");


            addCityVariations("חולון", "חולון");


            addCityVariations("רעננה", "רעננה");


            addCityVariations("רמלה", "רמלה");


            addCityVariations("אשקלון", "אשקלון");


            addCityVariations("כפר סבא", "כפר סבא", "כפר-סבא", "כ\"ס", "כס");


            addCityVariations("בת ים", "בת ים", "בת-ים");


            addCityVariations("הרצליה", "הרצליה", "הרצלייה");


            addCityVariations("הוד השרון", "הוד השרון", "הוד-השרון");


            addCityVariations("קרית אתא", "קרית אתא", "קרית-אתא", "קריית אתא", "קריית-אתא");


            addCityVariations("קרית מוצקין", "קרית מוצקין", "קריית מוצקין", "מוצקין");


            addCityVariations("רמת השרון", "רמת השרון", "רמת-השרון");


            addCityVariations("גבעתיים", "גבעתיים");


            addCityVariations("ראש העין", "ראש העין", "ראש-העין");


            addCityVariations("קרית ביאליק", "קרית ביאליק", "קריית ביאליק");


            addCityVariations("מודיעין", "מודיעין", "מודעין", "מודיעין-מכבים-רעות");


            addCityVariations("בני ברק", "בני ברק", "בני-ברק", "ב\"ב");


            addCityVariations("רחובות", "רחובות");


            addCityVariations("קרית גת", "קרית גת", "קריית גת");


            addCityVariations("עכו", "עכו");


            addCityVariations("אילת", "אילת");


            addCityVariations("חדרה", "חדרה");


            addCityVariations("נהריה", "נהריה", "נהרייה");


            addCityVariations("כרמיאל", "כרמיאל");


            addCityVariations("עפולה", "עפולה");


            addCityVariations("טבריה", "טבריה");


            addCityVariations("נס ציונה", "נס ציונה");


            addCityVariations("יבנה", "יבנה");


            addCityVariations("אור יהודה", "אור יהודה", "אור-יהודה");


            addCityVariations("צפת", "צפת");


            addCityVariations("קרית שמונה", "קרית שמונה", "קריית שמונה");


            addCityVariations("טירת הכרמל", "טירת הכרמל", "טירת-הכרמל");


            addCityVariations("לוד", "לוד");


            addCityVariations("דימונה", "דימונה");


            addCityVariations("שדרות", "שדרות");


            addCityVariations("בית שמש", "בית שמש", "בית-שמש");


            addCityVariations("אור עקיבא", "אור עקיבא", "אור-עקיבא");


            addCityVariations("מעלות", "מעלות", "מעלות-תרשיחא");


            addCityVariations("טבעון", "טבעון", "קרית טבעון", "קריית טבעון");


            addCityVariations("מבשרת ציון", "מבשרת ציון");


            addCityVariations("קרית אונו", "קרית אונו", "קריית אונו", "קריית-אונו", "קרית-אונו");


            addCityVariations("מגדל העמק", "מגדל העמק");


            addCityVariations("ערד", "ערד");


            addCityVariations("בית שאן", "בית שאן");


            addCityVariations("תל מונד", "תל מונד", "תל נונד", "תל-מונד");


            addCityVariations("נתיבות", "נתיבות");


            addCityVariations("יהוד", "יהוד", "יהוד-מונוסון");


            addCityVariations("קרית ים", "קרית ים", "קריית ים");


            addCityVariations("זכרון יעקב", "זכרון יעקב", "זכרון-יעקב", "זיכרון יעקב");


            addCityVariations("גני תקווה", "גני תקווה", "גני-תקוה", "גני-תקווה");


            addCityVariations("עומר", "עומר");


            addCityVariations("קצרין", "קצרין");


            addCityVariations("פרדס חנה", "פרדס חנה", "פרדס חנה-כרכור", "כרכור", "פרדס חנה כרכור");


            addCityVariations("דליית אל כרמל", "דליית אל כרמל");
            addCityVariations("צור יצחק", "צור יצחק");
            addCityVariations("מתן", "מתן");
            addCityVariations("אלעד", "אלעד");
            addCityVariations("שוהם", "שוהם");
            addCityVariations("אופקים", "אופקים");
            addCityVariations("כפר יונה", "כפר יונה", "כפר-יונה");
            addCityVariations("רעות", "רעות");
            addCityVariations("מכבים", "מכבים");
            addCityVariations("מזכרת בתיה", "מזכרת בתיה");
            addCityVariations("גבעת שמואל", "גבעת שמואל", "גבעת-שמואל");
            addCityVariations("קרית מלאכי", "קרית מלאכי", "קריית מלאכי");
            addCityVariations("גדרה", "גדרה");
            addCityVariations("עתלית", "עתלית");
            addCityVariations("ירוחם", "ירוחם");
            addCityVariations("קרית עקרון", "קרית עקרון");
            addCityVariations("מצפה רמון", "מצפה רמון", "מצפה-רמון");
            addCityVariations("גן יבנה", "גן יבנה");
            addCityVariations("חריש", "חריש");
            addCityVariations("בית חשמונאי", "בית חשמונאי");
            addCityVariations("צורן", "צורן", "קדימה צורן", "קדימה-צורן");
            addCityVariations("אבן יהודה", "אבן יהודה", "אבן-יהודה");
            addCityVariations("רמת ישי", "רמת ישי");
            addCityVariations("גבעת אולגה", "גבעת אולגה");
            addCityVariations("חצור הגלילית", "חצור הגלילית", "חצור-הגלילית");
            addCityVariations("כפר תבור", "כפר תבור");
            addCityVariations("אריאל", "אריאל");
            addCityVariations("מעלה אדומים", "מעלה אדומים");
            addCityVariations("באר יעקב", "באר יעקב");
            addCityVariations("בת חפר", "בת חפר");
            addCityVariations("רהט", "רהט");
            addCityVariations("שילת", "שילת");
            addCityVariations("אורנית", "אורנית");
            addCityVariations("אלקנה", "אלקנה");
            addCityVariations("יקנעם", "יקנעם", "יוקנעם", "יקנעם עילית", "יוקנעם עילית");
            addCityVariations("צור משה", "צור משה");
            addCityVariations("פרדסיה", "פרדסיה");
            addCityVariations("כפר ורדים", "כפר ורדים");
            addCityVariations("ביתר עלית", "ביתר עילית", "ביתר עלית");
            addCityVariations("קרית חיים", "קרית חיים");
            addCityVariations("קדימה", "קדימה");
            addCityVariations("טייבה", "טייבה");
            addCityVariations("שפרעם", "שפרעם");
            addCityVariations("מיתר", "מיתר");
            addCityVariations("להבים", "להבים");
            addCityVariations("בנימינה", "בנימינה", "בנימינה-גבעת עדה");
            addCityVariations("גבעת עדה", "גבעת עדה");

            // map code to city
            cityCodeMap.put("3000", "ירושלים");
            cityCodeMap.put("5000", "תל אביב");
            cityCodeMap.put("4000", "חיפה");
            cityCodeMap.put("7100", "אשקלון");
            cityCodeMap.put("70", "אשדוד");
            cityCodeMap.put("8700", "רעננה");
            cityCodeMap.put("8500", "רמלה");
            cityCodeMap.put("6900", "כפר סבא");
            cityCodeMap.put("9000", "באר שבע");
            cityCodeMap.put("8300", "ראשון לציון");
            cityCodeMap.put("7000", "לוד");
            cityCodeMap.put("8400", "רחובות");
            cityCodeMap.put("6100", "רמת גן");
            cityCodeMap.put("6200", "בת ים");
            cityCodeMap.put("6600", "חולון");
            cityCodeMap.put("7400", "נתניה");
            cityCodeMap.put("7900", "פתח תקווה");
            cityCodeMap.put("6500", "חדרה");
            cityCodeMap.put("2630", "קרית גת");
            cityCodeMap.put("2610", "בית שמש");
            cityCodeMap.put("2600", "אילת");
            cityCodeMap.put("7800", "פרדס חנה");
            cityCodeMap.put("9100", "נהריה");
            cityCodeMap.put("874", "מגדל העמק");
            cityCodeMap.put("1031", "שדרות");
            cityCodeMap.put("7600", "עכו");
            cityCodeMap.put("1139", "כרמיאל");
            cityCodeMap.put("7700", "עפולה");
            cityCodeMap.put("6700", "טבריה");
            cityCodeMap.put("1165", "מודיעין");
            cityCodeMap.put("2500", "נשר");
            cityCodeMap.put("1015", "מבשרת ציון");
            cityCodeMap.put("2640", "ראש העין");
            cityCodeMap.put("2660", "יבנה");
            cityCodeMap.put("246", "נתיבות");
            cityCodeMap.put("2800", "קרית שמונה");
            cityCodeMap.put("195", "קדימה");
            cityCodeMap.put("1034", "קרית מלאכי");
            cityCodeMap.put("2640", "ראש העין");
            cityCodeMap.put("2630", "קרית גת");
            cityCodeMap.put("1200", "מודיעין");
            cityCodeMap.put("3570", "אריאל");
            cityCodeMap.put("3616", "מעלה אדומים");
            cityCodeMap.put("9300", "זכרון יעקב");
            cityCodeMap.put("3780", "ביתר עלית");




        }

        private static void addCityVariations(String standardName, String... variations) {
            for (String variation : variations) {
                cityNameMap.put(variation.trim(), standardName);
            }
        }

        public static String cleanCityName(String storeCity) {
            if (storeCity == null || storeCity.trim().isEmpty() || storeCity.trim().equalsIgnoreCase("NULL")) {
                return null;
            }

            String city = storeCity.trim();

            // checks if it's city identifier
            if (Pattern.matches("\\d+", city)) {
                return cityCodeMap.getOrDefault(city, city);
            }

            //checks if the city is in the map
            String standardCity = cityNameMap.get(city);
            if (standardCity != null) {
                return standardCity;
            }

            //if the city is not mapped - return null
            return null;
    }
}