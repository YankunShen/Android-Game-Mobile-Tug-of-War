package shixi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import shixi.link;
import shixi.SocketServer;
public class shixiServlet extends HttpServlet {

	/**
		 * Constructor of the object.
		 */
	public shixiServlet() {
		super();
	}

	/**
		 * Destruction of the servlet. <br>
		 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
		 * The doGet method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to get.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
		 * The doPost method of the servlet. <br>
		 *
		 * This method is called when a form has its tag value method equals to post.
		 * 
		 * @param request the request send by the client to the server
		 * @param response the response send by the server to the client
		 * @throws ServletException if an error occurred
		 * @throws IOException if an error occurred
		 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//		response.setCharacterEncoding("gb2312");
		PrintWriter out = response.getWriter();
		String name=request.getParameter("um");
		String state=null;
		System.out.println(name);
		String sql="select state from test where name='"+name+"'";
		if(link.getConn()==null){
			new link();
			
		}
		try {
			Statement stmt=link.getConn().createStatement();
			ResultSet rs = stmt.executeQuery(sql); 
			while(rs.next()){
			
				state=rs.getString("state");
				System.out.print("组别："+state);
			}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		
		
		
//		String password=request.getParameter("psw");
//		String state=request.getParameter("state");
//		out.print("<html><head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\"/></head><body><center><h4>welcome "+ name + "!</h4></center></body></html>");
//		link.comzhuce(name,password,state);
		out.print("<html> <body> <center><a href=\"myapp://native/param?name="+name+"&state="+state+"\" style=\"color:#f00;\"\"><h1>Start games!</h1></a></center></body></html>");
		out.flush();
		out.close();
//		System.out.print("hello");
		SocketServer.startService();
//		System.out.print("hello1");
	}

	/**
		 * Initialization of the servlet. <br>
		 *
		 * @throws ServletException if an error occurs
		 */
	public void init() throws ServletException {
		// Put your code here
	}

}
