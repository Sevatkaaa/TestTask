package agile;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
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
        String url = args[0];
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select(A_HREF);
        Element okButton = null;
        for (Element element : elements) {
            Element actualElement = element.getElementById(OK_BUTTON_ID);
            if (actualElement != null) {
                okButton = actualElement;
            }
        }
        if (okButton == null) {
            System.out.println("Button not found");
            return;
        }
        List<Attribute> attributes = okButton.attributes().asList();
        Map<String, String> attrs = attributes.stream()
                .collect(Collectors.toMap(Attribute::getKey, Attribute::getValue));

        String targetUrl = args[1];
        Document targetDoc = Jsoup.connect(targetUrl).get();
        Elements targetElements = targetDoc.select(A_HREF);
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
}
