package com.lambdai.poly.util;

import com.lambdai.poly.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;

public class SearchDialog extends DialogFragment
{
	private EditText input;
	
	public static interface OnSearchListener {
		public void onSearch(String query);
	}
	
	private OnSearchListener listener;
	
	public SearchDialog() {
	}
	
	public void setListener(OnSearchListener listener) {
		this.listener = listener;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState)
	{
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		View view = inflater.inflate(R.layout.dialog_search, parent, false);
		input = (EditText) view.findViewById(R.id.search_text);
		((ImageButton)view.findViewById(R.id.ping_search)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onSearch(input.getText().toString());
				}
				dismiss();
			}
		});
		input.addTextChangedListener(new TextWatcher() {

	        @Override
	        public void onTextChanged(CharSequence s, int start, int before, int count) {
	        }

	        @Override
	        public void beforeTextChanged(CharSequence s, int start, int count,
	                int after) {
	        }

	        @Override
	        public void afterTextChanged(Editable s)
	        {
	        	if(s.length() - 1 >= 0)
	        	{
	        		if (s.charAt(s.length() - 1) == '\n')
		            {
		            	if (listener != null) {
							listener.onSearch(input.getText().toString());
						}
						dismiss();
		            }
	        	}
	        }
	    });
		
		input.requestFocus();
		
		return view;
	}
	
	public EditText getInputBox()
	{
		return input;
	}

}
