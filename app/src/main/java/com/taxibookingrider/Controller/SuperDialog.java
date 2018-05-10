package com.taxibookingrider.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.taxibookingrider.Firebase.MyFirebaseMessagingService;

/**
 * @author Sherif
 * <p>
 * Use this class to create Dialogs
 * <p>
 * Add as much dialogs as you want
 * <p>
 * Finally call SuperDialog.createDialog to create a dialog from anywhere
 */
public class SuperDialog extends Activity implements DialogInterface.OnCancelListener {

    //Edit these : Add as much dialogs as you want
    public final static int DIALOG_1 = 0;
    public final static int DIALOG_2 = 1;
    public final static int DIALOG_3 = 2;
    public final static int DIALOG_4 = 3;
    public final static int DIALOG_ERROR = 4;

    //Now edit this function
    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        switch (id) {
            case DIALOG_1:
                //create dialog1
                dialog = null;
                break;
            case DIALOG_2:
                //create dialog2
                dialog = null;
                break;
            case DIALOG_3:
                //create dialog3
                dialog = null;
                break;
            case DIALOG_4:
                //create dialog4
                dialog = null;
                break;
            case DIALOG_ERROR:
                //create dialog
                dialog = new AlertDialog.Builder(this).setMessage(MyFirebaseMessagingService.message).create();
                break;
            default:
                //create a default dialog
                dialog = null;
        }
        dialog.setOnCancelListener(this);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*showDialog(this.getIntent().getExtras().getInt("dialog"));*/


        if (MyFirebaseMessagingService.ntype.equals("d-trip-accept")) {
            DialogBox.setAccepted(SuperDialog.this, MyFirebaseMessagingService.message);
        } else if (MyFirebaseMessagingService.ntype.equals("d-trip-reject")) {
            DialogBox.setRejected(SuperDialog.this, MyFirebaseMessagingService.message, MyFirebaseMessagingService.driverid, MyFirebaseMessagingService.ctype, MyFirebaseMessagingService.tripid);
        } else if (MyFirebaseMessagingService.ntype.equals("d-start-trip")) {
            DialogBox.setStartTrip(SuperDialog.this, MyFirebaseMessagingService.message);
        } else if (MyFirebaseMessagingService.ntype.equals("d-end-trip")) {
            DialogBox.setEndTrip(SuperDialog.this, MyFirebaseMessagingService.message);
        } else if (MyFirebaseMessagingService.ntype.equals("a-global-notification")) {
            DialogBox.setPayby(SuperDialog.this, MyFirebaseMessagingService.message);
        } else if (MyFirebaseMessagingService.ntype.equals("c-device-change")) {
            DialogBox.setPaybyRegistor(SuperDialog.this, MyFirebaseMessagingService.message);
        }
    }

    @Override
    public void onCancel(DialogInterface arg0) {
        // THIS IS VERY IMPORTANT TO REMOVE THE ACTIVITY WHEN THE DIALOG IS DISMISSED
        // IF NOT ADDED USER SCREEN WILL NOT RECEIVE ANY EVENTS BEFORE USER PRESSES BACK
        finish();
    }

    /**
     * @param dialog  The dialog you want to show
     * @param context The context from which you are calling
     *                <p>
     *                this function will create a global dialog for you
     *                the dialog will appear no matter which activity or screen is showing
     * @author Sherif
     */
    public static void createDialog(int dialog, Context context, String msg) {
        Intent myIntent = new Intent(context, SuperDialog.class);
        Bundle bundle = new Bundle();
        bundle.putInt("dialog", dialog);
        bundle.putString("msg", msg);
        myIntent.putExtras(bundle);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }
}