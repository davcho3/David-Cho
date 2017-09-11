import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.lang.*;
import java.math.BigInteger;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class primesInput extends JFrame
{
  private static JLabel primeInstruc;
  private JButton inputPrimes;
  private JButton filePrimes;
    
   
  public primesInput()
  {
    super( "Get Prime Numbers" );

    setDefaultCloseOperation( JFrame.HIDE );
    getContentPane().setLayout(new BorderLayout());
 
     primeInstruc = new JLabel("Choose whether to Input your own prime number OR Get primes from a File");
    getContentPane().add (filePrimes, BorderLayout.NORTH );
    
    
    inputPrimes = new JButton("InputPrimes");
    getContentPane().add (inputPrimes, BorderLayout.EAST );
    inputPrimes.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                         inputPrimes();
                    }
                });


    filePrimes = new JButton("Block a message");
    getContentPane().add (filePrimes, BorderLayout.WEST );
    filePrimes.addActionListener(new ActionListener() {
                @Override
    public void actionPerformed(ActionEvent e) {
                    System.out.println("override files pressed");
                }
            });
   
    
    setSize( 200, 200 );
    setVisible( true );

  }
  
  
  public void inputPrimes()
  {
    
  }
  
  public void getFile()
  {
    
  }
  public static void main(String args[]) 
  {   
    new primesInput();
  }
  
  public void actionPerformed( ActionEvent event )
      {
         JOptionPane.showMessageDialog( null,
            "You pressed: " + event.getActionCommand() );
         
         if ( event.getSource() == inputPrimes )
         {
           
           System.out.println("uh");
           
         }
         else //if( event.getSource() == filePrimes)
         {
           System.out.println("files pressed");
         } 
      }
}