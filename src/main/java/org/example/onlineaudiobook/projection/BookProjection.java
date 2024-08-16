package org.example.onlineaudiobook.projection;


public interface BookProjection {
    Long getBookId();

    String getBookName();

    String getAuthorName();

    String getBookType();

    byte[] getImageContent();

    String getCategoryName();

}