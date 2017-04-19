package com.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;


/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/search")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static List<String>datas=new ArrayList<String>();
       static{
    	   datas.add("ajax");
    	   datas.add("ajax1");
    	   datas.add("ajax2");
    	   datas.add("ajax3");
    	   datas.add("bcd");
    	   datas.add("because");
    	   datas.add("dddd");
    	   datas.add("dd22");
    	   datas.add("ddss");
       }
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		System.out.println("123");
		String keyword=request.getParameter("keyword");
		List<String> result=getData(keyword);
		;
		response.getWriter().write(JSONArray.fromObject(result).toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
    public List<String> getData(String keyword){
    	List<String>ls=new ArrayList<String>();
    	for(String data:datas){
    		if(data.contains(keyword)){
    			ls.add(data);
    		}
    	}
    	return ls;
    }
}
