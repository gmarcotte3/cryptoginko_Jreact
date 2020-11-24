 import React from 'react'
 import PropTypes from 'prop-types';
 import styled from 'styled-components'

 // styled TD
const TD = styled.td`
    border: 1px solid #2c2b2b;
    width : 16vw;
`;

// styled TD
const TDPrice = styled.td`
    border: 1px solid #2c2b2b;
    width : 25vh;
    text-align: right;
`;

/**
 * This class represents a crypto coin with attributes of name, ticker, and price
 * This class also has a member function that get the current price from the internet.
 */
export default function Coin(props) {
    const currencyOptions = {
       minimumFractionDigits: 2,
       maximumFractionDigits: 2,
    }
    const currencyOptionsJpy = {
       minimumFractionDigits: 0,
       maximumFractionDigits: 0,
    }

    const getPriceUSD = () => {
        return (
            props.priceUSD.toLocaleString(undefined, currencyOptions)
        );
    }
    const getPriceNZD = () => {
        return (
            props.priceNZD.toLocaleString(undefined, currencyOptions)
        );
    }
    const getPriceJPY = () => {
        return (
            props.priceJPY.toLocaleString(undefined, currencyOptionsJpy)
        );
    }

    return (
        <tr className="coin-row">
            <TD>{props.coinName}</TD>
            <TD>{props.ticker}</TD>
            <TDPrice>${getPriceUSD()}</TDPrice>
            <TDPrice>${getPriceNZD()}</TDPrice>
            <TDPrice>{getPriceJPY()}円</TDPrice>
        </tr>
    )

}  //endCoin

// data member requirements and type.
Coin.protoType = {
    coinName: PropTypes.string.isRequired,
    ticker: PropTypes.string.isRequired,
    price: PropTypes.number.isRequired
}