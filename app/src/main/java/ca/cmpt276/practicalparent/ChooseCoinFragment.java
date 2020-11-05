package ca.cmpt276.practicalparent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ChooseCoinFragment extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create View
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.choose_coin_layout, null);

        // Create button listeners
        Button headsButton = v.findViewById(R.id.buttonHeads);
        Button tailsButton = v.findViewById(R.id.buttonTails);

        headsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "Child x chose HEADS");
                //TODO: add child name and choice to sharedPreferences
                dismiss();
            }
        });
        tailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG", "Child x chose TAILS");
                //TODO: add child name and choice to sharedPreferences
                dismiss();
            }
        });

        // Build the alert dialog

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .create();
    }

}
