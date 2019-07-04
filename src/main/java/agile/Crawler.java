package agile;

import agile.exception.ButtonNotFoundException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
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
    private static final int MIN_EQUALITIES_TO_FIND_BUTTON = 2;
    private static final String HTML_TAG = "html";
    private static final String DELIMITER = " > ";

    public static void main(String[] args) throws IOException {
        Map<String, String> attrs = getOriginButtonAttributes(args[0]);

        Elements targetElements = getLinksFromDocument(args[1]);
        Element resultButton = null;
        for (Element element : targetElements) {
            int equalities = 0;
            Attributes targetAttrs = element.attributes();
            for (Attribute attribute : targetAttrs) {
                String value = attrs.get(attribute.getKey());
                if (value == null) {
                    continue;
                }
                if (attribute.getValue().contains(value)) {
                    equalities++;
                }
            }
            if (equalities > MIN_EQUALITIES_TO_FIND_BUTTON) {
                resultButton = element;
                break;
            }
        }
        if (resultButton == null) {
            System.out.println("Button was not found, it was hidden in a very good way :(");
            return;
        }
        Element currentElem = resultButton;
        List<String> path = new LinkedList<>();
        String tagName = null;
        while (!HTML_TAG.equals(tagName)) {
            tagName = currentElem.tagName();
            path.add(0, tagName);
            currentElem = currentElem.parent();
        }
        System.out.println(String.join(DELIMITER, path));
    }

    private static Map<String, String> getOriginButtonAttributes(String url) throws IOException {
        Elements elements = getLinksFromDocument(url);
        Element okButton = findOkButton(elements);
        List<Attribute> attributes = okButton.attributes().asList();
        return attributes.stream()
                .collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));
    }

    private static Element findOkButton(Elements elements) {
        return elements.stream()
                .filter(element -> element.getElementById(OK_BUTTON_ID) != null)
                .findFirst()
                .orElseThrow(ButtonNotFoundException::new);
    }

    private static Elements getLinksFromDocument(String url) throws IOException {
        return Jsoup.connect(url).get().select(A_HREF);
    }
}
