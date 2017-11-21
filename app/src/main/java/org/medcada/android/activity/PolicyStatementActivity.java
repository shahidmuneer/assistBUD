package org.medcada.android.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import org.medcada.android.R;
import org.medcada.android.tools.Preferences;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PolicyStatementActivity extends AppCompatActivity {

    @BindView(R.id.tv_policy)
    TextView tvPolicy;
    @BindView(R.id.tv_policy_title)
    TextView tvPolicyTitle;
    @BindView(R.id.btn_accept)
    Button btnAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_statement);
        ButterKnife.bind(this);
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/lato.ttf");
        tvPolicy.setTypeface(tf);
        tvPolicyTitle.setTypeface(tf);
        String policy_title = "<u><b>Policy Statement</b></u>";

        String policy = "<b>AssistBUD</b> helps manage your daily tasks,<br>but you" +
                "should not rely on <b>AssistBUD</b> alone to manager" +
                "your tasks including medications/reminders.<br>The " +
                "use of <b>AssistBUD</b> may not be appropriate in all " +
                "circumstances.<br><b>AssistBUD</b> does not provide " +
                "health or medical advice.<br>" +
                "By tapping “<b>I Accept</b>” below, you are agreeing to " +
                "our Terms of Service at:<br>" +
                "<u><i>assistBUD.com/terms</i></u>";
        tvPolicy.setText(Html.fromHtml(policy));
        tvPolicyTitle.setText(Html.fromHtml(policy_title));
        btnAccept.setTypeface(tf);
    }

    @OnClick(R.id.btn_accept)
    public void onViewClicked() {
        startActivity(new Intent(this,SplashActivity.class));
        new Preferences(this).setIsPolicyAccepted(true);
        finish();
    }
}
