/**
 * Crypto currency portfoilo tracker application front-end.
 * version: prototype cryptoJ
 *
 * This application uses spring-boot back-end with a nodeJS/React
 * see readme files for details.
 *
 */
import React from 'react';
//import logo from './logo.svg';
import './App.css';
import GinkoHeader from '../GinkoHeader/GinkoHeader';
import Tabs from  "../Tabs/Tabs";
import Portfolio from '../Portfolio/Portfolio';
import ImportWalletCSV from '../ImportWalletCSV/ImportWalletCSV';
import CoinList from '../CoinList/CoinList';

export default class App extends React.Component {

state = {
    balance: 10001,
    myCoins : [
      {
        name: 'Bitcoin',
        ticker: 'BTC'
      },
      {
        name: 'bitcoin-cash',
        ticker: 'BCH'
      },
      {
        name: 'Ethereum',
        ticker: 'ETH'
      },
      {
        name: 'Dash',
        ticker: 'DASH'
      },
      {
        name: 'Cardano',
        ticker: 'ADA'
      }
    ],
    coinData: [
    ]
  }

render() {
  return (
    <div className="App">
      <GinkoHeader />
      <Tabs>
        <div label="Portfolio">
          <Portfolio />
        </div>
        <div label="current prices">
          <CoinList coinData={this.state.coinData}/>
        </div>
        <div label="import wallet csv">
          <ImportWalletCSV />
        </div>
      </Tabs>
    </div>
    );  // endReturn
  } // endRender
}
