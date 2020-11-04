import React, {useState, useEffect} from 'react'
import FiatCurrency from "../FiatCurrency/FiatCurrency";
import PotfolioByCoin from "../PotfolioByCoin/PotfolioByCoin";
import axios from 'axios';
import styled from 'styled-components'


// styled TD
const TD = styled.td`
    border: 1px solid #2c2b2b;
    width : 25vh;
`;

export default function Portfolio(props) {

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
                <div label="overall">
                    <h1>Protfolio value</h1>
                    <p></p>
                    <table>
                        <thead>
                            <tr>
                                <th>Total Portfolio Value</th>
                            </tr>
                        </thead>
                        <tbody>
                            {
                                porfolioFiatValues.map( ({coinValue, fiatCurrency}) =>
                                <FiatCurrency key={fiatCurrency}
                                    currency={fiatCurrency}
                                    coinValue={coinValue}
                                    />
                                )
                            }
                        </tbody>
                    </table>
                </div>
                <div>
                    <h3>list of coins balance and values</h3>
                    <table>
                        <thead>
                            <th>coin name</th>
                            <th>Ticker</th>
                            <th>coin balance</th>
                        </thead>
                        <tbody>
                            {
                              portfolioByCoins.map( ({coinName, ticker, coinBalance }) =>
                              <PotfolioByCoin coinName={coinName} ticker={ticker} coinBalance={coinBalance} />
                                )  
                            }
                        </tbody>
                    </table>
                </div>
            </div>
        )
    
}
