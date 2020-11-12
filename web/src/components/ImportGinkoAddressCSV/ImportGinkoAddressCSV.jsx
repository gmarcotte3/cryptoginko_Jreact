import React from 'react'
import axios from 'axios';
import FilePicker from '../FilePicker/FilePicker';
import CoinAddress from '../CoinAddress/CoinAddress';
import styled from 'styled-components'

const Div = styled.div`
    background: #2B2B2B 0% 0% no-repeat padding-box;
    //background: transparent linear-gradient(180deg, #393939 0%, #7A7A7A 100%) 0% 0% no-repeat padding-box;
    opacity: 1;
    height: 44px
`;

const DivPage = styled.div`
position: relative;
    top: -20px;
    background: transparent linear-gradient(180deg, #393939 0%, #7A7A7A 100%) 0% 0% no-repeat padding-box;
`;

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
                console.log("Success", response);  //debugging
                setCoinAddresses( response.data );
            })
            .catch(function (response) {
                //handle error
                console.log("error", response); //debugging
            });
    
    }

    return (
        <DivPage>
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
        </DivPage>
    )   
}
