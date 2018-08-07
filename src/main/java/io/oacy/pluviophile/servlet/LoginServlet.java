package io.oacy.pluviophile.servlet;

import io.oacy.pluviophile.domain.UserInfo;
import io.oacy.pluviophile.http.HttpRequest;
import io.oacy.pluviophile.http.HttpResponse;
import io.oacy.pluviophile.repository.UserInfoDAO;
/**
 * 完成登录操作
 * @author zephyr
 *
 */
public class LoginServlet extends HttpServlet{
	public void service(HttpRequest request,HttpResponse response){
		try {
			UserInfoDAO dao = new UserInfoDAO();	
			
			//获取用户登录信息
			String username = request.getParameter("username");
			String password = request.getParameter("password");		
			
			//先根据用户名获取该用户信息
			UserInfo userinfo = dao.findByUsername(username);
			
			//若查到了该用户,并且密码匹配则登录成功
			if(userinfo!=null&&userinfo.getPassword().equals(password)){
				forward("/myweb/login_suc.html",request,response);
			}else{
				forward("/myweb/login_fail.html",request,response);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}