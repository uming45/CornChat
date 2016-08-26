package ru.cft.chuldrenofcorn.cornchat.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.arellomobile.mvp.presenter.InjectPresenter;

import java.util.List;

import ru.cft.chuldrenofcorn.cornchat.R;
import ru.cft.chuldrenofcorn.cornchat.adapter.ChatAdapter;
import ru.cft.chuldrenofcorn.cornchat.data.models.ChatMessage;
import ru.cft.chuldrenofcorn.cornchat.mvp.presenter.ChatPresenter;
import ru.cft.chuldrenofcorn.cornchat.mvp.view.ChatView;
import ru.cft.chuldrenofcorn.cornchat.ui.common.BaseActivity;
import ru.cft.chuldrenofcorn.cornchat.xmpp.ChatService;
import ru.cft.chuldrenofcorn.cornchat.xmpp.LocalBinder;

public class MainActivity extends BaseActivity implements ChatView {

    private static final String TAG = "MainActivity";

    @InjectPresenter
    ChatPresenter presenter;

    private ChatAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initViews() {
        final ImageButton imageButtonSend = (ImageButton) findViewById(R.id.imageButtonSend);
        final EditText editTextMessage = (EditText) findViewById(R.id.editTextMessage);
        final RecyclerView recyclerViewMessages = (RecyclerView) findViewById(R.id.recyclerViewMessages);

        adapter = new ChatAdapter(this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMessages.setLayoutManager(linearLayoutManager);
        recyclerViewMessages.setAdapter(adapter);

		recyclerViewMessages.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(final RecyclerView recyclerView, final int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				Log.d(TAG, "onScrollStateChanged()");
				View view = MainActivity.this.getCurrentFocus();
				if (view != null) {
					InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
				}

			}
		});

        imageButtonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (!editTextMessage.getText().toString().isEmpty()) {
                    presenter.sendMessage(editTextMessage.getText().toString());
                }
            }
        });
    }

    @Override
    public void onDataReady(final List<ChatMessage> messages) {
        adapter.setMessages(messages);
    }
}
