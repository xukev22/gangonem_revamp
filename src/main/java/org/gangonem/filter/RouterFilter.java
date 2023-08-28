package org.gangonem.filter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
@Order(1)
public class RouterFilter implements Filter {
	
	private static String INDEX_CONTENT;
	
	static {
		ResourceLoader resourceLoader = new DefaultResourceLoader();
		INDEX_CONTENT = RouterFilter.asString(resourceLoader.getResource("classpath:/static/index.html"));
	}
	
	public static String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream())) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void doFilter(
      ServletRequest request, 
      ServletResponse response, 
      FilterChain chain) throws IOException, ServletException {
 
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (  uri.startsWith("/contact") || uri.startsWith("/recruit") || uri.startsWith("/college/") ) {
        	res.getWriter().println(INDEX_CONTENT);
        	return;
        }
 
        chain.doFilter(request, response);
    }

    // other methods 
}