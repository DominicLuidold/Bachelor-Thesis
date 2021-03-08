[...]

public class Sample extends Div implements HasUrlParameter<String> {

    @Override
    public void setParameter(BeforeEvent event, String param) {
        // "param" equals a String thats part of the url
        
        [...]
        
        // "queryParams" contains all query parameters
        Location location = event.getLocation();
        QueryParameters queryParams = location.getQueryParameters();
    }
}
