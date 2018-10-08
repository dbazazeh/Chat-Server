import java.awt.List;
import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import javax.naming.spi.DirStateFactory.Result;
 
 
 
public class client{
 
public static void main(String arg[]) throws Exception{//to handle all exceptions
int bytesRead;
    int current = 0;
    FileOutputStream fos = null;
    BufferedOutputStream bos = null;
    
 
Socket s= new Socket (arg[0],Integer.parseInt(arg[1]));
String [] k = new String[]{String.valueOf(arg[2].charAt(0)), String.valueOf(arg[2].charAt(1)), String.valueOf(arg[2].charAt(2)), String.valueOf(arg[2].charAt(3)), String.valueOf(arg[2].charAt(4)), String.valueOf(arg[2].charAt(5)), String.valueOf(arg[2].charAt(6)), String.valueOf(arg[2].charAt(7))};
int[] key = new int[] {Integer.parseInt(k[0]),Integer.parseInt(k[1]),Integer.parseInt(k[2]),Integer.parseInt(k[3]),Integer.parseInt(k[4]),Integer.parseInt(k[5]),Integer.parseInt(k[6]),Integer.parseInt(k[7])};

System.out.println("Connected to server on localhost and port"+s.getPort());
BufferedReader in=new BufferedReader (new InputStreamReader (System.in));
BufferedReader sin=new BufferedReader (new InputStreamReader (s.getInputStream()));
PrintWriter sout = new PrintWriter (s.getOutputStream(),true);
System.out.println(sin.readLine());
String line="";
while (true){
line=in.readLine();
 
if(line.equalsIgnoreCase("Exit"))
{
sout.println(line);


FileReader file = new FileReader("data.txt");

try{
    ArrayList <String> dec = new ArrayList<String>();
    
    Scanner sc = new Scanner(file);
    while(sc.hasNext())
    {
        //if(!(sc.nextLine().equalsIgnoreCase("exit")))
        	dec.add(sc.nextLine());
        	//System.out.println(dec);
        	
    }
    sc.close();
    
    //System.out.println(dec);
    
   for(int i=0;i<dec.size();i++)
   {
	   String a = dec.get(i);
	   a.replaceAll("\\[", "");
	   a.replaceAll("\\]", "");
	   a.replaceAll(",", "");
	   a.replaceAll("Exit", "");
	   
	   
	 //  System.out.println("a:" + a);
	   
	   int length = a.length();
	   String r = new String("");
	   for(int j=0;j<a.length();j++)
	   {
		   if(Character.isDigit(a.charAt(j)))
			   r = r+a.charAt(j);
		   else if(a.charAt(j) == ',')
			   r = r+" ";
	   }
	   
	   
	   int [] numbers = new int[8];
	   int [] t = new int [8];
	   int index=0;
	   
	   int f =0;
	   
	   String digit = "";
	   
	   
	   
	   while(f<r.length())
	   {
		   if(Character.isDigit((r.charAt(f))))
			   {
			   
			   digit = digit + r.charAt(f);
			   f++;
			   if(f==27)
			   { 
				   numbers[index] = Integer.parseInt(digit);
			       System.out.println("key:"+Arrays.toString(key));
			       System.out.println("numbers:"+Arrays.toString(numbers));
			   t=Decrypt(numbers, key);
			   System.out.println("Decrypted: "+ Arrays.toString(t));
			  for(int u=0;u<t.length;u++)
			  {
				  char C;
				  C=(char)t[u];
				  System.out.print(C);  
			  }
			   System.out.println();
			   index=0;
			   
			   }
				   
			  
			   }
		   else
		   {
			  
			   index++;
			   numbers[index] = Integer.parseInt(digit);
			 
			   f++;
			   digit = "";
		   }
		   
		   
		   
	   }
	   
	
	   } 
	   
   }

catch(Exception e)
{
    e.printStackTrace();
}

break;
}

else {String [] blocks = splitString(line);
for(int i =0 ; i< blocks.length; i++){
int[] plain = findASCII(blocks[i]);
 
 
       int[] state = plain;
 
 
       System.out.println("Plain: " + Arrays.toString(state));
       state = Encrypt(plain,key);
 
       line = Arrays.toString(state);
 
 
       System.out.println("Encrypted: " + line);
       sout.println(line);
       System.out.println("key:"+Arrays.toString(key));
 
//System.out.println(sin.readLine());
}
}
}

byte [] mybytearray  = new byte [6022386];
InputStream is = s.getInputStream();
fos = new FileOutputStream("data.txt");
bos = new BufferedOutputStream(fos);
bytesRead = is.read(mybytearray,0,mybytearray.length);
current = bytesRead;
 do {
        bytesRead =
           is.read(mybytearray, current, (mybytearray.length-current));
        if(bytesRead >= 0) current += bytesRead;
     } while(bytesRead > -1);

     bos.write(mybytearray, 0 , current);
     bos.flush();
s.close();

}



public static int[] Encrypt (int[] p, int[] k)
    {
    int[] state = p;
 
        int number_of_rounds = 32;
 
        for (int i=0; i<number_of_rounds; i++) {
                        state = Xor(state,k);
                        state = Permute(state);
                        state = Substitute(state);
        }
        state = Xor(state,k);
 
        return state;
    }
 
public static int[] Decrypt (int[] s, int[] k)
{
	int[] state = Xor(s,k);
	
	int number_of_rounds = 32;
	 
    for (int i=0; i<number_of_rounds; i++) {
                    state = Unsubstitute(state);
                    state = Unpermute(state);
                    state = Xor(state,k);
    }
    
    return state;
}
 
 
    public static String[] splitString (String x){
 
    String s = new String(x);
int l = s.length();
while(s.length()<8 || (s.length()%8)!=0)
{
s = s +"!";
}
int nBlocks = s.length()/8;
int index = 0;
String [] a = new String [nBlocks];
for(int i = 0; i < nBlocks; i++)
{
String sub = s.substring(index, index+8);
 
index = index + 8;
a[i] = sub;
}
return a;
    }
 
    public static int[] findASCII(String x){
    String s = x;
int[] ascii = new int[8];
for(int i =0; i<s.length(); i++)
{
ascii[i] = (int) s.charAt(i);
}
return ascii;
    }
 
    public static int[] Xor(int[] a, int[] b) {
                    int[] out;
 
                    out = new int[8];
 
                    for (int i=0; i<8; i++) {
                                    out[i] = a[i]^b[i];
                    }
                    return out;
    }
 
    public static int[] Permute(int[] a) {
                    int[] in, out, temp;
 
                    in  = new int[16];
                    out = new int[16];
                    temp = new int[8];
 
                    in[0] = a[0] % 16;
                    in[1] = (a[0]>>4) % 16;
                    in[2] = a[1] % 16;
                    in[3] = (a[1]>>4) % 16;
                    in[4] = a[2] % 16;
                    in[5] = (a[2]>>4) % 16;
                    in[6] = a[3] % 16;
                    in[7] = (a[3]>>4) % 16;
                    in[8] = a[4] % 16;
                    in[9] = (a[4]>>4) % 16;
                    in[10] = a[5] % 16;
                    in[11] = (a[5]>>4) % 16;
                    in[12] = a[6] % 16;
                    in[13] = (a[6]>>4) % 16;
                    in[14] = a[7] % 16;
                    in[15] = (a[7]>>4) % 16;
 
 
                        out[0] = in[3];
                                    out[1] = in[6];
                                    out[2] = in[9];
                                    out[3] = in[0];
                                    out[4] = in[5];
                                    out[5] = in[2];
                                    out[6] = in[11];
                                    out[7] = in[10];
                                    out[8] = in[15];
                                    out[9] = in[8];
                                    out[10] = in[14];
                                    out[11] = in[4];
                                    out[12] = in[1];
                                    out[13] = in[7];
                                    out[14] = in[12];
            out[15] = in[13];
 
                        temp[0]=out[0]+out[1]*16;
                                    temp[1]=out[2]+out[3]*16;
                                    temp[2]=out[4]+out[5]*16;
                                    temp[3]=out[6]+out[7]*16;
                                    temp[4]=out[8]+out[9]*16;
                                    temp[5]=out[10]+out[11]*16;
                                    temp[6]=out[12]+out[13]*16;
            temp[7]=out[14]+out[15]*16;
 
                    return temp;
    }
 
    public static int[] Unpermute(int[] a) {
                    int[] in, out, temp;
 
                    in  = new int[16];
                    out = new int[16];
                    temp = new int[8];
 
                    in[0] = a[0] % 16;
                    in[1] = (a[0]>>4) % 16;
                    in[2] = a[1] % 16;
                    in[3] = (a[1]>>4) % 16;
                    in[4] = a[2] % 16;
                    in[5] = (a[2]>>4) % 16;
                    in[6] = a[3] % 16;
                    in[7] = (a[3]>>4) % 16;
                    in[8] = a[4] % 16;
                    in[9] = (a[4]>>4) % 16;
                    in[10] = a[5] % 16;
                    in[11] = (a[5]>>4) % 16;
                    in[12] = a[6] % 16;
                    in[13] = (a[6]>>4) % 16;
                    in[14] = a[7] % 16;
                    in[15] = (a[7]>>4) % 16;
 
                        out[0] = in[3];
                                    out[1] = in[12];
                                    out[2] = in[5];
                                    out[3] = in[0];
                                    out[4] = in[11];
                                    out[5] = in[4];
                                    out[6] = in[1];
                                    out[7] = in[13];
                                    out[8] = in[9];
                                    out[9] = in[2];
                                    out[10] = in[7];
                                    out[11] = in[6];
                                    out[12] = in[14];
                                    out[13] = in[15];
                                    out[14] = in[10];
            out[15] = in[8];
 
                          temp[0]=out[0]+out[1]*16;
                                                                    temp[1]=out[2]+out[3]*16;
                                                                    temp[2]=out[4]+out[5]*16;
                                                                    temp[3]=out[6]+out[7]*16;
                                                                    temp[4]=out[8]+out[9]*16;
                                                                    temp[5]=out[10]+out[11]*16;
                                                                    temp[6]=out[12]+out[13]*16;
               temp[7]=out[14]+out[15]*16;
 
                    return temp;
    }
 
    public static int Lookup(int a) {
 
                    if (a<0 || a>255) {
                                    System.out.println("Invalid argument to Lookup: " + a);
                                    System.exit(0);
                    }
 
                    int[] sbox  = new int[] {48,116,122,75,163,53,204,110,137,69,225,118,180,221,51,18,139,70,117,30,46,155,109,100,87,55,171,252,128,168,113,24,146,15,37,178,231,240,64,93,94,111,63,153,230,132,214,59,185,0,78,154,27,228,161,226,192,165,28,213,123,173,183,197,66,124,65,73,35,32,223,187,157,83,119,219,80,145,218,141,195,133,19,49,105,3,130,114,224,169,23,2,189,13,22,191,251,112,60,125,16,92,162,206,247,131,250,86,77,211,249,209,190,120,170,115,156,236,99,201,254,42,179,84,97,106,39,56,58,203,43,121,217,181,107,182,232,101,216,176,148,38,202,138,7,134,135,12,205,81,91,158,21,89,90,233,253,245,29,186,129,136,95,96,41,6,98,184,222,62,177,11,20,36,255,151,52,220,164,199,215,88,57,149,9,198,200,237,244,229,17,142,103,160,10,14,172,44,150,242,26,239,166,72,210,4,102,47,144,68,34,227,71,246,127,50,208,243,194,238,241,67,196,85,45,5,248,33,212,140,74,61,167,76,79,188,1,174,108,25,126,193,175,31,235,159,40,82,8,207,234,143,147,54,152,104};
 
                    return(sbox[a]);
    }
 
    public static int Invlookup(int a) {
 
                    if (a<0 || a>255) {
                                    System.out.println("Invalid argument to Lookup: " + a);
                                    System.exit(0);
                    }
 
                    int[] sbox  = new int[] {49,236,91,85,205,225,165,144,248,184,194,171,147,93,195,33,100,190,15,82,172,152,94,90,31,239,200,52,58,158,19,243,69,227,210,68,173,34,141,126,246,164,121,130,197,224,20,207,0,83,215,14,176,5,253,25,127,182,128,47,98,231,169,42,38,66,64,221,209,9,17,212,203,67,230,3,233,108,50,234,76,149,247,73,123,223,107,24,181,153,154,150,101,39,40,162,163,124,166,118,23,137,206,192,255,84,125,134,238,22,7,41,97,30,87,115,1,18,11,74,113,131,2,60,65,99,240,214,28,160,86,105,45,81,145,146,161,8,143,16,229,79,191,251,208,77,32,252,140,183,198,175,254,43,51,21,116,72,151,245,193,54,102,4,178,57,202,232,29,89,114,26,196,61,237,242,139,170,35,122,12,133,135,62,167,48,159,71,235,92,112,95,56,241,218,80,222,63,185,179,186,119,142,129,6,148,103,249,216,111,204,109,228,59,46,180,138,132,78,75,177,13,168,70,88,10,55,211,53,189,44,36,136,155,250,244,117,187,219,201,37,220,199,217,188,157,213,104,226,110,106,96,27,156,120,174};
                    return(sbox[a]);
    }
 
    public static int[] Substitute(int[] a) {
 
                    int[] temp = new int[8];
 
                    for(int i=0; i<8; i++) {
                                    temp[i] = Lookup(a[i]);
                    }
 
                    return(temp);
    }
 
    public static int[] Unsubstitute(int[] a) {
 
                    int[] temp = new int[8];
 
                    for(int i=0; i<8; i++) {
                                    temp[i] = Invlookup(a[i]);
                    }
 
                    return(temp);
    }
}