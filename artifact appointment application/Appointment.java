package appointment;
//import the util date for the appointment class
import java.util.Date;

public class Appointment {
	private final String appointmentID;
	private final Date appointmentDate;
	private final String description;
	
	//constructor
	public Appointment(String appointmentID, Date appointmentDate, String description) {
		//make sure appointment ID is not null or above 10 characters in length
		if (appointmentID == null || appointmentID.length() > 10) {
			throw new IllegalArgumentException("Invalid appointment ID");
		}
		//appointment date can't be null and cannot be a past date
		if (appointmentDate == null || appointmentDate.before(new Date())) {
			throw new IllegalArgumentException("Invalid appointment date");
		}
		//description can't be null or above 50 characters in length
		if (description == null || description.length() > 50) {
			throw new IllegalArgumentException("Invalid Description");
		}
		this.appointmentID = appointmentID;
		this.appointmentDate = appointmentDate;
		this.description = description;
	}
	
	//methods for the Appointment Class
	public String getAppointmentID() {
		return appointmentID;
	}
	public Date getAppointmentDate() {
		return appointmentDate;
	}
	public String getDescription() {
		return description;
	}
		

}
