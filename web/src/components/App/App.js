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
import axios from 'axios';


// stypled tab bar
const Div2 = styled.div`
    position: relative;
    top: 0px;
    background: #2B2B2B 0% 0% no-repeat padding-box;
    //background: transparent linear-gradient(180deg, #101010 0%, #646464 100%) 0% 0% no-repeat padding-box;
    opacity: 1;
    color: white;
    height: 70px
`;


const PORTFOLIO_BACKEND_URL = "http://localhost:8082/ginkoJ/portfolio/total";
const PORTFOLIO_BYCOINS_URL = "http://localhost:8082/ginkoJ/portfolio/bycoins";
const IMPORT_GINKO_ADDR_URL = "http://localhost:8082/ginkoJ/import/addressescsv";


export default function App(props) {
//  const [portfolioFiatValue, setPortfolioFiatValue] =  useState(0);
  const [myCoins, setMyCoins] = useState([]);
  const [defaultFiatCurrency, setDefaultFiatCurrency] = useState('NZD');

  const [totalValue, setTotalValue] = React.useState(-1);
  const [porfolioFiatValues, setPortfolioFiatValues] = React.useState([]);
  const [portfolioByCoins, setPortfolioByCoins] = React.useState([]);

  const componentDidMount = async () => {
      let response = await axios.put(PORTFOLIO_BACKEND_URL);
      let updatedPorfolioFiatValues = response.data;
      console.log("porfolioFiatValues", updatedPorfolioFiatValues);

      let totalValue2 = 0;
      for( let i =0; i< updatedPorfolioFiatValues.length; i++) {
          totalValue2 += updatedPorfolioFiatValues[i].coinValue;
      }
      console.log("total Value=", totalValue2);
      setTotalValue(totalValue2);
      setPortfolioFiatValues(updatedPorfolioFiatValues);
      
      let response2 = await axios.get(PORTFOLIO_BYCOINS_URL);
      let newPortfolioByCoins = response2.data;
      console.log("newPortfolioByCoins", newPortfolioByCoins);
      setPortfolioByCoins(newPortfolioByCoins);
  }

  useEffect( function() {
    console.log("..;");
      if ( totalValue < 0)
      {
        setTotalValue(0);
        console.log("refreshing now ..... ");
          componentDidMount();
      }
  })


  return (
    <div className="App">
      <GinkoHeader />
      <Div2>
        <Tabs>
          <div label="Portfolio">
            <Portfolio defaultFiatCurrency={defaultFiatCurrency} 
              portfolioUrl={PORTFOLIO_BACKEND_URL} 
              portfolioByCoinsUrl={PORTFOLIO_BYCOINS_URL}
              totalValue={totalValue}
              porfolioFiatValues={porfolioFiatValues}
              portfolioByCoins={portfolioByCoins}
              />
          </div>
          <div label="current prices">
            <CoinList coinData={myCoins}/>
          </div>
          <div label="import wallet csv">
            <ImportWalletCSV
              importGinkoAddrURL={IMPORT_GINKO_ADDR_URL} />
          </div>
          
        </Tabs>
      </Div2>
    </div>
    );  // endReturn

}
