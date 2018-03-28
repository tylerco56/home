package com.myd.home.models;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class Links {

    private Integer id;

    //url of page to rake data from
    private String rakedDataUrl;

    //store the Whole webpage into the Document to filter raked data from
    private Document rakedDataPage;

    //a String that contains the css filter
    private String filter;

    //Elements that are generated from the css filter on the rakedDataPage document
    private Elements filteredData;

    //title of webpage where the raked data comes from
    private String titlePage;

    //temporary varible to hold individual link titles as it collects data from rakedDataPage
    private String linkTitle;

    //temporary variable to hold individual urls for each link that hold the criteria from rakedDataPage
    private String linkUrl;

    //Hashmap to hold combo of titles and urls
    private HashMap<String, String> titleAndUrl = new HashMap<>();

    public Integer getId() {
        return id;
    }

    public Links() {

    }

    public Links(String rakedDataUrl, String filter) {
        this.rakedDataUrl = rakedDataUrl;
        this.filter = filter;
    }


    public String getRakedDataUrl() {
        return rakedDataUrl;
    }

    public void setRakedDataUrl(String rakedDataUrl) {
        this.rakedDataUrl = rakedDataUrl;
    }

    public Document getRakedDataPage() {
        return rakedDataPage;
    }

    public void setRakedDataPage(Document rakedDataPage) {
        this.rakedDataPage = rakedDataPage;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Elements getFilterdData() {
        return filteredData;
    }

    public void setFilterdData(Elements filterdData) {
        this.filteredData = filterdData;
    }

    public String getTitlePage() {
        return titlePage;
    }

    public void setTitlePage(String titlePage) {
        this.titlePage = titlePage;
    }

    public String getLinkTitle() {
        return linkTitle;
    }

    public void setLinkTitle(String linkTitle) {
        this.linkTitle = linkTitle;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public HashMap<String, String> getTitleAndUrl() {
        return titleAndUrl;
    }


    public Elements generateFilterdData(){
        try {
            rakedDataPage = Jsoup.connect(rakedDataUrl).get();
            filteredData = rakedDataPage.select(filter);
            titlePage = rakedDataPage.title();

            return filteredData;

        } catch (IOException ex) {
            System.out.print(ex);
        }

        return null;

    }

    public void addToLinkList(String linkTitle, String linkUrl){
        //substring chops the title to 50 characters or less
        int titleLengthMax = 47;
        int maxLength = (linkTitle.length() < titleLengthMax ? linkTitle.length() : titleLengthMax );
        linkTitle = linkTitle.substring(0, maxLength);
        linkTitle.concat("...");
        titleAndUrl.put(linkTitle, linkUrl);
    }

    public void addToDatabase(HashMap titleAndUrl){

    }
}


