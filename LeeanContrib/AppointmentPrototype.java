package LeeanContrib;

public class AppointmentPrototype {
    public class AppointmentService {

        private AppointmentRepository repo;

        public AppointmentService(AppointmentRepository repo) {
            this.repo = repo;
        }

        public boolean bookAppointment(int lawyerId, int clientId, LocalDate date, String timeSlot, String caseType) {

            // Step 1: Check for slot conflict
            boolean exists = repo.existsAppointment(lawyerId, date, timeSlot);

            if (exists) {
                return false; // Slot already taken
            }

            // Step 2: Create new appointment
            Appointment a = new Appointment();
            a.setLawyerId(lawyerId);
            a.setClientId(clientId);
            a.setDate(date);
            a.setTimeSlot(timeSlot);
            a.setCaseType(caseType);
            a.setStatus("PENDING");

            repo.save(a);
            return true;
        }
    }

}
