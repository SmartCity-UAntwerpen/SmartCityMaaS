package be.uantwerpen.Controller;

import org.json.JSONObject;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


/**
 * Created by Revil on 20/04/2017.
 */
public class orderInput {

    @NotNull
    @Size(min=3, max=30)
    private String firstName;

    @NotNull
    @Size(min=3, max=30)
    private String lastName;

    //departure. will be one of the departure options definded in the reception page
    private String departure;

    //destination. will be one of the destination options definded in the reception page
    private String destination;

    @NotNull
    @Min(1)
    private Integer passengers;

    // getters
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getDeparture() {
        return this.departure;
    }
    public String getDestination() {
        return this.destination;
    }
    public Integer getPassengers() {
        return passengers;
    }

    // setters
    public void setFirstName(String name) {
        this.firstName = name;
    }
    public void setLastName(String name) {
        this.lastName = name;
    }
    public void setDeparture(String name) {
        this.departure = name;
    }
    public void setDestination(String name) {
        this.destination = name;
    }
    public void setPassengers(Integer passengers) {
        this.passengers = passengers;
    }

    // debugging code:
    public String toString() {
        return "Person(First name: " + this.firstName + ", last name: " + this.lastName + ", departure: " + this.departure + ", destination: " + this.destination + ", amount of passengers: " + this.passengers + ")";
    }

    /*
te gebruiken code voor maken van actie. echter gaat aan server kant gebruiken. voorlopig bijhouden tot deze functionaliteit in orde is
    <script>
    $( "action" ).submit(function( event ) {
        if th:errors
        if ( $( "input:first" ).val() === "correct" ) {
            $( "span" ).text( "Validated..." ).show();
            return;
        }

        $( "span" ).text( "Not valid!" ).show().fadeOut( 1000 );
        event.preventDefault();
    });
</script>


    @Produces("application/json")
    public Response makeJson ()
    {
        JSONObject data = new JSONObject();
        data.put("firstName", this.firstName);
        data.put("lastName", this.lastName);
        data.put("departure", this.departure);
        data.put("destination", this.destination);
        data.put("passengers", this.passengers);

        String result = data.toString();
        return Response.status(200).entity(result).build();
    }
    // TODO: method maken om alles om te zetten naar json
    */
}
