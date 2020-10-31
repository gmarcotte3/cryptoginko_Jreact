import React from 'react'
import styled from 'styled-components'

// styled TD
const TD = styled.td`
    border: 1px solid #2c2b2b;
    width : 25vh;
`;

/**
 * Fiat currency component that will display a row in a table with the correct
 * fiat symbol 
 * @param {*} props 
 */
export default function FiatCurrency(props) {

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
            props.coinValue.toLocaleString(undefined, currencyOptions)
        );
    }
    const getPriceNZD = () => { 
        return (
            props.coinValue.toLocaleString(undefined, currencyOptions)
        );
    }
    const getPriceJPY = () => { 
        return (
            props.coinValue.toLocaleString(undefined, currencyOptionsJpy)
        );
    }

    const getValue = () => {
        if ( props.fiatCurrency === 'JPM') {
            return ( getPriceJPY() +" 万円");
        } else if (props.fiatCurrency === 'JPY') {
            return ( getPriceJPY() +" 円");
        } else {
            return ( "$" + getPrice() );
        }
    }
    
    return (
        <tr>
            <TD>{getValue()}</TD>
            <TD>{props.fiatCurrency}</TD>
        </tr>
    )
}
