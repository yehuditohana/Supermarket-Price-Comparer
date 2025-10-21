package com.example.mystore.xml;

import com.example.mystore.dto.xml.ItemXmlDTO;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.List;



import java.util.ArrayList;
/**
 * SAX handler for parsing XML files containing item data.
 *
 * This handler builds a list of {@link ItemXmlDTO} objects from the parsed XML structure.
 */
public class ItemSAXHandler extends DefaultHandler {
    private List<ItemXmlDTO>  dtos = new ArrayList<>();
    private  ItemXmlDTO itemXmlDTO;
    private StringBuilder data;
    /**
     * Returns the list of parsed ItemXmlDTO objects after parsing is completed.
     *
     * @return list of ItemXmlDTO objects
     */
    public List<ItemXmlDTO> getDtos() {
        return dtos;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        data = new StringBuilder();

        if (qName.equalsIgnoreCase("item") || qName.equalsIgnoreCase("Product") ) {
            itemXmlDTO = new ItemXmlDTO();
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("item") || qName.equalsIgnoreCase("Product")) {
            dtos.add(itemXmlDTO);
        } else if (qName.equalsIgnoreCase("ItemCode") && !data.toString().trim().isEmpty()) {
            itemXmlDTO.setItemID(data.toString().trim());
        } else if (qName.equalsIgnoreCase("itemName") && !data.toString().trim().isEmpty()) {
            itemXmlDTO.setItemName(data.toString().trim());
        } else if (qName.equalsIgnoreCase("ManufacturerName") && !data.toString().trim().isEmpty()) {
            itemXmlDTO.setManufacturerName(data.toString().trim());
        }else if (qName.equalsIgnoreCase("ManufactureCountry") && !data.toString().trim().isEmpty()) {
            itemXmlDTO.setManufactureCountry(data.toString().trim());
        } else if (qName.equalsIgnoreCase("ManufacturerItemDescription") && !data.toString().isEmpty()) {
            itemXmlDTO.setManufacturerItemDescription(data.toString().trim());
        } else if (qName.equalsIgnoreCase("UnitQty") && !data.toString().trim().isEmpty()) {
            itemXmlDTO.setUnitQty(data.toString().trim());
        } else if (qName.equalsIgnoreCase("Quantity") && !data.toString().trim().isEmpty()) {
            itemXmlDTO.setQuantity(Double.parseDouble(data.toString().trim()));
        }else if (qName.equalsIgnoreCase("UnitOfMeasure") && !data.toString().trim().isEmpty()) {
            itemXmlDTO.setUnitOfMeasure(data.toString().trim());
        }else if (qName.equalsIgnoreCase("bIsWeighted")  && !data.toString().trim().isEmpty()) {
            Boolean isWeighted = data.toString().equals("0")? false : true;
           itemXmlDTO.setWeighted(isWeighted);
        }else if (qName.equalsIgnoreCase("QtyInPackage") && !data.toString().trim().isEmpty()) {
            itemXmlDTO.setQtyInPackage(data.toString().trim());
        }
        data.setLength(0);// Reset the buffer
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }


}
