package com.google.codeu.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/logo-detect")
public class LogoDetectServlet extends HttpServlet {

  JsonArray logoJsonArray;

  @Override
  public void init() {
    this.logoJsonArray = new JsonArray();
    //this.logoJsonArray.add("starbucks");
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getOutputStream().println(this.logoJsonArray.toString());
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get("image");

    if (blobKeys != null && !blobKeys.isEmpty()) {
      BlobKey blobKey = blobKeys.get(0);
      ImagesService imagesService = ImagesServiceFactory.getImagesService();
      ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
      String imageUrl = imagesService.getServingUrl(options);

      byte[] blobBytes = getBlobBytes(blobstoreService, blobKey);
      String logos = getLogoInformation(blobBytes);
      String[] logosArray = logos.split(",");
      this.logoJsonArray.add(logosArray[0]); //Chose the first logo for now
    }
    response.sendRedirect("/info-present.html");
  }

  /**
   * Private method to convert Blobstore image into byte array for analysis.
   */
  private byte[] getBlobBytes(BlobstoreService blobstoreService, BlobKey blobKey)
      throws IOException {

    ByteArrayOutputStream outputBytes = new ByteArrayOutputStream();

    int fetchSize = BlobstoreService.MAX_BLOB_FETCH_SIZE;

    long currentByteIndex = 0;
    boolean continueReading = true;
    while (continueReading) {
      // end index is inclusive, so we have to subtract 1 to get fetchSize bytes
      byte[] b =
          blobstoreService.fetchData(blobKey, currentByteIndex, currentByteIndex + fetchSize - 1);
      outputBytes.write(b);

      // if we read fewer bytes than we requested, then we reached the end
      if (b.length < fetchSize) {
        continueReading = false;
      }

      currentByteIndex += fetchSize;
    }

    return outputBytes.toByteArray();
  }

  /**
   * Private method that takes in the byte array representation of an image.
   * It returns the labels as a string.
   */
  private String getImageLabels(byte[] imgBytes) throws IOException {
    ByteString byteString = ByteString.copyFrom(imgBytes);
    Image image = Image.newBuilder().setContent(byteString).build();

    /*
      Type.LOGO_DETECTION can be very useful for our open project,
      See https://cloud.google.com/vision/docs/features
    */
    Feature feature = Feature.newBuilder().setType(Type.LOGO_DETECTION).build();
    AnnotateImageRequest request =
        AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build();
    List<AnnotateImageRequest> requests = new ArrayList<>();
    requests.add(request);

    ImageAnnotatorClient client = ImageAnnotatorClient.create();
    BatchAnnotateImagesResponse batchResponse = client.batchAnnotateImages(requests);
    client.close();
    List<AnnotateImageResponse> imageResponses = batchResponse.getResponsesList();
    AnnotateImageResponse imageResponse = imageResponses.get(0);

    if (imageResponse.hasError()) {
      System.err.println("Error getting image labels: " + imageResponse.getError().getMessage());
      return null;
    }

    String labelsString = imageResponse.getLabelAnnotationsList().stream()
        .map(EntityAnnotation::getDescription)
        .collect(Collectors.joining(", "));

    return labelsString;
  }

}