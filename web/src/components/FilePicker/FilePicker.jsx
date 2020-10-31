import React, {useState} from 'react';
import './FilePicker.css';
import styled from 'styled-components'
import axios from 'axios';


// Styled button
const Input = styled.input`
    border: 3px solid blue;
    background-color: black;
    color:white;
    border-radius: 10px;
    font-size: 18px;
    text-align: center;
    width: 150px;
`;


/**
 * file picker component. This component manges state using the new functional component useState options.
 * The component and all CSS files, images are in this directory. 
 * 
 * This is a single file selection
 * 
 * TODO: add component styling when a proper design for this componet is done.
 * @param {} props 
 */
export default function FilePicker(props) {
    const [selectedFile, setSelectedFile] = useState(null);
  
    const IMPORT_GINKO_ADDR_URL = "http://localhost:8082/blockhead/import/addressescsv";

    /**
     * Handle the file selection button event. 
     * we save the file name in local state.
     * @param {*} event 
     */
    const onChangeHandler=event=>{
      setSelectedFile(event.target.files[0]);
    }

    
    const handleSubmit = (event) => {
      console.log("sumit, selected file=", selectedFile);
      console.log("event", event);


      var bodyFormData = new FormData();
      bodyFormData.append('file', selectedFile); 

      console.log("file", selectedFile ); // debug

      props.onSubmitFile(bodyFormData);
      /*
      axios({
        method: 'post',
        url: IMPORT_GINKO_ADDR_URL,
        data: bodyFormData,
        headers: {'Content-Type': 'multipart/form-data' }
        })
        .then(function (response) {
            //handle success
            console.log("SUCKsess", response);  //debugging
        })
        .catch(function (response) {
            //handle error
            console.log("error", response); //debugging
        });
      */
      
      event.preventDefault();
    }
    
  
    //<form method="POST" onSubmit={handleSubmit} encType="multipart/form-data">
    //<form method="POST" action={IMPORT_GINKO_ADDR_URL} encType="multipart/form-data">
    return (
      <div >
        <div >
          <div >
            <form method="POST" onSubmit={handleSubmit} encType="multipart/form-data">
            <div >
              <label>Upload Your File </label>
              <input type="file" accept=".csv" name="file" onChange={onChangeHandler} />
              <Input type="submit" value="Upload csv" />
            </div>
            
          </form>
          </div>
        </div>
    </div>
    )
  
  }