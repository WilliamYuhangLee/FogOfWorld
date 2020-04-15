package li.yuhang.fogofworld.server.controller.v1;

import com.fasterxml.jackson.core.JsonProcessingException;
import li.yuhang.fogofworld.server.util.Response;
import li.yuhang.fogofworld.server.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/search")
public class SearchController {
    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @RequestMapping(value = "/locations", method = RequestMethod.GET)
    public Response getLocations(@RequestParam(value = "long", required = true) double longitude, @RequestParam(value = "lat", required = true) double latitude,
                                 @RequestParam(value = "radius", defaultValue = "1500") double radius) throws JsonProcessingException {
        if (searchService.isValidLocation(longitude, latitude)) {
            return Response.withStatus(HttpStatus.OK).setPayload(searchService.getLocations(longitude, latitude, radius));
        } else {
            return Response.withStatus(HttpStatus.BAD_REQUEST)
                           .addErrorMessage("Wrong range of longitude and latitude. Please ensure that -180 <= long <= 180 and -90 <= lat <= 90");
        }
    }
}
