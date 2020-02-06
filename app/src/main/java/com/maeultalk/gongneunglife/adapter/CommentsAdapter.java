package com.maeultalk.gongneunglife.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.PopupMenu;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.activity.CommentsActivity;
import com.maeultalk.gongneunglife.activity.SearchSpotActivity;
import com.maeultalk.gongneunglife.model.CommentModel;
import com.maeultalk.gongneunglife.model.Place;
import com.maeultalk.gongneunglife.request.DeleteCommentRequest;
import com.maeultalk.gongneunglife.request.EditCommentRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.maeultalk.gongneunglife.adapter.RecyclerViewAdapter.formatTimeString;

public class CommentsAdapter extends BaseAdapter{

    private static class ViewHolder {

        public TextView tv_nick;
        public TextView tv_comment;
        public AppCompatImageView imageView_more;

    }

    private Context mContext = null;
    private String contentID;
    private ArrayList<CommentModel> mListData = new ArrayList<CommentModel>();

    public CommentsAdapter(Context mContext, String contentID, ArrayList<CommentModel> mListData) {
        super();
        this.mContext = mContext;
        this.contentID = contentID;
        this.mListData = mListData;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_comment, null);

            holder.tv_nick = (TextView) convertView.findViewById(R.id.tv_nick);
            holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
            holder.imageView_more = (AppCompatImageView) convertView.findViewById(R.id.imageView_more);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        final CommentModel mData = mListData.get(position);

        String date = mData.getDate();
        Date original_date = null;
        String new_date = "";
        SimpleDateFormat original_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        SimpleDateFormat new_format = new SimpleDateFormat("yyyy.M.d.\na h:mm", Locale.KOREA);
        try {
            original_date = original_format.parse(date);
            new_date = new_format.format(original_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        holder.tv_nick.setText(mData.getNick() + " ⦁ " + formatTimeString(original_date));
        holder.tv_nick.setText(mData.getNick() + " · " + formatTimeString(original_date));
        holder.tv_comment.setText(mData.getComment());
        SharedPreferences pref = mContext.getSharedPreferences("user", MODE_PRIVATE);
        if(pref.getString("email", "").equals(mData.getEmail())) {
            holder.imageView_more.setVisibility(View.VISIBLE);
            holder.imageView_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //creating a popup menu
                    Context wrapper = new ContextThemeWrapper(mContext, R.style.MyPopupMenu);
                    PopupMenu popup = new PopupMenu(wrapper, holder.imageView_more);
                    //inflating menu from xml resource
                    popup.inflate(R.menu.menu_content_item_options);
                    //adding click listener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu_modify:
                                    //handle menu_modify click
                                    AlertDialog.Builder dialogSend = new AlertDialog.Builder(mContext);
//                                        dialogSend.setTitle("나누미 인증하기");
                                    dialogSend.setMessage("댓글 수정");
                                    final EditText editText = new EditText(mContext);
                                    FrameLayout container = new FrameLayout(mContext);
                                    FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                                    params.rightMargin = mContext.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                                    editText.setLayoutParams(params);
                                    editText.setText(mData.getComment());
                                    editText.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            editText.setFocusableInTouchMode(true);
                                            editText.requestFocus();
                                            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.showSoftInput(editText,0);
//                                            editText.setSelection(0, editText.length());
                                        }
                                    });
                                    container.addView(editText);
                                    dialogSend.setView(container);
                                    dialogSend.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialog, int which) {
                                            // 댓글 수정
                                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        final JSONObject jsonResponse = new JSONObject(response);
                                                        boolean success = jsonResponse.getBoolean("success");
                                                        if (success) {
                                                            Toast.makeText(mContext, "댓글을 수정하였습니다.", Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
//                                                        ((Activity)context).finish();

                                                            mData.setComment(editText.getText().toString());
                                                            CommentsAdapter.this.notifyDataSetChanged();

                                                        } else {
                                                            Toast.makeText(mContext, "처리 실패", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (JSONException e) {

                                                    }

                                                }
                                            };
                                            EditCommentRequest editCommentRequest = new EditCommentRequest(mData.getId(), editText.getText().toString(), responseListener);
                                            RequestQueue queue = Volley.newRequestQueue(mContext);
                                            queue.add(editCommentRequest);
                                        }
                                    });
                                    dialogSend.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialogSend.show();
                                    break;
                                case R.id.menu_delete:
                                    //handle menu_delete click
                                    AlertDialog.Builder dialogDelete = new AlertDialog.Builder(mContext);
//                                        dialogSend.setTitle("나누미 인증하기");
                                    dialogDelete.setMessage("댓글을 삭제하시겠습니까?");
                                    dialogDelete.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(final DialogInterface dialog, int which) {
                                            // 댓글 삭제
                                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        final JSONObject jsonResponse = new JSONObject(response);
                                                        boolean success = jsonResponse.getBoolean("success");
                                                        if (success) {
                                                            Toast.makeText(mContext, "댓글을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
//                                                        ((Activity)context).finish();

                                                            mListData.remove(position);
                                                            CommentsAdapter.this.notifyDataSetChanged();

                                                        } else {
                                                            Toast.makeText(mContext, "처리 실패", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (JSONException e) {

                                                    }

                                                }
                                            };
                                            DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(contentID, mData.getId(), responseListener);
                                            RequestQueue queue = Volley.newRequestQueue(mContext);
                                            queue.add(deleteCommentRequest);
                                        }
                                    });
                                    dialogDelete.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    dialogDelete.show();
                                    break;
                            }
                            return false;
                        }
                    });
                    //displaying the popup
                    popup.show();
                }
            });
        } else {
            holder.imageView_more.setVisibility(View.GONE);
        }

        return convertView;
    }
}