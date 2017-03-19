package com.mystery0.hitokoto.custom;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoLocal;
import com.mystery0.tools.Logs.Logs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

@SuppressWarnings("ConstantConditions")
public class CustomMultipleActivity extends AppCompatActivity
{
    private static final String TAG = "CustomMultipleActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_multiple);

        final TextInputLayout hitokotoContent = (TextInputLayout) findViewById(R.id.text);
        Button button_ok = (Button) findViewById(R.id.button_ok);
        Button button_cancel = (Button) findViewById(R.id.button_cancel);
        button_ok.getBackground().setAlpha(0);
        button_cancel.getBackground().setAlpha(0);

        check(hitokotoContent);

        button_ok.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isFormat(hitokotoContent))
                {
                    CustomConfigure.saveToDatabase(Analysis(hitokotoContent.getEditText().getText().toString()));
                    Toast.makeText(App.getContext(), R.string.hint_save_custom_done, Toast.LENGTH_SHORT)
                            .show();
                } else
                {
                    Toast.makeText(App.getContext(), R.string.ErrorFormat, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private void check(final TextInputLayout layout)
    {
        layout.getEditText().addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (s.toString().length() == 0)
                {
                    layout.setError(getString(R.string.ErrorNull));
                } else
                {
                    layout.setError(null);
                }
            }
        });
    }

    private boolean isFormat(TextInputLayout layout)
    {
        layout.setError(layout.getEditText().getText().length() != 0 ? null : getString(R.string.ErrorNull));
        return layout.getEditText().getText().length() != 0;
    }

    private List<HitokotoLocal> Analysis(String content)
    {
        List<HitokotoLocal> list = new ArrayList<>();
        String time = (new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)).format(Calendar.getInstance().getTime());
        Scanner scanner = new Scanner(content);
        try
        {
            while (scanner.hasNext())
            {
                String[] temp = scanner.nextLine().split(" ");
                list.add(new HitokotoLocal(temp[0], temp[1], time));
            }
        } catch (ArrayIndexOutOfBoundsException e)
        {
            Logs.e(TAG, "Analysis: " + e.getMessage());
            Toast.makeText(App.getContext(), R.string.ErrorData, Toast.LENGTH_LONG)
                    .show();
        }
        scanner.close();
        return list;
    }
}
