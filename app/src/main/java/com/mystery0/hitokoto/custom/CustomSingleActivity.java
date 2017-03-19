package com.mystery0.hitokoto.custom;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;

import com.mystery0.hitokoto.R;
import com.mystery0.tools.Logs.Logs;

@SuppressWarnings("ConstantConditions")
public class CustomSingleActivity extends AppCompatActivity
{
    private static final String TAG = "CustomSingleActivity";
    private TextInputLayout hitokotoContent;
    private TextInputLayout hitokotoSource;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_single);

        hitokotoContent = (TextInputLayout) findViewById(R.id.custom_content);
        hitokotoSource = (TextInputLayout) findViewById(R.id.custom_source);

        check(hitokotoContent);
        check(hitokotoSource);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.custom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_done:
                if (isFormat(hitokotoContent) && isFormat(hitokotoSource))
                {
                    CustomConfigure.saveToDatabase(
                            hitokotoContent.getEditText().getText().toString(),
                            hitokotoSource.getEditText().getText().toString());
                    Logs.i(TAG, "onOptionsItemSelected: 存储");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
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
                    layout.setError(getString(R.string.hint_null));
                } else
                {
                    layout.setError(null);
                }
            }
        });
    }

    private boolean isFormat(TextInputLayout layout)
    {
        return layout.getEditText().getText().length() != 0;
    }
}
