package com.marcotte.blockhead.services.portfolio;

import com.marcotte.blockhead.datastore.portfolio.PortfolioTracker;
import com.marcotte.blockhead.datastore.blockchainaddressstore.BlockchainAddressStore;
import com.marcotte.blockhead.datastore.datetracker.DateTracker;
import com.marcotte.blockhead.datastore.portfolio.PortfolioValueTracker;
import com.marcotte.blockhead.model.coin.*;
import com.marcotte.blockhead.model.fiat.FiatCurrency;
import com.marcotte.blockhead.model.wallet.WalletDTO;
import com.marcotte.blockhead.model.wallet.WalletDTOCompareByFiatValue;
import com.marcotte.blockhead.services.coin.CoinService;
import com.marcotte.blockhead.services.blockchainaddressstore.BlockchainAddressStoreService;
import com.marcotte.blockhead.services.datetracker.DateTrackerService;
import com.marcotte.blockhead.services.explorerServices.BitcoinCashComExplorerService;
import com.marcotte.blockhead.services.explorerServices.BitcoinExplorerServices;
import com.marcotte.blockhead.services.explorerServices.CardanoAdaService;
import com.marcotte.blockhead.services.explorerServices.dashExplorers.DashExplorerServices;
import com.marcotte.blockhead.services.explorerServices.eosexplorers.EOS_ExplorerServices;
import com.marcotte.blockhead.services.explorerServices.etheriumexplorers.EtheriumExplorerServices;
import com.marcotte.blockhead.services.explorerServices.litecoinexplorers.LiteCoinExplorerServices;
import com.marcotte.blockhead.services.explorerServices.pricequote.PriceServiceInterface;
import com.marcotte.blockhead.services.explorerServices.zcashExplorers.ZCashExplorerServices;
import com.marcotte.blockhead.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
    private PriceServiceInterface coinGeckoService;
    @Autowired
    private DateTrackerService dateTrackerService;
    @Autowired
    private PortfolioTrackerService portfolioTrackerService;
    @Autowired
    private PortfolioValueTrackerService portfolioValueTrackerService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private PortfolioByCoinsService portfolioByCoinsService;

    @Autowired
    private PortFolioByWalletAndCoinService portFolioByWalletAndCoinService;


    /**
     *
     * TODO  refactor this so we just return one PortfolioTracker with multiple columns for all the major fiat currencies
     * @return
     */
    public PortfolioValueTrackerDTO portfolioGetTotalValue() {
        List<CoinDTO> portfolioByCoinList = portfolioByCoinsService.findAllLatestSumBalanceGroupByCoin();
        HashMap<String, CoinDTO> coinHashMap = coinService.findAllReturnTickerCoinDTOMap();
        copyFiatPricesAndCalculateValueFromCoinPrices( portfolioByCoinList, coinHashMap);

        DateTracker dateTracker = createAndSaveDateTracker();
        PortfolioValueTrackerDTO portfollioSummary = calculatePortfolioSummary2(portfolioByCoinList);
        savePortfolioSummary( portfollioSummary, dateTracker);

        return portfollioSummary;
    }

    /**
     * returns the portfolio broken down by coin and sorted by value in reverse order.
     * @return
     */
    public List<CoinDTO> portfolioByCoins() {
        List<CoinDTO> portfolioByCoinList = portfolioByCoinsService.findAllLatestSumBalanceGroupByCoin();
        HashMap<String, CoinDTO> coinHashMap = coinService.findAllReturnTickerCoinDTOMap();

        copyFiatPricesAndCalculateValueFromCoinPrices( portfolioByCoinList, coinHashMap);
        Collections.sort(portfolioByCoinList, Collections.reverseOrder(new CoinDTOCompareByFiatBalance()));
        return portfolioByCoinList;
    }

    public List<CoinDTO> portfolioByCoins2()  {
        List<CoinSumDTO> blockchainAddressStoreList = blockchainAddressStoreService.findAllLatestSumBalanceGroupByTicker();
        HashMap<String, CoinDTO> coinHashMap = coinService.findAllReturnTickerCoinDTOMap();

        List<CoinDTO> coinDTOList = new ArrayList<CoinDTO>();
        for ( CoinSumDTO coinSum : blockchainAddressStoreList ) {
            coinDTOList.add( new CoinDTO(coinSum));
        }
        copyFiatPricesAndCalculateValueFromCoinPrices( coinDTOList, coinHashMap);
        Collections.sort(coinDTOList, Collections.reverseOrder(new CoinDTOCompareByFiatBalance()));
        return coinDTOList;
    }

    /**
     * go though the list of coins look up the ticker in the hash map that contains the current price for that coin,
     * if found set the current price for the coin, calculate the current value of the coin (price * balance)
     *
     * if coin is not found return a default price of 0.0 and value of 0.0
     *
     * @param portfolioByCoinList
     * @param coinHashMap
     */
    private void copyFiatPricesAndCalculateValueFromCoinPrices(List<CoinDTO> portfolioByCoinList, HashMap<String, CoinDTO> coinHashMap) {
        CoinDTO coinDefault = new CoinDTO();
        coinDefault.setCoinName("Unknown");
        for ( CoinDTO coinDTO : portfolioByCoinList ) {
            coinDefault.setTicker(coinDTO.getTicker());
            CoinDTO coinPriceDTO = coinHashMap.getOrDefault(coinDTO.getTicker(),coinDefault);
            coinDTO.setFiat_prices(coinPriceDTO.getFiat_prices());
            coinDTO.setCoinName(coinPriceDTO.getCoinName());
            coinDTO.calculateCoinValue();
        }
    }


    /**
     * finds the total fiat value for each wallet and group by the coins of each wallet.
     *
     * @deprecated
     * @return
     */
    public List<WalletDTO> portfolioByWalletCoins() {
        List<CoinDTO> portfolioByCoinList;
        List<WalletDTO> walletDTOS = portFolioByWalletAndCoinService.findBlockchainAddressStoreOrderByWalletNameAscCurrencyAsc();
        HashMap<String, CoinDTO> coinHashMap = coinService.findAllReturnTickerCoinDTOMap();

        // loop though all wallets and calculate balances
        for (WalletDTO walletDTO : walletDTOS ) {
            copyFiatPricesAndCalculateValueFromCoinPrices( walletDTO.getCoinDTOs(), coinHashMap);
            PortfolioValueTrackerDTO walletValueTotal = calculatePortfolioSummary2(walletDTO.getCoinDTOs());
            walletDTO.setFiat_balances(walletValueTotal.getFiat_balances());
            // sort coins by fiat value decending.
            Collections.sort(walletDTO.getCoinDTOs(), Collections.reverseOrder(new CoinDTOCompareByFiatBalance()));
        }

        // sort wallets by fiat value decending.
        Collections.sort(walletDTOS, Collections.reverseOrder(new WalletDTOCompareByFiatValue()));
        return walletDTOS;
    }

    /**
     * service to calculate the fiat value of a multi coin wallet
     * @return  List<WalletDTO> -- a list of wallets
     */
    public List<WalletDTO> portfolioByWallet() {
        List<WalletDTO> walletDTOs = new ArrayList<WalletDTO>();
        HashMap<String, CoinDTO> coinHashMap = coinService.findAllReturnTickerCoinDTOMap();
        List<BlockchainAddressStore> addressStores = blockchainAddressStoreService.findAllLatestSumBalanceGroupByWalletTicker( );
        WalletDTO currentWalletDTO = null;
        String CurrentWallet = "";

        for (BlockchainAddressStore addressStore : addressStores) {
            if ( CurrentWallet.compareToIgnoreCase(addressStore.getWalletName()) != 0) {
                if (currentWalletDTO != null ) {
                    // not the first time though

                    // STD calculate fiat value here
                    walletDTOs.add(currentWalletDTO);
                }
                currentWalletDTO = new WalletDTO();
                CurrentWallet = addressStore.getWalletName();
            }
            CoinDTO coinDefault = new CoinDTO();
            CoinDTO coinDTO = new CoinDTO(addressStore);
            CoinDTO coinPriceDTO = coinHashMap.getOrDefault(coinDTO.getTicker(),coinDefault);
            coinDTO.setFiat_prices(coinPriceDTO.getFiat_prices());
            coinDTO.calculateCoinValue();
            currentWalletDTO.addCoinDTO(coinDTO);
        }
        if ( currentWalletDTO != null) {
            // add the last record in.
            // STD calculate fiat value here
            walletDTOs.add(currentWalletDTO);
        }
        return walletDTOs;
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
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CoinCodes.POLKADOT_TICKER, refresh);
        updateCoinBalanceCacheCalculateFiatBalance( portfolioList, CoinCodes.SOLANA_TICKER, refresh); 


        // save copy of the portflio here
        DateTracker dateTracker = createAndSaveDateTracker();

        List<PortfolioTracker> portfollioSummary = calculatePortfolioSummary(portfolioList, dateTracker);
        //savePortfolioSummary( portfollioSummary);
        return portfollioSummary;
    }

    private DateTracker createAndSaveDateTracker()
    {
        DateTracker dateTracker = new DateTracker();
        dateTrackerService.save(dateTracker);
        return dateTracker;
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

    private CoinList getCoinBalancesForCoin(String coinName )
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
    private void updateCurrentCoinBalancesViaBlockExplorers(CoinList coinList)
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
        if (coinList.getCoinName().equals(CoinCodes.POLKADOT_TICKER))
        {
            //TODO implement an exporor service call here.
            return;
        }
        if (coinList.getCoinName().equals(CoinCodes.SOLANA_TICKER))
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

    private PortfolioValueTrackerDTO calculatePortfolioSummary2(List<CoinDTO> portfolioList )
    {
        HashMap<String, PortfolioTracker> portfoiloByFiatCurrency = new HashMap<String, PortfolioTracker>();
        PortfolioValueTrackerDTO portfolioValueTrackerDTO = new PortfolioValueTrackerDTO();

        for ( CoinDTO coin : portfolioList )
        {
            for (FiatCurrency currency : coin.getFiat_balances().getFiat_values() )
            {
                portfolioValueTrackerDTO.getFiat_balances().addToFiat(currency);
            }
        }
        return  portfolioValueTrackerDTO;
    }

    private void savePortfolioSummary(PortfolioValueTrackerDTO portfollioSummaryDTO, DateTracker dateTracker)
    {
        PortfolioValueTracker portfolioValueTracker = new PortfolioValueTracker(portfollioSummaryDTO);
        portfolioValueTracker.setDateTrackerID(dateTracker.getId());
        portfolioValueTracker.setDateUpdated(dateTracker.getDateUpdated());

        portfolioValueTrackerService.save(portfolioValueTracker);

    }
}
