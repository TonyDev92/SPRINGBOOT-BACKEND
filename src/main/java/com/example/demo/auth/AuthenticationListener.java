package com.example.demo.auth;

/*import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;*/

import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;
// import org.springframework.web.context.request.RequestContextHolder;
// import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * The listener interface for receiving authentication events.
 * The class that is interested in processing a authentication
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addAuthenticationListener<code> method. When
 * the authentication event occurs, that object's appropriate
 * method is invoked.
 *
 * @see AuthenticationEvent
 */
@Component
public class AuthenticationListener implements ApplicationListener<InteractiveAuthenticationSuccessEvent> {

	/** The Constant logger. */
    // private static final Logger logger = LoggerFactory.getLogger(AuthenticationListener.class);
    
    /** The autenticacion client service. */	
	// private AutenticacionClientService autenticacionClientService = new AutenticacionClientService();

    /**
	 * On application event.
	 *
	 * @param event the event
	 */
    @Override
    public void onApplicationEvent(InteractiveAuthenticationSuccessEvent event) {
    	/*if (event.getAuthentication().isAuthenticated()) {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

			HttpSession session = attr.getRequest().getSession(false);
			String lastHttpSession = null;
			for (Cookie cookie : attr.getRequest().getCookies()) {
				if (cookie.getName().equals("JSESSIONID")) {
					lastHttpSession = cookie.getValue();
				}
			}
			String currentHttpSession = session.getId();
			autenticacionClientService.updateShoppingcartHttpSession(lastHttpSession, currentHttpSession);
			logger.debug("onApplicationEven()-> Last session: " + lastHttpSession + ", Current session: " + currentHttpSession);
		}*/
    }
}
