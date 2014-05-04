package sve2.util;

import javax.naming.NamingException;

import sve2.fhbay.interfaces.ArticleAdminRemote;
import sve2.fhbay.interfaces.CustomerAdminRemote;

public class RemoteClient {

  private CustomerAdminRemote custAdmin;
  private ArticleAdminRemote artAdmin;

  public void init() {
    LoggingUtil.initJdkLogging("logging.properties");

    try {
      custAdmin = JndiUtil.getRemoteObject("ejb:fhbay/fhbay-beans/CustomerAdminBean!sve2.fhbay.interfaces.CustomerAdminRemote");
      artAdmin = JndiUtil.getRemoteObject("ejb:fhbay/fhbay-beans/ArticleAdminBean!sve2.fhbay.interfaces.ArticleAdminRemote");
    } catch (NamingException e) {
      throw new IllegalStateException(e);
    }
  }

  public CustomerAdminRemote getCustomerAdmin() {
    return custAdmin;
  }

  public ArticleAdminRemote getArticleAdmin() {
    return artAdmin;
  }

}
