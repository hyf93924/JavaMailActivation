package org.ygy.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.ygy.model.User;
import org.ygy.util.HibernateUtil;

public class UserDao {

	/**
	 * 保存用户
	 * @param user
	 */
	public void save(User user) {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		
		try {
			session.save(user);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			HibernateUtil.closeSession();
		}
		
	}

	/**
	 * 根据Email查找用户
	 * @param email
	 * @return
	 */
	public User findByEmail(String email) {
		Session session = HibernateUtil.getSession();
		session.beginTransaction();
		
		//select u from User u where u.email=?
		User user = (User) session.createQuery("select u from User u where u.email=?")
							.setString(0 , email)
							.uniqueResult();
		
		session.getTransaction().commit();
		HibernateUtil.closeSession();
		
		return user;
	}

	/**
	 * 更新用户状态 
	 * @param status
	 */
	public void updateUserStatus(Long id , int status) {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		
		//update User u set u.status=? where u.id=?
		try {
			session.createQuery("update User u set u.status=? where u.id=?")
					.setInteger(0 , status)
					.setLong(1 , id)
					.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			HibernateUtil.closeSession();
		}
	}
	
	/**
	 * 重置密码
	 */
	public void resetPwd(String email, String username, String password2){
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		
		try {
			session.createQuery("update User u set u.username=?, u.password=? where u.email=?")
					.setString(0, username)
					.setString(1, password2)
					.setString(2, email)
					.executeUpdate();
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			HibernateUtil.closeSession();
		}
		
		
		System.out.println("=================================");
	}

}
