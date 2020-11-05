import React, {useState, useEffect} from 'react'
import FiatCurrency from "../FiatCurrency/FiatCurrency";
import PotfolioByCoin from "../PotfolioByCoin/PotfolioByCoin";
import axios from 'axios';
import styled from 'styled-components'
import Tabs from  "../Tabs/Tabs";


// styled TD
const TD = styled.td`
    border: 1px solid #2c2b2b;
    width : 25vh;
`;

export default function PortfolioByCoins(props) {

    const [totalValue, setTotalValue] = React.useState(-1);
    const [porfolioFiatValues, setPortfolioFiatValues] = React.useState([]);
    const [portfolioByCoins, setPortfolioByCoins] = React.useState([]);

    const componentDidMount = async () => {
        let response = await axios.put(props.portfolioUrl);
        let updatedPorfolioFiatValues = response.data;
        console.log("porfolioFiatValues", updatedPorfolioFiatValues);

        let totalValue = 0;
        for( let i =0; i< updatedPorfolioFiatValues.length; i++) {
            totalValue += updatedPorfolioFiatValues[i].coinValue;
        }
        console.log("total Value=", totalValue);
        setPortfolioFiatValues(updatedPorfolioFiatValues);
        
        let response2 = await axios.put(props.portfolioByCoinsUrl);
        let newPortfolioByCoins = response2.data;
        console.log("newPortfolioByCoins", newPortfolioByCoins);

        setTotalValue(totalValue);
        setPortfolioByCoins(newPortfolioByCoins);
    }

    useEffect( function() {
        if ( totalValue === -1)
        {
            componentDidMount();
        }
    })

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
                        porfolioFiatValues.map( ({coinValue, fiatCurrency}) =>
                        <FiatCurrency key={fiatCurrency}
                            currency={fiatCurrency}
                            coinValue={coinValue}
                            />
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
                    portfolioByCoins.map( ({coinName, ticker, coinBalance, fiat_prices, fiat_balances }) =>
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
