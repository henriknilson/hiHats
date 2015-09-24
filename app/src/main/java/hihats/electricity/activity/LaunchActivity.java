package hihats.electricity.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.parse.*;
public class LaunchActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "w5w8u7YeyDApblFiC9XPn509REPNdFIv1SleClrR", "IxqGzyFnJUENwSpNl4vRajZaW9gPPgPdK3cArFse");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

        // Check if user is logged in or not
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // If user is not logged in send to Log in class
            Intent intent = new Intent(LaunchActivity.this, LogInAndSignUpActivity.class);
            startActivity(intent);
            finish();
        } else {
            // If user is logged in, get username
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(LaunchActivity.this,
                        LogInAndSignUpActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}