
import React from 'react'
import styled from 'styled-components'
import PotfolioByCoin from "../PotfolioByCoin/PotfolioByCoin";

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
   
 
    return (
        <>
            <tr>
                <TD>{props.walletName}</TD><TD></TD>
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
