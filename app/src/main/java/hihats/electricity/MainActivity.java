package hihats.electricity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.parse.*;
public class MainActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user is logged in or not
        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // If user is not logged in send to Log in class
            Intent intent = new Intent(MainActivity.this, LogInAndSignUpActivity.class);
            startActivity(intent);
            finish();
        } else {
            // If user is logged in, get username
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(MainActivity.this, Home.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(MainActivity.this,
                        LogInAndSignUpActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}