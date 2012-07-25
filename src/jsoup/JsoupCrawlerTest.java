package jsoup;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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

		jsoupCrawler = new JsoupCrawler("https://www.yelp.com", "/biz/vapiano-new-york-2");
		doc = jsoupCrawler.getDocument();
	}

	@Test
	public void testGetDocument() throws Exception {
		assertNotNull(doc);
		assertEquals("Vapiano - Greenwich Village - New York, NY", doc.title());
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
