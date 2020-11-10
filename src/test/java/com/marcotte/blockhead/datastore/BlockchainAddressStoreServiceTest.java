package com.marcotte.blockhead.datastore;

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

import static org.junit.Assert.*;

@TestPropertySource("BlockchainAddressStoreServiceTest.properties")
@RunWith(SpringRunner.class)
@DirtiesContext
@SpringBootTest
public class BlockchainAddressStoreServiceTest
{
    @Autowired
    public BlockchainAddressStoreService blockchainAddressStoreService;


    @Test
    public void save()
    {
        Date rightNow = new Date();
        BlockchainAddressStore addressStore = getAddress1(rightNow);
        blockchainAddressStoreService.save(addressStore);
        assertTrue(addressStore.getId() != null &&  addressStore.getId() > 0);

        List<BlockchainAddressStore> savedBlock = blockchainAddressStoreService.findByAddressAndCurrency("1234567890", "USDT");
        assertEquals(1, savedBlock.size());

        assertEquals("1234567890", savedBlock.get(0).getAddress() );
        assertEquals("USDT", savedBlock.get(0).getTicker() );
        assertEquals("Test balance", savedBlock.get(0).getMessage() );
        assertEquals((Integer) 42, (Integer) savedBlock.get(0).getNumTransactions() );
        assertEquals(new Timestamp(rightNow.getTime()), savedBlock.get(0).getLastUpdated() );
        assertEquals("memo1", savedBlock.get(0).getMemo());
        assertTrue(savedBlock.get(0).getId() != null &&  savedBlock.get(0).getId() > 0);
        assertEquals( "TESTWALLET", savedBlock.get(0).getWalletName());

        blockchainAddressStoreService.deleteAll();
    }


    /**
     * testing finding by coin ticker and the case where the record was modified
     * we should finde 2 DASH not 3.
     */
    @Test
    public void findAllByCoinName()
    {
        List<BlockchainAddressStore> listOfAddresses = getAddresses5();
        for (BlockchainAddressStore addressStore : listOfAddresses ) {
            blockchainAddressStoreService.save(addressStore);
        }

        List<BlockchainAddressStore> dashList = blockchainAddressStoreService.findAllByCoinName("DASH");

        assertEquals(2, dashList.size());
        assertEquals("X023232333332223j43jj3", dashList.get(0).getAddress());
        assertEquals("X024443w33323j43jj3", dashList.get(1).getAddress());
        blockchainAddressStoreService.deleteAll();
    }

    @Test
    public void findAll()
    {
        Date rightNow = new Date();
        BlockchainAddressStore addressStore = getAddress1(rightNow);
        blockchainAddressStoreService.save(addressStore);

        rightNow = new Date();
        BlockchainAddressStore addressStore2 = getAddress2(rightNow);
        blockchainAddressStoreService.save(addressStore2);

        List<BlockchainAddressStore> foundAddress = blockchainAddressStoreService.findAll();
        assertEquals(2, foundAddress.size());

        blockchainAddressStoreService.deleteAll();
    }


    /**
     * test to see if two different currencies with the same address can be stored and retrived
     * correctly. This is the case with ETH tokens, they all have the same base ETH address.
     */
    @Test
    public void findByAddress()
    {
        Date rightNow = new Date();
        BlockchainAddressStore addressStore = getAddress3(rightNow);
        blockchainAddressStoreService.save(addressStore);
        BlockchainAddressStore addressStore3a = getAddress3a(rightNow);
        blockchainAddressStoreService.save(addressStore3a);

        // look for the first address
        List<BlockchainAddressStore> foundAddres = blockchainAddressStoreService
            .findByAddressAndCurrency(addressStore.getAddress(), addressStore.getTicker());
        assertEquals(1, foundAddres.size());
        assertEquals(addressStore.getMessage(), foundAddres.get(0).getMessage() );
        assertEquals(addressStore.getTicker(), foundAddres.get(0).getTicker() );

        // look for the second address.
        List<BlockchainAddressStore> foundAddres2 = blockchainAddressStoreService
            .findByAddressAndCurrency(addressStore.getAddress(), addressStore3a.getTicker());
        assertEquals(1, foundAddres2.size());
        assertEquals(addressStore3a.getMessage(), foundAddres2.get(0).getMessage() );
        assertEquals(addressStore3a.getTicker(), foundAddres2.get(0).getTicker() );

        blockchainAddressStoreService.deleteAll();
    }

    @Test
    public void findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc() {
        List<BlockchainAddressStore> addressStores6 =  getAddresses6();
        for (BlockchainAddressStore addressStore : addressStores6 ) {
            blockchainAddressStoreService.save(addressStore);
        }

        List<BlockchainAddressStore> sortedArray = blockchainAddressStoreService.findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc();
        assertEquals(6, sortedArray.size());
        assertEquals("AYA-BTC", (sortedArray.get(0).getWalletName() + "-" + sortedArray.get(0).getTicker() ));
        assertEquals("AYA-ETH", (sortedArray.get(2).getWalletName() + "-" + sortedArray.get(2).getTicker() ));
        assertEquals("MIKA-DASH", (sortedArray.get(5).getWalletName() + "-" + sortedArray.get(5).getTicker() ));

        blockchainAddressStoreService.deleteAll();
    }

    @Test
    public void findAllLatestOrderByCoin() {

        List<BlockchainAddressStore> addressStores6 =  getAddresses6();
        for (BlockchainAddressStore addressStore : addressStores6 ) {
            blockchainAddressStoreService.save(addressStore);
        }

        List<BlockchainAddressStore> sortedArray = blockchainAddressStoreService.findAllLatestOrderByCoin();
        assertEquals(6, sortedArray.size());
        assertEquals("ADA", (sortedArray.get(0).getTicker() ));
        assertEquals("BTC", ( sortedArray.get(2).getTicker() ));
        assertEquals("ETH", (sortedArray.get(5).getTicker() ));
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
        addressStore.setWalletName("testWallet");
        return addressStore;
    }


    private BlockchainAddressStore getAddress2(Date rightNow)
    {
        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("9876543210");
        addressStore.setTicker("USDT");
        addressStore.setLastBalance( 999956.4323);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Test balance 2");
        addressStore.setNumTransactions(88);
        addressStore.setMemo("memo2");
        return addressStore;
    }

    private BlockchainAddressStore getAddress3(Date rightNow)
    {
        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("9876HomerWasHere543210");
        addressStore.setTicker("USDT");
        addressStore.setLastBalance( 99229956.4323);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Test balance 3");
        addressStore.setMemo("memo3");
        addressStore.setNumTransactions(98);
        return addressStore;
    }

    private BlockchainAddressStore getAddress3a(Date rightNow)
    {
        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("9876HomerWasHere543210");
        addressStore.setTicker("USDC");
        addressStore.setLastBalance( 99.29);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Test balance 3a");
        addressStore.setMemo("memo3a");
        addressStore.setNumTransactions(98);
        return addressStore;
    }


    private BlockchainAddressStore getAddress4(Date rightNow)
    {
        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("X023sdh23kjh2323j43jj3");
        addressStore.setTicker("USDT");
        addressStore.setLastBalance( 9956.23);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Test balance 4");
        addressStore.setMemo("memo4");
        addressStore.setNumTransactions(3);
        return addressStore;
    }

    private List<BlockchainAddressStore> getAddresses5()
    {
        Date rightNow = new Date();
        List<BlockchainAddressStore> addressList = new ArrayList<>();

        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("X023232333332223j43jj3");
        addressStore.setTicker("DASH");
        addressStore.setLastBalance( 1.0);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Dash test");
        addressStore.setMemo("memo dash1");
        addressStore.setNumTransactions(1);
        addressStore.setWalletName("wallet1");
        addressList.add(addressStore);

        // update a record here, this record should replace the existing.
        Date rightNow2 = new Date();
        BlockchainAddressStore addressStore2 = new BlockchainAddressStore(addressStore);
        addressStore2.setLastBalance( 10.0);
        addressStore2.setLastUpdated( new Timestamp(rightNow2.getTime()));
        addressList.add(addressStore2);


        BlockchainAddressStore addressStore3 = new BlockchainAddressStore();
        addressStore3.setAddress("0b1234sdsds2325456");
        addressStore3.setTicker("BTC");
        addressStore3.setLastBalance( 2.0);
        addressStore3.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore3.setMessage("Bitcoin test");
        addressStore3.setMemo("memo BTC");
        addressStore3.setNumTransactions(1);
        addressStore3.setWalletName("wallet1");
        addressList.add(addressStore3);

        Date rightNow3 = new Date();
        BlockchainAddressStore addressStore4 = new BlockchainAddressStore();
        addressStore4.setAddress("0b1223232345355466");
        addressStore4.setTicker("BTC");
        addressStore4.setLastBalance( 0.001200);
        addressStore4.setLastUpdated( new Timestamp(rightNow3.getTime()));
        addressStore4.setMessage("Bitcoin test2");
        addressStore4.setMemo("memo BTC");
        addressStore4.setNumTransactions(5);
        addressStore4.setWalletName("wallet1");
        addressList.add(addressStore4);

        Date rightNow4 = new Date();
        BlockchainAddressStore addressStore5 = new BlockchainAddressStore(addressStore4);
        addressStore5.setLastUpdated( new Timestamp(rightNow4.getTime()));
        addressStore5.setLastBalance( 100.120);
        addressList.add(addressStore5);

        BlockchainAddressStore addressStore6 = new BlockchainAddressStore();
        addressStore6.setAddress("X024443w33323j43jj3");
        addressStore6.setTicker("DASH");
        addressStore6.setLastBalance( 1000.0);
        addressStore6.setLastUpdated( new Timestamp(rightNow4.getTime()));
        addressStore6.setMessage("Dash big test");
        addressStore6.setMemo("memo dash2");
        addressStore6.setNumTransactions(5);
        addressStore6.setWalletName("wallet1");
        addressList.add(addressStore6);

        return addressList;
    }

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
        addressStore.setWalletName("Mika");
        addressList.add(addressStore);

        BlockchainAddressStore addressStore2 = new BlockchainAddressStore();
        addressStore2.setAddress("Xaaaaaaaaaaaaaaaaaaaaa2");
        addressStore2.setTicker("DASH");
        addressStore2.setLastBalance( 2.0);
        addressStore2.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore2.setMessage("Dash test");
        addressStore2.setMemo("memo dash1");
        addressStore2.setNumTransactions(1);
        addressStore2.setWalletName("Aya");
        addressList.add(addressStore2);

        BlockchainAddressStore addressStore3 = new BlockchainAddressStore();
        addressStore3.setAddress("0bbbbbbbbbbbbbbbb1");
        addressStore3.setTicker("BTC");
        addressStore3.setLastBalance( 1.1);
        addressStore3.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore3.setMessage("Bitcoin test1");
        addressStore3.setMemo("memo BTC");
        addressStore3.setNumTransactions(3);
        addressStore3.setWalletName("Aya");
        addressList.add(addressStore3);

        BlockchainAddressStore addressStore4 = new BlockchainAddressStore();
        addressStore4.setAddress("0bbbbbbbbbbbbbbbb2");
        addressStore4.setTicker("BTC");
        addressStore4.setLastBalance( 1.2);
        addressStore4.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore4.setMessage("Bitcoin test2");
        addressStore4.setMemo("memo BTC");
        addressStore4.setNumTransactions(1);
        addressStore4.setWalletName("Mika");
        addressList.add(addressStore4);

        BlockchainAddressStore addressStore5 = new BlockchainAddressStore();
        addressStore5.setAddress("0ethethewthewthwthethhhhhh1");
        addressStore5.setTicker("ETH");
        addressStore5.setLastBalance( 10.0);
        addressStore5.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore5.setMessage("Oshirium test1");
        addressStore5.setMemo("memo ETH");
        addressStore5.setNumTransactions(1);
        addressStore5.setWalletName("Aya");
        addressList.add(addressStore5);

        BlockchainAddressStore addressStore6 = new BlockchainAddressStore();
        addressStore6.setAddress("0edddddddddddddddddddog1");
        addressStore6.setTicker("ADA");
        addressStore6.setLastBalance( 20.0);
        addressStore6.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore6.setMessage("Cardanocoin test1");
        addressStore6.setMemo("memo Cardano ADA");
        addressStore6.setNumTransactions(1);
        addressStore6.setWalletName("Mika");
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