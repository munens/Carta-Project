package carta.server;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import carta.client.LoginInfo;
import carta.client.LoginService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class LoginServiceImpl extends RemoteServiceServlet implements
    LoginService {

  public LoginInfo login(String requestUri) {
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    LoginInfo loginInfo = new LoginInfo();
   
    if (user != null) {
      loginInfo.setLoggedIn(true);
      loginInfo.setEmailAddress(user.getEmail());
      loginInfo.setNickname(user.getNickname());
      loginInfo.setLogoutUrl(userService.createLogoutURL(requestUri));
           
      
      //-- added for use on the client side --//      
      if (userService.isUserAdmin())
      	loginInfo.isAdmin = true;
      else
      	loginInfo.isAdmin = false;
      
      loginInfo.userId = user.getUserId();
      //--//
    } 
    else {
      loginInfo.setLoggedIn(false);
      loginInfo.setLoginUrl(userService.createLoginURL(requestUri));
    }
    
    return loginInfo;
  }

}