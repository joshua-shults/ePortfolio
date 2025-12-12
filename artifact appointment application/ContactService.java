package appointment;
import java.util.HashMap;
import java.util.Map;

public class ContactService {
	private final Map<String, Contact> contacts = new HashMap<>();
	
	//add a new contact to the map
	public void addContact(Contact contact) {
		if (contacts.containsKey(contact.getContactId())) {
			throw new IllegalArgumentException("Contact Id already exists");
		}
		contacts.put(contact.getContactId(), contact);
	}
	//search and delete a contact by its contactId
	public void deleteContact(String contactId) {
		if (!contacts.containsKey(contactId)) {
			throw new IllegalArgumentException("That contact ID was not found");
		}
		contacts.remove(contactId);
	}
	//update a contact's first name
	public void updateFirstname(String contactId, String firstName) {
		Contact contact = getContact(contactId);
		contact.setFirstName(firstName);
	}
	// Update last name
    public void updateLastName(String contactId, String lastName) {
        Contact contact = getContact(contactId);
        contact.setLastName(lastName);
    }
	//update a contact phone number
	public void updatePhone(String contactId, String phone) {
		Contact contact = getContact(contactId);
		contact.setPhone(phone);
	}
	//update a contact's address
	public void updateAddress(String contactId, String address) {
		Contact contact = getContact(contactId);
		contact.setAddress(address);
	}
	//search for a contact by its id
	private Contact getContact(String contactId) {
		if (!contacts.containsKey(contactId)) {
			throw new IllegalArgumentException("Contact ID was not found");
		}
		return contacts.get(contactId);
	}

}
