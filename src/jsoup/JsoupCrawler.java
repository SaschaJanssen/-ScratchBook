package jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupCrawler {

	private static final String ratingClassName = "div.rating";
	private static final String messageDateClassName = "em.smaller";
	private static final String userNameLinkClassName = "li.user-name a";
	private static final String reviewCommentCssClassName = "p.review_comment";
	private static final String reviewDataCssClassName = "div.media-story";
	private static final String userDataCssClassName = "div.user-passport";
	private final String selectedPaginationCssClassName = "span.highlight2";
	private final String paginationControlsCssCLassName = "div.pagination_controls";
	private final String reviewContainerCssClassName = "div.media-block-no-margin";
	private String baseUrl;
	private String endpoint;
	private Document document = null;

	public static void main(String[] args) {
		System.setProperty("http.proxyHost", "cache.gsa.westlb.net");
		System.setProperty("http.proxyPort", "8080");

		System.setProperty("https.proxyHost", "cache.gsa.westlb.net");
		System.setProperty("https.proxyPort", "4430");

		JsoupCrawler c = new JsoupCrawler("https://www.yelp.com", "/biz/vapiano-new-york-2");
		c.crawl();
	}

	JsoupCrawler(String baseUrl, String endpoint) {
		this.baseUrl = baseUrl;
		this.endpoint = endpoint;
	}

	private void crawl() {

		while (endpoint != null && !endpoint.isEmpty()) {
			try {
				document = getDocument();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}

			Element body = document.body();
			Element reviewContainer = getReviewDataContainer(body);

			extractReviewDataFromHtml(reviewContainer, document.head());

			Element currentPaginationSite = getPaginationCurrentSelectedData(body);
			endpoint = getNextPageFromPagination(currentPaginationSite);
		}

	}

	public Document getDocument() throws IOException {
		return Jsoup.connect(baseUrl + endpoint).get();
	}

	public Element getReviewDataContainer(Element body) {
		return body.select(reviewContainerCssClassName).first();
	}

	public Element getPaginationCurrentSelectedData(Element body) {
		Element pagination = body.select(paginationControlsCssCLassName).first();
		Element currentPaginationSelection = pagination.select(selectedPaginationCssClassName).first();
		return currentPaginationSelection;
	}

	public String getNextPageFromPagination(Element currentPaginationElement) {
		String nextLink = null;

		Elements siblings = currentPaginationElement.siblingElements();
		int pageNo = convertElementTextToInt(currentPaginationElement);
		for (Element sibling : siblings) {
			int nextPageNo = convertElementTextToInt(sibling);
			if (nextPageNo == (pageNo + 1)) {
				nextLink = sibling.attr("href");
				break;
			}
		}

		return nextLink;

	}

	private int convertElementTextToInt(Element element) {
		return Integer.parseInt(getUserNameFromUserInfo(element));
	}

	public List<String> extractReviewDataFromHtml(Element reviewContainer, Element headerElements) {
		List<String> resultList = new ArrayList<String>();

		Element userData = getUserData(reviewContainer);
		Element reviewData = getReviewData(reviewContainer);

		String message = getReviewTextFromComment(reviewData);
		resultList.add(message);

		Element userInfo = userData.select(userNameLinkClassName).first();
		String networkUser = getUserNameFromUserInfo(userInfo);
		resultList.add(networkUser);

		String networkUserId = getUserIdFromUserInfo(userInfo);
		resultList.add(networkUserId);

		String language = getLanguageFromHeadMetaData(headerElements);
		resultList.add(language);

		// TODO String geoLocation;
		String networkId = "YELP";
		resultList.add(networkId);

		String networkMessageDate = getNetworkMessageDate(reviewData);
		resultList.add(networkMessageDate);

		String messageReceiveDate = "today";
		resultList.add(messageReceiveDate);

		String customerId = "1";
		resultList.add(customerId);

		String platformUserRating = getNetworkUserRating(reviewData);
		resultList.add(platformUserRating);

		return resultList;
	}

	private String getNetworkUserRating(Element reviewData) {
		String platformUserRating = reviewData.select(ratingClassName).first().getElementsByTag("meta").first().attr("content");
		return platformUserRating;
	}

	private String getNetworkMessageDate(Element reviewData) {
		String networkMessageDate = reviewData.select(messageDateClassName).first().text();
		return networkMessageDate;
	}

	private String getUserNameFromUserInfo(Element userInfo) {
		return userInfo.text();
	}

	private String getLanguageFromHeadMetaData(Element headerElements) {
		Elements metaData = headerElements.getElementsByTag("meta");
		String language = "";
		for (Element meta : metaData) {
			if (meta.hasAttr("http-equiv") && meta.attr("http-equiv").equals("Content-Language")) {
				language = meta.attr("content");
				break;
			}
		}
		return language;
	}

	private String getUserIdFromUserInfo(Element userInfo) {
		String href = userInfo.attr("href");
		String userId = href.substring(href.indexOf("=") + 1);
		String networkUserId = userId;
		return networkUserId;
	}

	private String getReviewTextFromComment(Element reviewData) {
		return reviewData.select(reviewCommentCssClassName).first().text();
	}

	private Element getReviewData(Element reviewContainer) {
		return reviewContainer.select(reviewDataCssClassName).first();
	}

	private Element getUserData(Element reviewContainer) {
		return reviewContainer.select(userDataCssClassName).first();
	}

}
