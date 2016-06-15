package org.ygy.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ygy.dao.UserDao;
import org.ygy.model.User;
import org.ygy.service.ServiceException;
import org.ygy.service.UserService;
import org.ygy.util.MailUtil;

public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 3202117956537528245L;
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req , resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter("action");
		
		UserDao dao = null;
		dao = new UserDao();
		
		UserService service = new UserService();
		if("register".equals(action)) {
			//注册
			String email = req.getParameter("email");
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			service.processRegister(email, username, password);
			
			req.getRequestDispatcher("register_success.jsp").forward(req , resp);
		} else if("activate".equals(action)) {
			//激活
			String email = req.getParameter("email");
			String validateCode = req.getParameter("validateCode");
			
			try {
				service.processActivate(email , validateCode);
				req.getRequestDispatcher("login.jsp").forward(req , resp);
			} catch (ServiceException e) {
				req.setAttribute("message" , e.getMessage());
				req.getRequestDispatcher("activate_failure.jsp").forward(req , resp);
			}
			
		} else if("forgotPwd".equals(action)){
			//忘记密码
			String email = req.getParameter("email");
			
			User user = dao.findByEmail(email);
			if(user == null){
				req.setAttribute("msg", email+"，不存在");
				req.getRequestDispatcher("test.jsp").forward(req, resp);
				return;
			}
			
			service.processRegister(email);
			req.setAttribute("msg", "重置密码邮件已发送");
			req.setAttribute("email", email);
			req.getRequestDispatcher("test.jsp").forward(req, resp);
			
			
		} else if("resetPassword".equals(action)){
			
			String email = req.getParameter("email");
			System.out.println("==================="+email);
			
			req.setAttribute("myEmail", email);
			req.getRequestDispatcher("rePwd.jsp").forward(req , resp);
			
		} else if("resetPwd".equals(action)){
			System.out.println("===========================进入重置密码");
			//重置密码
			String email = req.getParameter("myEmail");
			String username = req.getParameter("username");
			String password = req.getParameter("password");
			String password2 = req.getParameter("password2");
			
			System.out.println(email+"======"+username+"======="+password+"========"+password2);
			
			dao.resetPwd(email, username, password2);
			req.setAttribute("msg", "重置密码成功");
			req.getRequestDispatcher("test.jsp").forward(req, resp);
			
		}
	}

}
