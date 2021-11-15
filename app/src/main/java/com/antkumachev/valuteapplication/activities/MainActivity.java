package com.antkumachev.valuteapplication.activities;

import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.antkumachev.valuteapplication.R;
import com.antkumachev.valuteapplication.adapters.ValuteAdapter;
import com.antkumachev.valuteapplication.models.Valute;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    XmlPullParser prepareXpp() throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        String xml = getString(R.string.valutesXml);
        parser.setInput(new StringReader(xml));
        return parser;
    }

    private enum PullType {
        Code,
        Name,
        Value,
        None
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Valute> valutes = new ArrayList<>();
        Valute valute = new Valute();
        PullType pullType = PullType.None;

        try {
            XmlPullParser xpp = prepareXpp();
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
                                valute.setPrice(Float.parseFloat(xpp.getText()));
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

        } catch (Throwable ignored) {

        }

        ValuteAdapter va = new ValuteAdapter(valutes, this);
        ListView lw = findViewById(R.id.currency_list);
        lw.setAdapter(va);
    }
}
