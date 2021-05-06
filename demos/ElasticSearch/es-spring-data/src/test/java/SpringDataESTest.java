import com.itheima.entity.Article;
import com.itheima.repositories.ArticleRepository;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SpringDataESTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ElasticsearchTemplate template;

    @Test
    public void createIndex() {
        // 创建索引并配置映射关系
        template.createIndex(Article.class);
        // 配置映射关系
//        template.putMapping(Article.class);
    }

    @Test
    public void addDocument() {
        // 创建一个Article对象
        Article article = new Article();
        article.setId(1);
        article.setTitle("Maven是什么");
        article.setContent("Maven是一款由Apache软件基金会开发的，用来管理项目的构建，生成报告和文档的Java项目管理工具");
        // 把文档写入索引库
        articleRepository.save(article);
        // 修改也是添加，只要保证_id一致
    }

    @Test
    public void deleteDocument() {
        articleRepository.deleteById(1L);
        // 全部删除
//        articleRepository.deleteAll();
    }

    @Test
    public void addDocuments() {
        for (int i = 1; i < 20; i++) {
            Article article = new Article();
            article.setId(i);
            article.setTitle("Maven是什么" + i);
            article.setContent("Maven是一款由Apache软件基金会开发的，用来管理项目的构建，生成报告和文档的Java项目管理工具");
            articleRepository.save(article);
        }
    }

    @Test
    public void findAll() {
        Iterable<Article> articles = articleRepository.findAll();
        articles.forEach(a -> System.out.println(a));
    }

    @Test
    public void findById() {
        Optional<Article> optional = articleRepository.findById(1L);
        Article article = optional.get();
        System.out.println(article);
    }

    @Test
    public void findByTitle() {
        // 先分词再查询，每个词之间是and的关系
        List<Article> list = articleRepository.findByTitle("maven是");
        list.stream().forEach(a -> System.out.println(a));
    }

    @Test
    public void findByTitleOrContent() {
        // 默认返回10条
//        List<Article> list = articleRepository.findByTitleOrContent("maven是", "软件");
        // 从第0页开始
        Pageable pageable = PageRequest.of(0, 15);
        List<Article> list = articleRepository.findByTitleOrContent("maven是", "软件", pageable);
        list.stream().forEach(a -> System.out.println(a));
    }

    @Test
    public void findByQueryString() {
        // 先分词再查询，每个词之间是or的关系
        Pageable pageable = PageRequest.of(0, 15);
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.queryStringQuery("maven是一个工程构建工具").defaultField("title"))
                .withPageable(pageable).build();
        List<Article> list = template.queryForList(query, Article.class);
        list.stream().forEach(a -> System.out.println(a));
    }
}
