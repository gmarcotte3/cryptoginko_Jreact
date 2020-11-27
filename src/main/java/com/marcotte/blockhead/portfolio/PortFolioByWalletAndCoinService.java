package com.marcotte.blockhead.portfolio;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.BlockchainAddressStoreService;
import com.marcotte.blockhead.datastore.CoinService;
import com.marcotte.blockhead.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
public class PortFolioByWalletAndCoinService {


    @Autowired
    private BlockchainAddressStoreService blockchainAddressStoreService;

    @Autowired
    private CoinService coinService;



    /**
     * return a list of crypto currencies that are all the addresses sum over balances grouped by the wallet and then
     * the currency. This gives you a balance of each wallet's coins.
     *
     * @return
     */
    public List<WalletDTO> findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc()
    {
        List<BlockchainAddressStore> foundLatestOrderedByCurrency =
                blockchainAddressStoreService.findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc();

        return sumByWalletAndCoin(foundLatestOrderedByCurrency);
    }

    public List<WalletDTO> sumByWalletAndCoin(List<BlockchainAddressStore> foundLatestOrderedByCurrency)
    {
        List<CoinDTO> summedByCurrency = new ArrayList<CoinDTO>();
        List<WalletDTO> summedByWallet = new ArrayList<WalletDTO>();

        HashMap<String, CoinDTO> coinMap = coinService.findAllReturnTickerCoinDTOMap();


        // if there are no coins found then return []
        if ( foundLatestOrderedByCurrency.size() == 0) {
            return summedByWallet;
        }

        Double runningBalance = 0.0;
        String currentCoin = "";
        String currentWallet = "";
        boolean firstPass = true;
        for (BlockchainAddressStore addr : foundLatestOrderedByCurrency ) {
            if ( firstPass ) {
                currentWallet = addr.getWalletName();
                currentCoin = addr.getTicker();
                firstPass = false;
            }
            if ( currentWallet.compareToIgnoreCase(addr.getWalletName()) != 0 ) {
                CoinDTO newCoinDTO = new CoinDTO();
                newCoinDTO.setCoinBalance(runningBalance);
                newCoinDTO.setTicker(currentCoin);
                newCoinDTO.setCoinName(getCoinNameFromTicker(currentCoin,  coinMap));
                summedByCurrency.add(newCoinDTO);

                WalletDTO walletDTO = new WalletDTO();
                walletDTO.setWalletName(currentWallet);
                walletDTO.setCoinDTOs(summedByCurrency);
                summedByWallet.add( walletDTO);

                summedByCurrency = new ArrayList<CoinDTO>();
                currentCoin = addr.getTicker();
                runningBalance = addr.getLastBalance();
                currentWallet = addr.getWalletName();
            } else if (currentCoin.compareToIgnoreCase(addr.getTicker() )!= 0 ) {
                CoinDTO newCoinDTO = new CoinDTO();
                newCoinDTO.setCoinBalance(runningBalance);
                newCoinDTO.setTicker(currentCoin);
                newCoinDTO.setCoinName(getCoinNameFromTicker(currentCoin,  coinMap));
                summedByCurrency.add(newCoinDTO);
                runningBalance = addr.getLastBalance();
                currentCoin = addr.getTicker();
            } else {
                runningBalance += addr.getLastBalance();
            }
        }
        // save the last item
        CoinDTO newCoinDTO = new CoinDTO();
        newCoinDTO.setCoinBalance(runningBalance);
        newCoinDTO.setTicker(currentCoin);
        newCoinDTO.setCoinName(getCoinNameFromTicker(currentCoin,  coinMap));
        summedByCurrency.add(newCoinDTO);

        WalletDTO walletDTO = new WalletDTO();
        walletDTO.setWalletName(currentWallet);
        walletDTO.setCoinDTOs(summedByCurrency);
        summedByWallet.add( walletDTO);

        return summedByWallet;

    }

    public CoinList findAllByCoinNameAndWalletNameAndSummerize(String cryptoName, String walletName)
    {
        List<BlockchainAddressStore> blockchainAddressStores;
        blockchainAddressStores = blockchainAddressStoreService.findAllByCoinNameAndWalletName(cryptoName.toUpperCase(), walletName.toUpperCase());

        CoinList coinlist = new CoinList();
        coinlist.setCoinName(cryptoName);
        coinlist.setCoins(blockchainAddressStores);
        coinlist.calculateCoinBalance();
        return coinlist;
    }

    /**
     * gets all the addresstores of a given crypto currency sorted by wallet
     * accumilate balance by wallet and over all and returns this summary
     *
     * @param cryptoName
     * @return
     */
    public WalletList summarizeAddressStoreByCoinNameAndWalletName(String cryptoName)
    {
        WalletList walletList = new WalletList();
        walletList.setCryptoName(cryptoName);

        List<BlockchainAddressStore> addressStores =
                blockchainAddressStoreService.findAllByCoinName(cryptoName);

        addressStores.sort( new Comparator<BlockchainAddressStore>() {
            @Override
            public int compare(BlockchainAddressStore lhs, BlockchainAddressStore rhs) {
                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                return lhs.getWalletName().compareToIgnoreCase(rhs.getWalletName());
            }
        });

        if ( addressStores != null) {
            Wallet currentWallet = new Wallet();
            currentWallet.setCryptoName(cryptoName);
            currentWallet.setWalletName( addressStores.get(0).getWalletName());
            walletList.addWallet(currentWallet);

            for (BlockchainAddressStore addr: addressStores )
            {
                if ( currentWallet.getWalletName().compareToIgnoreCase(addr.getWalletName()) != 0)
                {
                    walletList.addBalance(currentWallet.getBalance());
                    currentWallet = new Wallet();
                    currentWallet.setCryptoName(cryptoName);
                    currentWallet.setWalletName( addr.getWalletName());
                    walletList.addWallet(currentWallet);
                }
                currentWallet.addAddressStores(addr);
                currentWallet.addBalance(addr.getLastBalance());
            }
            // save the last wallet.
            walletList.addBalance(currentWallet.getBalance());

        }
        return walletList;
    }

    private String getCoinNameFromTicker(String ticker, HashMap<String, CoinDTO>  coinMap) {
        CoinDTO coinDTO = coinMap.get(ticker);
        if ( coinDTO != null ) {
            return coinDTO.getCoinName();
        } else {
            return "UknownCoin";
        }
    }

}
