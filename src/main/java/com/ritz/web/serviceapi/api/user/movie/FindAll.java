package com.ritz.web.serviceapi.api.user.movie;

import com.ritz.web.serviceapi.frame.annotation.Api;
import com.ritz.web.serviceapi.frame.core.AbstractApiHandler;
import com.ritz.web.serviceapi.frame.ex.ApiException;
import com.ritz.web.serviceapi.frame.http.RequestModel;
import com.ritz.web.serviceapi.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(value = "user.movie.findAll", needLogin = false)
public class FindAll extends AbstractApiHandler {

    @Autowired
    private MovieService movieService;

    @Override
    public void handle(RequestModel requestModel, Map<String, Object> result,
                       HttpServletRequest request, HttpServletResponse response,
                       Integer userId) throws ApiException {
        result.put("movie",movieService.findById(1));
       /* try {
            Pages pages = movieService.fullSearch(FullSearchQuery.builder()
                    .entity(Movie.class).fields("summary").value("的").build());
            result.put("pages", pages);
        } catch (Exception e) {

        }*/
    }
}
