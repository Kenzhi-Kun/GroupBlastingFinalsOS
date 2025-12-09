package LeeanContrib;

import java.util.concurrent.Semaphore;

public class AppointmentLayerSync {
    public static int readCount = 0;
        public static Semaphore mutex = new Semaphore(1);      // Protect readCount
        public static Semaphore rw_mutex = new Semaphore(1);   // Writers get exclusive access

    }

    //Reader-Writers yung algo since simple lang siya and commonly used sa mga queuing system