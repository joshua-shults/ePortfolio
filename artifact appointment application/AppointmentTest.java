package appointment;

//import JUNIT test and date for testing
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Date;
public class AppointmentTest {
	
	@Test
	public void testValidAppointment() {
		//make a date in the future for the test
		Date correctDate = new Date(System.currentTimeMillis() + 100000);
		//create a test appointment
		Appointment appointment = new Appointment("12345", correctDate, "Some Description");
		//assert that the appointment ID is correct
		assertEquals("12345", appointment.getAppointmentID());
		//assert that the date is correct
		assertEquals(correctDate, appointment.getAppointmentDate());
		//assert that the description is correct
		assertEquals("Some Description", appointment.getDescription());
				
	}
	@Test
	public void testInvalidAppointmentID() {
		//test if appointment accepts a null ID
		assertThrows(IllegalArgumentException.class, () ->{
			new Appointment(null, new Date(), "Some Description");
		});
		//test if appointments accept an ID with too many characters
		assertThrows(IllegalArgumentException.class, () ->{
			new Appointment("1234567890123", new Date(), "Some Description");
		});
		
	}
	@Test
	public void testInvalidDate() {
		//this date is in the past and should throw an exception
		Date wrongDate = new Date(System.currentTimeMillis() - 100000);
		assertThrows(IllegalArgumentException.class, () ->{
			new Appointment("12345", wrongDate, "Some Description");
		});
	}
	@Test
	public void testInvalidDescription() {
		//test if it accepts a null descriptoin
		assertThrows(IllegalArgumentException.class, () ->{
			new Appointment("12345", new Date(), null);
		});
		//test if it accepts a sad but true description over 50 characters
		assertThrows(IllegalArgumentException.class, () ->{
			new Appointment("12345", new Date(), "This description is incredibly long and dull, much like my home life and my love life. One of these days I well get out there and make a name for myself :)");
		});
	}

}
