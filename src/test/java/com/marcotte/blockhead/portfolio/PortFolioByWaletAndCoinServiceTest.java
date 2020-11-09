package com.marcotte.blockhead.portfolio;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.BlockchainAddressStoreService;
import com.marcotte.blockhead.model.WalletDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@TestPropertySource("PortfolioTest.properties")
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class PortFolioByWaletAndCoinServiceTest {

    @Autowired
    private PortFolioByWaletAndCoinService portFolioByWaletAndCoinService;

    @Autowired
    private BlockchainAddressStoreService blockchainAddressStoreService;


    @Test
    public void findAllByCoinNameAndWalletNameAndSummerize() {
    }

    @Test
    public void sumByWalletAndCoin() {
        List<BlockchainAddressStore> addressStore6 = getAddresses6a();
        List<WalletDTO> results = portFolioByWaletAndCoinService.sumByWalletAndCoin(addressStore6);
        assertEquals( 2, results.size());
    }

    /**
     * test case where more than one wallet with more than one coin.
     */
    @Test
    public void findBlockchainAddressStoreByNextIdOrderByWalletNameAscCurrencyAsc() {
        List<BlockchainAddressStore> addressStore6 = getAddresses6a();
        for (BlockchainAddressStore addressStore : addressStore6 ) {
            blockchainAddressStoreService.saveWithHistory(addressStore);
        }
        List<WalletDTO> walletDTOS = portFolioByWaletAndCoinService.findBlockchainAddressStoreByNextIdOrderByWalletNameAscCurrencyAsc();
        assertEquals( 2, walletDTOS.size());
        blockchainAddressStoreService.deleteAll();
    }

    /**
     * test the case where the wallet name is blank
     */
    @Test
    public void findBlockchainAddressStoreByNextIdOrderByWalletNameAscCurrencyAsc1() {
        List<BlockchainAddressStore> addressStore6 = getAddresses6();
        for (BlockchainAddressStore addressStore : addressStore6 ) {
            blockchainAddressStoreService.saveWithHistory(addressStore);
        }
        List<WalletDTO> walletDTOS = portFolioByWaletAndCoinService.findBlockchainAddressStoreByNextIdOrderByWalletNameAscCurrencyAsc();
        assertEquals( 1, walletDTOS.size());
        blockchainAddressStoreService.deleteAll();
    }

    /**
     * check edge case where we dont have any records
     */
    @Test
    public void findBlockchainAddressStoreByNextIdOrderByWalletNameAscCurrencyAsc2() {
        List<WalletDTO> foundAddresses = portFolioByWaletAndCoinService.findBlockchainAddressStoreByNextIdOrderByWalletNameAscCurrencyAsc();
        int expectedSize = 0;
        assertEquals(expectedSize, foundAddresses.size());
        blockchainAddressStoreService.deleteAll();
    }

    /**
     * test the case where we only have one record.
     */
    @Test
    public void findBlockchainAddressStoreByNextIdOrderByWalletNameAscCurrencyAsc3() {
        Date rightNow = new Date();
        BlockchainAddressStore oneAddress =  getAddress1(rightNow);
        oneAddress.setWalletName("fredric");
        blockchainAddressStoreService.saveWithHistory(oneAddress);

        List<WalletDTO> foundAddresses = portFolioByWaletAndCoinService.findBlockchainAddressStoreByNextIdOrderByWalletNameAscCurrencyAsc();
        int expectedSize = 1;
        assertEquals(expectedSize, foundAddresses.size());
        blockchainAddressStoreService.deleteAll();
    }

    private BlockchainAddressStore getAddress1(Date rightNow)
    {
        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("1234567890");
        addressStore.setCurrency("USDT");
        addressStore.setLastBalance( 123456.4323);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Test balance");
        addressStore.setMemo("memo1");
        addressStore.setNumTransactions(42);
        addressStore.setNextId( null );
        return addressStore;
    }

    private List<BlockchainAddressStore> getAddresses6() {
        Date rightNow = new Date();
        List<BlockchainAddressStore> addressList = new ArrayList<>();

        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("Xaaaaaaaaaaaaaaaaaaaaa1");
        addressStore.setCurrency("DASH");
        addressStore.setLastBalance( 323434556767889.0);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Dash test");
        addressStore.setMemo("memo dash1");
        addressStore.setNumTransactions(1);
        addressStore.setNextId( null );
        addressList.add(addressStore);

        BlockchainAddressStore addressStore2 = new BlockchainAddressStore();
        addressStore2.setAddress("Xaaaaaaaaaaaaaaaaaaaaa2");
        addressStore2.setCurrency("DASH");
        addressStore2.setLastBalance( 2.0);
        addressStore2.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore2.setMessage("Dash test");
        addressStore2.setMemo("memo dash1");
        addressStore2.setNumTransactions(1);
        addressStore2.setNextId( null );
        addressList.add(addressStore2);

        BlockchainAddressStore addressStore3 = new BlockchainAddressStore();
        addressStore3.setAddress("0bbbbbbbbbbbbbbbb1");
        addressStore3.setCurrency("BTC");
        addressStore3.setLastBalance( 1.1);
        addressStore3.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore3.setMessage("Bitcoin test1");
        addressStore3.setMemo("memo BTC");
        addressStore3.setNumTransactions(3);
        addressStore3.setNextId( null );
        addressList.add(addressStore3);

        BlockchainAddressStore addressStore4 = new BlockchainAddressStore();
        addressStore4.setAddress("0bbbbbbbbbbbbbbbb2");
        addressStore4.setCurrency("BTC");
        addressStore4.setLastBalance( 1.2);
        addressStore4.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore4.setMessage("Bitcoin test2");
        addressStore4.setMemo("memo BTC");
        addressStore4.setNumTransactions(1);
        addressStore4.setNextId( null );
        addressList.add(addressStore4);

        BlockchainAddressStore addressStore5 = new BlockchainAddressStore();
        addressStore5.setAddress("0ethethewthewthwthethhhhhh1");
        addressStore5.setCurrency("ETH");
        addressStore5.setLastBalance( 10.0);
        addressStore5.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore5.setMessage("Oshirium test1");
        addressStore5.setMemo("memo ETH");
        addressStore5.setNumTransactions(1);
        addressStore5.setNextId( null );
        addressList.add(addressStore5);

        BlockchainAddressStore addressStore6 = new BlockchainAddressStore();
        addressStore6.setAddress("0edddddddddddddddddddog1");
        addressStore6.setCurrency("ADA");
        addressStore6.setLastBalance( 20.0);
        addressStore6.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore6.setMessage("Cardanocoin test1");
        addressStore6.setMemo("memo Cardano ADA");
        addressStore6.setNumTransactions(1);
        addressStore6.setNextId( null );
        addressList.add(addressStore6);

        return addressList;
    }

    /**
     * sorted by wallet, coin
     * @return
     */
    private List<BlockchainAddressStore> getAddresses6a() {
        Date rightNow = new Date();
        List<BlockchainAddressStore> addressList = new ArrayList<>();

        BlockchainAddressStore addressStore3 = new BlockchainAddressStore();
        addressStore3.setWalletName("allan");
        addressStore3.setCurrency("BTC");
        addressStore3.setLastBalance( 1.1);
        addressStore3.setAddress("0bbbbbbbbbbbbbbbb1");
        addressStore3.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore3.setMessage("Bitcoin test1");
        addressStore3.setMemo("memo BTC");
        addressStore3.setNumTransactions(3);
        addressStore3.setNextId( null );
        addressList.add(addressStore3);

        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setWalletName("allan");
        addressStore.setCurrency("DASH");
        addressStore.setAddress("Xaaaaaaaaaaaaaaaaaaaaa1");
        addressStore.setLastBalance( 10.1);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Dash test");
        addressStore.setMemo("memo dash1");
        addressStore.setNumTransactions(1);
        addressStore.setNextId( null );
        addressList.add(addressStore);

        BlockchainAddressStore addressStore2 = new BlockchainAddressStore();
        addressStore2.setWalletName("allan");
        addressStore2.setCurrency("DASH");
        addressStore2.setAddress("Xaaaaaaaaaaaaaaaaaaaaa2");
        addressStore2.setLastBalance( 10.2);
        addressStore2.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore2.setMessage("Dash test");
        addressStore2.setMemo("memo dash1");
        addressStore2.setNumTransactions(1);
        addressStore2.setNextId( null );
        addressList.add(addressStore2);

        BlockchainAddressStore addressStore6 = new BlockchainAddressStore();
        addressStore6.setWalletName("peter");
        addressStore6.setAddress("0edddddddddddddddddddog1");
        addressStore6.setCurrency("ADA");
        addressStore6.setLastBalance( 20.0);
        addressStore6.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore6.setMessage("Cardanocoin test1");
        addressStore6.setMemo("memo Cardano ADA");
        addressStore6.setNumTransactions(1);
        addressStore6.setNextId( null );
        addressList.add(addressStore6);

        BlockchainAddressStore addressStore4 = new BlockchainAddressStore();
        addressStore4.setWalletName("peter");
        addressStore4.setCurrency("BTC");
        addressStore4.setLastBalance( 30.0);
        addressStore4.setAddress("0bbbbbbbbbbbbbbbb2");
        addressStore4.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore4.setMessage("Bitcoin test2");
        addressStore4.setMemo("memo BTC");
        addressStore4.setNumTransactions(1);
        addressStore4.setNextId( null );
        addressList.add(addressStore4);

        BlockchainAddressStore addressStore5 = new BlockchainAddressStore();
        addressStore5.setAddress("0ethethewthewthwthethhhhhh1");
        addressStore5.setWalletName("peter");
        addressStore5.setCurrency("ETH");
        addressStore5.setLastBalance( 40.1);
        addressStore5.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore5.setMessage("Oshirium test1");
        addressStore5.setMemo("memo ETH");
        addressStore5.setNumTransactions(1);
        addressStore5.setNextId( null );
        addressList.add(addressStore5);

        return addressList;
    }

    /**
     * unsorted by wallet, coin
     * @return
     */
    private List<BlockchainAddressStore> getAddresses6b() {
        Date rightNow = new Date();
        List<BlockchainAddressStore> addressList = new ArrayList<>();

        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setWalletName("allan");
        addressStore.setCurrency("DASH");
        addressStore.setAddress("Xaaaaaaaaaaaaaaaaaaaaa1");
        addressStore.setLastBalance( 10.1);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Dash test");
        addressStore.setMemo("memo dash1");
        addressStore.setNumTransactions(1);
        addressStore.setNextId( null );
        addressList.add(addressStore);

        BlockchainAddressStore addressStore2 = new BlockchainAddressStore();
        addressStore2.setWalletName("allan");
        addressStore2.setCurrency("DASH");
        addressStore2.setAddress("Xaaaaaaaaaaaaaaaaaaaaa2");
        addressStore2.setLastBalance( 10.2);
        addressStore2.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore2.setMessage("Dash test");
        addressStore2.setMemo("memo dash1");
        addressStore2.setNumTransactions(1);
        addressStore2.setNextId( null );
        addressList.add(addressStore2);

        BlockchainAddressStore addressStore3 = new BlockchainAddressStore();
        addressStore3.setWalletName("allan");
        addressStore3.setCurrency("BTC");
        addressStore3.setLastBalance( 1.1);
        addressStore3.setAddress("0bbbbbbbbbbbbbbbb1");
        addressStore3.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore3.setMessage("Bitcoin test1");
        addressStore3.setMemo("memo BTC");
        addressStore3.setNumTransactions(3);
        addressStore3.setNextId( null );
        addressList.add(addressStore3);

        BlockchainAddressStore addressStore4 = new BlockchainAddressStore();
        addressStore4.setWalletName("peter");
        addressStore4.setCurrency("BTC");
        addressStore4.setLastBalance( 30.0);
        addressStore4.setAddress("0bbbbbbbbbbbbbbbb2");
        addressStore4.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore4.setMessage("Bitcoin test2");
        addressStore4.setMemo("memo BTC");
        addressStore4.setNumTransactions(1);
        addressStore4.setNextId( null );
        addressList.add(addressStore4);

        BlockchainAddressStore addressStore5 = new BlockchainAddressStore();
        addressStore5.setAddress("0ethethewthewthwthethhhhhh1");
        addressStore5.setWalletName("peter");
        addressStore5.setCurrency("ETH");
        addressStore5.setLastBalance( 40.1);
        addressStore5.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore5.setMessage("Oshirium test1");
        addressStore5.setMemo("memo ETH");
        addressStore5.setNumTransactions(1);
        addressStore5.setNextId( null );
        addressList.add(addressStore5);

        BlockchainAddressStore addressStore6 = new BlockchainAddressStore();
        addressStore6.setWalletName("peter");
        addressStore6.setAddress("0edddddddddddddddddddog1");
        addressStore6.setCurrency("ADA");
        addressStore6.setLastBalance( 20.0);
        addressStore6.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore6.setMessage("Cardanocoin test1");
        addressStore6.setMemo("memo Cardano ADA");
        addressStore6.setNumTransactions(1);
        addressStore6.setNextId( null );
        addressList.add(addressStore6);

        return addressList;
    }

}