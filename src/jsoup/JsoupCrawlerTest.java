package jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

public class JsoupCrawlerTest {

	JsoupCrawler jsoupCrawler;
	private Document doc;

	@Before
	public void setUp() throws Exception {
		System.setProperty("https.proxyHost", "cache.gsa.westlb.net");
		System.setProperty("https.proxyPort", "4430");

		System.setProperty("http.proxyHost", "cache.gsa.westlb.net");
		System.setProperty("http.proxyPort", "8080");


		// http://www.tripadvisor.com
		jsoupCrawler = new JsoupCrawler("http://www.tripadvisor.com", "/Restaurant_Review-g60763-d1846484-Reviews-Vapiano-New_York_City_New_York.html");
		doc = jsoupCrawler.getDocument();
	}

	@Test
	public void testGetDocument() throws Exception {
		assertNotNull(doc);

		Element body = doc.body();
		Element reviews = body.select("div.review_collection").first();
		Elements re = reviews.select("div.reviewSelector");
		for (Element element : re) {
			System.out.println(element.select("div.entry").text());
		}
	}

	@Test
	public void testGetContainerOfReviewData() throws Exception {
		Element body = doc.body();
		Element reviewContainer = jsoupCrawler.getReviewDataContainer(body);

		assertNotNull(reviewContainer);
		assertEquals("media-block-no-margin clearfix media-block", reviewContainer.className());
	}

	@Test
	public void testGetSitePagination() throws Exception {
		Element body = doc.body();
		Element paginationElement = jsoupCrawler.getPaginationCurrentSelectedData(body);

		assertNotNull(paginationElement);
		assertEquals("highlight2", paginationElement.className());
		assertEquals("1", paginationElement.text());
	}

	@Test
	public void testGetNextPageLinkFromPagination() throws Exception {
		Element body = doc.body();
		Element paginationElement = jsoupCrawler.getPaginationCurrentSelectedData(body);

		String nextLink = jsoupCrawler.getNextPageFromPagination(paginationElement);

		assertNotNull(nextLink);
		assertEquals("/biz/vapiano-new-york-2?start=40", nextLink);
	}

	@Test
	public void testExtractReviewData() throws Exception {
		Element body = doc.body();
		Element reviewContainer = jsoupCrawler.getReviewDataContainer(body);

		List<String> results = jsoupCrawler.extractReviewDataFromHtml(reviewContainer, doc.head());

		assertEquals(9, results.size());
		assertTrue(results.get(0).startsWith("Even though Vapiano is one of my favorite places"));
		assertEquals("Dorie L.", results.get(1));
		assertEquals("H9QzuPZn_wWlx0NYStSO6Q", results.get(2));
		assertEquals("en", results.get(3));
		assertEquals("YELP", results.get(4));
		assertEquals("7/8/2012", results.get(5));
		assertEquals("today", results.get(6));
		assertEquals("1", results.get(7));
		assertEquals("4.0", results.get(8));
	}

}
