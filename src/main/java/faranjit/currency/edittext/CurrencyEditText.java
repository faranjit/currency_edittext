package faranjit.currency.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by bulent.turkmen on 8/9/2016.
 */
public class CurrencyEditText extends EditText {
    private char mGroupDivider;
    private char mMonetaryDivider;
    private String mLocale = "";
    private boolean mShowSymbol;

    private char groupDivider;
    private char monetaryDivider;

    private Locale locale;
    private DecimalFormat numberFormat;

    private int fractionDigit;
    private String currencySymbol;

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.currencyEditText, 0, 0);

        try {
            if (a.getString(R.styleable.currencyEditText_groupDivider) != null) {
                this.mGroupDivider = a.getString(R.styleable.currencyEditText_groupDivider).charAt(0);
                this.groupDivider = mGroupDivider;
            }

            if (a.getString(R.styleable.currencyEditText_monetaryDivider) != null) {
                this.mMonetaryDivider = a.getString(R.styleable.currencyEditText_monetaryDivider).charAt(0);
                this.monetaryDivider = mMonetaryDivider;
            }

            if (a.getString(R.styleable.currencyEditText_locale) == null)
                this.locale = getDefaultLocale();
            else this.mLocale = a.getString(R.styleable.currencyEditText_locale);

            if (a.getString(R.styleable.currencyEditText_showSymbol) != null)
                this.mShowSymbol = a.getBoolean(R.styleable.currencyEditText_showSymbol, false);

            if (mLocale.equals("")) {
                locale = getDefaultLocale();
            } else {
                if (mLocale.contains("-"))
                    mLocale = mLocale.replace("-", "_");

                String[] l = mLocale.split("_");
                if (l.length > 1) {
                    locale = new Locale(l[0], l[1]);
                } else {
                    locale = new Locale("", mLocale);
                }
            }

            initSettings();
        } finally {
            a.recycle();
        }

        this.addTextChangedListener(onTextChangeListener);
    }

    private void initSettings() {
        boolean success = false;
        while (!success) {
            try {
                fractionDigit = Currency.getInstance(locale).getDefaultFractionDigits();

                DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
                if (mGroupDivider > 0)
                    symbols.setGroupingSeparator(mGroupDivider);
                groupDivider = symbols.getGroupingSeparator();

                if (mMonetaryDivider > 0)
                    symbols.setMonetaryDecimalSeparator(mMonetaryDivider);
                monetaryDivider = symbols.getMonetaryDecimalSeparator();

                currencySymbol = symbols.getCurrencySymbol();

                DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
                numberFormat = new DecimalFormat(df.toPattern(), symbols);

                success = true;
            } catch (IllegalArgumentException e) {
                Log.e(getClass().getCanonicalName(), e.getMessage());
                locale = getDefaultLocale();
            }
        }
    }

    private Locale getDefaultLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return getContext().getResources().getConfiguration().getLocales().get(0);
        else
            return getContext().getResources().getConfiguration().locale;
    }

    private void resetText() {
        String s = getText().toString();
        s = s.replace(groupDivider, '\u0020').replace(monetaryDivider, '\u0020')
                .replace(".", "").replace(" ", "")
                .replace(currencySymbol, "").trim();
        try {
            initSettings();
            s = format(s);
            removeTextChangedListener(onTextChangeListener);
            setText(s);
            setSelection(s.length());
            addTextChangedListener(onTextChangeListener);
        } catch (ParseException e) {
            Log.e(getClass().getCanonicalName(), e.getMessage());
        }
    }

    private TextWatcher onTextChangeListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0)
                return;

            removeTextChangedListener(this);

            String text = s.toString();
            text = text.replace(groupDivider, '\u0020').replace(monetaryDivider, '\u0020')
                    .replace(".", "").replace(" ", "")
                    .replace(currencySymbol, "").trim();
            try {
                text = format(text);
            } catch (ParseException e) {
                Log.e(getClass().getCanonicalName(), e.getMessage());
            }

            setText(text);
            setSelection(text.length());

            addTextChangedListener(this);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private String format(String text) throws ParseException {
        if (mShowSymbol)
            return numberFormat.format(Double.parseDouble(text) / Math.pow(10, fractionDigit));
        else
            return numberFormat.format(Double.parseDouble(text) / Math.pow(10, fractionDigit)).replace(currencySymbol, "");
    }

    public char getGroupDivider() {
        return groupDivider;
    }

    public void setGroupDivider(char groupDivider) {
        this.mGroupDivider = groupDivider;
        resetText();
    }

    public char getMonetaryDivider() {
        return monetaryDivider;
    }

    public void setMonetaryDivider(char monetaryDivider) {
        this.mMonetaryDivider = monetaryDivider;
        resetText();
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
        resetText();
    }

    public boolean showSymbol() {
        return this.mShowSymbol;
    }

    public void showSymbol(boolean showSymbol) {
        this.mShowSymbol = showSymbol;
        resetText();
    }
}
