/*
 * Project #3 - CS 342
 * 
 * Team Members: David Cho and Aiwan Hazari
 * 
 * Description: This file opens a specified file, reads and stores
 *              the content of the file in an array (ie the prime numbers
 *              in the file), and closes the file. 
 * 
 */

import java.io.*;
import java.util.*;

public class readFile
{
  private Scanner file;
  
  //open file
  public void openFile(String fileName)
  {
    try{
      file = new Scanner(new File(fileName));
    }
    catch(Exception fErr)
    {
      System.out.println("Cannot find file " + fileName);
      System.exit(0);
    }
  }
  
  //read and store values from file
  public String[] readFile(int primeSize)
  {
    int i = 0;
    String[] newPrimes = new String[primeSize];
    
    while(file.hasNext())
    {
      String num = file.next();
      newPrimes[i] = num;
      i++;
    }
    
    return newPrimes;
  }
  
  //close the file
  public void closeFile()
  {
    file.close();
  }
}