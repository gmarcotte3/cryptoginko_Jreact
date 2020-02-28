package com.marcotte.blockhead.util;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadCSV
{
  static public List<List<String>> readCsv(String filename) throws FileNotFoundException, IOException
  {
    List<List<String>> records = new ArrayList<List<String>>();
    try (CSVReader csvReader = new CSVReader(new FileReader(filename));)
    {
      String[] values = null;
      while ((values = csvReader.readNext()) != null)
      {
        records.add(Arrays.asList(values));
      }
    }
    return records;
  }
}
