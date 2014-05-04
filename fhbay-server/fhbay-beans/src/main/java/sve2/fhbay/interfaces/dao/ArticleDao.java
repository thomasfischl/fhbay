package sve2.fhbay.interfaces.dao;

import java.util.List;

import sve2.fhbay.domain.Article;
import sve2.fhbay.exceptions.IdNotFoundException;

public interface ArticleDao extends Dao<Article, Long> {

  List<Article> findByPatternAndCategory(Long categoryId, String pattern) throws IdNotFoundException;

}
