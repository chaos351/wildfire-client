package org.codework.wildfire.client.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codework.wildfire.client.model.ExceptionData;
import org.codework.wildfire.client.WildfireProjectConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Steven Benitez
 * @since 1.0.0
 */
public class WildfireHttpClient implements WildfireClient {
  private static final Logger logger = LoggerFactory.getLogger(WildfireHttpClient.class);

  private WildfireProjectConfig projectConfig;
  private String baseUrl;
  private CloseableHttpClient client;
  private Gson gson;

  public WildfireHttpClient(WildfireProjectConfig projectConfig, String host, int port, String version) {
    this.projectConfig = projectConfig;
    this.baseUrl = String.format("http://%s:%d/api/%s", host, port, version);
    this.client = HttpClients.createDefault();
    this.gson = new GsonBuilder().setPrettyPrinting()
        .registerTypeAdapter(DateTime.class, new DateTimeAdapter())
        .create();
  }

  @Override
  public void notify(@NotNull Throwable throwable) {
    notify(throwable, null);
  }

  @Override
  public void notify(@NotNull Throwable throwable, @Nullable Object details) {
    ExceptionData exceptionData = new ExceptionData(throwable, details);
    exceptionData.setApiKey(projectConfig.getApiKey());
    exceptionData.setProject(projectConfig.getProject());
    exceptionData.setEnvironment(projectConfig.getEnvironment());
    exceptionData.setRevision(projectConfig.getRevision());
    exceptionData.setBranch(projectConfig.getBranch());

    doPost(
        "/notify",
        new ExceptionData(throwable, details)
    );
  }

  private void doPost(String resource, Object payload) {
    String json = gson.toJson(payload);
    logger.debug("Msg to Wildfire API [POST]:: URL : {}", baseUrl + resource);
    logger.debug("Payload for [POST]:: {}", json);

    HttpPost httpPost = new HttpPost(baseUrl + resource);
    httpPost.setEntity(new StringEntity(json, StandardCharsets.UTF_8));
    callWildfireSafe(httpPost);
  }

  private void callWildfireSafe(HttpUriRequest request) {
    try {
      callWildfire(request);
    } catch (IOException e) {
      logger.warn("Error while calling Wildfire", e);
    }
  }

  private void callWildfire(HttpUriRequest request) throws IOException {
    request.setHeader("Accept", "application/json");
    request.setHeader("Content-Type", "application/json; charset=utf-8");

    try (CloseableHttpResponse response = client.execute(request)) {
      int statusCode = response.getStatusLine().getStatusCode();
      logger.debug("Response from Wildfire API :: {}", response.getStatusLine());
      HttpEntity entity = response.getEntity();

      try {
        String payload = EntityUtils.toString(entity);
        logger.debug("Msg from Wildfire API :: {}", payload);

        // Handle errors payload
        if (statusCode >= 400) {
          logger.warn("Wildfire error whilst calling: {}\n{}", request.getRequestLine().getUri(), payload);

          // todo: add error handling from service
        }
      } finally {
        // ensure the response body is fully consumed
        EntityUtils.consume(entity);
      }
    }
  }
}
