package com.marcotte.blockhead.services.portfolio;

import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressStore;
import com.marcotte.blockhead.model.coin.CoinDTO;
import com.marcotte.blockhead.model.fiat.FiatCurrencyList;
import com.marcotte.blockhead.model.wallet.WalletDTO;
import com.marcotte.blockhead.services.blockchainaddressstore.BlockchainAddressStoreService;
import com.marcotte.blockhead.services.coin.CoinService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PortFolioByWalletAndCoinService {
    private static final Logger log = LoggerFactory.getLogger(PortFolioByWalletAndCoinService.class);

    @Autowired
    private BlockchainAddressStoreService blockchainAddressStoreService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private CurrentPriceService currentPriceService;



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

    private List<WalletDTO> sumByWalletAndCoin(List<BlockchainAddressStore> foundLatestOrderedByCurrency) {
        List<CoinDTO> summedByCurrency = new ArrayList<CoinDTO>();
        List<WalletDTO> summedByWallet = new ArrayList<WalletDTO>();

        String currentCoin = "";
        String currentWallet = "";
        WalletDTO currentWwalletDTO = null;
        CoinDTO currentCoinDTO = null;


        for (BlockchainAddressStore addr : foundLatestOrderedByCurrency) {
            if (currentWallet.compareToIgnoreCase(addr.getWalletName()) != 0) {
                if (currentWwalletDTO != null) {
                    summedByWallet.add(currentWwalletDTO);
                    if (currentCoinDTO != null) {
                        currentWwalletDTO.addCoinDTO(currentCoinDTO);
                    }
                }
                currentWwalletDTO = new WalletDTO();
                currentWwalletDTO.setWalletName(addr.getWalletName());
                currentWallet = addr.getWalletName();
                currentCoinDTO = new CoinDTO();
                currentCoinDTO.setTicker(addr.getTicker());
                currentCoinDTO.setCoinBalance(addr.getLastBalance());
            } else {
                if (currentCoinDTO.getTicker().compareToIgnoreCase(addr.getTicker()) != 0) {
                    currentWwalletDTO.getCoinDTOs().add(currentCoinDTO);
                    currentCoinDTO = new CoinDTO();
                    currentCoinDTO.setTicker(addr.getTicker());
                    currentCoinDTO.setCoinBalance(addr.getLastBalance());
                } else {
                    currentCoinDTO.addCoinBalance(addr.getLastBalance());
                }
            }
        }
        if (foundLatestOrderedByCurrency.size() > 0) {
            currentWwalletDTO.getCoinDTOs().add(currentCoinDTO);
            summedByWallet.add(currentWwalletDTO);
            calculateWalletValue(summedByWallet);
        }
        return summedByWallet;
    }

    private void calculateWalletValue(List<WalletDTO> walletDTOList) {
        HashMap<String, CoinDTO> coinPriceMap = currentPriceService.getCoinPriceMap();
        for (WalletDTO walletDTO :walletDTOList ) {
            for (CoinDTO coinDTO : walletDTO.getCoinDTOs()) {
                CoinDTO coinPrice = coinPriceMap.get(coinDTO.getTicker());
                if ( coinPrice != null ) {
                    coinDTO.setFiat_prices(coinPrice.getFiat_prices());
                    coinDTO.calculateCoinValue();
                } else {
                    log.warn("Missing price information for Ticker:" + coinDTO.getTicker());
                }
                walletDTO.addValues(coinDTO.getFiat_balances());
            }
        }
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
