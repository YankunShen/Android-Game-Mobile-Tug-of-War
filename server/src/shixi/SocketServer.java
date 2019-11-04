package shixi;

import java.io.DataInputStream;  
import java.io.IOException;  
import java.net.ServerSocket;  
import java.net.Socket;
import java.sql.SQLException;  
import java.io.DataOutputStream; 
import shixi.link;
import java.io.*;
/** 
 * 服务端 
 * 
 */  
public class SocketServer{  
  
 //    public static void main(String[] args){
//    	startService();
//    }

    /**  
     * 启动服务监听，等待客户端连接 
     */ 
	public static Socket socket;
    public static void startService() { 
    	new Thread(){
    	@Override
    	public void run(){
    	
        try {  
            // 创建ServerSocket  
            ServerSocket serverSocket = new ServerSocket(9999);  
            System.out.println("--开启服务器，监听端口 9999--");  
  
            // 监听端口，等待客户端连接  
            while (true) {  
                System.out.println("--等待客户端连接--");
               // boolean isConnection=socket.isConnected() && !socket.isClosed(); 
                 socket = serverSocket.accept(); //等待客户端连接  
                System.out.println("得到客户端连接：" + socket);  
                
                startReader(socket);
                
               
            }  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }
    	}.start();
    }
    /**  
     * 从参数的Socket里获取最新的消息 
     */  
    private static void startReader(final Socket socket) {  
    	
        new Thread(){ 
        	
        	
            @Override  
            public void run() {  
                DataInputStream reader; 
                DataOutputStream writer;
                try {  
                    
                      
                	while(true){
                		
                	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(),"utf-8"));
                    String str = in.readLine();
                    
                    String[] s=null;
                    s=str.split(" ");
                    System.out.println("read:" + s[0]+" "+s[1]);
                    String sql="update test set count ="+s[1]+" where name='"+s[0]+"'";
                    link.executeUpdate(sql);
                    
                       String sql1="select count from test where state='A'";    		
           			String s1 = String.valueOf(link.sum(sql1));
       					System.out.print("A组：");
       					System.out.println(s1);
       			
       					// TODO Auto-generated catch block
       						
           			String sql2="select count from test where state='B'";
           		
           			String s2= String.valueOf(link.sum(sql2));
       					
       					System.out.print("B组：");
       					System.out.println(s2);
       					
       					
//       					writer = new DataOutputStream(socket.getOutputStream());
//       					writer.flush();
//       					writer.writeUTF(s1+","+s2+",null");
       				 PrintWriter pout = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "utf-8"));	
       				 pout.println(s1+" "+s2);
       			     pout.flush();
                  
                	}       
                      
                } catch(IOException | SQLException e){  
                    e.printStackTrace();  
                }  
            }  
            
			
        }.start();
        
    }
    
//    private static void startsend(final Socket socket){
//    	new Thread(){
//    		@Override
//    		public void run(){
//    			DataOutputStream writer;
//				try {
//					writer = new DataOutputStream(socket.getOutputStream());
//				
//    			String sql="select count from test where state='甲'";    		
//    			String s = null;
//				
//					s = String.valueOf(link.sum(sql));
//					System.out.println("甲组：");
//					System.out.print(s);
//			
//					// TODO Auto-generated catch block
//						
//    			String sql1="select count from test where state='乙'";
//    		
//    			String s1 = null;
//		
//					s1 = String.valueOf(link.sum(sql1));
//					
//					System.out.println("乙组：");
//					System.out.print(s1);
//			
//    			
//    	
//					writer.writeUTF(s+","+s1);
//					writer.flush();
//				} catch (IOException | SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} 
//    		
//    	}
//    }.start();
//    
//      
//} 
}