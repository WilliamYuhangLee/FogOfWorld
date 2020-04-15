package li.yuhang.fogofworld.server.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.List;

public interface SearchService {

    /**
     * Check the validity of input longitude and latitude
     *
     * @param longitude and latitude of the input coordinate
     * @return true if it is a valid location on the earth
     */
    boolean isValidLocation(double longitude, double latitude);

    /**
     * Get the nearby locations with
     *
     * @param longitude, latitude and radius of the search
     * @return ArrayList of Search Result Dto with the required fields.
     */
    List<SearchResultDto> getLocations(double longitude, double latitude, double radius) throws JsonProcessingException;
}
