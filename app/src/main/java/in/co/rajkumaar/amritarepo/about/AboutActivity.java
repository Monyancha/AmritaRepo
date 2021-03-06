/*
 * MIT License
 *
 * Copyright (c) 2018  RAJKUMAR S
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package in.co.rajkumaar.amritarepo.about;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Objects;

import in.co.rajkumaar.amritarepo.R;
import in.co.rajkumaar.amritarepo.helpers.Utils;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class AboutActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    public static void showDisclaimer(Context context) {
        try {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.disclaimer);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                TextView textView = dialog.findViewById(R.id.text);
                textView.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
            }
            dialog.show();
            dialog.getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Utils.showBigAd(this, (LinearLayout) findViewById(R.id.banner_container));
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Button disclaimer = findViewById(R.id.disclaimer);
        disclaimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDisclaimer(AboutActivity.this);
            }
        });
        ((TextView) findViewById(R.id.name)).setText(Html.fromHtml("Crafted with &hearts; by <br><strong>RAJKUMAR</strong>"));
        ((ImageView) findViewById(R.id.image)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle params = new Bundle();
                params.putString("IMAGE_CLICK", "About");
                mFirebaseAnalytics.logEvent("IMAGE_CLICK", params);
                String url = "http://rajkumaar.co.in";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    @SuppressLint("PrivateResource")
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    public void ossLicenses(View view) {
        Intent intent = new Intent(this, OssLicensesMenuActivity.class);
        startActivity(intent);
    }
}
