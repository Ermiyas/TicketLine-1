package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import at.ac.tuwien.sepm.groupphase.backend.entity.mapper.news.NewsMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.NewsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/news")
@Api(value = "news")
public class NewsEndpoint {

    private final NewsService newsService;
    private final NewsMapper newsMapper;

    public NewsEndpoint(NewsService newsService, NewsMapper newsMapper) {
        this.newsService = newsService;
        this.newsMapper = newsMapper;
    }


    @RequestMapping(method = RequestMethod.GET)
    @ApiOperation(value = "Get list of simple news entries", authorizations = {@Authorization(value = "apiKey")})
    public List<at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO> findAll() {
        return newsMapper.newsToSimpleNewsDTO(newsService.findAll());
    }


    @RequestMapping(value = "/unread", method = RequestMethod.GET)
    @ApiOperation(value = "Get list of unread News articles", authorizations = {@Authorization(value = "apiKey")})
    public List<at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDTO> findUnread(HttpServletRequest request) {
        return newsMapper.newsToSimpleNewsDTO(newsService.findUnread(request.getUserPrincipal().getName()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get detailed information about a specific news entry", authorizations = {@Authorization(value = "apiKey")})
    public at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO find(@PathVariable Long id) {
        return newsMapper.newsToDetailedNewsDTO(newsService.findOne(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation(value = "Publish a new news entry", authorizations = {@Authorization(value = "apiKey")})
    public at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO publishNews(@RequestBody at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDTO detailedNewsDTO) {
        News news = newsMapper.detailedNewsDTOToNews(detailedNewsDTO);
        news = newsService.publishNews(news);
        return newsMapper.newsToDetailedNewsDTO(news);
    }

}
