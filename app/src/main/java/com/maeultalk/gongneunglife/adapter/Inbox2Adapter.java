package com.maeultalk.gongneunglife.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.activity.CollectActivity;
import com.maeultalk.gongneunglife.activity.PlaceActivity;
import com.maeultalk.gongneunglife.model.ContentImage;
import com.maeultalk.gongneunglife.model.Inbox2;
import com.maeultalk.gongneunglife.request.InboxGoodRequest;
import com.maeultalk.gongneunglife.request.SendAsk;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.maeultalk.gongneunglife.activity.AnswerActivity2.inbox2s;
import static com.maeultalk.gongneunglife.key.Key.URL_IMAGES;

public class Inbox2Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int VIEW_TYPE_SEND = 0;
    public static final int VIEW_TYPE_RECEIVE = 1;

    private Context context;
//    private List<Inbox2> mItems = new ArrayList<>();

    public Inbox2Adapter(Context context, List<Inbox2> mItems) {
        this.context = context;
//        this.mItems = mItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SEND) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox2_send, parent, false);
            return new SendHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inbox2_receive, parent, false);
            return new ReceiveHolder(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (inbox2s.get(position).getSend().equals("send")) {
            return VIEW_TYPE_SEND;
        } else {
            return VIEW_TYPE_RECEIVE;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Inbox2 inbox2 = inbox2s.get(position);
        if (holder instanceof SendHolder) {
            ((SendHolder) holder).textView_content.setText(inbox2.getContents());
        } else {
            if(TextUtils.isEmpty(inbox2s.get(position).getPlace_code())) {
                ((ReceiveHolder) holder).goToSpot.setVisibility(View.GONE);
            } else {
                ((ReceiveHolder) holder).goToSpot.setVisibility(View.VISIBLE);
            }
            ((ReceiveHolder) holder).goToSpot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, contentModel.getSpot(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, PlaceActivity.class);
                    intent.putExtra("place_code", inbox2.getPlace_code());
                    intent.putExtra("place_name", inbox2.getPlace_name());
                    context.startActivity(intent);
                }
            });
            ((ReceiveHolder) holder).textView_place.setText(inbox2.getPlace_name());
            ((ReceiveHolder) holder).textView_content.setText(inbox2.getContents());
            if(TextUtils.isEmpty(inbox2s.get(position).getNmap())) {
                ((ReceiveHolder) holder).layout_nmap.setVisibility(View.GONE);
            } else {
                ((ReceiveHolder) holder).layout_nmap.setVisibility(View.VISIBLE);
            }
            ((ReceiveHolder) holder).layout_nmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = Uri.parse(inbox2.getNmap());
                    intent.setData(uri);
                    context.startActivity(intent);
                }
            });

            if(TextUtils.isEmpty(inbox2s.get(position).getCollect())) {
                ((ReceiveHolder) holder).layout_collect.setVisibility(View.GONE);
            } else {
                ((ReceiveHolder) holder).layout_collect.setVisibility(View.VISIBLE);
                ((ReceiveHolder) holder).textView_collect.setText("#" + inbox2s.get(position).getCollect());
            }
            ((ReceiveHolder) holder).layout_collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CollectActivity.class);
                    intent.putExtra("collect", inbox2s.get(position).getCollect());
                    context.startActivity(intent);
                }
            });

            final List<ContentImage> images = new ArrayList<>();
            if(!TextUtils.isEmpty(inbox2s.get(position).getImage())) {
                ((ReceiveHolder) holder).imageView_contentImg.setVisibility(View.VISIBLE);
                Glide.with(context).load(URL_IMAGES + inbox2.getImage())/*.listener(requestListener)*/.into(((ReceiveHolder) holder).imageView_contentImg);
                images.add(new ContentImage(inbox2.getImage()));
                ((ReceiveHolder) holder).imageView_contentImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    ImageOverlayView overlayView = new ImageOverlayView(this);
                        new ImageViewer.Builder<>(context, images)
                                .setStartPosition(0)
                                .setFormatter(new ImageViewer.Formatter<ContentImage>() {
                                    @Override
                                    public String format(ContentImage contentImage) {
                                        return contentImage.getUrl();
                                    }
                                })
//                            .setOverlayView(overlayView)
                                .show();
                    }
                });
                ((ReceiveHolder) holder).border.setVisibility(View.GONE);
            } else {
                ((ReceiveHolder) holder).imageView_contentImg.setVisibility(View.GONE);
                ((ReceiveHolder) holder).border.setVisibility(View.VISIBLE);
            }
            if(!TextUtils.isEmpty(inbox2s.get(position).getImage2())) {
                ((ReceiveHolder) holder).imageView_contentImg2.setVisibility(View.VISIBLE);
                Glide.with(context).load(URL_IMAGES + inbox2.getImage2()).into(((ReceiveHolder) holder).imageView_contentImg2);
                images.add(new ContentImage(inbox2.getImage2()));
                ((ReceiveHolder) holder).imageView_contentImg2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ImageViewer.Builder<>(context, images)
                                .setStartPosition(1)
                                .setFormatter(new ImageViewer.Formatter<ContentImage>() {
                                    @Override
                                    public String format(ContentImage contentImage) {
                                        return contentImage.getUrl();
                                    }
                                })
                                .show();
                    }
                });
            } else {
                ((ReceiveHolder) holder).imageView_contentImg2.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(inbox2s.get(position).getImage3())) {
                ((ReceiveHolder) holder).imageView_contentImg3.setVisibility(View.VISIBLE);
                Glide.with(context).load(URL_IMAGES + inbox2.getImage3()).into(((ReceiveHolder) holder).imageView_contentImg3);
                images.add(new ContentImage(inbox2.getImage3()));
                ((ReceiveHolder) holder).imageView_contentImg3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ImageViewer.Builder<>(context, images)
                                .setStartPosition(2)
                                .setFormatter(new ImageViewer.Formatter<ContentImage>() {
                                    @Override
                                    public String format(ContentImage contentImage) {
                                        return contentImage.getUrl();
                                    }
                                })
                                .show();
                    }
                });
            } else {
                ((ReceiveHolder) holder).imageView_contentImg3.setVisibility(View.GONE);
            }

            if(inbox2s.get(position).getGood().equals("like")) {
                ((ReceiveHolder) holder).imageView_like.setImageResource(R.drawable.like4);
                ((ReceiveHolder) holder).textView_like.setTextColor(Color.parseColor("#ffc107"));
                ((ReceiveHolder) holder).imageView_unlike.setImageResource(R.drawable.unlike3);
                ((ReceiveHolder) holder).textView_unlike.setTextColor(Color.parseColor("#808080"));
            } else if(inbox2s.get(position).getGood().equals("unlike")) {
                ((ReceiveHolder) holder).imageView_like.setImageResource(R.drawable.like3);
                ((ReceiveHolder) holder).textView_like.setTextColor(Color.parseColor("#808080"));
                ((ReceiveHolder) holder).imageView_unlike.setImageResource(R.drawable.unlike4);
                ((ReceiveHolder) holder).textView_unlike.setTextColor(Color.parseColor("#ffc107"));
            } else {
                ((ReceiveHolder) holder).imageView_like.setImageResource(R.drawable.like3);
                ((ReceiveHolder) holder).textView_like.setTextColor(Color.parseColor("#808080"));
                ((ReceiveHolder) holder).imageView_unlike.setImageResource(R.drawable.unlike3);
                ((ReceiveHolder) holder).textView_unlike.setTextColor(Color.parseColor("#808080"));
            }
            ((ReceiveHolder) holder).layout_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(inbox2s.get(position).getGood().equals("like")) { // 좋아요 취소("like" -> "")
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    final JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        ((ReceiveHolder) holder).imageView_like.setImageResource(R.drawable.like3);
                                        ((ReceiveHolder) holder).textView_like.setTextColor(Color.parseColor("#808080"));
                                        ((ReceiveHolder) holder).imageView_unlike.setImageResource(R.drawable.unlike3);
                                        ((ReceiveHolder) holder).textView_unlike.setTextColor(Color.parseColor("#808080"));
                                        inbox2s.get(position).setGood("");
                                    } else {
                                        Toast.makeText(context, "처리 실패", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {

                                }

                            }
                        };
                        InboxGoodRequest inboxGoodRequest = new InboxGoodRequest(inbox2s.get(position).getId(), "", responseListener);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(inboxGoodRequest);
                    } else if(inbox2s.get(position).getGood().equals("unlike")) { // 싫어요에서 좋아요로 바뀜("unlike" -> "like")
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    final JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        ((ReceiveHolder) holder).imageView_like.setImageResource(R.drawable.like4);
                                        ((ReceiveHolder) holder).textView_like.setTextColor(Color.parseColor("#ffc107"));
                                        ((ReceiveHolder) holder).imageView_unlike.setImageResource(R.drawable.unlike3);
                                        ((ReceiveHolder) holder).textView_unlike.setTextColor(Color.parseColor("#808080"));
                                        inbox2s.get(position).setGood("like");
                                    } else {
                                        Toast.makeText(context, "처리 실패", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {

                                }

                            }
                        };
                        InboxGoodRequest inboxGoodRequest = new InboxGoodRequest(inbox2s.get(position).getId(), "like", responseListener);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(inboxGoodRequest);
                    } else { // 아무것도 아닌 상태에서 좋아요로("" -> "like")
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    final JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        ((ReceiveHolder) holder).imageView_like.setImageResource(R.drawable.like4);
                                        ((ReceiveHolder) holder).textView_like.setTextColor(Color.parseColor("#ffc107"));
                                        ((ReceiveHolder) holder).imageView_unlike.setImageResource(R.drawable.unlike3);
                                        ((ReceiveHolder) holder).textView_unlike.setTextColor(Color.parseColor("#808080"));
                                        inbox2s.get(position).setGood("like");
                                    } else {
                                        Toast.makeText(context, "처리 실패", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {

                                }

                            }
                        };
                        InboxGoodRequest inboxGoodRequest = new InboxGoodRequest(inbox2s.get(position).getId(), "like", responseListener);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(inboxGoodRequest);
                    }
                }
            });
            ((ReceiveHolder) holder).layout_unlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(inbox2s.get(position).getGood().equals("like")) { // 좋아요에서 싫어요로("like" -> "unlike")
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    final JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        ((ReceiveHolder) holder).imageView_like.setImageResource(R.drawable.like3);
                                        ((ReceiveHolder) holder).textView_like.setTextColor(Color.parseColor("#808080"));
                                        ((ReceiveHolder) holder).imageView_unlike.setImageResource(R.drawable.unlike4);
                                        ((ReceiveHolder) holder).textView_unlike.setTextColor(Color.parseColor("#ffc107"));
                                        inbox2s.get(position).setGood("unlike");

                                        moreInfo(position);
                                    } else {
                                        Toast.makeText(context, "처리 실패", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {

                                }

                            }
                        };
                        InboxGoodRequest inboxGoodRequest = new InboxGoodRequest(inbox2s.get(position).getId(), "unlike", responseListener);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(inboxGoodRequest);
                    } else if(inbox2s.get(position).getGood().equals("unlike")) { // 싫어요에서 아무것도 아닌 상태로("unlike" -> "")
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    final JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        ((ReceiveHolder) holder).imageView_like.setImageResource(R.drawable.like3);
                                        ((ReceiveHolder) holder).textView_like.setTextColor(Color.parseColor("#808080"));
                                        ((ReceiveHolder) holder).imageView_unlike.setImageResource(R.drawable.unlike3);
                                        ((ReceiveHolder) holder).textView_unlike.setTextColor(Color.parseColor("#808080"));
                                        inbox2s.get(position).setGood("");
                                    } else {
                                        Toast.makeText(context, "처리 실패", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {

                                }

                            }
                        };
                        InboxGoodRequest inboxGoodRequest = new InboxGoodRequest(inbox2s.get(position).getId(), "", responseListener);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(inboxGoodRequest);
                    } else { // 아무것도 아닌 상태에서 싫어요로("" -> "unlike")
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    final JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        ((ReceiveHolder) holder).imageView_like.setImageResource(R.drawable.like3);
                                        ((ReceiveHolder) holder).textView_like.setTextColor(Color.parseColor("#808080"));
                                        ((ReceiveHolder) holder).imageView_unlike.setImageResource(R.drawable.unlike4);
                                        ((ReceiveHolder) holder).textView_unlike.setTextColor(Color.parseColor("#ffc107"));
                                        inbox2s.get(position).setGood("unlike");

                                        moreInfo(position);
                                    } else {
                                        Toast.makeText(context, "처리 실패", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {

                                }

                            }
                        };
                        InboxGoodRequest inboxGoodRequest = new InboxGoodRequest(inbox2s.get(position).getId(), "unlike", responseListener);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(inboxGoodRequest);
                    }
                }
            });
        }
    }

    void moreInfo(final int position) {
        //handle menu_modify click
        AlertDialog.Builder dialogSend = new AlertDialog.Builder(context);
        dialogSend.setTitle("아쉬운 정보에요");
        dialogSend.setMessage("어떤 정보가 더 있으면 좋을지 알려주세요. 저희에게 큰 힘이 됩니다.");
        final EditText editText = new EditText(context);
        FrameLayout container = new FrameLayout(context);
        FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = context.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        params.rightMargin = context.getResources().getDimensionPixelSize(R.dimen.dialog_margin);
        editText.setLayoutParams(params);
//        editText.setText(mData.getComment());
        editText.post(new Runnable() {
            @Override
            public void run() {
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText,0);
//                                            editText.setSelection(0, editText.length());
            }
        });
        container.addView(editText);
        dialogSend.setView(container);
        dialogSend.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            final JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if(success) {

//                                button.setEnabled(true);

//                            String newId = jsonResponse.getString("id");
                                inbox2s.add(new Inbox2("send", editText.getText().toString()));
                                editText.setText("");
                                notifyDataSetChanged();
//                            id = newId;

                            } else {
                                Toast.makeText(context, "처리실패", Toast.LENGTH_SHORT).show();
//                                button.setEnabled(true);
                            }
                        } catch (JSONException e) {
//                            button.setEnabled(true);
                        }
                    }
                };
                SendAsk sendAsk = new SendAsk(inbox2s.get(position).getInbox(), editText.getText().toString(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(sendAsk);
            }
        });
        dialogSend.setNegativeButton("나중에", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogSend.show();
    }

    private Inbox2 getItem(int position) {
        return inbox2s.get(position);
    }

    /*public void setItems(List<Inbox2> items) {
        mItems.clear();
        mItems.addAll(items);
    }*/

    /*public void addItem(Inbox2 inbox2){
        mItems.add(inbox2);
        notifyDataSetChanged();
    }*/

    @Override
    public int getItemCount() {
        return inbox2s.size();
    }

    public class SendHolder extends RecyclerView.ViewHolder {

        public TextView textView_content;

        public SendHolder(View view) {
            super(view);
            textView_content = (TextView) view.findViewById(R.id.textView_content);
        }
    }

    public class ReceiveHolder extends RecyclerView.ViewHolder {

        LinearLayout goToSpot;
        TextView textView_place;
        TextView textView_content;
        LinearLayout layout_nmap;
        private ImageView imageView_contentImg;
        private ImageView imageView_contentImg2;
        private ImageView imageView_contentImg3;
        private View border;
        private ImageView imageView_like;
        private ImageView imageView_unlike;
        private TextView textView_like;
        private TextView textView_unlike;
        private LinearLayout layout_like;
        private LinearLayout layout_unlike;

        LinearLayout layout_collect;
        TextView textView_collect;

        public ReceiveHolder(View view) {
            super(view);
            goToSpot = (LinearLayout) view.findViewById(R.id.goToSpot);
            /*if(callFromSpotHome == true) {
                goToSpot.setVisibility(View.VISIBLE);
                //border_below_spotName.setVisibility(View.GONE);
            } else {
                goToSpot.setVisibility(View.GONE);
            }*/
            textView_place = (TextView) view.findViewById(R.id.textView_place);
            textView_content = (TextView) view.findViewById(R.id.textView_content);
            layout_nmap = (LinearLayout) view.findViewById(R.id.layout_nmap);
            imageView_contentImg = (ImageView) view.findViewById(R.id.imageView_contentImg);
            imageView_contentImg2 = (ImageView) view.findViewById(R.id.imageView_contentImg2);
            imageView_contentImg3 = (ImageView) view.findViewById(R.id.imageView_contentImg3);
            border = (View) view.findViewById(R.id.border);
            imageView_like = (ImageView) view.findViewById(R.id.imageView_like);
            imageView_unlike = (ImageView) view.findViewById(R.id.imageView_unlike);
            textView_like = (TextView) view.findViewById(R.id.textView_like);
            textView_unlike = (TextView) view.findViewById(R.id.textView_unlike);
            layout_like = (LinearLayout) view.findViewById(R.id.layout_like);
            layout_unlike = (LinearLayout) view.findViewById(R.id.layout_unlike);

            layout_collect = (LinearLayout) view.findViewById(R.id.layout_collect);
            textView_collect = (TextView) view.findViewById(R.id.textView_collect);
        }
    }

}