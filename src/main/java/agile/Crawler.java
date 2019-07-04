package agile;

import agile.converter.ElementDataConverter;
import agile.data.ElementData;
import agile.exception.ButtonNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Crawler {

    private static final String OK_BUTTON_ID = "make-everything-ok-button";
    private static final String A_HREF = "a[href]";
    private static final long MIN_EQUALITIES_TO_FIND_BUTTON = 2;
    private static final String HTML_TAG = "html";
    
    private ElementDataConverter elementDataConverter = new ElementDataConverter();
    
    public List<ElementData> findButtonByOrigin(String originUrl, String targetUrl) throws IOException {
        Map<String, String> originAttributes = getOriginButtonAttributes(originUrl);

        Elements targetElements = getLinksFromDocument(targetUrl);
        Element resultButton = getResultButton(originAttributes, targetElements);
        
        return getResultPath(resultButton);
    }

    private Map<String, String> getOriginButtonAttributes(String url) throws IOException {
        Elements elements = getLinksFromDocument(url);
        Element okButton = findOkButton(elements);
        List<Attribute> attributes = okButton.attributes().asList();
        return attributes.stream()
                .collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));
    }

    private Elements getLinksFromDocument(String url) throws IOException {
        return Jsoup.connect(url).get().select(A_HREF);
    }

    private Element findOkButton(Elements elements) {
        return elements.stream()
                .filter(element -> element.getElementById(OK_BUTTON_ID) != null)
                .findFirst()
                .orElseThrow(ButtonNotFoundException::new);
    }

    private Element getResultButton(Map<String, String> originAttributes, Elements targetElements) {
        return targetElements.stream()
                .filter(element ->  getEqualitiesForElement(originAttributes, element) > MIN_EQUALITIES_TO_FIND_BUTTON)
                .findFirst()
                .orElseThrow(ButtonNotFoundException::new);
    }

    private long getEqualitiesForElement(Map<String, String> originAttributes, Element element) {
        return element.attributes().asList().stream()
                .filter(attribute -> containsAttribute(attribute, originAttributes))
                .count();
    }

    private boolean containsAttribute(Attribute attribute, Map<String, String> originAttributes) {
        String value = originAttributes.get(attribute.getKey());
        return value != null && attribute.getValue().contains(value);
    }

    private List<ElementData> getResultPath(Element currentElem) {
        List<ElementData> path = new LinkedList<>();
        String tagName = null;
        while (!HTML_TAG.equals(tagName)) {
            tagName = currentElem.tagName();
            path.add(0, elementDataConverter.convert(currentElem));
            currentElem = currentElem.parent();
        }
        return path;
    }
    
}
