import React from 'react'


/**
 * Fiat currency component that will display a row in a table with the correct
 * fiat symbol 
 * @param {*} props 
 */
export default function FiatCurrency(props) {

    console.log("fiate currency props=", props)

    const currencyOptions = {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
    }
    const currencyOptionsJpy = {
        minimumFractionDigits: 0,
        maximumFractionDigits: 0,
    }

    const getPriceUSD = () => { 
        if ( props.coinValue != undefined ) {
        return (
            props.coinValue.toLocaleString(undefined, currencyOptions)

        );
        } else { return "" };
    }
    const getPriceNZD = () => { 
        if ( props.coinValue == undefined ) {
            return "";
        } else {
        return (
            props.coinValue.toLocaleString(undefined, currencyOptions)
        )
        };
    }
    const getPriceJPY = () => { 
        if ( props.coinValue == undefined ) {
            return "";
        } else {
        return (
            props.coinValue.toLocaleString(undefined, currencyOptionsJpy)
        )
        };
    }

    // TODO support pounds euros and others here...
    const getValue = () => {
        if ( props.currency === 'JPM') {
            let symbol = (props.symbol? props.symbol: "¥");
            return (  symbol + getPriceJPY() +" 万円");
        } else if (props.currency === 'JPY') {
            let symbol = (props.symbol? props.symbol: "¥");
            return ( symbol + getPriceJPY() +" 円");
        } else {
            let symbol = (props.symbol? props.symbol: "$");
            return ( symbol + getPriceUSD() + " " + props.currency );
        }
    }
    
    return (
        <>
        {getValue()}
        </>
    )
}
