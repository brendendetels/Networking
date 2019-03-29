package latitude.serialization.test;
import latitude.serialization.LatitudeError;
import latitude.serialization.LatitudeProtocols;
import latitude.serialization.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/***********************************************
 *
 * Author: Brenden Detels and Andrew Bury
 * Assignment: Project 0
 * Class: CSI 4321
 *
 **********************************************/

public class LatitudeErrorTest {



    @Test
    @DisplayName("Test setErrorMessage")
    public void testSetErrorMessage() throws ValidationException {
        LatitudeError latitudeError = new LatitudeError(8, "meow");
        assertThrows(ValidationException.class, () ->{
            latitudeError.setMapId(-1);
        });
        assertThrows(ValidationException.class, () ->{
            latitudeError.setMapId(LatitudeProtocols.mapIdProtocols[1]);
        });
    }

    @Test
    @DisplayName("Test setErrorMessage")
    public void setErrorMessage() throws ValidationException{
        LatitudeError le = new LatitudeError( 8, "wow");
        assertThrows(ValidationException.class, () ->{
            le.setErrorMessage("¡¢µ¶");
        });
    }


    @Test
    @DisplayName("Test setErrorMessage")
    public void setErrorMessage2() throws ValidationException{

        assertThrows(ValidationException.class, () ->{
            LatitudeError le = new LatitudeError( 8, "wow");
            le.setErrorMessage(null);
        });
    }

    @Test
    @DisplayName("Test Constructor")
    public void testLatitudeErrorConstructor() throws ValidationException{
        LatitudeError latitudeError = new LatitudeError(8, "woof");
        assertTrue(latitudeError.getErrorMessage().matches("\\A\\p{ASCII}*\\z"));
        assertNotNull(latitudeError);
    }



}
