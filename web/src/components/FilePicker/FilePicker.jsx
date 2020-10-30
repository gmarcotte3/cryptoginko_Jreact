import React, {useState} from 'react';
import './FilePicker.css';
import styled from 'styled-components'

/*
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
*/

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
    const [loadedFiles, setLoadedFiles] = useState(0);
  
    const IMPORT_GINKO_ADDR_URL = "http://localhost:8082/blockhead/import/addressescsv";

    /**
     * Handle the file selection button event. 
     * we save the file name in local state.
     * @param {*} event 
     */
    const onChangeHandler=event=>{
      setSelectedFile(event.target.files[0]);
    }

    /*
    const handleSubmit = (event) => {
      event.preventDefault();
      console.log("sumit, selected file=", selectedFile);
      console.log("event", event);
    }
    */
  
    /**
     * handle the submit (upload) button
     */
    /*
    const onClickHandler = () => {
      if ( props.onSubmitFile === null) {
        alert("no submit handler");
      }

      const data = new FormData() 
      let file = props.selectedFile;
      data.append('file', file);
      props.onSubmitFile(data);
     
    }
    */
  
    // removed the <Button type="button" className="btn btn-success btn-block" onClick={onClickHandler}>Upload</Button>
    // render part here.
    return (
        <div >
          <div >
            <div >
              <form method="POST" action={IMPORT_GINKO_ADDR_URL} enctype="multipart/form-data">
              <div >
                <label>Upload Your File </label>
                <input type="file" accept=".csv" name="file" onChange={onChangeHandler} />
                <input type="submit" value="Upload csv" />
              </div>
            </form>
            </div>
          </div>
      </div>
    )
    /*
    return (
      <div className="upload-container">
        <div className="row">
          <div className="formupload">
            <form method="POST" onSubmit={handleSubmit} enctype="multipart/form-data">
            <div className="form-group files">
              <label>Upload Your File </label>
              <input type="file" accept=".csv" name="file" onChange={onChangeHandler} />
              <input type="submit" value="Upload csv" />
            </div>
            
          </form>
          </div>
        </div>
    </div>
    )
    */
  }