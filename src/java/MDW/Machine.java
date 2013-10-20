/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MDW;

import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.view.Location;
import javax.jms.Session;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author New
 */
@WebServlet(name = "Machine", urlPatterns = {"/Machine"})
public class Machine extends HttpServlet {
    private final String page = "/MDW-hw3/Machine";
    private final String next = "Dalsi";
    private final String c_state = "state";
    private String c_tmpValue;
    private String[] c_values = {"NEW", "PAYMENT", "COMPLETED"};
    private boolean DEBUG = false;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequestStart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Machine</title>");
            out.println("</head>");
            out.println("<body>");
        }
        finally {
            //processRequestEnd(request, response);
            //out.close();
        }
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequestEnd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            out.println("</body>");
            out.println("</html>");
        }
        finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequestStart(request, response);
        PrintWriter out = response.getWriter();

        c_tmpValue = whichValueOfCState(request, response);
        if (c_tmpValue == null) { //stav nenastaven
            stav0(request, response, out);
        }
        else if (c_tmpValue.equals(c_values[0])) { // stav NEW
            stav1(request, response, out);
        }
        else if (c_tmpValue.equals(c_values[1])) { // stav PAYMENT
            stav2(request, response, out);
        }
        else if (c_tmpValue.equals(c_values[2])) { // stav COMPLETED
            stav3(request, response, out);
        }

        processRequestEnd(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequestStart(request, response);
        processRequestEnd(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private String whichValueOfCState(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();

        if (request.getCookies() == null) { //Zadne cookies
            return null;
        }
        for (int i = 0; i < request.getCookies().length; i++) {
            out.write(DEBUG ? "i = " + i + " name: " + request.getCookies()[i].getName()
                              + " value: " + request.getCookies()[i].getValue() + "<br>" : "");
            if (request.getCookies()[i].getName().equals(c_state)) {
                return request.getCookies()[i].getValue();
            }
        }
        return null;
    }

    private void stav0(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        out.write("Stav zadny, nastavuji cookie na stav " + c_values[0]);
        out.println("<p><form action=\"" + page + "\" method=\"get\">");
        out.println("<input type=\"submit\" name=\"btn\" value=\"Pristoupit k objednavce\">");
        out.println("</form></p>");
        response.addCookie(new Cookie(c_state, c_values[0]));
    }

    private void stav1(HttpServletRequest request, HttpServletResponse response, PrintWriter out) { //stav NEW
        if (request.getQueryString() != null) {
            if (request.getQueryString().contains(next) && request.getQueryString() != null) {
                out.write(" Nastavuji cookie na stav " + c_values[1] + "<br>");
                response.addCookie(new Cookie(c_state, c_values[1]));
                response.setStatus(response.SC_MOVED_TEMPORARILY);
                response.addHeader("Location", "http://localhost:8080/MDW-hw3/Machine");
            }
        }
        htmlStav1(out);
    }
    
    private void htmlStav1(PrintWriter out) {
        out.write("<p>" + "Stav " + c_tmpValue + "</p>");
        out.println("<form action=\"" + page + "\" method=\"get\">");
        out.println("<p>Zadejte text: <input type=\"text\" name=\"txt1\"></p>");
        out.println("<p>Zaskertnete jednu z moznosti:<br>");
        out.println("A <input type=\"radio\" name=\"rb\" value=\"rb1\"><br>");
        out.println("B <input type=\"radio\" name=\"rb\" value=\"rb2\"><br>");
        out.println("C <input type=\"radio\" name=\"rb\" value=\"rb3\"></p>");
        out.println();
        out.println("<p>Zaskrtnete moznosti<br>");
        out.println("X <input type=\"checkbox\" name=\"chb1\" value=\"chb1\"><br>");
        out.println("Y <input type=\"checkbox\" name=\"chb2\" value=\"chb2\"><br>");
        out.println("Z <input type=\"checkbox\" name=\"chb3\" value=\"chb3\"></p>");
        out.println();
        out.println("<input type=\"submit\" name=\"btn" + next + "\" value=\"Prejit k platbe\">");
        out.println("<input type=\"submit\" name=\"btnReset\" value=\"Reset\">");
        out.println("</form>");
    }

    private void stav2(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        if (request.getQueryString() != null) {
            if (request.getQueryString().contains(next) && request.getQueryString() != null) {
                out.write(" Nastavuji cookie na stav " + c_values[2] + "<br>");
                response.addCookie(new Cookie(c_state, c_values[2]));
                response.setStatus(response.SC_MOVED_TEMPORARILY);
                response.addHeader("Location", "http://localhost:8080/MDW-hw3/Machine");
            }
        }
        htmlStav2(out);
    }
    
    private void htmlStav2(PrintWriter out) {
        out.write("<p>" + "Stav " + c_tmpValue + "</p>");
        out.println("<form action=\"" + page + "\" method=\"get\">");
        out.println("<p>Zadejte cislo karty: <input type=\"text\" name=\"txt1\"></p>");
        out.println("<p>Zaskertnete typ platby:<br>");
        out.println("A <input type=\"radio\" name=\"rb\" value=\"rb1\"><br>");
        out.println("B <input type=\"radio\" name=\"rb\" value=\"rb2\"><br>");
        out.println();
        out.println("<input type=\"submit\" name=\"btn" + next + "\" value=\"Zaplatit\">");
        out.println("<input type=\"submit\" name=\"btnReset\" value=\"Reset\">");
        out.println("</form>");
    }

    private void stav3(HttpServletRequest request, HttpServletResponse response, PrintWriter out) {
        out.write("<p>Stav " + c_tmpValue + "</p>");
        out.write("<p>Vase objednavka byla dokoncena</p>");
        out.write("**&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;**<br>");
        out.write("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;||<br>");
        out.write("&nbsp;&nbsp;&nbsp;\\&nbsp;__&nbsp;/<br>");
        Cookie c = new Cookie(c_state, c_values[0]);
        response.addCookie(c);
    }
}
