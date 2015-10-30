package hihats.electricity.fragment;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import hihats.electricity.R;

/**
 * This fragment is shown after you complete a ride and displays statistics over the ride you
 * just finished.
 */
public class SuccessFragment extends DialogFragment {

    private Button dismissButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullscreenDialog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Dialog d = getDialog();
        if (d != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            d.getWindow().setLayout(width, height);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success, container, false);

        // Create the "Find My Bus" button and set its properties
        dismissButton = (Button) view.findViewById(R.id.dismiss_button);
        dismissButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                dismiss();
            }
        });

        // Set all the labels to the value from the ride
        TextView points = (TextView)view.findViewById(R.id.success_points);
        points.setText(getArguments().getInt("Points") + " " + getString(R.string.success_green_points));
        TextView stops = (TextView)view.findViewById(R.id.success_stops);
        stops.setText(getArguments().getString("StopFrom") + " " + getString(R.string.to) + " " + getArguments().getString("StopToo"));
        long millis = getArguments().getLong("Time");
        TextView time = (TextView)view.findViewById(R.id.success_time);
        time.setText(String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
        ));
        return view;
    }
}
