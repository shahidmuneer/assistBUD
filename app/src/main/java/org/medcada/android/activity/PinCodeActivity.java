package org.medcada.android.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.nightonke.blurlockview.BlurLockView;
import com.nightonke.blurlockview.Password;

import org.medcada.android.R;
import org.medcada.android.object.ProfileDataBean;
import org.medcada.android.tools.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PinCodeActivity extends AppCompatActivity implements  BlurLockView.OnPasswordInputListener{

    @BindView(R.id.blurlockview)
    BlurLockView blurLockView;
    Preferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pin_code);
        ButterKnife.bind(this);
        prefs = new Preferences(this);
        ProfileDataBean bean= prefs.getProfiledata();
        blurLockView.setCorrectPassword(bean.getPinCode());

        blurLockView.setTitle(bean.getFirstName()!=null?bean.getFirstName():"");
        blurLockView.setType(getPasswordType(), false);
        blurLockView.setOnPasswordInputListener(this);
        blurLockView.setBackgroundColor(Color.parseColor("#333333"));
        blurLockView.setLeftButton("");
        blurLockView.setRightButton("");
    }

    private Password getPasswordType() {
        if ("PASSWORD_NUMBER".equals(getIntent().getStringExtra("PASSWORD_TYPE")))
            return Password.NUMBER;
        else if ("PASSWORD_NUMBER".equals(getIntent().getStringExtra("PASSWORD_TYPE")))
            return Password.TEXT;
        return Password.NUMBER;
    }
    @Override
    public void correct(String inputPassword) {
        Intent intent = new Intent(this,ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void incorrect(String inputPassword) {
        Toast.makeText(this,
                "False Pin Code",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void input(String inputPassword) {

    }
}
