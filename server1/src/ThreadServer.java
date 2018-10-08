import java.io.*;
import java.net.*;
import java.util.*;
 
 
public class ThreadServer {
 
public static void main(String arg[]){
try{
int port = Integer.parseInt(arg[0]);
ServerSocket ss= new ServerSocket (port);
System.out.println("MultiThread Serever started at "+new Date());
while (true){
Socket s=ss.accept();//nothing will be done after this line until the client connects
InetAddress a=s.getInetAddress();
System.out.println("Connected to "+a.getHostName()+ "/"+a.getHostAddress());
WorkerThread wt=new WorkerThread(s);
wt.start();
}
}catch(Exception e){e.printStackTrace();}
}
}
 
 
 
class WorkerThread extends Thread {
private Socket s;
static int counter =1;//it is better to start counting from 1 when giving such number to name threads
public WorkerThread (Socket s){
super("WorkerThread"+counter+":");//title, so any message come will come with this name
this.s=s;
System.out.println("A new WorkerThread "+counter++ +" is spawned on port "+s);
}
 
public void run(){
try{
 
BufferedReader in = new BufferedReader (new InputStreamReader (System.in));
BufferedReader sin = new BufferedReader (new InputStreamReader (s.getInputStream()));
BufferedReader fin = new BufferedReader (new FileReader("data.txt"));
PrintWriter out = new PrintWriter(new FileWriter("data.txt",true));
PrintWriter sout = new PrintWriter (s.getOutputStream(),true);
 
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
 
 
String line="";
sout.println("Welcome to this chat server, please type something or exit to quit");//write message to the client
while (true){
line=sin.readLine();
if (line.equalsIgnoreCase("exit")){
out.close();
sout.println("Exit");
File myFile = new File ("data.txt");
byte [] mybytearray  = new byte [(int)myFile.length()];
fis = new FileInputStream(myFile);
bis = new BufferedInputStream(fis);
bis.read(mybytearray,0,mybytearray.length);
os = s.getOutputStream();
os.write(mybytearray,0,mybytearray.length);
os.flush();
 if (bis != null) bis.close();
 if (os != null) os.close();

System.out.println(getName()+" exited");//getName() return the name of this thread
break;
}else{
//out = new PrintWriter(new FileWriter("data.txt",true));
if(!(line.equalsIgnoreCase("exit")))
		out.println(line);
//added
 
         
out.close();
System.out.println(getName()+" "+line);
}
sout.println(in.readLine());//the serveris answering now
}
s.close();
}catch (Exception e){
e.printStackTrace();
}
}
}