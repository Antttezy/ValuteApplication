package com.antkumachev.valuteapplication.activities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.antkumachev.valuteapplication.R;
import com.antkumachev.valuteapplication.adapters.ValuteAdapter;
import com.antkumachev.valuteapplication.models.Valute;
import com.antkumachev.valuteapplication.util.DownloadTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    XmlPullParser prepareXpp(String xml) throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(xml));
        return parser;
    }

    private enum PullType {
        Code,
        Name,
        Value,
        None
    }

    protected void parseXmlToDisplayData(String xml){
        ArrayList<Valute> valutes = new ArrayList<>();
        Valute valute = new Valute();
        PullType pullType = PullType.None;

        try {
            xml = xml.replace("\"", "'");
            XmlPullParser xpp = prepareXpp(xml);
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case (XmlPullParser.START_TAG):
                        String name = xpp.getName();
                        switch (name) {
                            case "Valute":
                                valute = new Valute();
                                pullType = PullType.None;
                                break;
                            case "CharCode":
                                pullType = PullType.Code;
                                break;
                            case "Name":
                                pullType = PullType.Name;
                                break;
                            case "Value":
                                pullType = PullType.Value;
                                break;
                        }
                        break;

                    case(XmlPullParser.TEXT):
                        switch (pullType) {
                            case Code:
                                String code = xpp.getText();
                                valute.setCode(code);

                                try {

                                    if (Objects.equals(code, "TRY")) {
                                        code = "TRL";
                                    }

                                    String resCode = MessageFormat.format("{0}:drawable/{1}", getPackageName(), code.toLowerCase(Locale.ROOT));
                                    int flagId = getResources().getIdentifier(resCode, null, null);

                                    valute.setFlag(ResourcesCompat.getDrawable(getResources(),flagId, null));
                                } catch (Throwable ignored) {

                                }
                                pullType = PullType.None;
                                break;
                            case Name:
                                valute.setName(xpp.getText());
                                pullType = PullType.None;
                                break;
                            case Value:
                                valute.setPrice(Float.parseFloat(xpp.getText().replace(",", ".")));
                                pullType = PullType.None;
                                break;

                            default:
                                break;
                        }

                    case(XmlPullParser.END_TAG):
                        String endName = xpp.getName();
                        if (endName != null && endName.compareTo("Valute") == 0){
                            valutes.add(valute);
                        }
                        break;

                    default:
                        break;
                }

                eventType = xpp.next();
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        ValuteAdapter va = new ValuteAdapter(valutes, this);
        ListView lw = findViewById(R.id.currency_list);
        lw.setAdapter(va);
    }

    private final String url = "https://www.cbr.ru/scripts/XML_daily.asp?date_req=?day/?month/?year";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String xml = "";
        GregorianCalendar calendar = new GregorianCalendar();
        DownloadTask task = new DownloadTask();
        String taskUrl = url
                .replace("?day", String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)))
                .replace("?month", String.valueOf(calendar.get(Calendar.MONTH)))
                .replace("?year", String.valueOf(calendar.get(Calendar.YEAR)));

        task.execute(taskUrl);

        try {
            xml = task.get(15, TimeUnit.SECONDS);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        parseXmlToDisplayData(xml);
    }
}
