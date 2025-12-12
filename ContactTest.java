package appointment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ContactTest {

    @Test
    void testValidContactCreation() {
        Contact contact = new Contact("12345", "Joshua", "Shults", "3865693268", "19 Wendlin Lane");
        assertEquals("12345", contact.getContactId());
        assertEquals("Joshua", contact.getFirstName());
        assertEquals("Shults", contact.getLastName());
        assertEquals("3865693268", contact.getPhone());
        assertEquals("19 Wendlin Lane", contact.getAddress());
    }


    @Test
    void testInvalidContactId() {
        assertThrows(IllegalArgumentException.class, () -> new Contact(null, "Joshua", "Shults", "3865693268", "19 Wendlin Lane"));
        assertThrows(IllegalArgumentException.class, () -> new Contact("12345678901", "Joshua", "Shults", "3865693268", "19 Wendlin Lane"));
    }

    @Test
    void testInvalidFirstName() {
        assertThrows(IllegalArgumentException.class, () -> new Contact("12345", null, "Shults", "3865693268", "19 Wendlin Lane"));
        assertThrows(IllegalArgumentException.class, () -> new Contact("12345", "JoshuaJoshua", "Shults", "3865693268", "19 Wendlin Lane"));
    }

    @Test
    void testInvalidLastName() {
        assertThrows(IllegalArgumentException.class, () -> new Contact("12345", "Joshua", null, "3865693268", "19 Wendlin Lane"));
        assertThrows(IllegalArgumentException.class, () -> new Contact("12345", "Joshua", "ShultsShults", "3865693268", "19 Wendlin Lane"));
    }

    @Test
    void testInvalidPhone() {
        assertThrows(IllegalArgumentException.class, () -> new Contact("12345", "Joshua", "Shults", null, "19 Wendlin Lane"));
        assertThrows(IllegalArgumentException.class, () -> new Contact("12345", "Joshua", "Shults", "123", "19 Wendlin Lane"));
        assertThrows(IllegalArgumentException.class, () -> new Contact("12345", "Joshua", "Shults", "abcdefghij", "19 Wendlin Lane"));
    }

    @Test
    void testInvalidAddress() {
        assertThrows(IllegalArgumentException.class, () -> new Contact("12345", "Joshua", "Shults", "3865693268", null));
        assertThrows(IllegalArgumentException.class, () -> new Contact("12345", "Joshua", "Shults", "3865693268", "You picked the wrong house fool"));
    }
}