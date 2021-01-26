package com.company;

import org.apache.http.HttpHost;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;
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
            MatchPhraseQueryBuilder mcb = QueryBuilders.matchPhraseQuery("text","whatever");
            //FuzzyQueryBuilder mcb = QueryBuilders.fuzzyQuery("message","try baca");

            scb.query(mcb);
            request.source(scb);

            SearchResponse response =
                    client.search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();

            SearchHit[] searchHits = hits.getHits();
            if(hits.getTotalHits().value != 0) {
                for (int i = 0; i < hits.getTotalHits().value; i++) {
                    Map<String, Object> sourceAsMap = searchHits[i].getSourceAsMap();

                    //System.out.println(i+1 + ": " + searchHits[i].getSourceAsString());
                    System.out.println(i+1 + ": Book: " + sourceAsMap.get("name") + " Page: " + sourceAsMap.get("page") + " Text: \n\n" + sourceAsMap.get("text"));


                    if(i == 9) break;
                }
            }
            //        Arrays.stream(searchHits)
            //                .filter(Objects::nonNull)
            //                .map(e -> toJson(e.getSourceAsString()))
            //                .collect(Collectors.toList());
            System.out.println("koniec");
           return catalogItems;
        } catch (IOException ex) {

        }
        return catalogItems;
    }
}
////                 System.out.println(searchHits[0].getIndex());