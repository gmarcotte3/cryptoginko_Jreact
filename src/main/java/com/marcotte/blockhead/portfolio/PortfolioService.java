package com.marcotte.blockhead.portfolio;

import com.marcotte.blockhead.datastore.*;
import com.marcotte.blockhead.explorerServices.bitcoincash.BitcoinCashComExplorerService;
import com.marcotte.blockhead.explorerServices.bitcoinexplorers.BitcoinExplorerServices;
import com.marcotte.blockhead.explorerServices.cardanoexplorers.CardanoAdaService;
import com.marcotte.blockhead.explorerServices.dashExplorers.DashExplorerServices;
import com.marcotte.blockhead.explorerServices.eosexplorers.EOS_ExplorerServices;
import com.marcotte.blockhead.explorerServices.etheriumexplorers.EtheriumExplorerServices;
import com.marcotte.blockhead.explorerServices.litecoinexplorers.LiteCoinExplorerServices;
import com.marcotte.blockhead.explorerServices.pricequote.CoinGeckoService;
import com.marcotte.blockhead.explorerServices.zcashExplorers.ZCashExplorerServices;
import com.marcotte.blockhead.model.*;
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


    /**
     * returns the portfolio broken down by coin
     * @return
     */
    public List<CoinDTO> portfolioByCoins() {
        List<CoinDTO> portfolioByCoinList = blockchainAddressStoreService.findAllLatestSumBalanceGroupByCurency();
        List<CoinDTO> coinPrices = coinGeckoService.getPriceAllCoinsNow();

        copyFiatPricesAndCalculateValueFromCoinPrices( portfolioByCoinList, coinPrices);
        return portfolioByCoinList;
    }

    private void  copyFiatPricesAndCalculateValueFromCoinPrices( List<CoinDTO> portfolioByCoinList, List<CoinDTO> coinPrices) {
        for ( CoinDTO portfolioCoin : portfolioByCoinList ) {
            for( int i=0; i < coinPrices.size(); i++) {
                if ( portfolioCoin.getTicker().equals(coinPrices.get(i).getTicker())) {
                    List<FiatCurrency> fiat_prices = new ArrayList<>();
                    List<FiatCurrency> fiat_values= new ArrayList<>();
                    for ( FiatCurrency price : coinPrices.get(i).getFiat_prices()) {
                        fiat_prices.add(price);
                        FiatCurrency value = new FiatCurrency(price.getValue() * portfolioCoin.getCoinBalance(),price.getFiatType() );
                        fiat_values.add( value );
                    }
                    portfolioCoin.setFiat_prices(fiat_prices);
                    portfolioCoin.setFiat_balances(fiat_values);
                    break;
                }
            }
        }
    }

    public List<WalletDTO> portfolioByWalletCoins() {
        List<CoinDTO> portfolioByCoinList;
        List<WalletDTO> walletDTOS = blockchainAddressStoreService.findBlockchainAddressStoreByNextIdOrderByWalletNameAscCurrencyAsc();
        List<CoinDTO> coinPrices = coinGeckoService.getPriceAllCoinsNow();

        for (WalletDTO walletDTO : walletDTOS ) {
            copyFiatPricesAndCalculateValueFromCoinPrices( walletDTO.getCoinDTOs(), coinPrices);
        }

        return walletDTOS;
    }

    /**
     * This routine looks up addresses from blockchain explorers and updates the balances.
     * This is currently hard coded for only the supported currencies.
     * support for BIC, BCH, DASH, EOS, ETH, ADA, LTE, ZEC
     *
     * There is a security issue in that some one listening can associate an IP address with individual
     * address look ups.
     * We should have two services 1] for accessing nodes on the network and/or local full node wallet directly
     * 2] one that connects to block chain explorer but uses some kind of boom filter or looks up by block so you
     * dont know what address are really being requested.
     *
     * This function should not be used
     *
     * @deprecated
     * @param refresh
     * @return
     */
    public List<PortfolioTracker> portfolioCheck(boolean refresh)
    {
        List<CoinList> portfolioList = new ArrayList<>();
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.BITCOIN.code, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.BITCOIN_CASH.code, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.DASH.code, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.ETHEREUM.code, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.CARDANO_ADA.code, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.EOS.code, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.LITE_COIN.code, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.ZCASH.code, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.MONERO.code, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.LINK.code, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CryptoNames.MKR.code, refresh);

        // save copy of the portflio here
        DateTracker dateTracker = createAndSaveDateTracker();

        List<PortfolioTracker> portfollioSummary = calculatePortfolioSummary(portfolioList, dateTracker);
        savePortfolioSummary( portfollioSummary);
        return portfollioSummary;
    }

    private DateTracker createAndSaveDateTracker()
    {
        DateTracker dateTracker = new DateTracker();
        dateTrackerService.save(dateTracker);
        return dateTracker;
    }

    public PortfolioCheckResults portfolioCheck(boolean refresh, String cryptoName)
    {
        List<CoinList> portfolioList = new ArrayList<>();
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, cryptoName, refresh);

        DateTracker dateTracker = createAndSaveDateTracker();
        List<PortfolioTracker> portfollioSummary = calculatePortfolioSummary(portfolioList, dateTracker);
        return new PortfolioCheckResults( portfollioSummary, portfolioList);
    }

    public PortfolioCheckResults portfolioCheck(boolean refresh, String cryptoName, String walletName)
    {
        List<CoinList> portfolioList = new ArrayList<>();
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, cryptoName, refresh, walletName);

        DateTracker dateTracker = createAndSaveDateTracker();
        List<PortfolioTracker> portfollioSummary = calculatePortfolioSummary(portfolioList, dateTracker);
        return new PortfolioCheckResults( portfollioSummary, portfolioList);
    }

    private void updateCoinBalanceCacheCalculateFiatBalance(List<CoinList> portfolioList, String cryptoName, boolean refresh, String walletName)
    {
        CoinList coinList;
        coinList = getCoinBalancesForCoin(cryptoName, walletName);
        if ( refresh) {
            updateCurrentCoinBalancesViaBlockExplorers(coinList);
            updateAddressStoreBalance(coinList);
        }
        calculateFiatBalance(coinList);
        portfolioList.add(coinList);
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
            this.blockchainAddressStoreService.saveWithHistory(addressStore);
        }
    }

    private void calculateFiatBalance(CoinList coinList)
    {
        QuoteGeneric quoteGeneric = coinGeckoService.getQuote(coinList.getCoinName());
        coinList.calculateCoinBalance();
        coinList.calculateFietBalances(quoteGeneric);
    }

    public CoinList getCoinBalancesForCoin(String coinName, String walletName )
    {
        CoinList coinList = new CoinList();
        coinList.setCoinName(coinName);

        List<BlockchainAddressStore>  blockchainAddressStores = blockchainAddressStoreService.findAllByCoinNameAndWalletName(coinName, walletName);
        coinList.setCoins(blockchainAddressStores);
        return coinList;
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
        if ( coinList.getCoinName().equals(CryptoNames.BITCOIN.code))
        {
            updateCurrentBalancesForBitcoin( coinList.getCoins());
            return;
        }

        if ( coinList.getCoinName().equals(CryptoNames.BITCOIN_CASH.code))
        {
            updateCurrentBalanceForBitcoinCash(coinList.getCoins());
            return;
        }

        if ( coinList.getCoinName().equals(CryptoNames.DASH.code))
        {
            updateCurrentBalanceForDash(coinList.getCoins());
            return;
        }

        if ( coinList.getCoinName().equals(CryptoNames.ETHEREUM.code))
        {
            updateCurrentBalancesForEtherium(coinList.getCoins() );
            return;
        }

        if (coinList.getCoinName().equals(CryptoNames.CARDANO_ADA.code))
        {
            updateCurrentBalancesForCardano(coinList.getCoins()  );
            return;
        }

        if (coinList.getCoinName().equals(CryptoNames.LITE_COIN.code))
        {
            updateCurrentBalancesForLiteCoin(coinList.getCoins() );
            return;
        }

        if (coinList.getCoinName().equals(CryptoNames.EOS.code))
        {
            updateCurrentBalancesForEOS_Coin(coinList.getCoins() );
            return;
        }

        if (coinList.getCoinName().equals(CryptoNames.ZCASH.code))
        {
            updateCurrentBalancesForZCashCoin(coinList.getCoins() );
            return;
        }

        if (coinList.getCoinName().equals(CryptoNames.MONERO.code))
        {
            //TODO implement an exporor service call here.
            return;
        }
        if (coinList.getCoinName().equals(CryptoNames.LINK.code))
        {
            //TODO implement an exporor service call here.
            return;
        }
        if (coinList.getCoinName().equals(CryptoNames.MKR.code))
        {
            //TODO implement an exporor service call here.
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

    private List<PortfolioTracker> calculatePortfolioSummary(List<CoinList> portfolioList, DateTracker dateTracker  )
    {

        HashMap<String, PortfolioTracker> portfoiloByFiatCurrency = new HashMap<String, PortfolioTracker>();

        for ( CoinList coin : portfolioList )
        {
            for (FiatCurrency currency : coin.getFiat_balances() )
            {
                PortfolioTracker portfolioTracker = portfoiloByFiatCurrency.get(currency.getCode());
                if ( portfolioTracker == null ) {
                    portfolioTracker = new PortfolioTracker();
                    portfolioTracker.setDateTrackerID(dateTracker.getId());
                    portfolioTracker.setDateUpdated(dateTracker.getDateUpdated());
                    portfolioTracker.setFiatCurrency( currency.getCode());

                    portfoiloByFiatCurrency.put(portfolioTracker.getFiatCurrency(), portfolioTracker);
                }
                portfolioTracker.setCoinValue(portfolioTracker.getCoinValue() + currency.getValue());
            }

        }
        return  new ArrayList<PortfolioTracker>(portfoiloByFiatCurrency.values());
    }

    private void savePortfolioSummary(List<PortfolioTracker> portfollioSummary)
    {
        for (PortfolioTracker portfolio : portfollioSummary )
        {
            portfolioTrackerService.save(portfolio);
        }
    }
}
