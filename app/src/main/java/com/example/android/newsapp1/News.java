package com.example.android.newsapp1;

/**
 */

import java.util.Date;

/**
 * An {@link News} object contains information related to a single news.
 */
class News {

    /**
     * Title of the news
     */
    private final String newsTitle;

    /**
     * Section name of the news
     */
    private final String newsSectionName;

    /**
     * Author name of the news
     */
    private final String newsAuthorName;

    /**
     * Publication date of the news
     */
    private final Date newsPublicationDate;

    /**
     * Website URL of the news
     */
    private final String newsUrl;

    /**
     * Image for the news
     */
    private final String newsImage;

    /**
     * Constructs a new {@link News} object.
     *
     * @param title           - title of the news
     * @param section         - the section where the news happened
     * @param author          - news author name
     * @param publicationDate - the news publication date
     * @param url             -the website URL to find more details about the news
     * @param image           - the image for the news
     */
    public News(String title, String section, String author, Date publicationDate, String url, String image) {
        newsTitle = title;
        newsSectionName = section;
        newsAuthorName = author;
        newsPublicationDate = publicationDate;
        newsUrl = url;
        newsImage = image;
    }

    /**
     * Returns the magnitude of the news.
     */
    public String getTitle() {
        return newsTitle;
    }

    /**
     * Returns the section name of the news.
     */
    public String getSectionName() {
        return newsSectionName;
    }

    /**
     * Returns the author of the news.
     */
    public String getAuthorName() {
        return newsAuthorName;
    }

    /**
     * Returns the publication date of the news.
     */
    public Date getPublicationDate() {
        return newsPublicationDate;
    }

    /**
     * Returns the website URL to find more information about the news.
     */
    public String getUrl() {
        return newsUrl;
    }

    /**
     * Returns the image for the news.
     */
    public String getImage() {
        return newsImage;
    }
}