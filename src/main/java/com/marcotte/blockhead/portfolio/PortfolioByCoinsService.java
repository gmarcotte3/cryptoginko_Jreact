package com.marcotte.blockhead.portfolio;

import com.marcotte.blockhead.datastore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.BlockchainAddressStoreRepository;
import com.marcotte.blockhead.model.CoinDTO;
import com.marcotte.blockhead.model.CryptoNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioByCoinsService {

    @Autowired
    private BlockchainAddressStoreRepository blockchainAddressStoreRepository;

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
        List<BlockchainAddressStore> foundLatestOrderedByCurrency = blockchainAddressStoreRepository.findBlockchainAddressStoreByNextIdOrderByCurrency(null);
        return sumByCryptoCurrency( foundLatestOrderedByCurrency);

    }

    public List<CoinDTO> sumByCryptoCurrency(List<BlockchainAddressStore> foundLatestOrderedByName) {
        List<CoinDTO> summedByCurrency = new ArrayList<CoinDTO>();

        // if there are no coins found then return []
        if ( foundLatestOrderedByName.size() == 0) {
            return summedByCurrency;
        }

        Double runningBalance = 0.0;
        String currentCoin = "";
        for (BlockchainAddressStore addr : foundLatestOrderedByName ) {
            if ( currentCoin.length() == 0) {
                currentCoin = addr.getCurrency();
                runningBalance = addr.getLastBalance();
            } else if (currentCoin.compareToIgnoreCase(addr.getCurrency() )!= 0 ) {
                CoinDTO newCoinDTO = new CoinDTO();
                newCoinDTO.setCoinBalance(runningBalance);
                newCoinDTO.setTicker(currentCoin);
                newCoinDTO.setCoinName((CryptoNames.valueOfCode(currentCoin)).getName());
                summedByCurrency.add(newCoinDTO);
                runningBalance = addr.getLastBalance();
                currentCoin = addr.getCurrency();
            } else {
                runningBalance += addr.getLastBalance();
            }
        }
        // save the last item
        CoinDTO newCoinDTO = new CoinDTO();
        newCoinDTO.setCoinBalance(runningBalance);
        newCoinDTO.setTicker(currentCoin);
        newCoinDTO.setCoinName((CryptoNames.valueOfCode(currentCoin)).getName());
        summedByCurrency.add(newCoinDTO);

        return summedByCurrency;
    }
}
