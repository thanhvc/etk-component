package org.etk.core.rest.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.etk.core.rest.BaseTest;
import org.etk.core.rest.impl.header.HeaderHelper;

public class ResponseImplTest extends BaseTest {

  public void testSetHeader() throws Exception {
    Response response = Response.ok()
                                .language(new Locale("en", "GB"))
                                .language(new Locale("en", "US"))
                                .type(new MediaType("text", "plain"))
                                .location(new URI("http://etk/ws/rs/test"))
                                .cookie(new NewCookie("name1", "value1"))
                                .cookie(new NewCookie("name2", "value2"))
                                .tag(new EntityTag("123456789", true))
                                .lastModified(new Date(1000))
                                .build();
    List<String> l = new ArrayList<String>();
    l.add("location:http://etk/ws/rs/test");
    l.add("set-cookie:name1=value1;Version=1");
    l.add("set-cookie:name2=value2;Version=1");
    l.add("content-language:en-us");
    l.add("content-type:text/plain");
    l.add("etag:W/\"123456789\"");
    l.add("last-modified:Thu, 01 Jan 1970 00:00:01 GMT");
    for(Map.Entry<String, List<Object>> e: response.getMetadata().entrySet()) {
      String name = e.getKey();
      for(Object o : e.getValue()) {
        String h = name + ":" + HeaderHelper.getHeaderAsString(o);
        assertTrue(l.contains(h));
      }
      
    }
  }
  
  
}
