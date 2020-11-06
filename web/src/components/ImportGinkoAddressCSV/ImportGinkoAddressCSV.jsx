import React from 'react'
import axios from 'axios';
import FilePicker from '../FilePicker/FilePicker';
import CoinAddress from '../CoinAddress/CoinAddress';



const IMPORT_GINKO_ADDR_URL = "http://localhost:8082/ginkoJ/import/addressescsv";

export default function ImportGinkoAddressCSV(props) {

    const URL = props.importGinkoAddrURL;
    const [coinAddresses, setCoinAddresses] = React.useState([]);

    // TODO move up to app
    const onSubmitFile = (bodyFormData) => {
        console.log("do the file upload now", bodyFormData);
        
        axios({
            method: 'post',
            url: IMPORT_GINKO_ADDR_URL,
            data: bodyFormData,
            headers: {'Content-Type': 'multipart/form-data' }
            })
            .then(function (response) {
                //handle success
                console.log("SUCKsess", response);  //debugging
                setCoinAddresses( response.data );
            })
            .catch(function (response) {
                //handle error
                console.log("error", response); //debugging
            });
    
    }

    return (
        <div>
            <h1>Import Ginko address csv file</h1>
            <p>
            This is where you can inport address export from this ginko portfolio management
            program. This will create/update coin address balance in the database so a update
            portfoilo value can be calculated.</p>
            <div>
            <FilePicker onSubmitFile={onSubmitFile}  />
            </div>
            <br />
            <table>
            <tbody>
            {
                coinAddresses.map( ({address, walletName, currency, lastBalance, lastUpdated, message}) =>
                <CoinAddress 
                    key={address} address={address} walletName={walletName} currency={currency}
                    lastBalance={lastBalance} lastUpdated= {lastUpdated} message={message}
                />
                 
                )
            }
            </tbody>
            </table>
        </div>
    )   
}
