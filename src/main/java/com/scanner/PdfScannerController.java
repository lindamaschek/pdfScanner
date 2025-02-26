package com.scanner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/pdf")
public class PdfScannerController {

  private final PdfValidator pdfValidator;

  public PdfScannerController() {
    this.pdfValidator = new PdfValidator(List.of(new BlacklistedIbanCheck()));
  }



  @PostMapping("/scan")
  public ResponseEntity<Map<String, String>> scanPdfFromUrl(@RequestBody PdfRequest request) {


    // handle one or more urls
    List<String> urls = request.getUrls() != null ? request.getUrls() : List.of(request.getUrl());

    if (urls.isEmpty() || (request.getUrl() == null
        && (request.getUrls() == null || request.getUrls().isEmpty()))) {
      return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "No valid URL"));
    }

    for (String pdfUrl : urls) {
      try {
        File downloadedFile = downloadPdf(pdfUrl);
        String extractedText = extractTextFromPdf(downloadedFile);

        boolean isValid = pdfValidator.isValid(extractedText);


        // Idea: better status/message, list of all failed PDFs/Ibans
        if (isValid) {
          return ResponseEntity.status(400).body(Map.of("status", "error", "message",
              "Blacklisted IBANs found in <" + pdfUrl + ">. Please Check!"));
        }

      } catch (Exception e) {
        // more detailed exception
        return ResponseEntity.badRequest()
            .body(Map.of("status", "error", "message", e.getMessage()));
      }
    }
    return ResponseEntity
        .ok(Map.of("status", "successfull", "message", "No blacklisted IBANs found!"));
  }


  /*
   * idea: configurable temp path, better logging, better exception handling
   * 
   * check url before creating temp file
   */
  private File downloadPdf(String pdfUrl) throws IOException {
    Path tempFilePath = Files.createTempFile("downloaded_", ".pdf");

    try (InputStream in = new URI(pdfUrl).toURL().openStream()) {
      Files.copy(in, tempFilePath, StandardCopyOption.REPLACE_EXISTING);
    } catch (URISyntaxException e) {
      throw new IOException("Invalid URL format: " + pdfUrl, e);
    }

    return tempFilePath.toFile();
  }



  /*
   * alternative: itext
   * 
   * but for this it was easier with pdfbox (free & open source and enough for this purpose)
   */
  private String extractTextFromPdf(File pdfFile) throws IOException {
    try (PDDocument document = PDDocument.load(pdfFile)) {
      PDFTextStripper stripper = new PDFTextStripper();
      return stripper.getText(document);
    }
  }



}
