package com.marcotte.blockhead.services.blockchainaddressstore;


import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressStore;
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

/**
 * handles all the report/csv exports
 */
@Service
public class BlockchainAddressCsvService
{

    /**
     * list of address ordered by crypto,walletName.  contains most the info on each address
     */
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
