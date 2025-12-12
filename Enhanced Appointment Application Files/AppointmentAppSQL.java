/*
 * 
 * This portion of the application needs to have an SQL server setup.
 * In the readme I have a description of how I set mine up for anyone
 * who wants to replicate my steps to inspect this application.
 * 
 * In short, I used XAMPP with default settings, if you do this the
 * code should run and update in your server. If you changed the username
 * or password in your server, you need to change it here in the DatabaseUtil
 * file.
 * 
 */



package appointment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class AppointmentAppSQL {

    private static final SimpleDateFormat DATE_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ContactDAO contactDAO = new ContactDAO();
        AppointmentDAO appointmentDAO = new AppointmentDAO();
        TaskDAO taskDAO = new TaskDAO();

        boolean running = true;
//while running create a main menu
        while (running) {
            System.out.println("\n=== Appointment / Contact / Task Manager (SQL) ===");
            System.out.println("1. Manage Contacts"); //go to contact menu
            System.out.println("2. Manage Appointments"); //go to appointments menu
            System.out.println("3. Manage Tasks"); //task menu
            System.out.println("0. Exit"); //set running to false
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleContacts(scanner, contactDAO);
                    break;
                case "2":
                    handleAppointments(scanner, contactDAO, appointmentDAO);
                    break;
                case "3":
                    handleTasks(scanner, contactDAO, taskDAO);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Try again."); //default to this
            }
        }

        scanner.close();
        System.out.println("Goodbye!");
    }

    // Contacts Menu

    private static void handleContacts(Scanner scanner, ContactDAO contactDAO) {
        boolean back = false;
        while (!back) { //while the user wants to stay in this menu:
            System.out.println("\n--- Contact Menu ---");
            System.out.println("1. Add Contact");
            System.out.println("2. View Contact");
            System.out.println("3. Update Phone");
            System.out.println("4. Update Address");
            System.out.println("5. Delete Contact");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) { //reuse prior existing methods, except add to DAO
                case "1":
                    addContact(scanner, contactDAO);
                    break;
                case "2":
                    viewContact(scanner, contactDAO);
                    break;
                case "3":
                    updateContactPhone(scanner, contactDAO);
                    break;
                case "4":
                    updateContactAddress(scanner, contactDAO);
                    break;
                case "5":
                    deleteContact(scanner, contactDAO);
                    break;
                case "0":
                    back = true; //returns to main menu
                    break;
                default:
                    System.out.println("Invalid option."); //default
            }
        }
    }

    private static void addContact(Scanner scanner, ContactDAO contactDAO) {
        try {
            System.out.print("Enter Contact ID: ");
            String id = scanner.nextLine().trim();

            if (contactDAO.exists(id)) {
                System.out.println("A contact with that ID already exists.");
                return;
            }

            System.out.print("Enter First Name: ");
            String first = scanner.nextLine().trim();

            System.out.print("Enter Last Name: ");
            String last = scanner.nextLine().trim();

            System.out.print("Enter Phone (10 digits): ");
            String phone = scanner.nextLine().trim();

            System.out.print("Enter Address: ");
            String address = scanner.nextLine().trim();

            Contact contact = new Contact(id, first, last, phone, address);
            contactDAO.addContact(contact);

            System.out.println("Contact saved to database.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error creating contact: " + ex.getMessage());
        }
    }

    private static void viewContact(Scanner scanner, ContactDAO contactDAO) {
        System.out.print("Enter Contact ID: ");
        String id = scanner.nextLine().trim();

        Contact contact = contactDAO.getContact(id);
        if (contact == null) {
            System.out.println("No contact found with that ID.");
        } else {
            System.out.println("Contact Details:");
            System.out.println("ID: " + contact.getContactId());
            System.out.println("Name: " + contact.getFirstName() + " " + contact.getLastName());
            System.out.println("Phone: " + contact.getPhone());
            System.out.println("Address: " + contact.getAddress());
        }
    }

    private static void updateContactPhone(Scanner scanner, ContactDAO contactDAO) {
        System.out.print("Enter Contact ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("Enter new phone: ");
        String phone = scanner.nextLine().trim();

        try {
            contactDAO.updatePhone(id, phone);
            System.out.println("Phone updated.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void updateContactAddress(Scanner scanner, ContactDAO contactDAO) {
        System.out.print("Enter Contact ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("Enter new address: ");
        String addr = scanner.nextLine().trim();

        try {
            contactDAO.updateAddress(id, addr);
            System.out.println("Address updated.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void deleteContact(Scanner scanner, ContactDAO contactDAO) {
        System.out.print("Enter Contact ID to delete: ");
        String id = scanner.nextLine().trim();

        try {
            contactDAO.deleteContact(id);
            System.out.println("Contact deleted (and related appointments/tasks via cascade).");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    //Appointment Menus

    private static void handleAppointments(Scanner scanner, ContactDAO contactDAO,
                                           AppointmentDAO appointmentDAO) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Appointment Menu ---");
            System.out.println("1. Add Appointment");
            System.out.println("2. View Appointment");
            System.out.println("3. Delete Appointment");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addAppointment(scanner, contactDAO, appointmentDAO);
                    break;
                case "2":
                    viewAppointment(scanner, appointmentDAO);
                    break;
                case "3":
                    deleteAppointment(scanner, appointmentDAO);
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void addAppointment(Scanner scanner, ContactDAO contactDAO,
                                       AppointmentDAO appointmentDAO) {
        try {
            System.out.print("Enter Appointment ID: ");
            String id = scanner.nextLine().trim();

            if (appointmentDAO.exists(id)) {
                System.out.println("An appointment with that ID already exists.");
                return;
            }

            System.out.print("Enter Contact ID for this appointment: ");
            String contactId = scanner.nextLine().trim();

            if (!contactDAO.exists(contactId)) {
                System.out.println("No contact found with that ID. Appointment NOT created.");
                return;
            }

            System.out.println("Enter appointment date & time in format: yyyy-MM-dd HH:mm");
            System.out.print("Example: 2027-01-31 14:30: ");
            String dateStr = scanner.nextLine().trim();

            Date date;
            try {
                date = DATE_FORMAT.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Invalid date format. Appointment NOT created.");
                return;
            }

            if (date.before(new Date())) {
                System.out.println("Appointment date cannot be in the past. NOT created.");
                return;
            }

            System.out.print("Enter description (max 50 chars): ");
            String desc = scanner.nextLine().trim();

            Appointment appointment = new Appointment(id, date, desc);
            appointmentDAO.addAppointment(appointment, contactId);

            System.out.println("Appointment created and saved to database.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error creating appointment: " + ex.getMessage());
        }
    }

    private static void viewAppointment(Scanner scanner, AppointmentDAO appointmentDAO) {
        System.out.print("Enter Appointment ID: ");
        String id = scanner.nextLine().trim();

        Appointment appointment = appointmentDAO.getAppointment(id);
        if (appointment == null) {
            System.out.println("No appointment found with that ID.");
        } else {
            System.out.println("Appointment Details:");
            System.out.println("ID: " + appointment.getAppointmentID());
            System.out.println("Date: " + DATE_FORMAT.format(appointment.getAppointmentDate()));
            System.out.println("Description: " + appointment.getDescription());
        }
    }

    private static void deleteAppointment(Scanner scanner, AppointmentDAO appointmentDAO) {
        System.out.print("Enter Appointment ID to delete: ");
        String id = scanner.nextLine().trim();

        try {
            appointmentDAO.deleteAppointment(id);
            System.out.println("Appointment deleted.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    // Taks Menu

    private static void handleTasks(Scanner scanner, ContactDAO contactDAO, TaskDAO taskDAO) {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Task Menu ---");
            System.out.println("1. Add Task");
            System.out.println("2. View Task");
            System.out.println("3. Update Task Name");
            System.out.println("4. Update Task Description");
            System.out.println("5. Delete Task");
            System.out.println("0. Back");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addTask(scanner, contactDAO, taskDAO);
                    break;
                case "2":
                    viewTask(scanner, taskDAO);
                    break;
                case "3":
                    updateTaskName(scanner, taskDAO);
                    break;
                case "4":
                    updateTaskDescription(scanner, taskDAO);
                    break;
                case "5":
                    deleteTask(scanner, taskDAO);
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private static void addTask(Scanner scanner, ContactDAO contactDAO, TaskDAO taskDAO) {
        try {
            System.out.print("Enter Task ID: ");
            String id = scanner.nextLine().trim();

            if (taskDAO.exists(id)) {
                System.out.println("A task with that ID already exists.");
                return;
            }

            System.out.print("Enter Contact ID for this task: ");
            String contactId = scanner.nextLine().trim();

            if (!contactDAO.exists(contactId)) {
                System.out.println("No contact found with that ID. Task NOT created.");
                return;
            }

            System.out.print("Enter task name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Enter description: ");
            String desc = scanner.nextLine().trim();

            Task task = new Task(id, name, desc);
            taskDAO.addTask(task, contactId);

            System.out.println("Task created and saved to database.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error creating task: " + ex.getMessage());
        }
    }

    private static void viewTask(Scanner scanner, TaskDAO taskDAO) {
        System.out.print("Enter Task ID: ");
        String id = scanner.nextLine().trim();

        Task task = taskDAO.getTask(id);
        if (task == null) {
            System.out.println("No task found with that ID.");
        } else {
            System.out.println("Task Details:");
            System.out.println("ID: " + task.getTaskId());
            System.out.println("Name: " + task.getName());
            System.out.println("Description: " + task.getDescription());
        }
    }

    private static void updateTaskName(Scanner scanner, TaskDAO taskDAO) {
        System.out.print("Enter Task ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("Enter new task name: ");
        String name = scanner.nextLine().trim();

        try {
            taskDAO.updateTaskName(id, name);
            System.out.println("Task name updated.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void updateTaskDescription(Scanner scanner, TaskDAO taskDAO) {
        System.out.print("Enter Task ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("Enter new description: ");
        String desc = scanner.nextLine().trim();

        try {
            taskDAO.updateTaskDescription(id, desc);
            System.out.println("Task description updated.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private static void deleteTask(Scanner scanner, TaskDAO taskDAO) {
        System.out.print("Enter Task ID to delete: ");
        String id = scanner.nextLine().trim();

        try {
            taskDAO.deleteTask(id);
            System.out.println("Task deleted.");
        } catch (IllegalArgumentException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
}
