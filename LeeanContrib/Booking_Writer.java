package LeeanContrib;

public boolean bookAppointment(int lawyerId, String name, String date,
                               String start, String end, String caseType) throws Exception {

    AppointmentSync.rw_mutex.acquire();  // writer entry

    // Check for conflict
    boolean taken = dao.isSlotTaken(lawyerId, date, start, end);
    if (taken) {
        AppointmentSync.rw_mutex.release();
        return false;
    }

    // Save appointment
    dao.saveAppointment(lawyerId, name, date, start, end, caseType);

    AppointmentSync.rw_mutex.release();  // writer exit
    return true;
}
