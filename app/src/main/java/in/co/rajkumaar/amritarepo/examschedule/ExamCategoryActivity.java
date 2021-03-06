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

package in.co.rajkumaar.amritarepo.examschedule;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Random;

import in.co.rajkumaar.amritarepo.R;
import in.co.rajkumaar.amritarepo.helpers.Utils;

public class ExamCategoryActivity extends AppCompatActivity {

    String url_exams;
    ArrayList<String> headings, texts, links;
    ListView listView;
    ArrayAdapter<String> scheduleBlockArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_schedule);
        Utils.showSmallAd(this, (LinearLayout) findViewById(R.id.banner_container));
        url_exams = getResources().getString(R.string.url_exams);
        headings = new ArrayList<>();
        texts = new ArrayList<>();
        links = new ArrayList<>();
        listView = findViewById(R.id.list);
        String quote = getResources().getStringArray(R.array.quotes)[new Random().nextInt(getResources().getStringArray(R.array.quotes).length)];
        ((TextView) findViewById(R.id.quote)).setText(quote);
        new retrieveSchedule().execute();
    }


    @SuppressLint("StaticFieldLeak")
    class retrieveSchedule extends AsyncTask<Void, Void, Void> {
        Document document = null;
        Elements titles, ul_lists;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                document = Jsoup.connect(url_exams).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            try {
                titles = document.select("div.field-items").select("p");
                ul_lists = document.select("div.field-items").select("ul");
                Log.e("ELEMENTS ", String.valueOf(titles.size()));
                for (int i = 0; i < titles.size(); ++i) {
                    String spanstyle = titles.get(i).select("span[style]").attr("style");
                    String head = titles.get(i).select("p").text().trim();
                    if (head.length() > 0)
                        headings.add(head);
                } //Log.e("TEXT SIZE",String.valueOf(scheduleBlocks.get(1).getTexts().size()));

                scheduleBlockArrayAdapter = new ArrayAdapter<>(ExamCategoryActivity.this, R.layout.custom_list_item, headings);
                listView.setAdapter(scheduleBlockArrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (Utils.isConnected(ExamCategoryActivity.this))
                            startActivity(new Intent(ExamCategoryActivity.this, ExamsListActivity.class).putExtra("block", position));
                        else
                            Snackbar.make(view, "Device not connected to Internet.", Snackbar.LENGTH_SHORT).show();
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ExamCategoryActivity.this, "Unexpected error. Please try again later", Toast.LENGTH_SHORT).show();
                finish();
            }
            findViewById(R.id.loading_indicator).setVisibility(View.GONE);
            super.onPostExecute(aVoid);
        }
    }

}
