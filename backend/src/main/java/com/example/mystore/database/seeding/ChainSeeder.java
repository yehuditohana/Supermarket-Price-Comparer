package com.example.mystore.database.seeding;
import com.example.mystore.database.entities.Chain;
import com.example.mystore.services.seederServices.ChainSeederService;
import com.example.mystore.xml.ChainSAXHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.List;
@Component
public class ChainSeeder {
    private static final Logger logger = LoggerFactory.getLogger(ChainSeeder.class);

    @Value("${chain.file.directory}")
    private String directoryPath;
    private final ChainSeederService chainService;

    public ChainSeeder(ChainSeederService chainService) {
        this.chainService = chainService;
    }


    public void  initializeChains() {
        logger.info("initialize Chains started...");

        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();// Setting up the SAX parser

        try{
            SAXParser saxParser = saxParserFactory.newSAXParser();// Creating a new SAXParser instance

            ChainSAXHandler chainSAXHandler = new ChainSAXHandler(); // Creating a handler to parse the XML data

            saxParser.parse(new File(directoryPath) , chainSAXHandler);// Parsing the XML file and handling the data

            List<Chain> chainList= chainSAXHandler.getChainList(); // Getting the list of chains from the handler

            chainService.saveAll(chainList); // Saving the parsed chains to the database



        } catch (ParserConfigurationException | SAXException | IOException e){
            e.printStackTrace();
        }
        logger.info("Initialization Chains completed successfully.");

    }

}
