package mahsa.com.onlineresturauntbookingsystem.fragments;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

import mahsa.com.onlineresturauntbookingsystem.R;

/**
 * Created by  on 26/05/2017.
 */

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    Button mButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        mButton=(Button)getActivity().findViewById(R.id.fragment_booking_date_btn);
        mButton.setText(i+"/"+i1+"/"+i2);

    }
}
