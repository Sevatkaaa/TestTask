package agile;

import java.io.IOException;
import java.util.List;

public class CrawlerRunner {
    
    private static final String DELIMITER = " > ";

    public static void main(String[] args) throws IOException {
        Crawler crawler = new Crawler();
        
        List<String> path = crawler.findButtonByOrigin(args[0], args[1]);
        
        System.out.println(String.join(DELIMITER, path));
    }
    
}
