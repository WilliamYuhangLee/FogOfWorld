package li.yuhang.fogofworld.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import li.yuhang.fogofworld.server.dto.SearchResultDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@Component
public class SearchServiceImpl implements SearchService {

    @Value("${spring.google.api-key}")
    private String API_key;

    @Override
    public boolean isValidLocation(double longitude, double latitude) {
        if (longitude > 180 || longitude < -180 || latitude > 90 || latitude < -90)
            return false;
        return true;
    }

    @Override
    public List<SearchResultDto> getLocations(double longitude, double latitude, double radius) throws JsonProcessingException {
        ArrayList<SearchResultDto> result_list = new ArrayList<SearchResultDto>();
        ArrayList<Double> lat_long_list = new ArrayList<Double>();
        final String uri = String.format("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%f,%f&radius=%f&type=point_of_interest&key=%s", latitude, longitude, radius, API_key);
        RestTemplate restTemplate = new RestTemplate();
        String responseString = restTemplate.getForObject(uri, String.class);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseString);

        JsonNode locations_list = rootNode.path("results");
        Iterator<JsonNode> locations = locations_list.elements();
        while (locations.hasNext()) {
            JsonNode location = locations.next();
            SearchResultDto search_result_entry = new SearchResultDto();
            search_result_entry.setName(location.path("name").asText());
            search_result_entry.setPriceLevel(location.path("price_level").asInt());
            search_result_entry.setAddress(location.path("vicinity").asText());
            search_result_entry.setRating(location.path("rating").asDouble());
            double result_lng = location.path("geometry").path("location").path("lng").asDouble();
            double result_lat = location.path("geometry").path("location").path("lat").asDouble();
            search_result_entry.setLng(result_lng);
            search_result_entry.setLat(result_lat);
            lat_long_list.add(result_lat); // always add latitude first, then longitude
            lat_long_list.add(result_lng);
            result_list.add(search_result_entry);
        }
        //calculate distances in one API call
        List<Double> distance_list = get_distance_list(latitude, longitude, lat_long_list);
        for (int counter = 0; counter < result_list.size(); counter++) {
            result_list.get(counter).setDistance(distance_list.get(counter));
        }

        // String response_json = mapper.writeValueAsString(result_list);
        return result_list;
    }

    public List<Double> get_distance_list(double origin_lat, double origin_long, ArrayList<Double> lat_long_list) throws JsonProcessingException {
        String lat_long_string = ""; //put the lat,long into the required format for api: lat, long|
        ArrayList<Double> distance_list = new ArrayList<Double>();
        for (int counter = 0; counter < lat_long_list.size(); counter++) {
            if (counter % 2 == 0) {
                lat_long_string += lat_long_list.get(counter) + ",";
            } else {
                lat_long_string += lat_long_list.get(counter) + "|";
            }
        }
        final String uri = String.format("https://maps.googleapis.com/maps/api/distancematrix/json?origins=%f,%f&destinations=%s&key=%s", origin_lat, origin_long, lat_long_string, API_key);

        RestTemplate restTemplate = new RestTemplate();
        String responseString = restTemplate.getForObject(uri, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(responseString);
        List<JsonNode> distanceNodes = rootNode.path("rows").findValues("distance");
        for (JsonNode distanceNode : distanceNodes) {
            distance_list.add(distanceNode.path("value").asDouble());
        }
        return distance_list;
    }


}
