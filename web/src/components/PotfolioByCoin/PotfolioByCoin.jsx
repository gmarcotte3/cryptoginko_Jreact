import React from 'react'
import styled from 'styled-components'
import FiatCurrency from "../FiatCurrency/FiatCurrency";
import btcIcon from './BTC.svg';
import unkIcon from './kiwiGold.png';
import adaIcon from './ADA.svg';
import bchIcon from './BCH.svg';
import dashIcon from './DASH.svg';
import eosIcon from './EOS.svg';
import iotIcon from './IOT.svg';
import ltcIcon from './LTC.svg';
import usdtIcon from './USDT.svg';
import xmrIcon from './XMR.svg';
import ethIcon from './ETH.png';
import linkIcon from './LINK.png';
import makerIcon from './MKR.png';
import zcashIcon from './ZEC.png';


// styled TD
const TD = styled.td`
    border: 1px solid #2c2b2b;
    width : 25vh;
`;

// styled TD
const TDTicker = styled.td`
    border: 1px solid #2c2b2b;
    width : 10vh;
`;

// styled TD
const TDbalance = styled.td`
    border: 1px solid #2c2b2b;
    width : 15vh;
    text-align: right;
`;

// styled TD
const TDPrice = styled.td`
    border: 1px solid #2c2b2b;
    width : 15vh;
    text-align: right;
`;

// stypled logo
const ImgCoin = styled.img`
    height: 2rem;
    pointer-events: none;
`;
// styled TD
const TDicon = styled.td`
    border: 1px solid #2c2b2b;
    width : 10vh;
`;




const coinIconsMap = new Map([
    ['ADA', adaIcon],
    ['BCH', bchIcon],
    ['BTC' , btcIcon],
    ['DASH', dashIcon],
    ['ETH', ethIcon],
    ['EOS', eosIcon],
    ['LINK', linkIcon],
    ['LTC', ltcIcon],
    ['MIOTA', iotIcon],
    ['MKR', makerIcon],
    ['USDT', usdtIcon],
    ['XMR', xmrIcon],
    ['ZEC', zcashIcon]
])

/**
 * this component displays one line from the PortfolioByCoin row. 
 * it displays the coin, coin balance, current price, coins value.
 * 
 */
export default function PotfolioByCoin(props) {

    console.log( "defaultFiatCurrency",props.defaultFiatCurrency)

    function findcode(item) {
        if (item.code === props.defaultFiatCurrency )
        return item;
    }
   
    let price = props.fiat_prices.find(findcode);
    let coinvalue = props.fiat_balances.find(findcode);
    let coinIcon = coinIconsMap.get(props.ticker);
    if ( typeof coinIcon === 'undefined') {
        coinIcon = unkIcon;
    }

    return (
            <tr><TDicon><ImgCoin src={coinIcon} /></TDicon>
                <TD>{props.coinName}</TD>
                <TDTicker>{props.ticker}</TDTicker>
                <TDbalance>{props.coinBalance}</TDbalance>
                <TDPrice>
                    <FiatCurrency   key={"price" + props.ticker}
                                currency={price.code}
                                coinValue={price.value}
                                symbol={price.symbol}
                    />
                </TDPrice>
                <TDPrice>
                    <FiatCurrency   key={"value" + props.ticker}
                                currency={coinvalue.code}
                                coinValue={coinvalue.value}
                                symbol={coinvalue.symbol}
                    />
                </TDPrice>
                
            </tr>
    );
}
