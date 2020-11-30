
import React from 'react'
import styled from 'styled-components'
import PotfolioByCoin from "../PotfolioByCoin/PotfolioByCoin";
import FiatCurrency from "../FiatCurrency/FiatCurrency";

// styled TD
const TD = styled.td`
    width : 25vh;
`;



/**
 * this component displays one line from the PortfolioByCoin row. 
 * it displays the coin, coin balance, current price, coins value.
 * 
 */
export default function PortfolioByWalletList(props) {
    
    function getValueFromFiat( props2 ) {
        console.log("here here now props2=", props2)
        switch(props.defaultFiatCurrency) {
            case "USD":
              return props2.valueUSD;
              break;
            case "NZD":
              return props2.valueNZD;
              break;
            case "JPY":
              return props2.valueJPY;
              break;
            case "JPM":
              return props2.valueJPM;
              break;
            case "KRW":
              return props2.valueKRW;
              break;
            case "EUR":
              return props2.valueEUR;
              break;
            case "INR":
              return props2.valueINR;
              break;
            case "AUD":
              return props2.valueAUD;
              break;
            case "GBP":
              return props2.valueGBP;
              break;
   
        }
        return props2.valueUSD;
    }

 
    return (
        <>
            <tr>
                <TD>{props.walletName}&nbsp;&nbsp;&nbsp;
                    <FiatCurrency key={props.walletName + "-" + props.defaultFiatCurrency}
                                  currency={props.defaultFiatCurrency}
                                  coinValue={getValueFromFiat(props)}
                                  symbol="$"
                    /> 
                    
                </TD>
                <TD></TD>
            </tr>
            <tr>
                <TD></TD>
                <TD>
                    <table>
                        <thead>
                            <tr>
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
                </TD>
            </tr>
        </>
    );
}
