package sve2.fhbay.client.rest;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.ArticleState;
import sve2.fhbay.domain.Category;
import sve2.fhbay.domain.Customer;
import sve2.fhbay.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.ArticleAdminRemote;
import sve2.fhbay.interfaces.CustomerAdminRemote;
import sve2.util.DateUtil;
import sve2.util.RemoteClient;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;

public class BidRestTest {

  @Test
  public void test() throws IdNotFoundException {

    RemoteClient client = new RemoteClient();
    client.init();
    ArticleAdminRemote articleAdmin = client.getArticleAdmin();
    CustomerAdminRemote customerAdmin = client.getCustomerAdmin();

    Customer c1 = new Customer("Max", "Mustermann", "mm", "secret", "mm@gmail.com");
    c1.setId(customerAdmin.saveCustomer(c1));

    Category category = new Category("Elektronik");
    category.setId(articleAdmin.saveCategory(category));

    Date now = DateUtil.now();
    Article article = new Article("Samsung T166", "Samsung Spinpoint T166, 500GB, SATA", 9, now, DateUtil.addSeconds(now, 20));
    article.setId(articleAdmin.offerArticle(article, c1.getId()));

    Article a1 = articleAdmin.findArticleById(article.getId());
    assertEquals(ArticleState.OFFERED, a1.getArticleState());
    assertEquals(0, a1.getBids().size());
    assertEquals(c1.getId(), a1.getSeller().getId());

    JsonPath json = RestAssured.get("/fhbay/rest/bids/" + a1.getId()).then().extract().jsonPath();

    assertEquals(9.0, json.getDouble("minPrice"), 0.0);
    assertEquals(0, json.get("numberOfBids"));
    assertEquals("OFFERED", json.get("articleState"));

    // send bid
    RestAssured.put("/fhbay/rest/bids/pushBid?articleId=" + a1.getId() + "&amount=10&customerId=" + c1.getId()).then().statusCode(204);

    json = RestAssured.get("/fhbay/rest/bids/" + a1.getId()).then().extract().jsonPath();

    assertEquals(10.0, json.getDouble("minPrice"), 0.0);
    assertEquals(1, json.get("numberOfBids"));
    assertEquals("OFFERED", json.get("articleState"));

  }
}
