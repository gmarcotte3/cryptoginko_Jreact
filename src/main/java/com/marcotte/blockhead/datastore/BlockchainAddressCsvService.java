package com.marcotte.blockhead.datastore;


import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.util.List;

@Service
public class BlockchainAddressCsvService
{
    private static final Logger logger = LoggerFactory.getLogger(BlockchainAddressCsvService.class);
    public void writeAddressesToCSVStream(PrintWriter writer, List<BlockchainAddressStore> addresses)
    {
        try {
            ColumnPositionMappingStrategy mappingStrategy = new ColumnPositionMappingStrategy();
            mappingStrategy.setType(BlockchainAddressStore.class );
            mappingStrategy.generateHeader();

            String[] columrns = new String[] {
                    "id",
                    "address",
                    "currency",
                    "lastBalance",
                    "numTransactions",
                    "lastUpdated",
                    "message",
                    "memo",
                    "updatedViaBlockChainExplorer"
            };
            mappingStrategy.setColumnMapping(columrns);

            StatefulBeanToCsv btcsv = new StatefulBeanToCsvBuilder(writer)
                    .withQuotechar(CSVWriter.NO_ESCAPE_CHARACTER)
                    .withMappingStrategy(mappingStrategy)
                    .withSeparator(',')
                    .build();
            btcsv.write(addresses);

        } catch ( CsvException ex )
        {
            logger.error("Error mapping addressStore bean to CSV", ex);
        }

    }
}
