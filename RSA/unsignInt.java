/*
 * Project #3 - CS 342
 * 
 * Team Members: David Cho and Aiwan Hazari
 * 
 * Description: This file contains our class for the unsigned integer.
 *              Since we will be using large numbers (and are not allowed
 *              to use built-in libraries to analyze large numbers), we 
 *              had to implement our own class. This class implements the
 *              add, multiply, subtraction, division, modulus, and relational
 *              operator functions for the large-unsigned-integer. 
 * 
 */

//package rsa;

import java.util.Arrays;
import java.util.Scanner;

public class unsignInt 
{
    public static boolean          zeros = true;
    private int                    MAX = 100;
    private int                    digit;
    private int[]                  digits;
    
    public unsignInt()
    {
//      System.out.println(this.digit);
       digits = new int[MAX];
       for(int i=0; i<MAX; i++){
           digits[i] = 0;
       }
    }
    
    //string input
    public unsignInt(String digit)
    {
//        System.out.println(digit);
        this.digit = digit.length();
       
        // initialize then allocate
        digits = new int[MAX];
        for(int i=0; i<MAX; i++)
        {
            digits[i] = 0;
        }
        
        int p = 0; 
        for(int j=digit.length()-1; j>=0; j--){
            digits[p] = digit.charAt(j) - '0';
            p++;
        }
        
    }
    
    //long input
    public unsignInt (long a)
    {
        
        int length = String.valueOf(a).length();

        digits = new int[MAX];
        for(int i=0; i<MAX; i++)
        {
            digits[i] = 0;
        }
        
//        int j=length-1; j>=0; j--
        for(int i = length-1; i>=0; i--){
            int rem = (int) (a % 10);
            digits[i] = rem;
            a = a / 10;
                    
        }

    }
    
    //unsigned input
    public unsignInt (unsignInt a)
    {
        int length = a.getDigit();        
        digits = new int[MAX];
        for(int i=0; i<MAX; i++)
        {
            digits[i] = 0;
        }
        
        for(int i = 0; i<length; i++){
            digits[i] = a.digits[i];
        }
        

    }
    
    //add
    public unsignInt add(unsignInt a)
    {
                
        unsignInt sum = new unsignInt();
        int carry = 0;

        for(int i=0; i< MAX; i++)
        {  
            sum.digits[i] = (this.digits[i] + a.digits[i] + carry) % 10;
//            
            carry = (this.digits[i] + a.digits[i] + carry)/10;  
        }
       
//        for(int i=MAX-1; i>=0; i--){
//            System.out.print(sum.digits[i]);
//        }

        return sum;
    }
    
    //subtract
    public unsignInt subtraction(unsignInt a)
    {
      
        unsignInt sub = new unsignInt();
        
        int carry = 0;

        for(int i=0; i<MAX; i++)
        {  
            int temp = (digits[i] - a.digits[i])-carry;
            carry = 0;
            
            if(temp < 0)
            {
                temp += 10;
                carry = 1;
            }
            sub.digits[i] = temp;
        }
        
        if(carry == 1){
            return null;
        }
        return sub;
    }
    
    //multiply
    public unsignInt multiply(unsignInt a)
    {
        // length1 is the length of value1
        int length1 = this.digit;
        // length2 is the length of a or value2
        int length2 = a.getDigit();
        
        // creates a new object called multiply.. this object is basically an int array that
        // is already allocated and initialized
        unsignInt multiply = new unsignInt();
        
      
        int partial = 0;
        int carry = 0;
        int second = 0;
        int index = 0;
        for(int i=0; i<length2; i++)
        {      
            
            for(int j=0; j < length1; j++)
            {  
                index = i+j;
                // partial gives a value
                /*
                Each iteration through loop partial stores a value, depending on the index.
                example: 25
                        x25: 
                1st iteration:  5*5        = 25
                2nd iteration: (2*5)+2     = 10
                3rd iteration:  2*5        = 10
                4th iteration: (2*2)+1     = 5
                */
                
                partial = (a.digits[i] * this.digits[j]);

                // you divide the partial by 10 to get the first digit (most left) and keep adding
                /*
                From about example, the first digit is:
                1st iteration:  2
                2nd iteration:  1
                3rd iteration:  1
                4th iteration:  0
                */
                carry = partial/10;
//                System.out.println("Carry is stored in index " + index+1 + " - " + carry);
                multiply.digits[index+1] += carry;
                // you mod %10 to get the second digit
                /*
                From about example, the first digit is:
                1st iteration:  5
                2nd iteration:  2
                3rd iteration:  0
                4th iteration:  5
                */
                second = partial%10;
                multiply.digits[index] += second;
//                System.out.println("Second is stored in index " + index + " - " + second);
                
                multiply.digits[index+1] += multiply.digits[index] / 10;
                
//                System.out.println("This is stored in index " + index+1 + " - "+ multiply.digits[index+1]);
                multiply.digits[index] %= 10;
//                System.out.println("This is stored in index " + index + " - "+ multiply.digits[index]);
//                multiply.digits[j] = second;
            }
        }
        
        return multiply;
    }
    
    //divide
    public unsignInt divide(unsignInt a)
    {
        int length1 = this.digit;
        // length2 is the length of a or value2
        int length2 = a.getDigit();
        unsignInt div = new unsignInt();
        unsignInt rem = new unsignInt();
        unsignInt ten = new unsignInt(10);
        
        
        
        return div;
    }

    //is literally equal
    public boolean equal(unsignInt a)
    {
        int length = a.getDigit();
        for(int i=0; i<length; i++)
        {
            if(digits[i] != a.digits[i])
                return false;
        }
        return true;
    }
    
    // >
    public boolean greaterThan(unsignInt a)
    {
        int length = a.getDigit();
        for(int i=length-1; i>=0; i--)
        {
            if(digits[i] > a.digits[i])
                return true;
            else if (digits[i] < a.digits[i])
                return false;
        }
        
        return false;
    }
    
    // !=
    public boolean notEqual(unsignInt a)
    {
        return !this.equal(a);
    }
    
    // <
    public boolean lessThan(unsignInt a)
    {
        return !this.greaterThan(a) && !this.equal(a);
    }
    
    // >=
    public boolean greaterEqual(unsignInt a)
    {
        return this.equal(a) || this.greaterThan(a);
    }
    
    // <=
    public boolean lessEqual(unsignInt a)
    {
        return this.equal(a) || this.lessThan(a);
    }
    
    
    public void setDigit(String a)
    {
        this.digit = a.length();
    }
    
    public int getDigit() {
        return digit;
    }
    
    @Override
    public String toString()
    {
        String p = "";
        int i;
        if(zeros == true)
        {
            for(i=this.digit-1; i>=0; i--){
                p = digits[i] + p;
            }
        }
        else if(zeros == false)
        {
            for(i=MAX-1; i>=0; i--)
            {
                if(digits[i] != 0)
                    break;
            }
            
            for(; i>=0; i--)
            {
                p = p + digits[i];
            }
        }
        
        if(p.equals(""))
            p="0";
            
        return p;
    }
    
    
    //function to test specific input on values
    public void test()
    {
      
      Scanner reader = new Scanner(System.in);
      //System.out.println("Please enter a large integer");
      //String n = reader.nextLine();
      
      // set to false so it does not print leading 0s
      unsignInt.zeros = false;
      unsignInt value = new unsignInt("2");
      unsignInt value2 = new unsignInt("4");
      unsignInt value3 = new unsignInt("987654321");

      unsignInt a = new unsignInt(value);
      
      /* arithmetic operations multiply, divide, and modulus operations */

      a = value.add(value2);
      a = value3.subtraction(value);


      a = value.multiply(value2);
      a = value.divide(value2);
      System.out.println(a);

        /* relational operators prints true or false*/
      System.out.println(value.greaterThan(value2));
      System.out.println(value.lessEqual(value2));
      System.out.println(value.lessThan(value2));
      System.out.println(value.notEqual(a));
      System.out.println( value.equal(value2) );
      System.out.println( value.greaterEqual(value2) );
  }
}
