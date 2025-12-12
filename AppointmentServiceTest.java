package appointment;

//import JUNIT tests and the date util
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Date;

public class AppointmentServiceTest {
	//first test if an appointment with a unique ID can be added and deleted
	@Test
	public void testAddAndDeleteAppointment() {
		AppointmentService service = new AppointmentService();
		Date correctDate = new Date(System.currentTimeMillis() + 100000);
		Appointment appointment = new Appointment("12345", correctDate, "Some Description");
		//first assert that the appointment exists and can be retrieved via its ID
		service.addAppointment(appointment);
		assertEquals(appointment, service.getAppointment("12345"));
		//now retrieve the appointment by ID and delete it
		service.deleteAppointment("12345");
		//check if it still exists
		assertNull(service.getAppointment("12345"));
	}
	//now test if an appointment with the same ID as a previously existing ID can be added to the map
	public void testAddDuplicateAppointmentID() {
		AppointmentService service = new AppointmentService();
		Date correctDate = new Date(System.currentTimeMillis() + 100000);
		Appointment appointment1 = new Appointment("12345", correctDate, "Some Description");
		Appointment appointment2 = new Appointment("12345", correctDate, "Duplicate ID");
		
		service.addAppointment(appointment1);
		assertThrows(IllegalArgumentException.class, () -> service.addAppointment(appointment2));
	}
	//and finally test if a non existent appointment can be deleted
	@Test
	public void testDeleteNonexistentAppointment() {
		AppointmentService service = new AppointmentService();
		assertThrows(IllegalArgumentException.class, () -> service.deleteAppointment("123456"));
	}

}
