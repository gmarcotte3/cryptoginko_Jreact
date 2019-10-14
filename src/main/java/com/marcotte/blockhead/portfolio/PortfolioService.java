package com.marcotte.blockhead.portfolio;

import com.marcotte.blockhead.datastore.*;
import com.marcotte.blockhead.explorerservices.bitcoincash.BitcoinCashComExplorerService;
import com.marcotte.blockhead.explorerservices.blockcypher.BlockCypherComService;
import com.marcotte.blockhead.explorerservices.pricequote.CoinGeckoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PortfolioService
{
    private static final Logger log = LoggerFactory.getLogger(PortfolioService.class);

    @Autowired
    private BlockCypherComService blockCypherComService;

    @Autowired
    private BlockchainAddressStoreService blockchainAddressStoreService;

    @Autowired
    private BitcoinCashComExplorerService bitcoinCashComExplorerService;

    @Autowired
    private CoinGeckoService coinGeckoService;


    public List<CoinList> portfolioCheck(boolean refresh)
    {
        List<CoinList> portfolioList = new ArrayList<>();
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.BITCOIN, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.BITCOIN_CASH, refresh);

        // TODO do the rest
        return portfolioList;
    }


    private void updateCoinBalanceCacheCalculateFiatBalance(List<CoinList> portfolioList, String cryptoName, boolean refresh)
    {
        CoinList coinList;
        coinList = getCoinBalancesForCoin(cryptoName);
        if ( refresh) {
            updateCurrentCoinBalancesViaBlockExplorers(coinList);
            updateAddressStoreBalance(coinList);
        }
        calculateFiatBalance(coinList);
        portfolioList.add(coinList);
    }

    private void updateAddressStoreBalance(CoinList coinList)
    {
        for (BlockchainAddressStore addressStore: coinList.getCoins())
        {
            this.blockchainAddressStoreService.save(addressStore);
        }
    }

    private void calculateFiatBalance(CoinList coinList)
    {
        QuoteGeneric quoteGeneric = coinGeckoService.getQuote(coinList.getCoinName());
        coinList.calculateCoinBalance();
        coinList.calculateFietBalances(quoteGeneric);
    }

    public CoinList getCoinBalancesForCoin(String coinName )
    {
        CoinList coinList = new CoinList();
        coinList.setCoinName(coinName);

        List<BlockchainAddressStore>  blockchainAddressStores = blockchainAddressStoreService.findAllByCoinName(coinName);
        coinList.setCoins(blockchainAddressStores);
        return coinList;
    }

    public void updateCurrentCoinBalancesViaBlockExplorers(CoinList coinList)
    {
        if ( coinList.getCoinName().equals(CryptoNames.BITCOIN))
        {
            updateCurrentBalancesForBitcoin( coinList.getCoins());
            return;
        }

        if ( coinList.getCoinName().equals(CryptoNames.BITCOIN_CASH))
        {
            updateCurrentBalanceForBitcoinCash(coinList.getCoins());
            return;
        }

        if ( coinList.getCoinName().equals(CryptoNames.DASH))
        {
            return;
        }

        if ( coinList.getCoinName().equals(CryptoNames.ETHEREUM))
        {
            return;
        }

        log.error("unsupported crypto name={}", coinList.getCoinName());
        coinList.addErrorMessage("unsupported crypto name=" + coinList.getCoinName());
    }

    private void updateCurrentBalancesForBitcoin( List<BlockchainAddressStore>  blockchainAddressStores)
    {
        blockCypherComService.addressInfo(blockchainAddressStores);
    }

    private void updateCurrentBalanceForBitcoinCash(List<BlockchainAddressStore>  blockchainAddressStores)
    {
        bitcoinCashComExplorerService.addressInfo(blockchainAddressStores);
    }
}
