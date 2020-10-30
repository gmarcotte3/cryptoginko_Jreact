package com.marcotte.blockhead.util;

import com.opencsv.CSVReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple csv reader, does nothing but create an array of rows which are and array of column values in string notation.
 */
public class ReadCSV
{
  /**
   * read the csv file into the array of rows array of column values.
   * @param filename  full path to a file accessable from this application (on the server)
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  static public List<List<String>> readCsv(String filename) throws FileNotFoundException, IOException
  {
    List<List<String>> records = new ArrayList<List<String>>();
    try (CSVReader csvReader = new CSVReader(new FileReader(filename)))
    {
      records = readTheCsvFileAndCreateTheRecords(csvReader );
    }
    return records;
  }

  /**
   * read the csv file stream into the array of rows array of column values.
   * @param file  multi part stream file input
   * @return
   * @throws IOException
   */
  static public List<List<String>> readFileCsv(MultipartFile file) throws IOException
  {
    List<List<String>> records = new ArrayList<List<String>>();
    try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream())))
    {
      records = readTheCsvFileAndCreateTheRecords(csvReader );
    }
    return records;
  }

  /**
   * does the actual reading from the file stream and create the array of rows array of column values.
   * @param csvReader
   * @return
   * @throws IOException
   */
  static private List<List<String>> readTheCsvFileAndCreateTheRecords(CSVReader csvReader ) throws IOException
  {
    List<List<String>> records = new ArrayList<List<String>>();
    String[] values = null;
    while ((values = csvReader.readNext()) != null)
    {
      records.add(Arrays.asList(values));
    }
    return records;
  }
}
