package com.example.demo.auth;

// import java.io.IOException;

import jakarta.servlet.http.HttpSessionEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
/*import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.authentication.WebAuthenticationDetails;*/
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.example.demo.services.LogoutService;

/**
 * The Class CustomHttpSessionEventPublisher.
 */
public class CustomHttpSessionEventPublisher extends HttpSessionEventPublisher {

	/** The Constant logger. */
    // private static final Logger logger = LoggerFactory.getLogger(CustomHttpSessionEventPublisher.class);
    
    /** The autenticacion client service. */
	// private AutenticacionClientService autenticacionClientService = new AutenticacionClientService();

	/** The Constant URL_SECURITY_SERVICE_ACTIVE_SESSION. */
    // private static final String URL_SECURITY_LOGOUT_UPDATE = "https://www.soy3eres.es/books-business-security-service/security-api/logout-and-update-shoppingcart-http-session/";
    
    /** The http client. */
	// private HttpClient httpClient;
	
	/** The cookie store. */
	// private CookieStore cookieStore;
	
	/** The http context. */
	// private HttpContext httpContext;
	
	/** The http uri request. */
	// private HttpUriRequest httpUriRequest;
	
	/** The http response. */
	// private org.apache.http.HttpResponse httpResponse;
	@Autowired
	private LogoutService logoutService;
	

	/**
	 * Session created.
	 *
	 * @param event the event
	 */
    @Override
    public void sessionCreated(HttpSessionEvent event) {
        super.sessionCreated(event);
    }

    /**
	 * Session destroyed.
	 *
	 * @param event the event
	 */
    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        String currentHttpSession = ((WebAuthenticationDetails) ((SecurityContextImpl) event.getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT")).getAuthentication().getDetails()).getSessionId();
        
        try {
        	logoutService.logout(currentHttpSession);
        } catch (Exception e) {
        	
            System.err.println("No se pudo eliminar el token de sesión: " + e.getMessage());
        }
 
       super.sessionDestroyed(event);
    }
}
