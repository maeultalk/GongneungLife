package com.maeultalk.gongneunglife.adapter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.activity.CollectAllActivity;
import com.maeultalk.gongneunglife.activity.CommentsActivity;
import com.maeultalk.gongneunglife.activity.EditContentActivity;
import com.maeultalk.gongneunglife.activity.PlaceActivity;
import com.maeultalk.gongneunglife.model.Content;
import com.maeultalk.gongneunglife.model.ContentImage;
import com.maeultalk.gongneunglife.model.Item;
import com.maeultalk.gongneunglife.request.DeleteContentRequest;
import com.maeultalk.gongneunglife.request.FavorOffRequest;
import com.maeultalk.gongneunglife.request.FavorOnRequest;
import com.maeultalk.gongneunglife.request.GoodOffRequest;
import com.maeultalk.gongneunglife.request.GoodOnRequest;
import com.maeultalk.gongneunglife.util.RecyclerViewDecorationHorizontal;
import com.stfalcon.frescoimageviewer.ImageViewer;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.maeultalk.gongneunglife.fragment.mainActivity.HomeFragment.collects;
//import static com.maeultalk.gongneunglife.fragment.mainActivity.HomeFragment.contentsInHome;
import static com.maeultalk.gongneunglife.activity.CollectActivity.contentsInCollect;
import static com.maeultalk.gongneunglife.key.Key.URL_IMAGES;

/**
 * Created by charlie on 2017. 4. 24..
 */

public class CollectRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    // 어떤 위치에서 부르는지(홈프래그먼트 or 플레이스액티비티)
    boolean callFromSpotHome;
//    ArrayList<Content> contents = contentsInHome;

    public static final int VIEW_TYPE_NORMAL = 0;
    public static final int VIEW_TYPE_COLLECT = 1;

    public CollectRecyclerViewAdapter(Context context, boolean callFromSpotHome, ArrayList<Content> contents){
        this.context = context;
        this.callFromSpotHome = callFromSpotHome;
//        this.contents = contents;
    }

    // 새로운 뷰 홀더 생성
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content, parent,false);
            return new NormalHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collect, parent,false);
            return new CollectHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
        /*if (position!=1) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_COLLECT;
        }*/
    }
    
    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CollectHolder) {

        } else {
            final int pos2;
            if(position ==0) {
                pos2 = position;
            } else {
                pos2 = position;
            }
            final Content content = contentsInCollect.get(pos2);
            ((NormalHolder) holder).goToSpot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, contentModel.getSpot(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, PlaceActivity.class);
                    intent.putExtra("place_code", content.getPlace_code());
                    intent.putExtra("place_name", content.getPlace_name());
                    context.startActivity(intent);
                    //activity.overridePendingTransition(R.anim.slide_in_right, android.R.anim.fade_out); // 액티비티 전환 애니메이션
                    //activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_in_left); // 액티비티 전환 애니메이션
                    //activity.overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_out_right); // 액티비티 전환 애니메이션
                    //activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right); // 액티비티 전환 애니메이션
                    //activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left); // 액티비티 전환 애니메이션
                    //activity.overridePendingTransition() // 액티비티 전환 애니메이션
                }
            });
            ((NormalHolder) holder).textView_place.setText(content.getPlace_name());
            ((NormalHolder) holder).mNameTv.setText(content.getUser());

            String date = content.getDate();
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
            ((NormalHolder) holder).textView_time.setText(formatTimeString(original_date));

            SharedPreferences pref = context.getSharedPreferences("user", MODE_PRIVATE);
            if(pref.getString("email", "").equals(content.getEmail())) {
                ((NormalHolder) holder).imageView_more.setVisibility(View.VISIBLE);
                ((NormalHolder) holder).imageView_more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //creating a popup menu
                        PopupMenu popup = new PopupMenu(context, ((NormalHolder) holder).imageView_more);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.menu_content_item_options);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.menu_modify:
                                        //handle menu_modify click
                                        Intent intent = new Intent(context, EditContentActivity.class);
                                        intent.putExtra("from", "collect");
                                        intent.putExtra("content", content);
                                        context.startActivity(intent);
//                                        ((Activity)mContext).finish();
                                        break;
                                    case R.id.menu_delete:
                                        //handle menu_delete click
                                        AlertDialog.Builder dialogSend = new AlertDialog.Builder(context);
//                                        dialogSend.setTitle("나누미 인증하기");
                                        dialogSend.setMessage("게시글을 삭제하시겠습니까?");
                                        dialogSend.setPositiveButton("네", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(final DialogInterface dialog, int which) {
                                                // 나눔 게시글 삭제
                                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            final JSONObject jsonResponse = new JSONObject(response);
                                                            boolean success = jsonResponse.getBoolean("success");
                                                            if (success) {
                                                                Toast.makeText(context, "게시글을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
//                                                        ((Activity)context).finish();
                                                                contentsInCollect.remove(pos2);
                                                                CollectRecyclerViewAdapter.this.notifyDataSetChanged();
                                                            } else {
                                                                Toast.makeText(context, "처리 실패", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (JSONException e) {

                                                        }

                                                    }
                                                };
                                                DeleteContentRequest deleteContentRequest = new DeleteContentRequest(content.getId(), responseListener);
                                                RequestQueue queue = Volley.newRequestQueue(context);
                                                queue.add(deleteContentRequest);
                                            }
                                        });
                                        dialogSend.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                        dialogSend.show();
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
                ((NormalHolder) holder).imageView_more.setVisibility(View.GONE);
            }

            ((NormalHolder) holder).textView_content.setText(content.getContent());

            RequestListener requestListener = new RequestListener() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                    Toast.makeText(context, "준비완료 " + pos2, Toast.LENGTH_SHORT).show();
                    return false;
                }
            };

            ((NormalHolder) holder).imageView_contentImg.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("image1_name", content.getImage());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(context, content.getImage(),Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            ((NormalHolder) holder).imageView_contentImg2.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("image2_name", content.getImage2());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(context, content.getImage2(),Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            ((NormalHolder) holder).imageView_contentImg3.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("image3_name", content.getImage3());
                    clipboardManager.setPrimaryClip(clipData);
                    Toast.makeText(context, content.getImage3(),Toast.LENGTH_SHORT).show();
                    return true;
                }
            });

            final List<ContentImage> images = new ArrayList<>();
            if(!TextUtils.isEmpty(contentsInCollect.get(pos2).getImage())) {
                ((NormalHolder) holder).imageView_contentImg.setVisibility(View.VISIBLE);
                Glide.with(context).load(URL_IMAGES + content.getImage())/*.listener(requestListener)*/.thumbnail(0.1f).into(((NormalHolder) holder).imageView_contentImg);
                images.add(new ContentImage(content.getImage()));
                ((NormalHolder) holder).imageView_contentImg.setOnClickListener(new View.OnClickListener() {
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
                ((NormalHolder) holder).border.setVisibility(View.GONE);
            } else {
                ((NormalHolder) holder).imageView_contentImg.setVisibility(View.GONE);
                ((NormalHolder) holder).border.setVisibility(View.VISIBLE);
            }
            if(!TextUtils.isEmpty(contentsInCollect.get(pos2).getImage2())) {
                ((NormalHolder) holder).imageView_contentImg2.setVisibility(View.VISIBLE);
                Glide.with(context).load(URL_IMAGES + content.getImage2()).thumbnail(0.1f).into(((NormalHolder) holder).imageView_contentImg2);
                images.add(new ContentImage(content.getImage2()));
                ((NormalHolder) holder).imageView_contentImg2.setOnClickListener(new View.OnClickListener() {
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
                ((NormalHolder) holder).imageView_contentImg2.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(contentsInCollect.get(pos2).getImage3())) {
                ((NormalHolder) holder).imageView_contentImg3.setVisibility(View.VISIBLE);
                Glide.with(context).load(URL_IMAGES + content.getImage3()).thumbnail(0.1f).into(((NormalHolder) holder).imageView_contentImg3);
                images.add(new ContentImage(content.getImage3()));
                ((NormalHolder) holder).imageView_contentImg3.setOnClickListener(new View.OnClickListener() {
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
                ((NormalHolder) holder).imageView_contentImg3.setVisibility(View.GONE);
            }
            ((NormalHolder) holder).textView_comments.setText("댓글 " + content.getComments());
            ((NormalHolder) holder).layout_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra("id", content.getId());
                    intent.putExtra("content", content.getContent());
                    intent.putExtra("from", "collect");
                    context.startActivity(intent);
                }
            });
        /*holder.textView_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(activity, "준비중입니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, CommentsActivity.class);
                intent.putExtra("id", content.getId());
                intent.putExtra("content", content.getContent());
                context.startActivity(intent);
            }
        });*/
//        final ColorStateList oldColors = holder.textView11_like.getTextColors();
            if(contentsInCollect.get(pos2).isGood()) {
                ((NormalHolder) holder).imageView10_like.setImageResource(R.drawable.like4);
                ((NormalHolder) holder).textView11_like.setTextColor(Color.parseColor("#ffc107"));
            } else {
                ((NormalHolder) holder).imageView10_like.setImageResource(R.drawable.like3);
                ((NormalHolder) holder).textView11_like.setTextColor(Color.parseColor("#808080"));
            }
            if(contentsInCollect.get(pos2).getGood_cnt().equals("0")) {
                ((NormalHolder) holder).textView11_like.setText("좋아요");
            } else {
                ((NormalHolder) holder).textView11_like.setText(contentsInCollect.get(pos2).getGood_cnt());
            }
            ((NormalHolder) holder).layout_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(contentsInCollect.get(pos2).isGood()) {
                        SharedPreferences pref = context.getSharedPreferences("user", MODE_PRIVATE);
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    final JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        ((NormalHolder) holder).imageView10_like.setImageResource(R.drawable.like3);
                                        ((NormalHolder) holder).textView11_like.setTextColor(Color.parseColor("#808080"));
                                        contentsInCollect.get(pos2).setGood(false);
                                        contentsInCollect.get(pos2).setGood_cnt((Integer.valueOf(contentsInCollect.get(pos2).getGood_cnt()) - 1) + "");
                                        if(contentsInCollect.get(pos2).getGood_cnt().equals("0")) {
                                            ((NormalHolder) holder).textView11_like.setText("좋아요");
                                        } else {
                                            ((NormalHolder) holder).textView11_like.setText(contentsInCollect.get(pos2).getGood_cnt());
                                        }
//                                    RecyclerViewAdapter.this.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(context, "처리 실패", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {

                                }

                            }
                        };
                        GoodOffRequest goodOffRequest = new GoodOffRequest(pref.getString("email", ""), content.getId(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(goodOffRequest);
                    } else {
                        SharedPreferences pref = context.getSharedPreferences("user", MODE_PRIVATE);
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    final JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        ((NormalHolder) holder).imageView10_like.setImageResource(R.drawable.like4);
                                        ((NormalHolder) holder).textView11_like.setTextColor(Color.parseColor("#ffc107"));
                                        contentsInCollect.get(pos2).setGood(true);
                                        contentsInCollect.get(pos2).setGood_cnt((Integer.valueOf(contentsInCollect.get(pos2).getGood_cnt()) + 1) + "");
                                        if(contentsInCollect.get(pos2).getGood_cnt().equals("0")) {
                                            ((NormalHolder) holder).textView11_like.setText("좋아요");
                                        } else {
                                            ((NormalHolder) holder).textView11_like.setText(contentsInCollect.get(pos2).getGood_cnt());
                                        }
//                                    RecyclerViewAdapter.this.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(context, "처리 실패", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {

                                }

                            }
                        };
                        GoodOnRequest goodOnRequest = new GoodOnRequest(pref.getString("email", ""), content.getId(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(goodOnRequest);
                    }
                }
            });
            ((NormalHolder) holder).layout_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "준비중입니다.", Toast.LENGTH_SHORT).show();

                    Intent intentShare = new Intent(android.content.Intent.ACTION_SEND);
                    intentShare.setType("text/plain");
                    // Set default text message
                    // 카톡, 이메일, MMS 다 이걸로 설정 가능
                    //String subject = "문자의 제목";
                    String text = "공릉동 생활을 편리하게! \"공릉생활\" - https://play.google.com/store/apps/details?id=com.maeultalk.gongneunglife";
                    //intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    intentShare.putExtra(Intent.EXTRA_TEXT, text);
                    // Title of intent
                    Intent chooser = Intent.createChooser(intentShare, "공유하기");
                    context.startActivity(chooser);
                }
            });

            if(contentsInCollect.get(pos2).isFavorite()) {
                ((NormalHolder) holder).imageView1022.setImageResource(R.drawable.folder_closed);
                ((NormalHolder) holder).textView_save.setTextColor(Color.parseColor("#ffc107"));
            } else {
                ((NormalHolder) holder).imageView1022.setImageResource(R.drawable.folder_open);
                ((NormalHolder) holder).textView_save.setTextColor(Color.parseColor("#808080"));
            }
            ((NormalHolder) holder).layout_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(contentsInCollect.get(pos2).isFavorite()) {
                        SharedPreferences pref = context.getSharedPreferences("user", MODE_PRIVATE);
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    final JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        ((NormalHolder) holder).imageView1022.setImageResource(R.drawable.folder_open);
                                        ((NormalHolder) holder).textView_save.setTextColor(Color.parseColor("#808080"));
                                        contentsInCollect.get(pos2).setFavorite(false);
//                                    RecyclerViewAdapter.this.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(context, "처리 실패", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {

                                }

                            }
                        };
                        FavorOffRequest favorOffRequest = new FavorOffRequest(pref.getString("email", ""), content.getId(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(favorOffRequest);
                    } else {
                        SharedPreferences pref = context.getSharedPreferences("user", MODE_PRIVATE);
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    final JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");
                                    if (success) {
                                        ((NormalHolder) holder).imageView1022.setImageResource(R.drawable.folder_closed);
                                        ((NormalHolder) holder).textView_save.setTextColor(Color.parseColor("#ffc107"));
                                        contentsInCollect.get(pos2).setFavorite(true);
//                                    RecyclerViewAdapter.this.notifyDataSetChanged();
                                    } else {
                                        Toast.makeText(context, "처리 실패", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {

                                }

                            }
                        };
                        FavorOnRequest favorOnRequest = new FavorOnRequest(pref.getString("email", ""), content.getPlace_code(), content.getId(), responseListener);
                        RequestQueue queue = Volley.newRequestQueue(context);
                        queue.add(favorOnRequest);
                    }
                }
            });
        }
    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        return contentsInCollect.size();
    }

    // 커스텀 뷰홀더
    // item layout 에 존재하는 위젯들을 바인딩합니다.
    class CollectHolder extends RecyclerView.ViewHolder{

        TextView textView_showall;

        final int ITEM_SIZE = 5;

        public CollectHolder(View itemView) {
            super(itemView);

            textView_showall = (TextView) itemView.findViewById(R.id.textView_showall);
            textView_showall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CollectAllActivity.class);
//                    intent.putExtra("place_code", content.getPlace_code());
                    context.startActivity(intent);
                }
            });

            RecyclerView recyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerview);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new RecyclerViewDecorationHorizontal(context, 12));

            List<Item> items = new ArrayList<>();
            /*Item[] item = new Item[ITEM_SIZE];
            item[0] = new Item(R.drawable.a, "실시간 공부하기 좋은 카페 빈자리", true);
            item[1] = new Item(R.drawable.b, "실시간 콘센트 있는 카페 빈자리", true);
            item[2] = new Item(R.drawable.c, "회식 장소", true);
            item[3] = new Item(R.drawable.d, "마카롱", true);
            item[4] = new Item(R.drawable.e, "마트 저녁 할인 상품", true);
            item[5] = new Item(R.drawable.a, "철봉, 평행봉 위치", true);

            for (int i = 0; i < ITEM_SIZE; i++) {
                items.add(item[i]);
            }*/
            for (int i = 0; i < ITEM_SIZE; i++) {
                if(collects.size() == i) {
                    break;
                }
                items.add(new Item(collects.get(i).getId(), collects.get(i).getImage(), collects.get(i).getCollect(), false));
            }

            recyclerView.setAdapter(new CardviewAdapter(context, items, R.layout.activity_main));


            /*ViewPager viewPager = itemView.findViewById(R.id.viewPager);
            FragmentAdapter fragmentAdapter = new FragmentAdapter(((MainActivity)context).getSupportFragmentManager());
            // ViewPager와  FragmentAdapter 연결
            viewPager.setAdapter(fragmentAdapter);

            viewPager.setClipToPadding(false);
            int dpValue = 16;
            float d = context.getResources().getDisplayMetrics().density;
            int margin = (int) (dpValue * d);
            viewPager.setPadding(margin, 0, margin, 0);
            viewPager.setPageMargin(margin/2);

            // FragmentAdapter에 Fragment 추가, Image 개수만큼 추가
            for (int i = 0; i < 4; i++) {
                ImageFragment imageFragment = new ImageFragment();
                Bundle args = new Bundle();
                args.putInt("imgRes", R.drawable.badge_item_count);
                imageFragment.setArguments(args);
                fragmentAdapter.addItem(imageFragment);
            }
            fragmentAdapter.notifyDataSetChanged();*/

        }
    }

    class NormalHolder extends RecyclerView.ViewHolder{
        private LinearLayout goToSpot;
        private TextView textView_place;
        private TextView mNameTv;
        private TextView textView_time;
        private ImageView imageView_more;
        private TextView textView_content;
        private ImageView imageView_contentImg;
        private ImageView imageView_contentImg2;
        private ImageView imageView_contentImg3;
        private TextView textView_comments;
        private RelativeLayout layout_comment;
        private RelativeLayout layout_like;
        private ImageView imageView10_like;
        private TextView textView11_like;
        private RelativeLayout layout_share;
        private RelativeLayout layout_save;
        private ImageView imageView1022;
        private TextView textView_save;
        private View border;
        public NormalHolder(View itemView) {
            super(itemView);
            goToSpot = itemView.findViewById(R.id.goToSpot);
            if(callFromSpotHome == true) {
                goToSpot.setVisibility(View.VISIBLE);
                //border_below_spotName.setVisibility(View.GONE);
            } else {
                goToSpot.setVisibility(View.GONE);
            }
            textView_place = (TextView) itemView.findViewById(R.id.textView_place);
            mNameTv = (TextView) itemView.findViewById(R.id.textView_nick);
            textView_time = (TextView) itemView.findViewById(R.id.textView_time);
            imageView_more = (ImageView) itemView.findViewById(R.id.imageView_more);
            textView_content = (TextView) itemView.findViewById(R.id.textView_content);
            imageView_contentImg = (ImageView) itemView.findViewById(R.id.imageView_contentImg);
            imageView_contentImg2 = (ImageView) itemView.findViewById(R.id.imageView_contentImg2);
            imageView_contentImg3 = (ImageView) itemView.findViewById(R.id.imageView_contentImg3);
            textView_comments = (TextView) itemView.findViewById(R.id.textView_comments);
            layout_comment = (RelativeLayout) itemView.findViewById(R.id.layout_comment);
            layout_like = (RelativeLayout) itemView.findViewById(R.id.layout_like);
            imageView10_like = (ImageView) itemView.findViewById(R.id.imageView10_like);
            textView11_like = (TextView) itemView.findViewById(R.id.textView11_like);
            layout_share = (RelativeLayout) itemView.findViewById(R.id.layout_share);
            layout_save = (RelativeLayout) itemView.findViewById(R.id.layout_save);
            imageView1022 = (ImageView) itemView.findViewById(R.id.imageView1022);
            textView_save = (TextView) itemView.findViewById(R.id.textView_save);
            border = (View) itemView.findViewById(R.id.border);
        }
    }

    /** 몇분전, 방금 전, */
    private static class TIME_MAXIMUM {

        public static final int SEC = 60;

        public static final int MIN = 60;

        public static final int HOUR = 24;

        public static final int DAY = 30;

        public static final int MONTH = 12;

    }



    public static String formatTimeString(Date tempDate) {



        long curTime = System.currentTimeMillis();

        long regTime = tempDate.getTime();

        long diffTime = (curTime - regTime) / 1000;



        String msg = null;

        if (diffTime < TIME_MAXIMUM.SEC) {

            // sec

            msg = "방금 전";

        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {

            // min

            msg = diffTime + "분 전";

        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {

            // hour

            msg = (diffTime) + "시간 전";

        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {

            // day

            msg = (diffTime) + "일 전";

        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {

            // day

            msg = (diffTime) + "달 전";

        } else {

            msg = (diffTime) + "년 전";

        }



        return msg;

    }

}
