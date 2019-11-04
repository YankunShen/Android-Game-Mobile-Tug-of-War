package shixi;

import java.sql.*;






public class link {
	private static final String URL="jdbc:mysql://localhost:3306/shixi?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false";//motong为数据库名 
		private static final String USER="root"; 
		private static final String PASSWORD="zf666888"; 
		private static Connection conn = null;	
		public link() {										//Ĭ�Ϲ��캯��
			try {
				if (getConn() == null) {							//������Ӷ���Ϊ��
					Class.forName("com.mysql.jdbc.Driver");			//����������
					setConn(DriverManager.getConnection(URL,USER,PASSWORD));//������Ӷ���
				}
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		//1.加载驱动程序 
		//Class.forName("com.mysql.jdbc.Driver");//http://blog.csdn.net/kaiwii/article/details/7405761 
		//2.获得数据库的链接 
		//Connection conn=DriverManager.getConnection(URL,USER,PASSWORD); 
		//3.通过数据库的链接操作数据库，实现增删改查 
//		Statement stmt=conn.createStatement();
//		ResultSet rs = stmt.executeQuery("select * from test"); 
//		while(rs.next()){//遍历student表中的每一条记录 
//		System.out.println(rs.getString("name")); 
//		System.out.println(rs.getInt("id"));
//		System.out.println(rs.getString("state"));
//		System.out.println(rs.getInt("count"));
		//} 
		
		public static ResultSet executeQuery(String sql) {	//��ѯ����
			try {
				if(getConn()==null)  new link();  //������Ӷ���Ϊ�գ������µ��ù��췽��
				return getConn().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
						ResultSet.CONCUR_UPDATABLE).executeQuery(sql);//ִ�в�ѯ
			} catch (SQLException e) {
				e.printStackTrace();
				return null;				//����nullֵ
			} finally {
			}
		}

//public static int users check(String name, String password) {
//	
//	String sql = "select * from test where name= '" + name
//	+ "' and password='" + password+"'";
//	if(conn==null)  new link();
//	
//	ResultSet rs = link.executeQuery(sql);//ִ�в�ѯ
//	try {
//		while(rs.next()) {//�����ѯ���˼�¼
//			if(name==rs.getString("name")&&password==rs.getString("psw")){
//				return 1;
//			}
//			
//		}	
//	} catch (SQLException e){
//		e.printStackTrace();
//	}
//	link.close();	//�ر����Ӷ���
//	return 0;//���ز���Ա��Ϣ����
//}
public static void close() {//�رշ���
	try {
		getConn().close();//�ر����Ӷ���		
	} catch (SQLException e) {
		e.printStackTrace();
	}finally{
		setConn(null);	//�������Ӷ���Ϊnullֵ
	}
}
public static void comzhuce(String name,String password,String state){
	
	try{
		String sql="insert test(name,psw,state) values('"+name+"','"+password+"','"+state+"')";//���û���Ϣ�������ݿ���
		link.executeUpdate(sql);
	}catch(Exception e){
		e.printStackTrace();
	}
	
}
public static int sum(String sql) throws SQLException{
	String sql1=sql;
	int a,b=0;
	if(getConn()==null) {new link();}
	Statement stmt=getConn().createStatement();
	ResultSet rs = stmt.executeQuery(sql1); 
	while(rs.next()){
		a=rs.getInt("count");
		b+=a;
	}
	
	return b;
	
}
public static void executeUpdate(String sql) {		//���·���
	try {
		if(getConn()==null) {  new link();}
	//������Ӷ���Ϊ�գ������µ��ù��췽��
		Statement stmt=getConn().createStatement();
		stmt.executeUpdate(sql);
	} catch (SQLException e) {
		e.printStackTrace();
		
	} finally {
	}
}

public static Connection getConn() {
	return conn;
}

public static  void setConn(Connection conn) {
	
	link.conn = conn;
}
}
