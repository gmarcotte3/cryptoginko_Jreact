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
public class PortFolioByWalletAndCoinServiceTest {

    @Autowired
    private PortFolioByWalletAndCoinService portFolioByWalletAndCoinService;

    @Autowired
    private BlockchainAddressStoreService blockchainAddressStoreService;


    @Test
    public void findAllByCoinNameAndWalletNameAndSummerize() {
    }

    /**
     * test the summing over wallets and coin
     *
     * Initial setup of addresses:
     *    Ticker    value   wallet
     * 0] BTC       1.1     allan
     * 1] DASH      10.1    allan
     * 2] DASH      10.2    allan
     * 3] ADA       20.0    peter
     * 4] BTC       30.0    peter
     * 5] ETH       40.1    peter
     *
     * So the wallet should be
     * ALLAN    BTC      1.1
     *          DASH    20.3
     * PETER    ADA     20.0
     *          BTC     30.0
     *          ETH     40.1
     */

    /**
     * test the case where we have a sorted array already saved to the database then
     * retrieved and summarized.
     */
    @Test
    public void findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc_unsorted() {
        List<BlockchainAddressStore> addressStore6 = getAddresses6a();
        for (BlockchainAddressStore addressStore : addressStore6 ) {
            blockchainAddressStoreService.save(addressStore);
        }
        List<WalletDTO> walletDTOS = portFolioByWalletAndCoinService.findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc();
        assertEquals( 2, walletDTOS.size());  // two wallets
        assertEquals( "ALLAN", walletDTOS.get(0).getWalletName());
        assertEquals( "PETER", walletDTOS.get(1).getWalletName());
        assertEquals(2, walletDTOS.get(0).getCoinDTOs().size()); // first wallet two coins
        assertEquals(3, walletDTOS.get(1).getCoinDTOs().size()); // second wallet three coins

        blockchainAddressStoreService.deleteAll();
    }

    /**
     * test the case where the wallet name is blank
     */
    @Test
    public void findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc1() {
        List<BlockchainAddressStore> addressStore6 = getAddresses6();
        for (BlockchainAddressStore addressStore : addressStore6 ) {
            blockchainAddressStoreService.save(addressStore);
        }
        List<WalletDTO> walletDTOS = portFolioByWalletAndCoinService.findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc();
        assertEquals( 1, walletDTOS.size());
        blockchainAddressStoreService.deleteAll();
    }

    /**
     * check edge case where we dont have any records
     */
    @Test
    public void findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc2() {
        List<WalletDTO> foundAddresses = portFolioByWalletAndCoinService.findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc();
        int expectedSize = 0;
        assertEquals(expectedSize, foundAddresses.size());
        blockchainAddressStoreService.deleteAll();
    }

    /**
     * test the case where we only have one record.
     */
    @Test
    public void findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc3() {
        Date rightNow = new Date();
        BlockchainAddressStore oneAddress =  getAddress1(rightNow);
        oneAddress.setWalletName("fredric");
        blockchainAddressStoreService.save(oneAddress);

        List<WalletDTO> foundAddresses = portFolioByWalletAndCoinService.findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc();
        int expectedSize = 1;
        assertEquals(expectedSize, foundAddresses.size());
        blockchainAddressStoreService.deleteAll();
    }

    private BlockchainAddressStore getAddress1(Date rightNow)
    {
        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("1234567890");
        addressStore.setTicker("USDT");
        addressStore.setLastBalance( 123456.4323);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Test balance");
        addressStore.setMemo("memo1");
        addressStore.setNumTransactions(42);
        return addressStore;
    }

    /**
     * list of coins and the wallet name is blank.
     * @return
     */
    private List<BlockchainAddressStore> getAddresses6() {
        Date rightNow = new Date();
        List<BlockchainAddressStore> addressList = new ArrayList<>();

        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("Xaaaaaaaaaaaaaaaaaaaaa1");
        addressStore.setTicker("DASH");
        addressStore.setLastBalance( 323434556767889.0);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Dash test");
        addressStore.setMemo("memo dash1");
        addressStore.setNumTransactions(1);
        addressList.add(addressStore);

        BlockchainAddressStore addressStore2 = new BlockchainAddressStore();
        addressStore2.setAddress("Xaaaaaaaaaaaaaaaaaaaaa2");
        addressStore2.setTicker("DASH");
        addressStore2.setLastBalance( 2.0);
        addressStore2.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore2.setMessage("Dash test");
        addressStore2.setMemo("memo dash1");
        addressStore2.setNumTransactions(1);
        addressList.add(addressStore2);

        BlockchainAddressStore addressStore3 = new BlockchainAddressStore();
        addressStore3.setAddress("0bbbbbbbbbbbbbbbb1");
        addressStore3.setTicker("BTC");
        addressStore3.setLastBalance( 1.1);
        addressStore3.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore3.setMessage("Bitcoin test1");
        addressStore3.setMemo("memo BTC");
        addressStore3.setNumTransactions(3);
        addressList.add(addressStore3);

        BlockchainAddressStore addressStore4 = new BlockchainAddressStore();
        addressStore4.setAddress("0bbbbbbbbbbbbbbbb2");
        addressStore4.setTicker("BTC");
        addressStore4.setLastBalance( 1.2);
        addressStore4.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore4.setMessage("Bitcoin test2");
        addressStore4.setMemo("memo BTC");
        addressStore4.setNumTransactions(1);
        addressList.add(addressStore4);

        BlockchainAddressStore addressStore5 = new BlockchainAddressStore();
        addressStore5.setAddress("0ethethewthewthwthethhhhhh1");
        addressStore5.setTicker("ETH");
        addressStore5.setLastBalance( 10.0);
        addressStore5.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore5.setMessage("Oshirium test1");
        addressStore5.setMemo("memo ETH");
        addressStore5.setNumTransactions(1);
        addressList.add(addressStore5);

        BlockchainAddressStore addressStore6 = new BlockchainAddressStore();
        addressStore6.setAddress("0edddddddddddddddddddog1");
        addressStore6.setTicker("ADA");
        addressStore6.setLastBalance( 20.0);
        addressStore6.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore6.setMessage("Cardanocoin test1");
        addressStore6.setMemo("memo Cardano ADA");
        addressStore6.setNumTransactions(1);
        addressList.add(addressStore6);

        return addressList;
    }

    /**
     *
     *
     * returns addreess
     *    Ticker    value   wallet
     *  BTC       1.1     allan
     *  DASH      10.2    allan
     *  BTC       30.0    peter
     *  ADA       20.0    peter
     *  DASH      10.1    allan
     *  ETH       40.1    peter
     *
     * @return
     */
    private List<BlockchainAddressStore> getAddresses6a() {
        Date rightNow = new Date();
        List<BlockchainAddressStore> addressList = new ArrayList<>();

        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setWalletName("allan");
        addressStore.setTicker("BTC");
        addressStore.setLastBalance( 1.1);
        addressStore.setAddress("0bbbbbbbbbbbbbbbb1");
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Bitcoin test1");
        addressStore.setMemo("memo BTC");
        addressStore.setNumTransactions(3);
        addressList.add(addressStore);


        addressStore = new BlockchainAddressStore();
        addressStore.setWalletName("allan");
        addressStore.setTicker("DASH");
        addressStore.setAddress("Xaaaaaaaaaaaaaaaaaaaaa2");
        addressStore.setLastBalance( 10.2);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Dash test");
        addressStore.setMemo("memo dash1");
        addressStore.setNumTransactions(1);
        addressList.add(addressStore);

        addressStore = new BlockchainAddressStore();
        addressStore.setWalletName("peter");
        addressStore.setTicker("BTC");
        addressStore.setLastBalance( 30.0);
        addressStore.setAddress("0bbbbbbbbbbbbbbbb2");
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Bitcoin test2");
        addressStore.setMemo("memo BTC");
        addressStore.setNumTransactions(1);
        addressList.add(addressStore);

        addressStore = new BlockchainAddressStore();
        addressStore.setWalletName("peter");
        addressStore.setAddress("0edddddddddddddddddddog1");
        addressStore.setTicker("ADA");
        addressStore.setLastBalance( 20.0);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Cardanocoin test1");
        addressStore.setMemo("memo Cardano ADA");
        addressStore.setNumTransactions(1);
        addressList.add(addressStore);

        addressStore = new BlockchainAddressStore();
        addressStore.setWalletName("allan");
        addressStore.setTicker("DASH");
        addressStore.setAddress("Xaaaaaaaaaaaaaaaaaaaaa1");
        addressStore.setLastBalance( 10.1);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Dash test");
        addressStore.setMemo("memo dash1");
        addressStore.setNumTransactions(1);
        addressList.add(addressStore);

        addressStore = new BlockchainAddressStore();
        addressStore.setAddress("0ethethewthewthwthethhhhhh1");
        addressStore.setWalletName("peter");
        addressStore.setTicker("ETH");
        addressStore.setLastBalance( 40.1);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Oshirium test1");
        addressStore.setMemo("memo ETH");
        addressStore.setNumTransactions(1);
        addressList.add(addressStore);

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
        addressStore.setTicker("DASH");
        addressStore.setAddress("Xaaaaaaaaaaaaaaaaaaaaa1");
        addressStore.setLastBalance( 10.1);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Dash test");
        addressStore.setMemo("memo dash1");
        addressStore.setNumTransactions(1);
        addressList.add(addressStore);

        BlockchainAddressStore addressStore2 = new BlockchainAddressStore();
        addressStore2.setWalletName("allan");
        addressStore2.setTicker("DASH");
        addressStore2.setAddress("Xaaaaaaaaaaaaaaaaaaaaa2");
        addressStore2.setLastBalance( 10.2);
        addressStore2.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore2.setMessage("Dash test");
        addressStore2.setMemo("memo dash1");
        addressStore2.setNumTransactions(1);
        addressList.add(addressStore2);

        BlockchainAddressStore addressStore3 = new BlockchainAddressStore();
        addressStore3.setWalletName("allan");
        addressStore3.setTicker("BTC");
        addressStore3.setLastBalance( 1.1);
        addressStore3.setAddress("0bbbbbbbbbbbbbbbb1");
        addressStore3.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore3.setMessage("Bitcoin test1");
        addressStore3.setMemo("memo BTC");
        addressStore3.setNumTransactions(3);
        addressList.add(addressStore3);

        BlockchainAddressStore addressStore4 = new BlockchainAddressStore();
        addressStore4.setWalletName("peter");
        addressStore4.setTicker("BTC");
        addressStore4.setLastBalance( 30.0);
        addressStore4.setAddress("0bbbbbbbbbbbbbbbb2");
        addressStore4.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore4.setMessage("Bitcoin test2");
        addressStore4.setMemo("memo BTC");
        addressStore4.setNumTransactions(1);
        addressList.add(addressStore4);

        BlockchainAddressStore addressStore5 = new BlockchainAddressStore();
        addressStore5.setAddress("0ethethewthewthwthethhhhhh1");
        addressStore5.setWalletName("peter");
        addressStore5.setTicker("ETH");
        addressStore5.setLastBalance( 40.1);
        addressStore5.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore5.setMessage("Oshirium test1");
        addressStore5.setMemo("memo ETH");
        addressStore5.setNumTransactions(1);
        addressList.add(addressStore5);

        BlockchainAddressStore addressStore6 = new BlockchainAddressStore();
        addressStore6.setWalletName("peter");
        addressStore6.setAddress("0edddddddddddddddddddog1");
        addressStore6.setTicker("ADA");
        addressStore6.setLastBalance( 20.0);
        addressStore6.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore6.setMessage("Cardanocoin test1");
        addressStore6.setMemo("memo Cardano ADA");
        addressStore6.setNumTransactions(1);
        addressList.add(addressStore6);

        return addressList;
    }

}