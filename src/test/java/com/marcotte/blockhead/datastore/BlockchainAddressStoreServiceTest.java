package com.marcotte.blockhead.datastore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
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

        List<BlockchainAddressStore> savedBlock = addressStoreService.findByAddress("1234567890");
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

    @Test
    public void findAll()
    {
        Date rightNow = new Date();
        BlockchainAddressStore addressStore = getAddress1(rightNow);
        addressStoreService.save(addressStore);

        rightNow = new Date();
        addressStore = getAddress2(rightNow);
        addressStoreService.save(addressStore);

        List<BlockchainAddressStore> foundAddress = addressStoreService.findAll();
        assertEquals(2, foundAddress.size());

        addressStoreRepository.deleteAll();
    }

    @Test
    public void findByAddress()
    {
        Date rightNow = new Date();
        BlockchainAddressStore addressStore = getAddress3(rightNow);
        addressStoreService.save(addressStore);

        List<BlockchainAddressStore> foundAddres = addressStoreService.findByAddress("9876HomerWasHere543210");
        assertEquals(1, foundAddres.size());
        assertEquals("Test balance 3", foundAddres.get(0).getMessage() );
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
        return addressStore;
    }
}