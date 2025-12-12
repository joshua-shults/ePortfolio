package appointment;

//import hash maps to store appointments
import java.util.HashMap;
import java.util.Map;

public class AppointmentService {
	//initialize the map that will store the appointments
	private final Map<String, Appointment> appointments = new HashMap<>();
	
	//method to add appointment to the map
	public void addAppointment(Appointment appointment) {
		if (appointments.containsKey(appointment.getAppointmentID())) {
			//throw an exception if a duplicate ID is attempted to be entered into the map
			throw new IllegalArgumentException("An appointment with that ID already exists");
		}
		appointments.put(appointment.getAppointmentID(), appointment);
	}
	//method to delete an appointment
	public void deleteAppointment(String appointmentID) {
		if (!appointments.containsKey(appointmentID)) {
			throw new IllegalArgumentException("That appointment ID does not exist");
		}
		appointments.remove(appointmentID);
	}
	public Appointment getAppointment(String appointmentID) {
		return appointments.get(appointmentID);
	}

}
