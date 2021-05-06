package com.itheima.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itheima.pojo.Article;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ESClientTest {

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

    // 创建索引库
    @Test
    public void createIndex() throws UnknownHostException {
        // 使用client对象创建一个索引库 get()执行操作
        client.admin().indices().prepareCreate("index_hello").get();
        // 关闭client对象
        client.close();
    }

    // 设置mappings
    @Test
    public void setMappings() throws IOException {
        // 创建一个mappings信息
        String jsonStr = "{\n" +
                "    \"article\": {\n" +
                "        \"properties\": {\n" +
                "            \"id\": {\n" +
                "                \"type\": \"long\",\n" +
                "                \"store\": true\n" +
                "            },\n" +
                "            \"title\": {\n" +
                "                \"type\": \"text\",\n" +
                "                \"store\": true,\n" +
                "                \"index\": true,\n" +
                "                \"analyzer\": \"standard\"\n" +
                "            },\n" +
                "            \"content\": {\n" +
                "                \"type\": \"text\",\n" +
                "                \"store\": true,\n" +
                "                \"index\": true,\n" +
                "                \"analyzer\": \"standard\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
        XContentBuilder builder = XContentFactory.jsonBuilder()
                .startObject()
                    .startObject("article")
                        .startObject("properties")
                            .startObject("id")
                                .field("type", "long")
                                .field("store", true)
                            .endObject()
                            .startObject("title")
                                .field("type", "text")
                                .field("store", true)
                                .field("index", true)
                                .field("analyzer", "ik_smart")
                            .endObject()
                            .startObject("content")
                                .field("type", "text")
                                .field("store", true)
                                .field("index", true)
                                .field("analyzer", "ik_smart")
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject();
        // 使用client把mappings信息设置到索引库中
        client.admin().indices().preparePutMapping("index_hello")
                .setType("article")
//                .setSource(builder)
                .setSource(jsonStr, XContentType.JSON)
                .get();

        client.close();
    }

    // 添加document
    @Test
    public void addDocument() {
        // 创建一个文档对象
        String jsonStr = "{\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"ElasticSearch是一个基于Lucene的搜索服务器\",\n" +
                "    \"content\": \"它提供了一个分布式多用户能力的全文搜索引擎，基于RESTful web接口。Elasticsearch是用Java开发的，并作为Apache许可条款下的开放源码发布，是当前流行的企业级搜索引擎。设计用于云计算中，能够达到实时搜索，稳定，可靠，快速，安装使用方便。\"\n" +
                "}";
        // 把文档对象添加到索引库中
        client.prepareIndex()
                .setIndex("index_hello")
                .setType("article")
                .setId("1") // 设置文档_id，不设置会自动生成
                .setSource(jsonStr, XContentType.JSON)
                .get();

        client.close();
    }

    // 添加document（使用jackson）
    @Test
    public void addDocumentUseJackson() throws JsonProcessingException {
        Article article = new Article();
        article.setId(2);
        article.setTitle("习近平总书记关于青年工作重要论述综述");
        article.setContent("广大青年要肩负历史使命，坚定前进信心，立大志、明大德、成大才、担大任，努力成为堪当民族复兴重任的时代新人，让青春在为祖国、为民族、为人民、为人类的不懈奋斗中绽放绚丽之花。");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(article);

        client.prepareIndex("index_hello", "article", "2")
                .setSource(jsonStr, XContentType.JSON)
                .get();

        client.close();
    }
}
