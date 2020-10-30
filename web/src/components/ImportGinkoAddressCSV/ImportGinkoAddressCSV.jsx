import React from 'react'
import axios from 'axios';
import FilePicker from '../FilePicker/FilePicker';



const IMPORT_GINKO_ADDR_URL = "http://localhost:8082/blockhead/import/addressescsv";

export default function ImportGinkoAddressCSV(props) {


    const onSubmitFile = (filedata) => {
        console.log("do the file upload now", filedata);
        
        axios({
            url: IMPORT_GINKO_ADDR_URL, 
            method: 'POST',
            data: filedata,
            headers: {
                Accept: 'application/json, text/plain',
                'Content-Type': 'multipart/form-data',
            }
            }, {
          // receive two parameter endpoint url ,form data 
          }).then(
            res => { // then print response status
            console.log(res.statusText)
          }).catch ( function (err) {
            console.log("we got an error", err);
          });     
    }

/*
    handleClick = async (event) => {
        event.preventDefault();
        let URL = "http://localhost:8082/blockhead/import/addresses?filename=%2Fhome%2Fgmarcotte%2FDesktop%2Fblockhead%2FpublicAddresses_test.csv";
        let filename=document.getElementById('theFileName').value;
        let fullPathFilename=document.getElementById('theFullPathFilename').value;
        console.log("filename=",filename);
        console.log("fullpath=",fullPathFilename);
        let response = await axios.put(IMPORT_GINKO_ADDR_URL);
        console.log(response);
    }
*/

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
        </div>
    )   
}
