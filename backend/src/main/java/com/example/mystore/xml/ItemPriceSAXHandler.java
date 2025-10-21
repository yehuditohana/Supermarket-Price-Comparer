package com.example.mystore.xml;
import com.example.mystore.dto.xml.ItemPriceXmlDTO;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * SAX handler for parsing XML files containing item price data.
 *
 * This handler builds a list of {@link ItemPriceXmlDTO} objects from the parsed XML structure.
 */
public class ItemPriceSAXHandler extends DefaultHandler {
    private List<ItemPriceXmlDTO> dtoList = new ArrayList<>();
    private ItemPriceXmlDTO itemPriceDTO;
    private StringBuilder data;
    /**
     * Returns the list of parsed ItemPriceXmlDTO objects after parsing is completed.
     *
     * @return list of ItemPriceXmlDTO objects
     */
    public List<ItemPriceXmlDTO> getItemPriceList() {
        return dtoList;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        data = new StringBuilder();

        if (qName.equalsIgnoreCase("item") || qName.equalsIgnoreCase("Product") ) {
            itemPriceDTO = new ItemPriceXmlDTO();
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("item") || qName.equalsIgnoreCase("Product") ) {
            dtoList.add(itemPriceDTO);
        }else if (qName.equalsIgnoreCase("ItemCode")) {
            itemPriceDTO.setItemID(data.toString().trim());
        }else if (qName.equalsIgnoreCase("ItemStatus")) {
            itemPriceDTO.setStatus(Boolean.parseBoolean(data.toString().trim()));
        }else if (qName.equalsIgnoreCase("PriceUpdateDate") ) {
            itemPriceDTO.setPriceDate(parsePriceDate(data.toString()));
        }else if (qName.equalsIgnoreCase("itemPrice") ) {
            itemPriceDTO.setPrice(Double.parseDouble(data.toString()));
        }
        data.setLength(0); // Clear the buffer
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }

    /**
     * Parses a date string from the XML into a LocalDate object.
     * Supports multiple formats found in different XML files.
     *
     * @param dateStr the date string to parse
     * @return parsed LocalDate, or current date if parsing fails
     */
    private LocalDate parsePriceDate(String dateStr) {
        String[] formats = {
                "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd HH:mm",
                "yyyy/MM/dd HH:mm:ss",
                "yyyy/MM/dd HH:mm"
        };

        dateStr = dateStr.trim();  //Removing unnecessary spaces

        for (String pattern : formats) {
            try {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
                LocalDateTime dateTime = LocalDateTime.parse(dateStr, dtf);
                return dateTime.toLocalDate();
            } catch (DateTimeParseException e) {
                //The format is not correct, we will try the next one.
            }
        }
        //There is no suitable format
        return LocalDate.now();
    }


}
