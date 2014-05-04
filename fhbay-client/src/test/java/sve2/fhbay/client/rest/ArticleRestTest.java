package sve2.fhbay.client.rest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sve2.fhbay.domain.Category;
import sve2.fhbay.domain.Customer;
import sve2.fhbay.interfaces.ArticleAdminRemote;
import sve2.fhbay.interfaces.CustomerAdminRemote;
import sve2.util.RemoteClient;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;

public class ArticleRestTest {

  @Test
  public void test() {

    RemoteClient client = new RemoteClient();
    client.init();
    ArticleAdminRemote articleAdmin = client.getArticleAdmin();
    CustomerAdminRemote customerAdmin = client.getCustomerAdmin();

    Customer c1 = new Customer("Max", "Mustermann", "mm", "secret", "mm@gmail.com");
    c1.setId(customerAdmin.saveCustomer(c1));

    String content = RestAssured.get("/fhbay/rest/articles/search?pattern=").then().extract().asString();
    int articleCount = content.split("articleState").length - 1;
    System.out.println(articleCount);

    content = RestAssured
        .given()
        .contentType(ContentType.JSON)
        .content(
            "{ \"name\": \"OCZ Agility 2 T166\", \"description\": \"SSD mit neuartiger Flash-Technologie\",\"sellerId\": " + c1.getId()
                + ",  \"initialPrice\": 10, \"startDate\": \"04-5-2014 09:57:50\"," + " \"endDate\": \"04-5-2014 09:57:50\" }").when()
        .put("fhbay/rest/articles/offer").then().extract().asString();

    int articleId = Integer.parseInt(content);
    System.out.println("ArticleId: " + articleId);
    JsonPath json = RestAssured.get("/fhbay/rest/articles/" + articleId).then().extract().jsonPath();

    assertEquals(articleId, json.get("id"));
    assertEquals("OCZ Agility 2 T166", json.get("name"));
    assertEquals(c1.getId().longValue(), json.getLong("sellerId"));
    assertEquals(10.0, json.getDouble("initialPrice"), 0.0);

    content = RestAssured.get("/fhbay/rest/articles/search?pattern=").then().extract().asString();
    int articleCountNew = content.split("articleState").length - 1;
    assertEquals(articleCount + 1, articleCountNew);
  }
}
