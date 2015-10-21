package hihats.electricity.fragment;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import hihats.electricity.R;

/**
 * Created by fredrikkindstrom on 20/10/15.
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
        return view;
    }
}
