/*
 * Project #3 - CS 342
 * 
 * Team Members: David Cho and Aiwan Hazari
 * 
 * Description: This program uses an RSA Encryption/Decryption method to 
 *              encode and decode messages. The program gives the user the
 *              options of Creating a key, Blocking a file, Unblocking a file, 
 *              or Encrypting/Decrypting. Creating a key creates the the private 
 *              and public keys and their respective XML files. The user needs
 *              to either input prime numbers for this or choose to get the prime
 *              numbers from a file. Blocking a file creates a Òblocked fileÓ from 
 *              an ASCII text file and a block size value (which the user picks). 
 *              Unblocking a file creates an ASCII text file from a Òblocked fileÓ.
 *              Encrypting/Decrypting creates a new Òblocked fileÓ that is the result 
 *              of running the encryption algorithm on each line of another blocked 
 *              file using the key pair (pair is in either the public or private key 
 *              file, which the user specifies).
 *       
 * Errors:  The functions divide and modulus (in unsignInt class) do NOT work. We 
 *          replaced wherever these should occur with a multiply function instead, 
 *          since that works well. 
 * 
 */

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.io.File;
import java.lang.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
//libraries to make XML files
import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError; 
import javax.xml.parsers.ParserConfigurationException; 
import org.xml.sax.SAXException; 
import org.xml.sax.SAXParseException; 
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.dom.DOMSource; 
import javax.xml.transform.stream.StreamResult;


public class proj3 extends JFrame implements ActionListener
{
  //buttons for user to choose task
  JButton createKeys;
  JButton blockMsg;
  JButton unblockMsg;
  JButton rsaEnDe;
  public static String[] primes;       //string array of prime numbers  
  public static int primeArrSize;     //# of prime numbers - size of array
  public static readFile fileRd;       //class to read file and store prime #s from
  //classes - need throughout program
  public static rsaAlg rsa;
  public static blocking block;
  public static unsignInt largeInt;
  //significant file names - need throughout program
  public static String primeNumbersFile;
  public static String originalMsgFile;
  public static String blockAsciiMsgFile;
  public static String blockFile;
  public static String xmlPublicFile;
  public static String xmlPrivateFile;
  //xml files' private and public key tags
  public static String pubKeyTag; 
  public static String privKeyTag;
   
  
  public proj3()
  {
    //make gui with 4 buttons
    super( "RSA Encryption/Decryption" );

    setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    getContentPane().setLayout(new BorderLayout());
    getContentPane().setLayout(new GridLayout(4, 1, 5, 5));
  
    //button for making keys
    createKeys = new JButton("Create public/private keys");
    getContentPane().add (createKeys);
    createKeys.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                      getPubPrivKeys();
                    }
                });

    //button for blocking a message
    blockMsg = new JButton("Block a message");
    getContentPane().add (blockMsg);
    blockMsg.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    blockMsg();
                }
            });
    
    //button for unblocking a message
    unblockMsg = new JButton("Unblock a message");
    getContentPane().add (unblockMsg);
    unblockMsg.addActionListener(new ActionListener() {
                @Override
    public void actionPerformed(ActionEvent e) {
                    unblock();
                }
            });
    
    //button for encrypting and decrypting
    rsaEnDe = new JButton("Encrypt/Decrypt");
    getContentPane().add (rsaEnDe);
    rsaEnDe.addActionListener(new ActionListener() {
                @Override
    public void actionPerformed(ActionEvent e){
                    endecode();
                }
            });
    
    setSize( 400, 400 );
    setVisible( true );
  }
   
  
  // Function to check whether a number is prime or not 
  // (Code referenced from: http://www.mkyong.com/java/how-to-determine-a-prime-number-in-java/)
   boolean isPrime(unsignInt n) 
   {
      //check if n is a multiple of 2
     unsignInt checkZero = new unsignInt(0);                                                       //*** error doing mod here - replaced with divide
     
     unsignInt mod2 = new unsignInt(2);
     unsignInt a = new unsignInt(mod2);
     a = n.multiply(mod2);
     
     if (checkZero.equal(a) )
     {
        return false;
     }//if not, no need to check any evens --> check all odd numbers for any factorizations
     String nStr = "" + n;
     int max = Integer.valueOf(nStr);
      for(int i=3;i*i<=max;i+=2) 
      {
        unsignInt modi = new unsignInt(i);
        unsignInt a2 = new unsignInt(modi);
        a2 = modi.multiply(n);
        if(checkZero.equal(a2) )
            return false;
      }
     return true;
   }
  
   
   //Function to input the prime numbers from a user ( 20 values inputted )
   private void inputPrimesFromUser()
   {
    //get 20 prime numbers from user
    for(int i = 0; i < 20; i++)
    {
     String num = JOptionPane.showInputDialog("Enter a prime number:");
     //user didn't enter anything, have them enter again
     if(num == null)
     {
      i = i -1;
      continue;
     }
     
     boolean verifyPrime;
     unsignInt val = new unsignInt(num);    //get large number from user as an unsignInt class number
     
     verifyPrime = isPrime(val);    //make sure number is prime
     
     if(verifyPrime == true)   //if prime --> store number in an array 
     {
      //System.out.println("Number is prime!");
      primes[i] = num;
     }
     else   //warn user that the number was not a prime & go back a loop iteration to make sure we get 20 numbers
     {
      JOptionPane.showMessageDialog(null, "Number is not prime! Try again");
      i = i -1;
     }
    }
    
    JOptionPane.showMessageDialog(null, "Done inputting primes!");    //tell user we're done inputting numbers
  }
  
  
  //Function to get prime numbers from a file rather than user input
  void getPrimesFromFile(readFile fileRd)
  {
    fileRd.openFile(primeNumbersFile);
    primes = fileRd.readFile(primeArrSize);
    fileRd.closeFile();
  }
  
  
  //Function which initiates getting primes (from user or file)
  public void getPrimes()
    {
      String cmd = JOptionPane.showInputDialog("Enter 'input' if you want to input primes, otherwise enter 'file' to get primes from file");
      //System.out.println(cmd);
      if((cmd.equals( "input")) || (cmd.equals( "Input")) || (cmd.equals( "INPUT")))
        inputPrimesFromUser();
      else if((cmd.equals( "file")) || (cmd.equals( "File")) || (cmd.equals( "FILE")))
      {
        fileRd = new readFile();
        getPrimesFromFile(fileRd);
      }
      else
      {
        JOptionPane.showMessageDialog(null,"ERROR: Invalid input! Exiting...");
        System.exit(0);
      }
     }
  
 
  // main - calls all file names and class initializations for continuous use
  public static void main(String args[])
  {   
    primeArrSize =  20;
    primes = new String[primeArrSize];
    
    primeNumbersFile = "primeNumbers.txt";
    originalMsgFile = "originalMsg.txt";
    blockAsciiMsgFile = "asciiMsgFile.txt";
    xmlPublicFile = "publicKey.xml";
    xmlPrivateFile = "privateKey.xml";
    
    rsa = new rsaAlg();
    block = new blocking();
    
    new proj3();   //gui
  }   

  
  //RSA Algorithm class
  public static class rsaAlg
  {
    private String n;   // n = p * q
    private String e;
    private String d;
    private String p;   //prime #1
    private String q;   //prime #2
   
    //find if 2 numbers are relatively coprime                                                   //** error doing mod here - replaced with multiply
    boolean isCoPrime(unsignInt x, unsignInt y)
   {
     //find the maximum we will divide until
     unsignInt max;
     if(x.greaterEqual(y)) //x >= y)
       max = x;
     else
       max =  y;
     
     //check all numbers upto max to see if there is any divisibility 
     int numDivBy = 0;
     String maxStr = "" + max;
     int maxDiv = Integer.valueOf(maxStr);
     
     unsignInt zero = new unsignInt(0);
     for(int i = 2; i <= maxDiv; i++)
     {
       unsignInt iUI = new unsignInt(i);
       unsignInt xModi = iUI.multiply(x);
       unsignInt yModi = iUI.multiply(y);
       if((zero.equal(xModi))  && (zero.equal(yModi)))                   //(x % i ==0) && (y % i == 0))  //they ARE divisible by this number
       {
         numDivBy++;  //increment how many numbers they are both divisible by
         break;   //if we incremented at all we don't have relatively coprime numbers - so break
       }
     }
     
     //if there are no numbers both are divisible by, they are coprime
     if(numDivBy == 0)
       return true;
     else
       return false;
    }
    
    //Function to do calculations and get public and private keys 
    void getPublicPrivateKey(unsignInt p, unsignInt q)
    {
      unsignInt n;
      n = p.multiply(q); 
      
      unsignInt psi;
      //subtract 1 from both p and q
      unsignInt one, pminus1, qminus1;       //psi = (p-1) * (q -1);
      one = new unsignInt(1);
      
      pminus1 = p.subtraction(one);
      qminus1 = q.subtraction(one);
      psi = pminus1.multiply(qminus1); 
      
      //find e -> e < n and coprime to psi
      unsignInt e = new unsignInt(0);    //initialize e to zero
      boolean verifyCoPrime;
      
      //to find e, look through all values less than n
      String nStr = "" + n;
      float nFt = Float.valueOf(nStr);                                         //lose precision here? 
      
      for(int i = 2; i < nFt; i++)
      {
        unsignInt iUI = new unsignInt(i);
        verifyCoPrime = isCoPrime(iUI, psi);
         
         if(verifyCoPrime == true)
         {
           e = iUI;
           break;
         }
      }
      //check if e changed from original 0
      unsignInt zero = new unsignInt("0");
      if(e.equal(zero))
      {
        JOptionPane.showMessageDialog(null,"ERROR: Prime numbers are not relatively co-prime to find private and public keys!");
        System.exit(0);
      }
      
      
  
      //now find d                                                                     
      unsignInt d;
      unsignInt eModPsi = e.multiply(psi);                                             //*** error doing modulus here - replaced with multiply
      
      d = one.multiply(eModPsi);   // 1 / (e % psi)                      
      
      //store all values into class
      this.n = nStr;    //store n in class
      
      String dStr = "" + d;
      this.d = dStr;
   
      String eStr = "" + e;
      this.e = eStr;
  
      String pStr = "" + p;
      String qStr = "" + q;
      this.p = pStr;
      this.q = qStr;
      
       //debugging...
      System.out.println("n: " + n); 
      System.out.println("psi: " + psi);
      System.out.println("d: " + d); 
      System.out.println("e: " + e);
      System.out.println("prime 1: " + p);
      System.out.println("prime 2: " + q);
    }
    
    
    //Function to store public and private keys in an XML file
    void storeKeysInXML(String n, String e, String d)
    {
      //make public key file
      try
      {
        DocumentBuilderFactory docBuildFac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuild = docBuildFac.newDocumentBuilder();
        
        Document doc = docBuild.newDocument();
        
        Element elem = doc.createElement(pubKeyTag);
        doc.appendChild(elem);
        
        Element eInfo = doc.createElement("evalue");
        eInfo.appendChild(doc.createTextNode(e));
        elem.appendChild(eInfo);
        
        Element nInfo = doc.createElement("nvalue");
        nInfo.appendChild(doc.createTextNode(n));
        elem.appendChild(nInfo);
        
        TransformerFactory transformerFac = TransformerFactory.newInstance();
        Transformer trans = transformerFac.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult strResult = new StreamResult(new File(xmlPublicFile));
        trans.transform(source, strResult);
      }
      catch(Exception e2)
      {
        System.out.println("Failed to make public key xml file! Exiting...");
        System.exit(0);
      }
      
      
      //make private key xml file
      try
      {
        DocumentBuilderFactory docBuildFac2 = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuild2 = docBuildFac2.newDocumentBuilder();
        
        Document doc2 = docBuild2.newDocument();
        
        Element elem2 = doc2.createElement(privKeyTag);
        doc2.appendChild(elem2);
        
        Element dInfo = doc2.createElement("dvalue");
        dInfo.appendChild(doc2.createTextNode(d));
        elem2.appendChild(dInfo);
        
        Element nInfo2 = doc2.createElement("nvalue");
        nInfo2.appendChild(doc2.createTextNode(n));
        elem2.appendChild(nInfo2);
        
        TransformerFactory transformerFac2 = TransformerFactory.newInstance();
        Transformer trans2 = transformerFac2.newTransformer();
        DOMSource source2 = new DOMSource(doc2);
        StreamResult strResult2 = new StreamResult(new File(xmlPrivateFile));
        trans2.transform(source2, strResult2);
      }
      catch(Exception pk)
      {
        System.out.println("Failed to make private key! Exiting...");
        System.exit(0);
      }
    }
    
    //Function to perform the encryption and decryption operations
    void deEnCrypt(String blockFileName, String keyFileName, String newBlockFile)
    {
      String item1, item2;    //public or private key items  ( in order of -> item1 = e OR d  and item2 = n )
      String xmlFile;
      
      try{
      //Read xml file and store pair of values 
      DocumentBuilderFactory docBuildFacGet = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuildGet = docBuildFacGet.newDocumentBuilder();
      Document docGet;
      //docGet.normalize();
      NodeList list;
      Node node;
      
      //check if public or private key is needed & read its XML file to get each key
      if((keyFileName.equals("public")) || (keyFileName.equals("Public")) || (keyFileName.equals("PUBLIC")))    //public key file
      {
        xmlFile = xmlPublicFile;
        
        docGet = docBuildGet.parse(xmlFile);     //XMLReader.class.getResourceAsStream(
        list = docGet.getElementsByTagName(pubKeyTag);    //key tht user inputted
        node = list.item(0);
        
        Element elem = (Element) node;
        item1 = elem.getElementsByTagName("evalue").item(0).getTextContent();
        item2 = elem.getElementsByTagName("nvalue").item(0).getTextContent();
        
      }
      else    //private key file
      {
        xmlFile = xmlPrivateFile;
        docGet = docBuildGet.parse(xmlFile);
        
        list = docGet.getElementsByTagName(privKeyTag);
        node = list.item(0);
        
        Element elem = (Element) node;
        item1 = elem.getElementsByTagName("dvalue").item(0).getTextContent();
        item2 = elem.getElementsByTagName("nvalue").item(0).getTextContent();
    
      }
      
      //Read the blockFile to get input numbers and output this onto a new file
      String outputValStr;
      unsignInt outputVal;
      
      PrintWriter newFile;
      Scanner inputFile;
      //write a new Block file
      try
      {
        newFile = new PrintWriter(newBlockFile, "UTF-8");
        
        //get input values and store outputs into new file
        try
        {
          inputFile = new Scanner(new File(blockFileName));
          
          String numStr;
          unsignInt num;
          
          unsignInt item1Num = new unsignInt(item1);
          unsignInt item2Num = new unsignInt(item2);
          
          while(inputFile.hasNext())
          {
            outputVal = new unsignInt("1");    //algorithm for large exponentiation  - prepare for next input
            
            numStr = inputFile.next();
            num = new unsignInt(numStr);   //convert this string to unsignInt
            
            //do equation with each exponent increment - to get ouput number for this input number
            String it1Str = "" + item1Num;
            float it1 = Float.parseFloat(it1Str);
            for(int i = 0; i < it1; i++)                                                                      //*** error doing mod here - replaced with multiply
            {
              //helpful for large exponentiation --> output = (output * input) % n
              outputVal = outputVal.multiply(num);
              outputVal =  item2Num.multiply(outputVal) ;             //(outputVal*num) % item2Num;       
            }
            
            outputValStr = "" + outputVal;
            newFile.println(outputValStr);    //output this number to new file
          }
          inputFile.close();
         }
        catch(Exception readFile)
        {
          System.out.println("Unable to read block file! Exiting...");
          System.exit(0);
        }
        newFile.close();
      }
      catch(Exception e)
      {
        System.out.println("Unable to make new blocked file! Exiting..."); 
        System.exit(0);
      }
      }
      catch(Exception xmlunread)
      {
        System.out.println("Unable to read/locate XML file! Exiting....");
        System.exit(0);
      }
    }
  }//end class
  
  
  //Class for blocking purposes
   public static class blocking
  {
    String msg;
    String msgAscii;
    int blockSize;
      
    void reverse(int blockSize)    //unblock to decrypt message
    {
      //open blockedFile and read line by line to decrypt message
      Scanner inputFile;
      
      String[] overallMsgArr = new String[100];    //array to store all strings
      String overallMsg = "";    //string to get final message in the right order
      int ind = 0;
      
      //write a new Block file
      try
      {
        inputFile = new Scanner(new File(blockFile));
          
        String numStr;
        int blockNum;
        
        while(inputFile.hasNext())
        {
          String lineMsg = "";
          numStr = inputFile.next();   //get number from file
          int num = Integer.parseInt(numStr);   //convert this number to an int
          //convert this integer back to a new string to get rid of any 0-padding
          String oneLine = "" + num;
          
          int iter;
          //check if there was 0-padding, we dont have to iterate the entire blocksize
          if(oneLine.length() < blockSize*2)
          {
            //iterate only length required
            iter = oneLine.length();
          }
          else
          {
            iter = blockSize*2;
          }
          
          //go through this blockLine and convert this number to ascii values
          int i = 0;           
          String oneAsciiCharTEMP = "";
          while(i < iter)
          {
            char in1 = numStr.charAt(i);
            char in2  = numStr.charAt(i+1);
            oneAsciiCharTEMP += in1 + in2;  //this is the 2 digit number to convert ascii value to
            
            int letter = Integer.parseInt(oneAsciiCharTEMP);
            letter += 27;     //add 27 since we're using an ascii table thats off (less) by 27
            
            char letChar = (char)letter;     //character that corresponds to ascii value
            lineMsg += letChar;         //add character onto the line message
            
            i += 2;
            oneAsciiCharTEMP = "";
          }
          
          //add to the overall message array (when outputting --> output backwards) 
          overallMsgArr[ind] = lineMsg;
          ind++;
        }
        
        //get the final message in the right (aka backwards) order  (since file originally stores the words backwards a little)
        for(int msg = ind-1; msg > -1; msg--)
        {
          overallMsg += overallMsgArr[ind];
        }
        
        //write this message onto a new file
        PrintWriter fileOut;     //write a new file
        try
        {
          fileOut = new PrintWriter("decodedMessage.txt", "UTF-8");
          
          //store decoded message in a file
          fileOut.println(overallMsg);
          
          //prompt user that decoded file was made and give filename to user
          JOptionPane.showMessageDialog(null, "Decoded file was made! (in file: decodedMessage.txt)");
          
          fileOut.close();
        }
        catch(Exception decrypt)
        {
          System.out.println("Unable to make decrypted file for unblocking!");
          System.exit(0);
        }
        inputFile.close();
      }
      catch(Exception blkFile)
      {
        System.out.println("Unable to read/find blocked file!");
        System.exit(0);
      }
      
    }
    
    //Function to block a message received from user
    void blockMessage(String fileNameToStore, int blockSize)
    {
      //write a new file
      PrintWriter fileOut;
      try
      {
      fileOut = new PrintWriter(fileNameToStore, "UTF-8");
        
      String msgNums = this.msgAscii;    //get the message's ascii value (stored in class earlier)
      
      this.blockSize = blockSize;
      
      int msgSize = msgNums.length();
      
      int i = msgSize - 1; //start at back of string
      while(i >= 0)
      {
        String blocked = "";
        //check if we need 0-padding for number
          if(i < (2*blockSize))
          {
            //find out how many 0s are needed
            int numZeros = (2*blockSize) - i;
            for(int addzeros = 0; addzeros < numZeros-1; addzeros++)
            {
              blocked += "0";
            }
            
            //now add messageascii values
            for(int restMsg = 0; restMsg <= i; restMsg++)
            {
              blocked += msgNums.charAt(restMsg);
            }
            i = -1;  //end loop since now we got every digit
          }
          else    //no 0-padding needed - block off
          {
            int start = i - (2*blockSize) +1;    //test: 55 - 16 = 39 + 1 = 40 --> index 40 is included
            for(int blk = 0; blk < 2*blockSize; blk++)   //block off message with block*2 size parts
            {
              blocked += msgNums.charAt(start);    //add letters from front of this block
              start++;
            }
          }
          
          //store block-strings in a file
          fileOut.println(blocked);
          
          i = i - (2*blockSize);  //start at new index past blocked off digits
       }
      fileOut.close();
      }
      catch(Exception e){
        System.out.println("Block file not made! Exiting..."); 
        System.exit(0);
      }
    }
    
    //Function to convert message to ascii and store this in a file
    void convertToAscii(String asciiMsgFile)
    {
      //store ascii message in a new file
      PrintWriter fileOut;
      try
      {
       String msg = this.msg;
       int msgSize = msg.length();
 
          fileOut = new PrintWriter(asciiMsgFile, "UTF-8");    //make a new file to output to
          unsignInt msgAscii = new unsignInt("0");          //initialize to 0
          
          //go through all characters &&  add their (ascii value - 27)*(100^positionOfLetter)
          for(int i = 0; i < msgSize; i++)
          {
            int av = (int)msg.charAt((i)) - 27;   //get ascii value
            unsignInt tempascii = new unsignInt(av);    //convert this ascii to our unsign int class value

            //get position's power of 100
            unsignInt powtemp = new unsignInt(100);    
            unsignInt charFinalVal;
            
            //multiply 100 by itself i times (according to the index's position)
            //no exponentiation function in unsignInt so do it manually
            unsignInt expFinal = new unsignInt(1);
            for(int ex = 0; ex < i; ex++)
            {
              expFinal = expFinal.multiply(powtemp);    //multiply 100  i amount of times
            }
            
            //now multiply this number by the ascii value's number
            charFinalVal = expFinal.multiply(tempascii);

            //add to overall sum of message's value
            msgAscii =  msgAscii.add(charFinalVal);   
          }

          String mStr = "" + msgAscii;     //store this large value into a string
      
          fileOut.println(mStr);  //store ascii message in a file
          this.msgAscii = mStr;   //store this value as string in class as well
          fileOut.close();
      }
        catch(Exception e)
        {
          System.out.println("File with ASCII-Message not made! Exiting..."); 
          System.exit(0);
        }
     }
  }//end class
   
  
  //Function for Button #1 - get public and private keys & store in XML file
  public void getPubPrivKeys()
  {
    //get and store prime values
    getPrimes();
    
    //choose 2 primes values RANDOMLY from array of primes
    int r1, r2;
    Random rand = new Random();
    r1 = rand.nextInt(19) + 0; //19 is the maximum and the 0 is our minimum 
    r2 = rand.nextInt(19) + 0; //19 is the maximum and the 0 is our minimum 
    
    //change these primes from a string to an unsignInt number
    String p1Str = primes[r1];
    String p2Str = primes[r2];
    unsignInt p1 = new unsignInt(p1Str);
    unsignInt p2 = new unsignInt(p2Str);
    
    //use rsa algorithm to get n, e, and d
    rsa.getPublicPrivateKey(p1, p2);
    
    //debugging
    //System.out.println("Public Key: " + rsa.e + ", " + rsa.n + ".  Private Key: " + rsa.d  + ", "+ rsa.n);
    
    String n = rsa.n;
    String e = rsa.e;
    String d = rsa.d;
    
    //get user input for name of keys' tags
    pubKeyTag = JOptionPane.showInputDialog("Enter your desired public key name: ");
    privKeyTag = JOptionPane.showInputDialog("Enter your desired private key name: ");
    
    rsa.storeKeysInXML(n, e, d);   //store public and private keys in an XML file
  }
   
  //Function for Button #2 - block a message
  public void blockMsg()
  {
    //get user input of message to block
    String msg = JOptionPane.showInputDialog("Enter a message you want to encrypt: ");
    
    //store message into a new file & to class
      PrintWriter fileOut;
      try
      {
        fileOut = new PrintWriter(originalMsgFile, "UTF-8");
        fileOut.println(msg);  //store original message into file
        fileOut.close();
      }
      catch(Exception e)
      {
        System.out.println("Unable to make message file! Exiting..."); 
        System.exit(0);
      }
      
     block.msg = msg;
     
     //get blocking size from user
    String blockSizeStr = JOptionPane.showInputDialog("Enter a blocking size: ");
    int blockSize = Integer.parseInt(blockSizeStr);
    
    block.convertToAscii(blockAsciiMsgFile);   //convert message to ascii equivalent & store in a file
    
    //get filename of blocks-file from user
    blockFile = JOptionPane.showInputDialog("Enter the filename to store the encrypted blocks: ");
    
    block.blockMessage(blockFile, blockSize);   //block message and store in file
  }
  
  //Function for Button #3 - unblock the code
  public void unblock()
  {
    //call function to unblock/reverse the message back
    int blockSize = block.blockSize;
    block.reverse(blockSize);
  }
  
  
  //Function for Button #4 - encrypt/decrypt 
  public void endecode()
  {
    //get the newBlockFile name from user
    String newBlkFileName = JOptionPane.showInputDialog("Enter the filename to store the Output Numbers: ");
    String keyFileName = JOptionPane.showInputDialog("Enter 'private' or 'public' to choose which key file to use: ");
    
    rsa.deEnCrypt(blockFile, keyFileName, newBlkFileName);
  }
  
  
  // Action performed function here - but overrided earlier
  public void actionPerformed( ActionEvent event )
      {  
         if ( event.getSource() == createKeys )
         {
           
           System.out.println("uh");
           
         }
         else if( event.getSource() == blockMsg)
         {
           System.out.println("block message pressed");
         }
         else    
         {
           System.out.println("rsa pressed");
         }  
      }
}