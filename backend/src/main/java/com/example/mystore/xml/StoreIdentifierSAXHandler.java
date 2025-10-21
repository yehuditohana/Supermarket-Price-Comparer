package com.example.mystore.xml;

import com.example.mystore.dto.xml.StoreXmlDTO;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

//This class is responsible for parsing only the header section of Price and PriceFull XML files,
//in order to extract the values (store_number, chain_id, sub_chain_id).
//This allows the system to later skip files that are not relevant for insertion into the database.
public class StoreIdentifierSAXHandler extends DefaultHandler {
    private StoreXmlDTO storeXmlDTO;
    private StringBuilder data;
    /**
     * Returns the parsed StoreXmlDTO object after parsing is completed.
     *
     * @return StoreXmlDTO object containing the store identification information
     */
    public StoreXmlDTO getStore() {
        return storeXmlDTO;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        data = new StringBuilder();
        if (qName.equalsIgnoreCase("chainid")) {
            storeXmlDTO = new StoreXmlDTO();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("chainID")) {
            storeXmlDTO.setChainID(Long.parseLong(data.toString().trim()));
        } else if (qName.equalsIgnoreCase("subChainID")) {
            storeXmlDTO.setSubChainID(Long.parseLong(data.toString().trim()));
        }else if (qName.equalsIgnoreCase("StoreId")) {
            storeXmlDTO.setStoreNumber(Long.parseLong(data.toString()));
            return;
        }
        data.setLength(0);// Clear buffer after processing each tag
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }


}
