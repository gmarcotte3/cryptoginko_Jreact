 import React from 'react'
 import PropTypes from 'prop-types';
 import styled from 'styled-components'

 // styled TD
const TD = styled.td`
    border: 1px solid #2c2b2b;
    width : 25vh;
`;

// Styled button
const Button = styled.button`
    border: 3px solid blue;
    background-color: black;
    color:white;
    border-radius: 10px;
    font-size: 18px;
    text-align: center;
    width: 150px;
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

    const getPrice = () => {
        return (
            this.props.price.toLocaleString(undefined, this.currencyOptions)
        );
    }
    const getPriceNZD = () => {
        return (
            this.props.nzd.toLocaleString(undefined, this.currencyOptions)
        );
    }
    const getPriceJPY = () => {
        return (
            this.props.jpy.toLocaleString(undefined, this.currencyOptionsJpy)
        );
    }

    return (
       <tr className="coin-row">
         <TD>{this.props.name}</TD>
         <TD>{this.props.ticker}</TD>
         <TD>${this.getPrice()}</TD>
           <TD>${this.getPriceNZD()}</TD>
           <TD>{this.getPriceJPY()}円</TD>
         </tr>
    )

 }  //endCoin

 // data member requirements and type.
 Coin.protoType = {
     name: PropTypes.string.isRequired,
     ticker: PropTypes.string.isRequired,
     price: PropTypes.number.isRequired
 }
 