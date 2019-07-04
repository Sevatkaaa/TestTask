package agile;

import agile.data.ElementData;

import java.io.IOException;
import java.util.List;

public class CrawlerRunner {
    
    private static final String DELIMITER = " > ";

    public static void main(String[] args) throws IOException {
        Crawler crawler = new Crawler();
        
        List<ElementData> path = crawler.findButtonByOrigin(args[0], args[1]);
        
        path.stream().forEach(element -> System.out.println(element + DELIMITER));
        System.out.println("Found button!");
    }
    
}
