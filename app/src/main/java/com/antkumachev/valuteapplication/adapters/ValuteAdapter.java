package com.antkumachev.valuteapplication.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.antkumachev.valuteapplication.R;
import com.antkumachev.valuteapplication.models.Valute;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class ValuteAdapter extends BaseAdapter {

    private final ArrayList<Valute> currencies;
    private final Context context;
    private final Random random;

    public ValuteAdapter(ArrayList<Valute> currencies, Context context) {
        this.currencies = currencies;
        this.context = context;
        this.random = new Random();
    }

    @Override
    public int getCount() {
        return currencies.size();
    }

    @Override
    public Object getItem(int position) {
        return currencies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return currencies.get(position).getCode().hashCode();
    }

    private static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private View fillView(View currency,
                             Drawable currencyFlag,
                             String currencyCode,
                             String currencyName,
                             float currencyBuyValue,
                             float currencySellValue,
                             boolean buyTrend,
                             boolean sellTrend) {
        ImageView flagImage = currency.findViewById(R.id.currency_image);
        flagImage.setImageDrawable(currencyFlag);

        TextView currencyCodeView = currency.findViewById(R.id.currency_code);
        currencyCodeView.setText(currencyCode);

        TextView currencyNameView = currency.findViewById(R.id.currency_name);
        currencyNameView.setText(currencyName);

        TextView currencyBuyView = currency.findViewById(R.id.buy_value);
        currencyBuyView.setText(String.format("%.2f", currencyBuyValue));

        TextView currencySellView = currency.findViewById(R.id.sell_value);
        currencySellView.setText(String.format("%.2f", currencySellValue));

        ImageView currencyBuyIndView = currency.findViewById(R.id.buy_value_indicator);
        currencyBuyIndView.setImageResource(buyTrend ? R.drawable.arrow_up : R.drawable.arrow_down);

        ImageView currencySellIndView = currency.findViewById(R.id.sell_value_indicator);
        currencySellIndView.setImageResource(sellTrend ? R.drawable.arrow_up : R.drawable.arrow_down);

        return currency;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_currency, parent, false);
        }

        Valute valute = currencies.get(position);
        convertView = fillView(convertView,
                valute.getFlag(),
                valute.getCode(),
                valute.getName(),
                valute.getPrice() * 0.9f,
                valute.getPrice() * 1.1f,
                random.nextBoolean(),
                random.nextBoolean());

        return convertView;
    }
}
