package io.oacy.pluviophile.servlet;

import io.oacy.pluviophile.domain.UserInfo;
import io.oacy.pluviophile.http.HttpRequest;
import io.oacy.pluviophile.http.HttpResponse;
import io.oacy.pluviophile.repository.UserInfoDAO;

/**
 * 用来处理/myweb/reg请求,完成注册业务
 * 
 * @author zephyr
 *
 */
public class RegServlet extends HttpServlet {
	/**
	 * 处理请求的方法
	 * 
	 * @param request
	 * @param response
	 */
	public void service(HttpRequest request, HttpResponse response) {

		try {
			UserInfoDAO dao = new UserInfoDAO();

			// 开始注册
			System.out.println("开始注册!");

			// 获取用户注册信息
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String nickname = request.getParameter("nickname");
			String phonenumber = request.getParameter("phonenumber");

			System.out.println(username + "," + password + "," + nickname + "," + phonenumber);
			/*
			 * 首先检查该用户是否存在,不存在才写入文件 存在则跳转一个显示该用户已存在的页面
			 */
			UserInfo userinfo = dao.findByUsername(username);
			if (userinfo != null) {
				// 跳转提示页面,该用户存在
				forward("/myweb/reg_haveUser.html", request, response);
			} else {
				UserInfo user = new UserInfo(username, password, nickname, phonenumber);
				dao.save(user);
				System.out.println("注册完毕!");

				// 响应客户端注册成功页面
				forward("/myweb/reg_success.html", request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
