package com.marcotte.blockhead.services.portfolio;

import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressStore;
import com.marcotte.blockhead.services.blockchainaddressstore.BlockchainAddressStoreService;
import com.marcotte.blockhead.services.coin.CoinService;
import com.marcotte.blockhead.model.coin.CoinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PortfolioByCoinsService {


    @Autowired
    private BlockchainAddressStoreService blockchainAddressStoreService;

    @Autowired
    private CoinService coinService;

    /**
     * return a list of crypto currencies that are all the addresses sum over balances grouped by the
     * coin. This gives you a balance of each of the latest coins in the coin address store.
     *
     * This function sets the ticker, coinname and coin balance, other fields are null and must be
     * filled in by the caller.
     *
     * @return
     */
    public List<CoinDTO> findAllLatestSumBalanceGroupByCoin( )
    {
        List<BlockchainAddressStore> foundLatestOrderedByCurrency = blockchainAddressStoreService.findAllLatestOrderByCoin();
        return sumByCryptoCurrency( foundLatestOrderedByCurrency);

    }

    private List<CoinDTO> sumByCryptoCurrency(List<BlockchainAddressStore> foundLatestOrderedByName) {
        List<CoinDTO> summedByCurrency = new ArrayList<CoinDTO>();

        HashMap<String, CoinDTO>  coinMap = coinService.findAllReturnTickerCoinDTOMap();
        // if there are no coins found then return []
        if ( foundLatestOrderedByName.size() == 0) {
            return summedByCurrency;
        }

        Double runningBalance = 0.0;
        String currentCoin = "";
        for (BlockchainAddressStore addr : foundLatestOrderedByName ) {
            if ( currentCoin.length() == 0) {
                currentCoin = addr.getTicker();
                runningBalance = addr.getLastBalance();
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

        return summedByCurrency;
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
