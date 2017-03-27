package com.mystery0.hitokoto.custom;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.R;
import com.mystery0.hitokoto.class_class.HitokotoSource;
import com.mystery0.hitokoto.test_source.TestSource;
import com.mystery0.hitokoto.test_source.TestSourceListener;
import com.mystery0.tools.Logs.Logs;

import org.litepal.crud.DataSupport;

import java.util.List;

import static android.support.v7.widget.helper.ItemTouchHelper.RIGHT;

@SuppressWarnings("ConstantConditions")
public class CustomSourceActivity extends AppCompatActivity implements CustomItemListener
{
    private static final String TAG = "CustomSourceActivity";
    private Toolbar toolbar;
    private Button button;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private RelativeLayout layout;
    private List<HitokotoSource> list;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        initialize();
        monitor();
    }

    private void initialize()
    {
        list = DataSupport.where("source = ?", getResources().getStringArray(R.array.list_source_type)[2]).find(HitokotoSource.class);
        setContentView(R.layout.activity_custom_source);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        button = (Button) findViewById(R.id.test);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layout = (RelativeLayout) findViewById(R.id.head);
        TextView null_data = (TextView) findViewById(R.id.null_data);
        TextView head_name = (TextView) layout.findViewById(R.id.source_name);
        TextView head_enable = (TextView) layout.findViewById(R.id.source_enable);
        TextView head_content = (TextView) layout.findViewById(R.id.source_content);
        TextView head_from = (TextView) layout.findViewById(R.id.source_from);
        head_name.setText(getString(R.string.Name));
        head_enable.setText(getString(R.string.Enable));
        head_content.setText(getString(R.string.Content));
        head_from.setText(getString(R.string.From));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(list, this);
        recyclerView.setAdapter(adapter);
        if (list.size() == 0)
        {
            layout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            null_data.setVisibility(View.VISIBLE);
        }


        setSupportActionBar(toolbar);
    }

    private void monitor()
    {
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(0, RIGHT)
        {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
            {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {
                int position = viewHolder.getAdapterPosition();
                list.remove(position).delete();
                adapter.notifyItemRemoved(position);
                Logs.i(TAG, "onSwiped: 滑动删除");
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for (int i = 0; i < list.size(); i++)
                {
                    final int finalI = i;
                    TestSource.test(list.get(i), new TestSourceListener()
                    {
                        @Override
                        public void result(boolean result)
                        {
                            if (result)
                            {
                                list.get(finalI).setEnable(getString(R.string.Enable));
                            } else
                            {
                                list.get(finalI).setEnable(getString(R.string.Disable));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onItemClick(final HitokotoSource hitokotoSource, int position)
    {
        //noinspection RestrictedApi
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(App.getContext(), R.style.AlertDialogStyle);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(contextThemeWrapper).inflate(R.layout.dialog_custom_source_new, null);
        final TextInputLayout source_name = (TextInputLayout) view.findViewById(R.id.custom_source_name);
        final TextInputLayout source_address = (TextInputLayout) view.findViewById(R.id.custom_source_address);
        final TextInputLayout source_content = (TextInputLayout) view.findViewById(R.id.custom_source_content);
        final TextInputLayout source_from = (TextInputLayout) view.findViewById(R.id.custom_source_from);
        Button button = (Button) view.findViewById(R.id.test);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.list_request_method));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        source_name.getEditText().setText(hitokotoSource.getName());
        source_address.getEditText().setText(hitokotoSource.getAddress());
        source_content.getEditText().setText(hitokotoSource.getContent_key());
        source_from.getEditText().setText(hitokotoSource.getFrom_key());
        spinner.setSelection(hitokotoSource.getMethod() - 1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                hitokotoSource.setMethod(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                hitokotoSource.setName(source_name.getEditText().getText().length() > 0 ? source_name.getEditText().getText().toString() : getString(R.string.Custom));
                hitokotoSource.setAddress(source_address.getEditText().getText().toString());
                hitokotoSource.setContent_key(source_content.getEditText().getText().toString());
                hitokotoSource.setFrom_key(source_from.getEditText().getText().toString());
                TestSource.test(hitokotoSource, new TestSourceListener()
                {
                    @Override
                    public void result(boolean result)
                    {
                        source_address.setError(result ? getString(R.string.Enable) : getString(R.string.Disable));
                    }
                });
            }
        });
        new AlertDialog.Builder(CustomSourceActivity.this, R.style.AlertDialogStyle)
                .setView(view)
                .setTitle(R.string.text_custom_source_new)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (isFormat(source_address) && isFormat(source_content) && isFormat(source_from))
                        {
                            hitokotoSource.setName(source_name.getEditText().getText().length() > 0 ? source_name.getEditText().getText().toString() : getString(R.string.Custom));
                            hitokotoSource.setAddress(source_address.getEditText().getText().toString());
                            hitokotoSource.setContent_key(source_content.getEditText().getText().toString());
                            hitokotoSource.setFrom_key(source_from.getEditText().getText().toString());
                            TestSource.test(hitokotoSource, new TestSourceListener()
                            {
                                @Override
                                public void result(boolean result)
                                {
                                    source_address.setError(result ? getString(R.string.Enable) : getString(R.string.Disable));
                                    Toast.makeText(App.getContext(),
                                            result ?
                                                    (hitokotoSource.saveOrUpdate("address = ?", hitokotoSource.getAddress()) ?
                                                            getString(R.string.hint_custom_add_source_done) :
                                                            getString(R.string.hint_custom_add_source_error)) :
                                                    getString(R.string.hint_invalid_source),
                                            Toast.LENGTH_SHORT)
                                            .show();
                                }
                            });
                        } else
                        {
                            Toast.makeText(App.getContext(), getString(R.string.ErrorFormat), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private boolean isFormat(TextInputLayout layout)
    {
        layout.setError(layout.getEditText().getText().length() != 0 ? null : getString(R.string.ErrorNull));
        return layout.getEditText().getText().toString().trim().length() != 0;
    }
}
