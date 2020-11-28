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

    @Autowired
    private CoinService coinService;

    @Autowired
    private PortfolioByCoinsService portfolioByCoinsService;

    @Autowired
    private PortFolioByWalletAndCoinService portFolioByWalletAndCoinService;


    public List<PortfolioTracker> portfolioGetTotalValue() {
        List<CoinDTO> portfolioByCoinList = portfolioByCoinsService.findAllLatestSumBalanceGroupByCoin();
        HashMap<String, FiatCurrency> coinPriceList = coinService.findAllReturnTickerFiatHashmap();
        copyFiatPricesAndCalculateValueFromCoinPrices( portfolioByCoinList, coinPriceList);

        DateTracker dateTracker = createAndSaveDateTracker();
        List<PortfolioTracker> portfollioSummary = calculatePortfolioSummary2(portfolioByCoinList, dateTracker);
        savePortfolioSummary( portfollioSummary);

        return portfollioSummary;
    }

    // new and improved method
    private void copyFiatPricesAndCalculateValueFromCoinPrices( List<CoinDTO> portfolioByCoinList, HashMap<String, FiatCurrency> coinPrices) {
        FiatCurrency fiat;

        for ( CoinDTO portfolioCoin : portfolioByCoinList ) {
            String currentTicker = portfolioCoin.getTicker();
            List<FiatCurrency> fiat_prices = new ArrayList<>();
            List<FiatCurrency> fiat_values= new ArrayList<>();

            calculateValueBasedOnPriceAndBalance(  currentTicker, portfolioCoin, coinPrices,fiat_prices,fiat_values);

            portfolioCoin.getFiat_prices().setFiat_values(fiat_prices);
            portfolioCoin.getFiat_balances().setFiat_values(fiat_values);
        }
    }

    /**
     * generate an entry for fiatPrice for a set number of fiats
     * calculate coorisponding fiat values (price * balance)
     *
     * @param currentTicker
     * @param portfolioCoin
     * @param coinPrices
     * @param fiat_prices
     * @param fiat_values
     */
    private void calculateValueBasedOnPriceAndBalance(  String currentTicker,
                                                        CoinDTO portfolioCoin,
                                                        HashMap<String, FiatCurrency>  coinPrices,
                                                        List<FiatCurrency> fiat_prices,
                                                        List<FiatCurrency> fiat_values)
    {
        String fiatCodes[] = {"USD", "NZD", "JPY", "JPM"};
        for (String fiatCode : fiatCodes ) {
            String mapKey = currentTicker + "-" + fiatCode;
            FiatCurrency fiatPrice = coinPrices.get(mapKey);
            fiat_prices.add(fiatPrice);
            fiat_values.add( new FiatCurrency(fiatPrice.getValue() * portfolioCoin.getCoinBalance(), fiatCode ));
        }
    }

    /**
     * returns the portfolio broken down by coin
     * @return
     */
    public List<CoinDTO> portfolioByCoins() {
        List<CoinDTO> portfolioByCoinList = portfolioByCoinsService.findAllLatestSumBalanceGroupByCoin();
        HashMap<String, CoinDTO> coinHashMap = coinService.findAllReturnTickerCoinDTOMap();

        copyFiatPricesAndCalculateValueFromCoinPrices3( portfolioByCoinList, coinHashMap);
        return portfolioByCoinList;
    }

    private void  copyFiatPricesAndCalculateValueFromCoinPrices3( List<CoinDTO> portfolioByCoinList, HashMap<String, CoinDTO> coinHashMap) {
        CoinDTO coinDefault = new CoinDTO();
        coinDefault.setCoinName("Unknown");
        for ( CoinDTO coinDTO : portfolioByCoinList ) {
            CoinDTO coinPriceDTO = coinHashMap.getOrDefault(coinDTO.getTicker(),coinDefault);
            coinDTO.setFiat_prices(coinPriceDTO.getFiat_prices());
            coinDTO.calculateCoinValue();
        }
    }


    public List<WalletDTO> portfolioByWalletCoins() {
        List<CoinDTO> portfolioByCoinList;
        List<WalletDTO> walletDTOS = portFolioByWalletAndCoinService.findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc();
        HashMap<String, CoinDTO> coinHashMap = coinService.findAllReturnTickerCoinDTOMap();

        for (WalletDTO walletDTO : walletDTOS ) {
            copyFiatPricesAndCalculateValueFromCoinPrices3( walletDTO.getCoinDTOs(), coinHashMap);
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
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CoinCodes.BITCOIN_TICKER, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CoinCodes.BITCOINCASH_TICKER, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CoinCodes.DASH_TICKER, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CoinCodes.ETHEREUM_TICKER, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CoinCodes.CARDANO_ADA_TICKER, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CoinCodes.EOS_TICKER, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CoinCodes.LITE_COIN_TICKER, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CoinCodes.ZCASH_TICKER, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CoinCodes.MONERO_TICKER, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CoinCodes.LINK_TICKER, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CoinCodes.MAKER_TICKER, refresh);

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
            this.blockchainAddressStoreService.save(addressStore);
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
        if ( coinList.getCoinName().equals(CoinCodes.BITCOIN_TICKER))
        {
            updateCurrentBalancesForBitcoin( coinList.getCoins());
            return;
        }

        if ( coinList.getCoinName().equals(CoinCodes.BITCOINCASH_TICKER))
        {
            updateCurrentBalanceForBitcoinCash(coinList.getCoins());
            return;
        }

        if ( coinList.getCoinName().equals(CoinCodes.DASH_TICKER))
        {
            updateCurrentBalanceForDash(coinList.getCoins());
            return;
        }

        if ( coinList.getCoinName().equals(CoinCodes.ETHEREUM_TICKER))
        {
            updateCurrentBalancesForEtherium(coinList.getCoins() );
            return;
        }

        if (coinList.getCoinName().equals(CoinCodes.CARDANO_ADA_TICKER))
        {
            updateCurrentBalancesForCardano(coinList.getCoins()  );
            return;
        }

        if (coinList.getCoinName().equals(CoinCodes.LITE_COIN_TICKER))
        {
            updateCurrentBalancesForLiteCoin(coinList.getCoins() );
            return;
        }

        if (coinList.getCoinName().equals(CoinCodes.EOS_TICKER))
        {
            updateCurrentBalancesForEOS_Coin(coinList.getCoins() );
            return;
        }

        if (coinList.getCoinName().equals(CoinCodes.ZCASH_TICKER))
        {
            updateCurrentBalancesForZCashCoin(coinList.getCoins() );
            return;
        }

        if (coinList.getCoinName().equals(CoinCodes.MONERO_TICKER))
        {
            //TODO implement an exporor service call here.
            return;
        }
        if (coinList.getCoinName().equals(CoinCodes.LINK_TICKER))
        {
            //TODO implement an exporor service call here.
            return;
        }
        if (coinList.getCoinName().equals(CoinCodes.MAKER_TICKER))
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

    private List<PortfolioTracker> calculatePortfolioSummary2(List<CoinDTO> portfolioList, DateTracker dateTracker  )
    {

        HashMap<String, PortfolioTracker> portfoiloByFiatCurrency = new HashMap<String, PortfolioTracker>();

        for ( CoinDTO coin : portfolioList )
        {
            for (FiatCurrency currency : coin.getFiat_balances().getFiat_values() )
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
