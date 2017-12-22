package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBConnection conn = DBConnectionFactory.getDBConnection();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 * GET method is used to verify if the current session is logged in or not. e.g. you close one logged in web site and open it again (only used in init() in main.js)
		If the session is logged in, return HTTP 200, and the payload is {"user_id":"1111","name":"John Smith","status":"OK"}
		If the session is not logged in, return HTTP 403, and the payload is {"status":"Session Invalid"}

	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			JSONObject msg = new JSONObject();
			HttpSession session = request.getSession();
			if (session.getAttribute("user") == null) {
				response.setStatus(403);
				msg.put("status", "Session Invalid");
			} else {
				String user = (String) session.getAttribute("user");
				String name = conn.getFullname(user);
				msg.put("status", "OK");
				msg.put("user_id", user);
				msg.put("name", name);
			}
			RpcHelper.writeJsonObject(response, msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * POST method is the actual login implementation. It verifies the username and password with the database. If they match, store an attribute in the session to mark this session as logged in.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			JSONObject msg = new JSONObject();
			// get request parameters for userID and password
			String user = request.getParameter("user_id");
			String pwd = request.getParameter("password");
			if (conn.verifyLogin(user, pwd)) {
				// getSession() will return a HttpSession object anyway (if not exist, create it)
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				// setting session to expire in 10 minutes
				session.setMaxInactiveInterval(10 * 60);
				// Get user name
				String name = conn.getFullname(user);
				msg.put("status", "OK");
				msg.put("user_id", user);
				msg.put("name", name);
			} else {
				response.setStatus(401);
			}
			RpcHelper.writeJsonObject(response, msg);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
