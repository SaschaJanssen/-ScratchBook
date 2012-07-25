package jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupCrawler {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JsoupCrawler mwc = new JsoupCrawler();
		mwc.crawl();
	}

	private void crawl() {

		String baseUrl = "https://www.yelp.com";
		String endpoint = "/biz/vapiano-new-york-2";

		System.setProperty("https.proxyHost", "cache.gsa.westlb.net");
		System.setProperty("https.proxyPort", "4430");

		System.setProperty("http.proxyHost", "cache.gsa.westlb.net");
		System.setProperty("http.proxyPort", "8080");

		Document doc = null;
		Elements reviews = new Elements();

		while (endpoint != null && !endpoint.isEmpty()) {
			try {
				doc = Jsoup.connect(baseUrl + endpoint).get();
				Element body = doc.body();

				reviews.addAll(body.select("div.media-block-no-margin"));

				Element pagination = body.select("div.pagination_controls").first();

				Element currentPaginationSite = pagination.select("span.highlight2").first();
				Elements siblings = currentPaginationSite.siblingElements();
				int pageNo = Integer.parseInt(currentPaginationSite.text());
				for (Element sibling : siblings) {
					int nextPageNo = Integer.parseInt(sibling.text());
					if (nextPageNo == (pageNo + 1)) {
						endpoint = sibling.attr("href");
						System.out.println(currentPaginationSite + " --> " + endpoint);
						break;
					}

					endpoint = null;
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("Found: " + reviews.size() + " reviews.");

	}

}
