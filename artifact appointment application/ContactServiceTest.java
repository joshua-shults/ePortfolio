package appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContactServiceTest {
	private ContactService contactService;
	
	@BeforeEach
	void setUp() {
		contactService = new ContactService();
	}
	
	@Test
	void testAddContact() {
		Contact contact = new Contact("12345", "Joshua", "Shults", "3865693268", "19 Wendlin Lane");
		contactService.addContact(contact);
		assertThrows(IllegalArgumentException.class, () -> contactService.addContact(contact));
	}
	@Test
	void testDeleteContact() {
		Contact contact = new Contact("12345", "Joshua", "Shults", "3865693268", "19 Wendlin Lane");
		contactService.addContact(contact);
		contactService.deleteContact("12345");
		assertThrows(IllegalArgumentException.class, () -> contactService.deleteContact("12345"));
	}
	@Test
	void testUpdateFirstName() {
		Contact contact = new Contact("12345", "Joshua", "Shults", "3865693268", "19 Wendlin Lane");
		contactService.addContact(contact);
		contactService.updateFirstname("12345", "Joshy");
		assertEquals("Joshy", contact.getFirstName());
		
	}
	@Test
	void testUpdateLastName() {
		Contact contact = new Contact("12345", "Joshua", "Shults", "3865693268", "19 Wendlin Lane");
		contactService.addContact(contact);
		contactService.updateLastName("12345", "Schultz");
		assertEquals("Schultz", contact.getLastName());
		
	}
	@Test
	void testUpdatePhone() {
		Contact contact = new Contact("12345", "Joshua", "Shults", "3865693268", "19 Wendlin Lane");
		contactService.addContact(contact);
		contactService.updatePhone("12345", "3865693222");
		assertEquals("3865693222", contact.getPhone());
	}
	@Test
	void testUpdateAddress() {
		Contact contact = new Contact("12345", "Joshua", "Shults", "3865693268", "19 Wendlin Lane");
		contactService.addContact(contact);
		contactService.updateAddress("12345", "That Way");
		assertEquals("That Way", contact.getAddress());
	}
}
	
        
