package com.itheima.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.pojo.Article;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;

public class SearchTest {

    private TransportClient client;

    @Before
    public void init() throws UnknownHostException {
        // 创建一个Settings对象，相当于是一个配置信息，主要配置集群的名称
        Settings settings = Settings.builder()
                .put("cluster.name", "my-escluster")
                .build();
        // 创建一个客户端client对象
        client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.18"), 9301));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.18"), 9302));
        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.3.18"), 9303));
    }

    private void search(QueryBuilder queryBuilder) {
        // 执行搜索
        SearchResponse searchResponse = client.prepareSearch("index_hello")
                .setTypes("article")
                .setQuery(queryBuilder)
                .get();
        showSearchResult(searchResponse);

        client.close();
    }

    private void showSearchResult(SearchResponse searchResponse) {
        // 取得搜索结果
        SearchHits searchHits = searchResponse.getHits();
        // 搜索结果总记录数
        System.out.println("搜索结果总记录数：" + searchHits.getTotalHits());
        // 搜索结果列表
        Iterator<SearchHit> iterator = searchHits.iterator();
        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next();
            // 打印文档对象，以json格式输出
            System.out.println(searchHit.getSourceAsString());
            // 取文档的属性
            System.out.println("文档的属性");
            Map<String, Object> document = searchHit.getSource();
            System.out.println(document.get("id"));
            System.out.println(document.get("title"));
            System.out.println(document.get("content"));
        }
    }

    // 根据id搜索
    @Test
    public void searchById() {
        // 默认返回10条
        QueryBuilder queryBuilder = QueryBuilders.idsQuery().addIds("1", "2");
        search(queryBuilder);
    }

    // 根据term搜索
    @Test
    public void searchByTerm() {
        // 默认返回10条
        QueryBuilder queryBuilder = QueryBuilders.termQuery("title", "是");
        search(queryBuilder);
    }

    // 根据queryString搜索
    @Test
    public void searchByQueryString() {
        // 默认返回10条
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("习近平 青年").defaultField("title");
        search(queryBuilder);
    }

    // 添加分页搜索的数据
    @Test
    public void addArticles() throws JsonProcessingException {
        for (int i = 0; i < 100; i ++) {
            Article article = new Article();
            article.setId(2);
            article.setTitle("标题" + i);
            article.setContent("内容" + i);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonStr = objectMapper.writeValueAsString(article);

            client.prepareIndex("index_hello", "article", i + "")
                    .setSource(jsonStr, XContentType.JSON)
                    .get();
        }
        client.close();
    }

    // 分页搜索
    @Test
    public void searchByPage() {
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("标题").defaultField("title");
        SearchResponse searchResponse = client.prepareSearch("index_hello")
                .setTypes("article")
                .setQuery(queryBuilder)
                .setFrom(0) // 起始行
                .setSize(5) // 每页显示的行数
                .get();
        showSearchResult(searchResponse);
    }

    // 高亮搜索
    @Test
    public void searchHighlight() {
        QueryBuilder queryBuilder = QueryBuilders.queryStringQuery("习近平 青年").defaultField("title");
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.preTags("<em>");
        highlightBuilder.postTags("</em>");
        // 执行搜索
        SearchResponse searchResponse = client.prepareSearch("index_hello")
                .setTypes("article")
                .setQuery(queryBuilder)
                .highlighter(highlightBuilder)
                .get();
        // 取得搜索结果
        SearchHits searchHits = searchResponse.getHits();
        // 搜索结果总记录数
        System.out.println("搜索结果总记录数：" + searchHits.getTotalHits());
        // 搜索结果列表
        Iterator<SearchHit> iterator = searchHits.iterator();
        while (iterator.hasNext()) {
            SearchHit searchHit = iterator.next();
            System.out.println("**********高亮结果**********");
            Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
            System.out.println(highlightFields);
            System.out.println("**********高亮内容**********");
            HighlightField field = highlightFields.get("title");
            Text[] fragments = field.getFragments();
            if (fragments != null) {
                String title = fragments[0].toString();
                System.out.println(title);
            }
        }

        client.close();
    }
}
