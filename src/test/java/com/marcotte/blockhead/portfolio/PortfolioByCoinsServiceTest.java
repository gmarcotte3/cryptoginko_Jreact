package com.marcotte.blockhead.portfolio;

import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressStore;
import com.marcotte.blockhead.services.blockchainaddressstore.BlockchainAddressStoreService;
import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.services.portfolio.PortfolioByCoinsService;
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
import static org.junit.Assert.assertTrue;


@TestPropertySource("PortfolioTest.properties")
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class PortfolioByCoinsServiceTest {

    @Autowired
    private PortfolioByCoinsService portfolioByCoinsService;

    @Autowired
    private BlockchainAddressStoreService blockchainAddressStoreService;

    @Test
    public void sumByCryptoCurrency() {
    }

    /**
     * testing if we can do a group by currency summing up the balances.
     */
    @Test
    public void findAllLatestSumBalanceGroupByCoin1() {


        // save 6 addresses --------------------
        /**
         * 6 addresses
         * 0] DASH   323434556767889.0
         * 1] DASH   2.0
         * 2] BTC    1.1
         * 3] BTC    1.2
         * 4] ETH   10.0
         * 5] ADA   20.0
         *
         * Total DASH 323434556767891.0
         *       BTC  2.3
         *       ETH 10.0
         *       ADA 20.0
         *
         * @return
         */
        List<BlockchainAddressStore> listOfAddresses = getAddresses6();
        for (BlockchainAddressStore addressStore : listOfAddresses ) {
            blockchainAddressStoreService.save(addressStore);
        }

        // change the first record with new data so the DASH total will be 3.0
        Date rightNow = new Date();
        listOfAddresses.get(0).setLastBalance(1.0);
        listOfAddresses.get(0).setLastUpdated( new Timestamp(rightNow.getTime()));
        blockchainAddressStoreService.save(listOfAddresses.get(0));

        // find all the latest coins group by crypto curency
        // we should have 4 elements in the array one for ADA, BTC, DASH, ETH and in that order.
        List<CoinDTO> foundAddresses = portfolioByCoinsService.findAllLatestSumBalanceGroupByCoin();

        // check order
        assertEquals( "ADA", foundAddresses.get(0).getTicker());
        assertEquals( "BTC", foundAddresses.get(1).getTicker());
        assertEquals( "DASH", foundAddresses.get(2).getTicker());
        assertEquals( "ETH", foundAddresses.get(3).getTicker());

        // check results.
        assertTrue( Math.abs(foundAddresses.get(0).getCoinBalance() - 20.0) < 0.001 ); // ADA balance
        assertTrue( Math.abs(foundAddresses.get(1).getCoinBalance() - 2.3) < 0.001 );  // BTC balance
        assertTrue( Math.abs(foundAddresses.get(2).getCoinBalance() - 3.0) < 0.001 );  // DASH balance
        assertTrue( Math.abs(foundAddresses.get(3).getCoinBalance() - 10.0) < 0.001 );  // ETH balance

        blockchainAddressStoreService.deleteAll();
    }

    /**
     * check edge case where we dont have any records
     */
    @Test
    public void findAllLatestSumBalanceGroupByCoin2() {
        List<CoinDTO> foundAddresses = portfolioByCoinsService.findAllLatestSumBalanceGroupByCoin();
        int expectedSize = 0;
        assertEquals(expectedSize, foundAddresses.size());
        blockchainAddressStoreService.deleteAll();
    }

    /**
     * create one address and then see if we have any problems with just one
     */
    @Test
    public void findAllLatestSumBalanceGroupByCoin3() {
        Date rightNow = new Date();
        BlockchainAddressStore oneAddress =  getAddress1(rightNow);
        blockchainAddressStoreService.save(oneAddress);

        List<CoinDTO> foundAddresses = portfolioByCoinsService.findAllLatestSumBalanceGroupByCoin();
        int expectedSize = 1;
        assertEquals(expectedSize, foundAddresses.size());
        blockchainAddressStoreService.deleteAll();
    }

    /**
     * get one address of USDT with a balance of 123456.4323
     * @param rightNow
     * @return
     */
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
     * 6 addresses
     * 0] DASH   323434556767889.0
     * 1] DASH   2.0
     * 2] BTC    1.1
     * 3] BTC    1.2
     * 4] ETH   10.0
     * 5] ADA   20.0
     *
     * Total DASH 323434556767891.0
     *       BTC  2.3
     *       ETH 10.0
     *       ADA 20.0
     *
     * @return
     */
    private List<BlockchainAddressStore> getAddresses6() {
        Date rightNow = new Date();
        List<BlockchainAddressStore> addressList = new ArrayList<>();

        // DASH
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

        // BTC
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

        // ETH
        BlockchainAddressStore addressStore5 = new BlockchainAddressStore();
        addressStore5.setAddress("0ethethewthewthwthethhhhhh1");
        addressStore5.setTicker("ETH");
        addressStore5.setLastBalance( 10.0);
        addressStore5.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore5.setMessage("Oshirium test1");
        addressStore5.setMemo("memo ETH");
        addressStore5.setNumTransactions(1);
        addressList.add(addressStore5);

        // ADA
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
     * sorted by wallet, coin
     * @return
     */
    private List<BlockchainAddressStore> getAddresses6a() {
        Date rightNow = new Date();
        List<BlockchainAddressStore> addressList = new ArrayList<>();

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