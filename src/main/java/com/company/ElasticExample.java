package com.company;

import org.apache.http.HttpHost;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.SimpleQueryStringBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ElasticExample {
    private static List<User> catalogItems;
    public static void main(String[] args) {
        findCatalogItem("");
    }

    public static List<User> findCatalogItem(String text) {
        try {

            RestHighLevelClient client = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost("localhost", 9200, "http"),
                            new HttpHost("localhost", 9201, "http")));


            SearchRequest request = new SearchRequest();
            SearchSourceBuilder scb = new SearchSourceBuilder();
            MatchPhraseQueryBuilder mcb = QueryBuilders.matchPhraseQuery("message","Elasticsearch");
            scb.query(mcb);
            request.source(scb);

            SearchResponse response =
                    client.search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            SearchHit[] searchHits = hits.getHits();
            if(hits.getTotalHits().value != 0) {
                for (int i = 0; i <= 2; i++) {
                    System.out.println(searchHits[i].getSourceAsString());
                }
            }
            //        Arrays.stream(searchHits)
            //                .filter(Objects::nonNull)
            //                .map(e -> toJson(e.getSourceAsString()))
            //                .collect(Collectors.toList());
            //System.out.println(searchHits[0].getSourceAsString());
            System.out.println("baca");
           return catalogItems;
        } catch (IOException ex) {

        }
        return catalogItems;
    }
}
////                 System.out.println(searchHits[0].getIndex());