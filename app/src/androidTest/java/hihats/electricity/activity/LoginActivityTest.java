package hihats.electricity.activity;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import hihats.electricity.R;

/**
 * Created by henriknilson on 02/10/15.
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    public void testActivityExists() {
        LoginActivity activity = getActivity();
        assertNotNull(activity);
    }

    public void testLogin() {

        LoginActivity activity = getActivity();

        Parse.enableLocalDatastore(activity);
        Parse.initialize(activity, "w5w8u7YeyDApblFiC9XPn509REPNdFIv1SleClrR", "IxqGzyFnJUENwSpNl4vRajZaW9gPPgPdK3cArFse");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        final EditText userNameInput = (EditText) activity.findViewById(R.id.username);
        final EditText passwordInput = (EditText) activity.findViewById(R.id.password);

        // Fill in username
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                userNameInput.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync("henrik");

        // Fill in password
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                passwordInput.requestFocus();
            }
        });

        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync("kalle");

        // Click button login
        Button loginButton = (Button) activity.findViewById(R.id.login);
        TouchUtils.clickView(this, loginButton);

        assertEquals("henrik", ParseUser.getCurrentUser().getUsername());
    }
}