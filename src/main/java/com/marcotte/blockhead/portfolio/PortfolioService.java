package com.marcotte.blockhead.portfolio;

import com.marcotte.blockhead.datastore.*;
import com.marcotte.blockhead.explorerservices.bitcoincash.BitcoinCashComExplorerService;
import com.marcotte.blockhead.explorerservices.bitcoinexplorers.BitcoinExplorerServices;
import com.marcotte.blockhead.explorerservices.blockcypher.BlockCypherComService;
import com.marcotte.blockhead.explorerservices.cardanoexplorers.CardanoAdaService;
import com.marcotte.blockhead.explorerservices.dashExplorers.DashExplorerServices;
import com.marcotte.blockhead.explorerservices.dashExplorers.ExplorerDashOrg;
import com.marcotte.blockhead.explorerservices.eosexplorers.EOS_ExplorerServices;
import com.marcotte.blockhead.explorerservices.etheriumexplorers.EtheriumExplorerServices;
import com.marcotte.blockhead.explorerservices.litecoinexplorers.LiteCoinExplorerServices;
import com.marcotte.blockhead.explorerservices.pricequote.CoinGeckoService;
import com.marcotte.blockhead.explorerservices.zcashExplorers.ZCashExplorerServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class PortfolioService
{
    private static final Logger log = LoggerFactory.getLogger(PortfolioService.class);

    @Autowired
    private BitcoinExplorerServices bitcoinExplorerServices;
    @Autowired
    private BitcoinCashComExplorerService bitcoinCashComExplorerService;
    @Autowired
    private DashExplorerServices dashExplorerServices;
    @Autowired
    private EtheriumExplorerServices etheriumExplorerServices;
    @Autowired
    private CardanoAdaService cardanoAdaService;
    @Autowired
    private LiteCoinExplorerServices liteCoinExplorerServices;
    @Autowired
    private EOS_ExplorerServices eos_explorerServices;
    @Autowired
    private ZCashExplorerServices zCashExplorerServices;


    @Autowired
    private BlockchainAddressStoreService blockchainAddressStoreService;
    @Autowired
    private CoinGeckoService coinGeckoService;
    @Autowired
    private DateTrackerService dateTrackerService;
    @Autowired
    private PortfolioTrackerService portfolioTrackerService;



    public List<CoinList> portfolioCheck(boolean refresh)
    {
        List<CoinList> portfolioList = new ArrayList<>();
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.BITCOIN, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.BITCOIN_CASH, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.DASH, refresh);

        // TODO do the rest

        // save copy of the portflio here
        List<PortfolioTracker> portfollioSummary = calculatePortfolioSummary(portfolioList);
        savePortfoloioSummary( portfollioSummary);
        return portfolioList;
    }


    public List<CoinList> portfolioCheck(boolean refresh, String cryptoName)
    {
        List<CoinList> portfolioList = new ArrayList<>();
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, cryptoName, refresh);
        List<PortfolioTracker> portfollioSummary = calculatePortfolioSummary(portfolioList);
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

    /**
     * updates balance from the block chain explorers
     * Supported coins: BTC, BCH, DASH, ETH, ADA, LTE, EOS
     *
     * @param coinList
     */
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
            updateCurrentBalanceForDash(coinList.getCoins());
            return;
        }

        if ( coinList.getCoinName().equals(CryptoNames.ETHEREUM))
        {
            updateCurrentBalancesForEtherium(coinList.getCoins() );
            return;
        }

        if (coinList.getCoinName().equals(CryptoNames.CARDANO_ADA))
        {
            updateCurrentBalancesForCardano(coinList.getCoins()  );
            return;
        }

        if (coinList.getCoinName().equals(CryptoNames.LITE_COIN))
        {
            updateCurrentBalancesForLiteCoin(coinList.getCoins() );
            return;
        }

        if (coinList.getCoinName().equals(CryptoNames.EOS))
        {
            updateCurrentBalancesForEOS_Coin(coinList.getCoins() );
            return;
        }

        if (coinList.getCoinName().equals(CryptoNames.ZCASH))
        {
            updateCurrentBalancesForZCashCoin(coinList.getCoins() );
            return;
        }


        log.error("unsupported crypto name={}", coinList.getCoinName());
        coinList.addErrorMessage("unsupported crypto name=" + coinList.getCoinName());
    }

    private void updateCurrentBalancesForBitcoin( List<BlockchainAddressStore>  blockchainAddressStores)
    {
        bitcoinExplorerServices.addressInfo(blockchainAddressStores);
    }

    private void updateCurrentBalanceForBitcoinCash(List<BlockchainAddressStore>  blockchainAddressStores)
    {
        bitcoinCashComExplorerService.addressInfo(blockchainAddressStores);
    }

    private void updateCurrentBalanceForDash(List<BlockchainAddressStore>  blockchainAddressStores)
    {
        dashExplorerServices.addressInfo(blockchainAddressStores);
    }

    private void updateCurrentBalancesForEtherium(List<BlockchainAddressStore>  blockchainAddressStores )
    {
        etheriumExplorerServices.addressInfo(blockchainAddressStores);
    }

    private void updateCurrentBalancesForCardano(List<BlockchainAddressStore>  blockchainAddressStores )
    {
        cardanoAdaService.addressInfo(blockchainAddressStores);
    }

    private void updateCurrentBalancesForLiteCoin(List<BlockchainAddressStore>  blockchainAddressStores )
    {
        liteCoinExplorerServices.addressInfo(blockchainAddressStores);
    }

    private void updateCurrentBalancesForEOS_Coin(List<BlockchainAddressStore>  blockchainAddressStores )
    {
        eos_explorerServices.addressInfo(blockchainAddressStores);
    }

    private void updateCurrentBalancesForZCashCoin(List<BlockchainAddressStore>  blockchainAddressStores )
    {
        zCashExplorerServices.addressInfo(blockchainAddressStores);
    }

    private List<PortfolioTracker> calculatePortfolioSummary(List<CoinList> portfolioList )
    {
        DateTracker dateTracker = new DateTracker();
        dateTrackerService.save(dateTracker);
        HashMap<String, PortfolioTracker> portfoiloByFiatCurrency = new HashMap<String, PortfolioTracker>();

        for ( CoinList coin : portfolioList )
        {
            for (Currency currency : coin.getFiat_balances() )
            {
                PortfolioTracker portfolioTracker = portfoiloByFiatCurrency.get(currency.getCode());
                if ( portfolioTracker == null ) {
                    portfolioTracker = new PortfolioTracker();
                    portfolioTracker.setDateTrackerID(dateTracker.getId());
                    portfolioTracker.setDateUpdated(dateTracker.getDateUpdated());
                    portfolioTracker.setFiatCurrency( currency.getCode());

                    portfoiloByFiatCurrency.put(portfolioTracker.getFiatCurrency(), portfolioTracker);
                }
                portfolioTracker.setCoinValue(portfolioTracker.getCoinValue() + currency.getRate());
            }

        }
        return  new ArrayList<PortfolioTracker>(portfoiloByFiatCurrency.values());
    }

    private void  savePortfoloioSummary( List<PortfolioTracker> portfollioSummary)
    {
        for (PortfolioTracker portfolio : portfollioSummary )
        {
            portfolioTrackerService.save(portfolio);
        }
    }
}
