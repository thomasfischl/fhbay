package sve2.fhbay.web.rest;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import sve2.fhbay.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.ArticleAdminLocal;

@Path("/articles")
public class ArticleResource {

  @EJB
  private ArticleAdminLocal articleAdmin;

  @GET
  @Path("/{id}")
  @Produces({ "application/json" })
  public ArticleDTO getArticle(@PathParam("id") Long id) {
    try {
      return new ArticleDTO(articleAdmin.findArticleById(id));
    } catch (IdNotFoundException e) {
      throw new WebApplicationException(Status.NOT_FOUND);
    }
  }
}