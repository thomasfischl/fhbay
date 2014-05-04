package sve2.fhbay.web.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import sve2.fhbay.domain.Article;
import sve2.fhbay.domain.ArticleState;
import sve2.fhbay.exceptions.IdNotFoundException;
import sve2.fhbay.interfaces.ArticleAdminLocal;
import sve2.fhbay.web.rest.dto.ArticleDTO;

@Path("/articles")
public class ArticleRestService {

  @EJB
  private ArticleAdminLocal articleAdmin;

  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public ArticleDTO getArticle(@PathParam("id") Long id) {
    try {
      Article article = articleAdmin.findArticleById(id);
      return Converter.mapToDto(article);
    } catch (IdNotFoundException e) {
      throw new WebApplicationException(Status.NOT_FOUND);
    }
  }

  @GET
  @Path("/search")
  @Produces(MediaType.APPLICATION_JSON)
  public List<ArticleDTO> search(@QueryParam("categoryId") Long categoryId, @QueryParam("pattern") String pattern) {
    try {
      List<Article> articles = articleAdmin.findAllMatchingArticles(categoryId, pattern, false);

      List<ArticleDTO> result = new ArrayList<>();
      for (Article article : articles) {
        result.add(Converter.mapToDto(article));
      }
      return result;
    } catch (IdNotFoundException e) {
      throw new WebApplicationException(Status.NOT_FOUND);
    }
  }

  @PUT
  @Path("offer")
  @Consumes(MediaType.APPLICATION_JSON)
  public void offer(ArticleDTO article) throws Exception {
    try {
      Article domain = Converter.mapToDomain(article);
      domain.setId(null);
      domain.setArticleState(ArticleState.OFFERED);
      articleAdmin.offerArticle(domain, article.getSellerId());
    } catch (IdNotFoundException e) {
      throw new WebApplicationException(Status.NOT_FOUND);
    }
  }
  
  
  
}