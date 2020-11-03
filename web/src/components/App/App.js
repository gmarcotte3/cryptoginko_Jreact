/**
 * Crypto currency portfoilo tracker application front-end.
 * version: prototype cryptoJ
 *
 * This application uses spring-boot back-end with a nodeJS/React
 * see readme files for details.
 *
 */
import React, {useState, useEffect} from 'react';
import './App.css';
import GinkoHeader from '../GinkoHeader/GinkoHeader';
import Tabs from  "../Tabs/Tabs";
import Portfolio from '../Portfolio/Portfolio';
import ImportWalletCSV from '../ImportWalletCSV/ImportWalletCSV';
import CoinList from '../CoinList/CoinList';

import 'bootswatch/dist/flatly/bootstrap.min.css';
import '@fortawesome/fontawesome-free/js/all';

export default function App(props) {
  const [portfolioFiatValue, setPortfolioFiatValue] =  useState(0);
  const [myCoins, setMyCoins] = useState([]);
  const [defaultFiatCurrency, setDefaultFiatCurrency] = useState('JPM');


  return (
    <div className="App">
      <GinkoHeader />
      <Tabs>
        <div label="Portfolio">
          <Portfolio defaultFiatCurrency={defaultFiatCurrency} />
        </div>
        <div label="current prices">
          <CoinList coinData={myCoins}/>
        </div>
        <div label="import wallet csv">
          <ImportWalletCSV />
        </div>
      </Tabs>
    </div>
    );  // endReturn

}
