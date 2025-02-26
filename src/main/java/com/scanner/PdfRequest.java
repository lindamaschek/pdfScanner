/**
 * 
 */
package com.scanner;

import java.util.List;

public class PdfRequest {

  /*
   * one or more urls possible
   */
  private List<String> urls;
  private String url;

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public List<String> getUrls() {
    return urls;
  }

  public void setUrls(List<String> urls) {
    this.urls = urls;
  }

}
