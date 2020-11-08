import React, {useState, useEffect} from 'react'
import FiatCurrency from "../FiatCurrency/FiatCurrency";
import PotfolioByCoin from "../PotfolioByCoin/PotfolioByCoin";
import axios from 'axios';
import styled from 'styled-components'
import Tabs from  "../Tabs/Tabs";


// styled TD
const TDPrice = styled.td`
    border: 1px solid #2c2b2b;
    width : 25vh;
    text-align: right;
`;

export default function PortfolioByCoins(props) {


    return (
        <div> 
            <h1>Protfolio value</h1>
            <p></p>
            <table>
                <thead>
                    <tr>
                        <th>Total Portfolio Value</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        {
                            props.porfolioFiatValues.map( ({coinValue, fiatCurrency}) =>
                            <TDPrice><FiatCurrency key={fiatCurrency}
                                currency={fiatCurrency}
                                coinValue={coinValue}
                                />
                            </TDPrice>
                            )
                        }
                    </tr>
                </tbody>
            </table>
        
            <h3>list of coins balance and values</h3>
            <table>
                <thead>
                    <tr>
                        <th>coin name</th>
                        <th>Ticker</th>
                        <th>coin balance</th>
                        <th>Current Price</th>
                        <th>value</th>
                    </tr>
                </thead>
                <tbody>
                    {
                    props.portfolioByCoins.map( ({coinName, ticker, coinBalance, fiat_prices, fiat_balances }) =>
                    <PotfolioByCoin 
                            key={ticker}
                            coinName={coinName} 
                            ticker={ticker} 
                            coinBalance={coinBalance} 
                            fiat_prices={fiat_prices}
                            fiat_balances={fiat_balances}
                            defaultFiatCurrency={props.defaultFiatCurrency}
                            />
                        )  
                    }
                </tbody>
            </table>

        </div>
    )

}
