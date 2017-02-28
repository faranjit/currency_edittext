## How to Include currency_edittext?

```gradle
compile 'com.github.faranjit:currency-edittext:1.0.1'
```

#Usage

These lines formats simply your input for default locale.

```xml
<faranjit.currency.edittext.CurrencyEditText
        android:id="@+id/edt_currency"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:inputType="numberDecimal"
        android:textColor="@android:color/black" />
```

You can choose any locale.
```xml
<faranjit.currency.edittext.CurrencyEditText
        android:id="@+id/edt_currency"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:inputType="numberDecimal"
        android:textColor="@android:color/black"
        app:locale="en_US" />
```

or

```java
final CurrencyEditText currencyEditText = (CurrencyEditText) findViewById(R.id.edt_currency);
currencyEditText.setLocale(new Locale("en", "US"));
```

`CurrencyEditText` shows currency symbol depending on locale or you can set it not to show.
```xml
<faranjit.currency.edittext.CurrencyEditText
        android:id="@+id/edt_currency"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:inputType="numberDecimal"
        android:textColor="@android:color/black"
        app:locale="en_US"
        app:showSymbol="false" />
```
or
```java
currencyEditText.showSymbol(false);
```

If you want to change grouping and monetary seperators for money symbolization you can like this.
```xml
<faranjit.currency.edittext.CurrencyEditText
        android:id="@+id/edt_currency"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:inputType="numberDecimal"
        android:textColor="@android:color/black"
        app:groupDivider="."
        app:monetaryDivider=","
        app:locale="en_US"
        app:showSymbol="true" />
```
or
```java
currencyEditText.setGroupDivider('.');
currencyEditText.setMonetaryDivider(',');
```

When set text to _123450_, this gives to output _$1.234,50_ instead of _$1,234.50_.

When you want to get double or String value of input it is enough to type these lines:
```java
double d = currencyEditText.getCurrencyDouble();
String s = currencyEditText.getCurrencyText();
```
