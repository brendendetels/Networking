package latitude.serialization.test;
import latitude.serialization.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import latitude.serialization.LocationRecord;

import static org.junit.jupiter.api.Assertions.*;


/***********************************************
 *
 * Author: Brenden Detels
 * Assignment: Project 0
 * Class: CSI 4321
 *
 **********************************************/


public class ValidationExceptionTest {

//______________________ look at these links for help
    // For assistance https://stackoverflow.com/questions/871216/junit-possible-to-expect-a-wrapped-exception
    // https://blog.goyello.com/2015/10/01/different-ways-of-testing-exceptions-in-java-and-junit/
    // ExpectedException

    /**
     * Test #1 for Validation Exception Constructor
     */
    @Test
    public void testValidationExceptionConstructor(){
        ValidationException ve = new ValidationException("token", "user tried this");
        assertEquals(ve.getInvalidToken(), "token");
    }


    @Test
    @DisplayName("Trying to test an Exception")
    public void testValidationException() throws Exception{
        Throwable throwable = new Throwable();
        ValidationException validationException1, validationException2;
        {
            try {
                validationException1 = new ValidationException("Test1", "Test1 Message");
                validationException2 = new ValidationException("Test2", "Test2 Message", throwable);
                assertNotNull(validationException1);
                assertNotNull(validationException2);
                assertNotEquals(validationException1, validationException2);
                assertNotSame(validationException1, validationException2);
                assertNotSame(validationException1.getCause(), validationException2.getCause());
                assertNotEquals(validationException1.getCause(), validationException2.getCause());
                assertNotNull(validationException2.getInvalidToken());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Test
    @DisplayName("Test actual throw of ValidationException with first constructor")
    public void testActualThrowOfValidationException() throws ValidationException{
        assertThrows(ValidationException.class, () -> {
            throw new ValidationException("Test", "Drew Test");
        });
    }


    @Test
    @DisplayName("Test actual throw of ValidationException with second constructor")
    public void testActualThrowOfValidationException2() throws ValidationException {
        assertThrows(ValidationException.class, () -> {
            Throwable throwable = new Throwable();
            throw new ValidationException("Test", "Drew Test", throwable);
        });
    }




}
