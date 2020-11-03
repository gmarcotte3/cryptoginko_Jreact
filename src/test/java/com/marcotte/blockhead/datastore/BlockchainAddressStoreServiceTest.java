package com.marcotte.blockhead.datastore;

import com.marcotte.blockhead.model.Coin;
import com.marcotte.blockhead.util.Utils;
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
    public BlockchainAddressStoreService addressStoreService;

    @Autowired
    public BlockchainAddressStoreRepository addressStoreRepository;

    @Test
    public void save()
    {
        Date rightNow = new Date();
        BlockchainAddressStore addressStore = getAddress1(rightNow);
        addressStoreService.save(addressStore);
        assertTrue(addressStore.getId() != null &&  addressStore.getId() > 0);

        List<BlockchainAddressStore> savedBlock = addressStoreService.findByAddressAndCurrency("1234567890", "JPYT");
        assertEquals(1, savedBlock.size());

        assertEquals("1234567890", savedBlock.get(0).getAddress() );
        assertEquals("JPYT", savedBlock.get(0).getCurrency() );
        assertEquals("Test balance", savedBlock.get(0).getMessage() );
        assertEquals((Integer) 42, (Integer) savedBlock.get(0).getNumTransactions() );
        assertEquals(new Timestamp(rightNow.getTime()), savedBlock.get(0).getLastUpdated() );
        assertEquals("memo1", savedBlock.get(0).getMemo());
        assertTrue(savedBlock.get(0).getId() != null &&  savedBlock.get(0).getId() > 0);

        addressStoreRepository.deleteAll();
    }

    /**
     * test the condition where the balance changes. it should create two records with the older one pointing to the newer one.
     */
    @Test
    public void saveWithHistory()
    {
        Date rightNow = new Date();
        BlockchainAddressStore addressStore = getAddress1(rightNow);
        addressStoreService.saveWithHistory(addressStore);
        assertTrue(addressStore.getId() != null &&  addressStore.getId() > 0);

        List<BlockchainAddressStore> savedBlock = addressStoreService.findByAddressAndCurrency("1234567890", "JPYT");
        assertEquals(1, savedBlock.size());

        assertEquals("1234567890", savedBlock.get(0).getAddress() );
        assertEquals("JPYT", savedBlock.get(0).getCurrency() );
        assertEquals("Test balance", savedBlock.get(0).getMessage() );
        assertEquals((Integer) 42, (Integer) savedBlock.get(0).getNumTransactions() );
        assertEquals(new Timestamp(rightNow.getTime()), savedBlock.get(0).getLastUpdated() );
        assertEquals("memo1", savedBlock.get(0).getMemo());
        assertTrue(savedBlock.get(0).getId() != null &&  savedBlock.get(0).getId() > 0);


        BlockchainAddressStore addressStore2 = new BlockchainAddressStore(addressStore);
        addressStore2.setLastBalance(addressStore.getLastBalance() * 1.5);
        addressStore2.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStoreService.saveWithHistory(addressStore2);
        List<BlockchainAddressStore> savedBlock2 = addressStoreService.findByAddressAndCurrency("1234567890", "JPYT");
        assertEquals(2, savedBlock2.size());
        assertEquals( savedBlock2.get(0).getNextId(), savedBlock2.get(1).getId());
        assertEquals( savedBlock2.get(0).getNextId(), addressStore2.getId() );

        BlockchainAddressStore latestSavedBlock = addressStoreService.findLatestByAddressAndCurrency("1234567890","JPYT");
        assertEquals("1234567890", latestSavedBlock.getAddress() );
        assertEquals("JPYT", latestSavedBlock.getCurrency() );
        assertEquals("Test balance", latestSavedBlock.getMessage() );
        assertEquals((Integer) 42, (Integer) latestSavedBlock.getNumTransactions() );
        assertEquals(new Timestamp(rightNow.getTime()), latestSavedBlock.getLastUpdated() );
        assertEquals("memo1", latestSavedBlock.getMemo());
        assertEquals(addressStore2.getId(), latestSavedBlock.getId());
        assertTrue(Utils.almostEqual(addressStore2.getLastBalance(), latestSavedBlock.getLastBalance()));

        addressStoreRepository.deleteAll();
    }

    /**
     * test save where no change in balance happens so its just an update.
     */
    @Test
    public void saveWithHistory2()
    {
        Date rightNow = new Date();
        BlockchainAddressStore addressStore = getAddress1(rightNow);
        addressStoreService.saveWithHistory(addressStore);
        assertTrue(addressStore.getId() != null &&  addressStore.getId() > 0);

        List<BlockchainAddressStore> savedBlock = addressStoreService.findByAddressAndCurrency("1234567890", "JPYT");
        assertEquals(1, savedBlock.size());

        assertEquals("1234567890", savedBlock.get(0).getAddress() );
        assertEquals("JPYT", savedBlock.get(0).getCurrency() );
        assertEquals("Test balance", savedBlock.get(0).getMessage() );
        assertEquals((Integer) 42, (Integer) savedBlock.get(0).getNumTransactions() );
        assertEquals(new Timestamp(rightNow.getTime()), savedBlock.get(0).getLastUpdated() );
        assertEquals("memo1", savedBlock.get(0).getMemo());
        assertTrue(savedBlock.get(0).getId() != null &&  savedBlock.get(0).getId() > 0);

        addressStore.setMemo("memo2");
        addressStore.setMessage("Test balance2");
        Date moreRightNow = new Date();
        addressStore.setLastUpdated(new Timestamp(moreRightNow.getTime()));
        addressStoreService.saveWithHistory(addressStore);

        List<BlockchainAddressStore> savedBlock2 = addressStoreService.findByAddressAndCurrency("1234567890", "JPYT");
        assertEquals(1, savedBlock2.size());

        BlockchainAddressStore latestSavedBlock = addressStoreService.findLatestByAddressAndCurrency("1234567890", "JPYT");
        assertEquals( addressStore.getId(),                           latestSavedBlock.getId());
        assertEquals("1234567890",                          latestSavedBlock.getAddress() );
        assertEquals("JPYT",                                latestSavedBlock.getCurrency() );
        assertEquals("Test balance2",                       latestSavedBlock.getMessage() );
        assertEquals((Integer) 42, (Integer)                          latestSavedBlock.getNumTransactions() );
        assertEquals(new Timestamp(moreRightNow.getTime()),           latestSavedBlock.getLastUpdated() );
        assertEquals("memo2",                               latestSavedBlock.getMemo());
        assertTrue(Utils.almostEqual(addressStore.getLastBalance(),   latestSavedBlock.getLastBalance()));

        addressStoreRepository.deleteAll();
    }

    @Test
    public void findAllByCoinName()
    {
        List<BlockchainAddressStore> listOfAddresses = getAddresses5();
        for (BlockchainAddressStore addressStore : listOfAddresses ) {
            addressStoreService.saveWithHistory(addressStore);
        }

        List<BlockchainAddressStore> dashList = addressStoreService.findAllByCoinName("DASH");

        assertEquals(2, dashList.size());
        assertEquals("X023232333332223j43jj3", dashList.get(0).getAddress());
        assertEquals("X024443w33323j43jj3", dashList.get(1).getAddress());
        addressStoreRepository.deleteAll();
    }

    @Test
    public void findAll()
    {
        Date rightNow = new Date();
        BlockchainAddressStore addressStore = getAddress1(rightNow);
        addressStoreRepository.save(addressStore);

        rightNow = new Date();
        addressStore = getAddress2(rightNow);
        addressStoreRepository.save(addressStore);

        List<BlockchainAddressStore> foundAddress = addressStoreService.findAll();
        assertEquals(2, foundAddress.size());

        addressStoreRepository.deleteAll();
    }

    /**
     * testing if we can do a group by currency summing up the balances.
     */
    @Test public void findAllLatest_test() {
        List<BlockchainAddressStore> listOfAddresses = getAddresses6();
        for (BlockchainAddressStore addressStore : listOfAddresses ) {
            addressStoreService.saveWithHistory(addressStore);
        }
        Date rightNow = new Date();

        listOfAddresses.get(0).setLastBalance(1.0);
        listOfAddresses.get(0).setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStoreService.saveWithHistory(listOfAddresses.get(0));
        // dash should have balance of 3.0

        List<Coin> foundAddresses = addressStoreService.findAllLatestSumBalanceGroupByCurency();
        assertTrue( foundAddresses.get(0).getCoinBalance() > 19.0 && foundAddresses.get(0).getCoinBalance() < 21.0 );
        assertEquals( "ADA", foundAddresses.get(0).getTicker());

        assertTrue( foundAddresses.get(2).getCoinBalance() > 2.0 && foundAddresses.get(2).getCoinBalance() < 4.0 );

        addressStoreRepository.deleteAll();
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
        addressStoreRepository.save(addressStore);
        BlockchainAddressStore addressStore3a = getAddress3a(rightNow);
        addressStoreRepository.save(addressStore3a);

        // look for the first address
        List<BlockchainAddressStore> foundAddres = addressStoreService
            .findByAddressAndCurrency(addressStore.getAddress(), addressStore.getCurrency());
        assertEquals(1, foundAddres.size());
        assertEquals(addressStore.getMessage(), foundAddres.get(0).getMessage() );
        assertEquals(addressStore.getCurrency(), foundAddres.get(0).getCurrency() );

        // look for the second address.
        List<BlockchainAddressStore> foundAddres2 = addressStoreService
            .findByAddressAndCurrency(addressStore.getAddress(), addressStore3a.getCurrency());
        assertEquals(1, foundAddres2.size());
        assertEquals(addressStore3a.getMessage(), foundAddres2.get(0).getMessage() );
        assertEquals(addressStore3a.getCurrency(), foundAddres2.get(0).getCurrency() );

        addressStoreRepository.deleteAll();
    }

    @Test
    public void findByAddressAndNextID()
    {
        Date rightNow = new Date();
        BlockchainAddressStore addressStore = getAddress4(rightNow);
        addressStoreRepository.save(addressStore);

        BlockchainAddressStore addressStore3b = new BlockchainAddressStore( addressStore );
        addressStore3b.setLastBalance(addressStore.getLastBalance() * 1.1F);
        addressStoreRepository.save(addressStore3b);

        addressStore.setNextId(addressStore3b.getId());
        addressStoreRepository.save(addressStore);

        List<BlockchainAddressStore> foundAddres = addressStoreService.findByAddressAndCurrency(addressStore.getAddress(), addressStore.getCurrency());
        assertEquals(2,foundAddres.size());

        BlockchainAddressStore foundAddres1 = addressStoreService.findLatestByAddressAndCurrency(addressStore.getAddress(), addressStore.getCurrency());
        assertEquals( addressStore3b.getId() , foundAddres1.getId());


        List<BlockchainAddressStore> foundAddres2 = addressStoreService.findByAddressAndNextId(addressStore.getAddress(), addressStore.getCurrency(), addressStore.getNextId());
        assertEquals(1, foundAddres2.size() );
        assertEquals( addressStore.getId() , foundAddres2.get(0).getId());

        addressStoreRepository.deleteAll();
    }

    private BlockchainAddressStore getAddress1(Date rightNow)
    {
        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("1234567890");
        addressStore.setCurrency("JPYT");
        addressStore.setLastBalance( 123456.4323);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Test balance");
        addressStore.setMemo("memo1");
        addressStore.setNumTransactions(42);
        addressStore.setNextId( null );
        return addressStore;
    }


    private BlockchainAddressStore getAddress2(Date rightNow)
    {
        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("9876543210");
        addressStore.setCurrency("JPYT");
        addressStore.setLastBalance( 999956.4323);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Test balance 2");
        addressStore.setNumTransactions(88);
        addressStore.setMemo("memo2");
        addressStore.setNextId( null );
        return addressStore;
    }

    private BlockchainAddressStore getAddress3(Date rightNow)
    {
        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("9876HomerWasHere543210");
        addressStore.setCurrency("JPYT");
        addressStore.setLastBalance( 99229956.4323);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Test balance 3");
        addressStore.setMemo("memo3");
        addressStore.setNumTransactions(98);
        addressStore.setNextId( null );
        return addressStore;
    }

    private BlockchainAddressStore getAddress3a(Date rightNow)
    {
        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("9876HomerWasHere543210");
        addressStore.setCurrency("USDT");
        addressStore.setLastBalance( 99.29);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Test balance 3a");
        addressStore.setMemo("memo3a");
        addressStore.setNumTransactions(98);
        addressStore.setNextId( null );
        return addressStore;
    }


    private BlockchainAddressStore getAddress4(Date rightNow)
    {
        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("X023sdh23kjh2323j43jj3");
        addressStore.setCurrency("JPYT");
        addressStore.setLastBalance( 9956.23);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Test balance 4");
        addressStore.setMemo("memo4");
        addressStore.setNumTransactions(3);
        addressStore.setNextId( null );
        return addressStore;
    }

    private List<BlockchainAddressStore> getAddresses5()
    {
        Date rightNow = new Date();
        List<BlockchainAddressStore> addressList = new ArrayList<>();

        BlockchainAddressStore addressStore = new BlockchainAddressStore();
        addressStore.setAddress("X023232333332223j43jj3");
        addressStore.setCurrency("DASH");
        addressStore.setLastBalance( 1.0);
        addressStore.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore.setMessage("Dash test");
        addressStore.setMemo("memo dash1");
        addressStore.setNumTransactions(1);
        addressStore.setNextId( null );
        addressList.add(addressStore);

        Date rightNow2 = new Date();
        BlockchainAddressStore addressStore2 = new BlockchainAddressStore(addressStore);

        addressStore2.setLastBalance( 10.0);
        addressStore2.setLastUpdated( new Timestamp(rightNow2.getTime()));
        addressList.add(addressStore2);


        BlockchainAddressStore addressStore3 = new BlockchainAddressStore();
        addressStore3.setAddress("0b1234sdsds2325456");
        addressStore3.setCurrency("BTC");
        addressStore3.setLastBalance( 2.0);
        addressStore3.setLastUpdated( new Timestamp(rightNow.getTime()));
        addressStore3.setMessage("Bitcoin test");
        addressStore3.setMemo("memo BTC");
        addressStore3.setNumTransactions(1);
        addressStore3.setNextId( null );
        addressList.add(addressStore3);

        Date rightNow3 = new Date();
        BlockchainAddressStore addressStore4 = new BlockchainAddressStore();
        addressStore4.setAddress("0b1223232345355466");
        addressStore4.setCurrency("BTC");
        addressStore4.setLastBalance( 0.001200);
        addressStore4.setLastUpdated( new Timestamp(rightNow3.getTime()));
        addressStore4.setMessage("Bitcoin test2");
        addressStore4.setMemo("memo BTC");
        addressStore4.setNumTransactions(5);
        addressStore4.setNextId( null );
        addressList.add(addressStore4);

        Date rightNow4 = new Date();
        BlockchainAddressStore addressStore5 = new BlockchainAddressStore(addressStore4);
        addressStore5.setLastUpdated( new Timestamp(rightNow4.getTime()));
        addressStore5.setLastBalance( 100.120);
        addressList.add(addressStore5);

        BlockchainAddressStore addressStore6 = new BlockchainAddressStore();
        addressStore6.setAddress("X024443w33323j43jj3");
        addressStore6.setCurrency("DASH");
        addressStore6.setLastBalance( 1000.0);
        addressStore6.setLastUpdated( new Timestamp(rightNow4.getTime()));
        addressStore6.setMessage("Dash big test");
        addressStore6.setMemo("memo dash2");
        addressStore6.setNumTransactions(5);
        addressStore6.setNextId( null );
        addressList.add(addressStore6);

        return addressList;
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
}