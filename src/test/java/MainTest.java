import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.http.HttpResponse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.crawler.backend.Main;
import com.crawler.backend.model.CrawlRequest;
import com.crawler.backend.model.CrawlResponse;
import com.google.gson.Gson;

import spark.Spark;

public class MainTest {

    private static final Gson gson = new Gson();

    @BeforeAll
    static void setUp() {
        Spark.stop();
        Spark.awaitStop();
        Main.main(new String[] {});
        Spark.awaitInitialization();
    }

    @AfterAll
    static void tearDown() {
        Spark.stop();
        Spark.awaitStop();
    }

    @Test
    void shouldReturnErrorWhenKeywordIsNull() throws Exception {
        CrawlRequest request = new CrawlRequest(null);
        HttpResponse<String> response = TestUtils.post("/crawl", gson.toJson(request));
        CrawlResponse crawlResponse = gson.fromJson(response.body(), CrawlResponse.class);

        assertEquals(400, response.statusCode());
        assertTrue(crawlResponse.isError());
        assertTrue(crawlResponse.getMessage().contains("keyword must be between 4 and 32 characters"));
    }

    @Test
    void shouldReturnErrorWhenKeywordIsTooShort() throws Exception {
        CrawlRequest request = new CrawlRequest("abc");
        HttpResponse<String> response = TestUtils.post("/crawl", gson.toJson(request));
        CrawlResponse crawlResponse = gson.fromJson(response.body(), CrawlResponse.class);

        assertEquals(400, response.statusCode());
        assertTrue(crawlResponse.isError());
        assertTrue(crawlResponse.getMessage().contains("keyword must be between 4 and 32 characters"));
    }

    @Test
    void shouldReturnErrorWhenKeywordIsTooLong() throws Exception {
        String longKeyword = "a".repeat(33);
        CrawlRequest request = new CrawlRequest(longKeyword);
        HttpResponse<String> response = TestUtils.post("/crawl", gson.toJson(request));
        CrawlResponse crawlResponse = gson.fromJson(response.body(), CrawlResponse.class);

        assertEquals(400, response.statusCode());
        assertTrue(crawlResponse.isError());
        assertTrue(crawlResponse.getMessage().contains("keyword must be between 4 and 32 characters"));
    }

    @Test
    void shouldAcceptValidKeyword() throws Exception {
        CrawlRequest request = new CrawlRequest("java");
        HttpResponse<String> response = TestUtils.post("/crawl", gson.toJson(request));
        CrawlResponse crawlResponse = gson.fromJson(response.body(), CrawlResponse.class);

        assertEquals(200, response.statusCode());
        assertFalse(crawlResponse.isError());
        assertNotNull(crawlResponse.getId());
    }

    @Test
    void shouldReturnStatusWhenIdExists() throws Exception {
        CrawlRequest request = new CrawlRequest("java");
        HttpResponse<String> crawlResponse = TestUtils.post("/crawl", gson.toJson(request));
        CrawlResponse responseBody = gson.fromJson(crawlResponse.body(), CrawlResponse.class);

        HttpResponse<String> response = TestUtils.get("/crawl/" + responseBody.getId());
        CrawlResponse statusResponse = gson.fromJson(response.body(), CrawlResponse.class);

        assertEquals(200, response.statusCode());
        assertFalse(statusResponse.isError());
    }

    @Test
    void shouldReturnErrorWhenIdNotFound() throws Exception {
        String nonExistentId = "non-existent-id";
        HttpResponse<String> response = TestUtils.get("/crawl/" + nonExistentId);
        CrawlResponse crawlResponse = gson.fromJson(response.body(), CrawlResponse.class);

        assertEquals(404, response.statusCode());
        assertTrue(crawlResponse.isError());
        assertTrue(crawlResponse.getMessage().contains("Id not found"));
    }
}