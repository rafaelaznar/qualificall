package net.ausiasmarch.sepivos;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class session extends HttpServlet {

    private static String getBody(HttpServletRequest request) throws IOException {
        //https://stackoverflow.com/questions/14525982/getting-request-payload-from-post-request-in-java-servlet
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {        
        response.setContentType("application/json;charset=UTF-8");
        Gson oGson = new Gson();
        try ( PrintWriter out = response.getWriter()) {
            String op = request.getParameter("op");
            if (op != null) {
                HttpSession oSession = request.getSession();
                switch (op) {
                    case "check":
                        response.setStatus(HttpServletResponse.SC_OK);

//                        UserBean oUserBean = (UserBean) oSession.getAttribute("usuario");
//                        String login1=oUserBean.getLogin();
//                        out.print(oGson.toJson(login1));
                        out.print(oGson.toJson((String) ((UserBean) oSession.getAttribute("usuario")).getLogin()));
                        break;
                    case "get":
                        UserBean oUserBean1 = (UserBean) oSession.getAttribute("usuario");
                        String name = null;
                        if (oUserBean1 != null) {
                            name = oUserBean1.getLogin();
                        }
//                        NullPointerException is a run-time exception which is not recommended to catch it, but instead avoid it:
//                        https://stackoverflow.com/questions/15146339/catching-nullpointerexception-in-java  
//                        String name;
//                        try {
//                            name = ((UserBean) oSession.getAttribute("usuario")).getLogin();
//                        } catch (Exception ex) {
//                            name = null;
//                        }
                        if (name != null) {
                            if (name.equalsIgnoreCase("admin")) {
                                response.setStatus(HttpServletResponse.SC_OK);
                                out.print(oGson.toJson("QWERTY"));
                            } else {
                                response.setStatus(HttpServletResponse.SC_OK);
                                out.print(oGson.toJson("ASDFG"));
                            }
                        } else {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            out.print(oGson.toJson("Unauthorized"));
                        }
                        break;
                    default:
                        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        out.print(oGson.toJson("Method Not Allowed"));
                        break;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                out.print(oGson.toJson("Method Not Allowed"));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        Gson oGson = new Gson();
        try ( PrintWriter out = response.getWriter()) {
            HttpSession oSession = request.getSession();

            String payloadRequest = getBody(request);
            UserBean oUserBean = new UserBean();
            oUserBean = oGson.fromJson(payloadRequest, oUserBean.getClass());

            if (oUserBean.getLogin() != null && oUserBean.getPassword() != null) {
                if (oUserBean.getLogin().equalsIgnoreCase("admin") && oUserBean.getPassword().equalsIgnoreCase("8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918")) { //admin
                    oSession.setAttribute("usuario", oUserBean);
                    response.setStatus(HttpServletResponse.SC_OK);
                    out.print(oGson.toJson("Welcome"));
                } else {
                    if (oUserBean.getLogin().equalsIgnoreCase("user") && oUserBean.getPassword().equalsIgnoreCase("04f8996da763b7a969b1028ee3007569eaf3a635486ddab211d512c85b9df8fb")) { //user
                        oSession.setAttribute("usuario", oUserBean);
                        response.setStatus(HttpServletResponse.SC_OK);
                        out.print(oGson.toJson("Welcome"));
                    } else {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        out.print(oGson.toJson("Auth Error"));
                    }
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print(oGson.toJson("Auth Error"));
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        Gson oGson = new Gson();
        try ( PrintWriter out = response.getWriter()) {
            HttpSession oSession = request.getSession();
            oSession.invalidate();
            response.setStatus(HttpServletResponse.SC_OK);
            out.print(oGson.toJson("Session closed"));
        }
    }

}
