//package com.example.finalsgroupblasting;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
//
//    private List<Appointment> appointmentList;
//
//    public AppointmentAdapter(List<Appointment> appointmentList) {
//        this.appointmentList = appointmentList;
//    }
//
//    @NonNull
//    @Override
//    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item, parent, false);
//        return new AppointmentViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
//        Appointment appointment = appointmentList.get(position);
//        holder.titleTextView.setText(appointment.getTitle());
//        holder.timeTextView.setText(appointment.getTime());
//    }
//
//    @Override
//    public int getItemCount() {
//        return appointmentList.size();
//    }
//
//    public void setAppointments(List<Appointment> appointments) {
//        this.appointmentList = appointments;
//        notifyDataSetChanged();
//    }
//
//    static class AppointmentViewHolder extends RecyclerView.ViewHolder {
//        TextView titleTextView;
//        TextView timeTextView;
//
//        public AppointmentViewHolder(@NonNull View itemView) {
//            super(itemView);
//            titleTextView = itemView.findViewById(R.id.appointmentTitleTextView);
//            timeTextView = itemView.findViewById(R.id.appointmentTimeTextView);
//        }
//    }
//}
