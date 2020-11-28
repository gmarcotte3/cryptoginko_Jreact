package com.marcotte.blockhead.portfolio;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.BlockchainAddressStoreService;
import com.marcotte.blockhead.datastore.CoinService;
import com.marcotte.blockhead.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    private List<WalletDTO> sumByWalletAndCoin(List<BlockchainAddressStore> foundLatestOrderedByCurrency)
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


    private String getCoinNameFromTicker(String ticker, HashMap<String, CoinDTO>  coinMap) {
        CoinDTO coinDTO = coinMap.get(ticker);
        if ( coinDTO != null ) {
            return coinDTO.getCoinName();
        } else {
            return "UknownCoin";
        }
    }

}
