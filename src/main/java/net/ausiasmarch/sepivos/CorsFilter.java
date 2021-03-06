
package net.ausiasmarch.sepivos;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CorsFilter implements Filter {

    public CorsFilter() {
    }

    @Override
    public void init(FilterConfig fConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        if (req instanceof HttpServletRequest && res instanceof HttpServletResponse) {
            HttpServletRequest oRequest = (HttpServletRequest) req;
            HttpServletResponse oResponse = (HttpServletResponse) res;

            if (!(oRequest.getMethod().equalsIgnoreCase("OPTIONS"))) {
                oResponse.setHeader("Cache-control", "no-cache, no-store");
                oResponse.setHeader("Pragma", "no-cache");
                oResponse.setHeader("Expires", "-1");
                oResponse.setHeader("Access-Control-Allow-Origin", oRequest.getHeader("origin"));
                oResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH");
                oResponse.setHeader("Access-Control-Max-Age", "86400");
                oResponse.setHeader("Access-Control-Allow-Credentials", "true");
                oResponse.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, "
                       + "Origin, "
                       + "Accept, "
                       + "Authorization, "
                       + "ResponseType, "
                       + "Observe, "
                       + "X-Requested-With, "
                       + "Content-Type, "
                       + "Access-Control-Expose-Headers, "
                       + "Access-Control-Request-Method, "
                       + "Access-Control-Request-Headers");
            } else {
                //https://stackoverflow.com/questions/56479150/access-blocked-by-cors-policy-response-to-preflight-request-doesnt-pass-access
                System.out.println("Pre-flight");
                oResponse.setHeader("Access-Control-Allow-Origin", oRequest.getHeader("origin"));
                oResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH");
                oResponse.setHeader("Access-Control-Max-Age", "3600");
                oResponse.setHeader("Access-Control-Allow-Credentials", "true");
                oResponse.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, "
                       + "Origin, "
                       + "Accept, "
                       + "Authorization, "
                       + "ResponseType, "
                       + "Observe, "
                       + "X-Requested-With, "
                       + "Content-Type, "
                       + "Access-Control-Expose-Headers, "
                       + "Access-Control-Request-Method, "
                       + "Access-Control-Request-Headers");
                oResponse.setStatus(HttpServletResponse.SC_OK);
            }
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
       
    }

}
