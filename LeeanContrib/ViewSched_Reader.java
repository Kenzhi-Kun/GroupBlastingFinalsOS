package LeeanContrib;

public void main() {

public ResultSet viewSchedule(int lawyerId, String date) throws Exception {

    // Begin Reader Section
    AppointmentSync.mutex.acquire();
    AppointmentSync.readCount++;
    if (AppointmentSync.readCount == 1)
        AppointmentSync.rw_mutex.acquire();  // First reader blocks writers
    AppointmentSync.mutex.release();

    // Read schedule
    ResultSet rs = dao.getAppointments(lawyerId, date);

    // End Reader Section
    AppointmentSync.mutex.acquire();
    AppointmentSync.readCount--;
    if (AppointmentSync.readCount == 0)
        AppointmentSync.rw_mutex.release();  // Last reader frees writers
    AppointmentSync.mutex.release();

    return rs;
}


}
