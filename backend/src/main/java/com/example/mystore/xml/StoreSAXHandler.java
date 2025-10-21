package com.example.mystore.xml;

import com.example.mystore.dto.xml.StoreXmlDTO;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
/**
 * SAX handler for parsing XML files containing store data (branches).
 *
 * This handler builds a list of {@link StoreXmlDTO} objects from the parsed XML structure.
 * It maintains the current Chain ID and Sub-Chain ID across the document.
 */
public class StoreSAXHandler extends DefaultHandler {
    private List<StoreXmlDTO> dtos = new ArrayList<>();
    private StoreXmlDTO storeXmlDTO = null;

    // Keep the current chainID across the document
    private Long currentChainId;
    // keep subChainID across the document or until overridden
    private Long currentSubChainId;
    private StringBuilder data;
    /**
     * Returns the list of parsed StoreXmlDTO objects after parsing is completed.
     *
     * @return list of StoreXmlDTO objects
     */
    public List<StoreXmlDTO> getDtos() {
        return dtos;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        data = new StringBuilder();
        if (qName.equalsIgnoreCase("Branch") || qName.equalsIgnoreCase("STORE")) {
            storeXmlDTO = new StoreXmlDTO();
            storeXmlDTO.setChainID(currentChainId);
            storeXmlDTO.setSubChainID(currentSubChainId);
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("Branch") || qName.equalsIgnoreCase("STORE")) {
            dtos.add(storeXmlDTO);
        } else if (qName.equalsIgnoreCase("StoreId")) {
            storeXmlDTO.setStoreNumber(Long.parseLong(data.toString()));
        } else if (qName.equalsIgnoreCase("chainID")) {
            currentChainId = Long.parseLong(data.toString());
        } else if (qName.equalsIgnoreCase("subChainID")) {
            currentSubChainId = Long.parseLong(data.toString());
        } else if (qName.equalsIgnoreCase("StoreName") && !data.toString().isEmpty()) {
            storeXmlDTO.setStoreName(data.toString());
        } else if (qName.equalsIgnoreCase("city") && !data.toString().isEmpty()) {
            storeXmlDTO.setStoreCity(data.toString());
        } else if (qName.equalsIgnoreCase("address") && !data.toString().isEmpty()) {
            storeXmlDTO.setStoreAddress(data.toString());
        } else if (qName.equalsIgnoreCase("zipCode") && !data.toString().isEmpty()) {
            storeXmlDTO.setStoreZipCode(Long.parseLong(data.toString()));
        }
        data.setLength(0);// Clear the buffer
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }

}
