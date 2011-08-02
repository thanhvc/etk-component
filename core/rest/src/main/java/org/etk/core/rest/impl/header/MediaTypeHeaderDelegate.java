package org.etk.core.rest.impl.header;

import java.util.Map;
import java.util.Map.Entry;
import java.text.ParseException;

import javax.ws.rs.core.MediaType;


public class MediaTypeHeaderDelegate extends AbstractHeaderDelegate<MediaType> {

  @Override
  public MediaType fromString(String header) throws IllegalArgumentException {
    if (header == null)
      throw new IllegalArgumentException();
    
    try {
      int p = header.indexOf('/');
      int col = header.indexOf(';');
      String type = null;
      String subType = null;
      
      if (p < 0 && col < 0) //no '/' and ';'
        return new MediaType(header, null);
      else if (p > 0 && col < 0)
        return new MediaType(HeaderHelper.removeWhitespaces(header.substring(0, p)),
                             HeaderHelper.removeWhitespaces(header.substring(p + 1)));
      else if (p < 0 && col > 0) { //there is no '/' but present ';'
        type = HeaderHelper.removeWhitespaces(header.substring(0, col));
      } else {//presents '/' and ';'
        type = HeaderHelper.removeWhitespaces(header.substring(0, p));
        subType = header.substring(p + 1, col);
      }
      
      Map<String, String> m = new HeaderParameterParser().parse(header);
      return new MediaType(type, subType, m);
    } catch(ParseException e) {
      throw new IllegalArgumentException(e);
    }
    
  }

  @Override
  public String toString(MediaType mime) {
    StringBuffer sb = new StringBuffer();
    sb.append(mime.getType()).append('/').append(mime.getSubtype());
    for (Entry<String, String> entry : mime.getParameters().entrySet()) {
      sb.append(';').append(entry.getKey()).append('=');
      HeaderHelper.appendWithQuote(sb, entry.getValue());
      
    }
    return sb.toString();
  }

  @Override
  public Class<MediaType> support() {
    return MediaType.class;
  }

  

}
