package com.example.mystore.xml;

import com.example.mystore.database.entities.Chain;
import com.example.mystore.database.entities.ChainKey;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * SAX handler for parsing XML data related to Chains and SubChains.
 *
 * It reads the XML elements into Chain and ChainKey entities,
 * building a list of Chains from the parsed XML structure.
 */
public class ChainSAXHandler extends DefaultHandler {
    private List<Chain> chainList = new ArrayList<>();
    private Chain chain;
    private ChainKey chainKey;
    private StringBuilder data;
    private Long chainId;
    private String chainName;

    /**
     * Returns the list of parsed Chain entities after parsing is completed.
     *
     * @return list of Chain entities
     */
    public List<Chain> getChainList() {
        return chainList;
    }


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        data = new StringBuilder();

        if (qName.equalsIgnoreCase("SubChain")) {
            chain = new Chain();
            chainKey = new ChainKey();
        }
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("ChainId")) {
            chainId = Long.parseLong(data.toString());
        } else if (qName.equalsIgnoreCase("ChainName")) {
            chainName = data.toString();
        } else if (qName.equalsIgnoreCase("SubChainId")) {
            chainKey.setSubChainID(Long.parseLong(data.toString()));
        } else if (qName.equalsIgnoreCase("SubChainName")) {
            chain.setSubChainName(data.toString());
        } else if (qName.equalsIgnoreCase("SubChain")) {
            chainKey.setChainID(chainId);
            chain.setChainName(chainName);
            chain.setChainKey(chainKey);
            chainList.add(chain);
        }
        data.setLength(0);// Reset the buffer
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        data.append(new String(ch, start, length));
    }

}
