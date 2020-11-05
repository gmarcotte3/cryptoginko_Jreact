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
import styled from 'styled-components'


// stypled logo
const Div2 = styled.div`
    position: relative;
    top: -20px;
    //background: #2B2B2B 0% 0% no-repeat padding-box;
    background: transparent linear-gradient(180deg, #101010 0%, #646464 100%) 0% 0% no-repeat padding-box;
    opacity: 1;
    color: white;
`;


const PORTFOLIO_BACKEND_URL = "http://localhost:8082/blockhead/portfolio";
const PORTFOLIO_BYCOINS_URL = "http://localhost:8082/blockhead/portfolio/bycoins";

export default function App(props) {
  const [portfolioFiatValue, setPortfolioFiatValue] =  useState(0);
  const [myCoins, setMyCoins] = useState([]);
  const [defaultFiatCurrency, setDefaultFiatCurrency] = useState('NZD');


  return (
    <div className="App">
      <GinkoHeader />
      <Div2>
        <Tabs>
          <div label="Portfolio">
            <Portfolio defaultFiatCurrency={defaultFiatCurrency} 
              portfolioUrl={PORTFOLIO_BACKEND_URL} 
              portfolioByCoinsUrl={PORTFOLIO_BYCOINS_URL}
              />
          </div>
          <div label="current prices">
            <CoinList coinData={myCoins}/>
          </div>
          <div label="import wallet csv">
            <ImportWalletCSV />
          </div>
          
        </Tabs>
      </Div2>
    </div>
    );  // endReturn

}
