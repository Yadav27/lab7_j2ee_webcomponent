package com.example.servlet;

import com.example.model.Category;
import com.example.model.Employee;
import com.example.model.MenuItem;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MenuItemServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		double price = Double.parseDouble(request.getParameter("price"));
		String category = request.getParameter("category");

		
		Category categoryObj = new Category(category);
		
		Transaction transaction = null;
		if(!isCategoryExist(category)) {			
			try (Session session = HibernateUtil.getSessionFactory().openSession()) {
				transaction = session.beginTransaction();
				session.save(categoryObj);
				transaction.commit();
			} catch (Exception e) {
				if (transaction != null)
					transaction.rollback();
				e.printStackTrace();
			}
		}
		
		
		int categoryId = 0;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
		    List<Integer> result = session.createQuery("SELECT id FROM Category WHERE name = :name", Integer.class)
		            .setParameter("name", category)
		            .getResultList();

		    if (!result.isEmpty()) {
		        categoryId = result.get(0);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		}

		
		
		MenuItem menuItem = new MenuItem(name, description, price, categoryId);

		transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(menuItem);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null)
				transaction.rollback();
			e.printStackTrace();
		}

		response.sendRedirect("menuItem");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		List<MenuItem> menuItem = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			menuItem = session.createQuery("FROM MenuItem", MenuItem.class).list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		List<Category> categoryList = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			categoryList = session.createQuery("FROM Category", Category.class).list();
		} catch (Exception e) {
			e.printStackTrace();
		}

		request.setAttribute("menuItem", menuItem);
		request.setAttribute("category", categoryList);
		request.getRequestDispatcher("menuItem.jsp").forward(request, response);

	}
	
	protected boolean isCategoryExist(String category) {
		System.out.println("Category : " + category);
		List<Category> categoryList = null;

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			categoryList = session.createQuery("FROM Category WHERE name=:name", Category.class).setParameter("name", category).list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(categoryList.size()>0) {
			return true;
		}
		else {
			return false;
		}
	}
}
