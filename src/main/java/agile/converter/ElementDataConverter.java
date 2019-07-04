package agile.converter;

import agile.data.ElementData;
import org.jsoup.nodes.Element;

public class ElementDataConverter {
    
    public ElementData convert(Element source) {
        ElementData target = new ElementData();
        target.setTag(source.tagName());
        target.setId(source.id());
        target.setClazz(source.className());
        return target;
    }
    
}
