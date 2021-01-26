package com.company;

import org.apache.http.HttpHost;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PDFBoxExample {
    public static void main(String args[]) throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));


        File file = new File("C:\\Users\\Luksor\\IdeaProjects\\SSLExample\\src\\main\\java\\com\\company\\The_energy_bus-10_rules_to_fuel_your_life_work_and_team_with_positive_energy.pdf");
        PDFTextStripper pdfStripper = new PDFTextStripper();
        pdfStripper.setLineSeparator(" ");
        PDDocument document = PDDocument.load(file);

        //String lines[] = text.split("\\r?\\n");


        for (int i = 1; i < 196; i++) {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            pdfStripper.setStartPage(i);
            pdfStripper.setEndPage((i));
            String text = pdfStripper.getText(document);
            System.out.println(text);
            String is = Integer.toString(i);
            builder.startObject();
            {
                builder.field("name", "The_energy_bus-10_rules_to_fuel_your_life_work_and_team_with_positive_energy");
                builder.field("page", i);
                builder.field("text", text);

            }
            builder.endObject();
            IndexRequest indexRequest = new IndexRequest("the_energy_bus-10_rules_to_fuel_your_life_work_and_team_with_positive_energy")
                    .id(is).source(builder);

            IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
            System.out.println("baca");
            }
            //document.close();


        /*
        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        File file = new File("C:\\Users\\Luksor\\IdeaProjects\\SSLExample\\src\\main\\java\\com\\company\\Living_in_the_Light-A_guide_to_personal_transformation.pdf");
        PDFParser parser = new PDFParser((RandomAccessRead) new FileInputStream(file));
        parser.parse();
        try (COSDocument cosDoc = parser.getDocument()) {
            pdfStripper = new PDFTextStripper();
            pdDoc = new PDDocument(cosDoc);
            pdfStripper.setStartPage(0);
            pdfStripper.setEndPage(1);
            String parsedText = pdfStripper.getText(pdDoc);
            System.out.println(parsedText);
        }

         */
        }
    }
