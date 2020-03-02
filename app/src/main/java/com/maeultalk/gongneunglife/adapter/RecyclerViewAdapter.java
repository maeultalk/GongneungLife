package com.maeultalk.gongneunglife.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.maeultalk.gongneunglife.R;
import com.maeultalk.gongneunglife.activity.CollectActivity;
import com.maeultalk.gongneunglife.activity.CollectAllActivity;
import com.maeultalk.gongneunglife.activity.CommentsActivity;
import com.maeultalk.gongneunglife.activity.EditContentActivity;
import com.maeultalk.gongneunglife.activity.MainActivity;
import com.maeultalk.gongneunglife.activity.PlaceActivity;
import com.maeultalk.gongneunglife.fragment.ImageFragment;
import com.maeultalk.gongneunglife.model.Content;
import com.maeultalk.gongneunglife.model.ContentImage;
import com.maeultalk.gongneunglife.model.Item;
import com.maeultalk.gongneunglife.request.DeleteContentRequest;
import com.maeultalk.gongneunglife.request.FavorOffRequest;
import com.maeultalk.gongneunglife.request.FavorOnRequest;
import com.maeultalk.gongneunglife.request.GoodOffRequest;
import com.maeultalk.gongneunglife.request.GoodOnRequest;
import com.maeultalk.gongneunglife.util.RecyclerViewDecorationHorizontal;
import com.squareup.picasso.Picasso;
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
import static com.maeultalk.gongneunglife.fragment.mainActivity.HomeFragment.contentsInHome;
import static com.maeultalk.gongneunglife.fragment.placeActivity.TimeLineFragment.contentsInPlace;
import static com.maeultalk.gongneunglife.key.Key.URL_IMAGES;

/**
 * Created by charlie on 2017. 4. 24..
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    Fragment fragment;
    // 어떤 위치에서 부르는지(홈프래그먼트 or 플레이스액티비티)
    boolean callFromSpotHome;
//    ArrayList<Content> contents = contentsInHome;

    public static final int VIEW_TYPE_NORMAL = 0;
    public static final int VIEW_TYPE_COLLECT = 1;
    private final int VIEW_TYPE_LOADING = 2;

    public RecyclerViewAdapter(Context context, Fragment fragment, boolean callFromSpotHome, ArrayList<Content> contents){
        this.context = context;
        this.fragment = fragment;
        this.callFromSpotHome = callFromSpotHome;
//        this.contents = contents;
    }

    // 새로운 뷰 홀더 생성
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_NORMAL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content, parent,false);
            return new NormalHolder(view, fragment);
        } else if (viewType == VIEW_TYPE_COLLECT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collect, parent,false);
            return new CollectHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position!=1) {
            final int pos2;
            if(position ==0) {
                pos2 = position;
            } else {
                pos2 = position-1;
            }
            return contentsInHome.get(pos2) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_COLLECT;
        }
    }
    
    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof CollectHolder) {

        } else if (holder instanceof NormalHolder) {
            final int pos2;
            if(position ==0) {
                pos2 = position;
            } else {
                pos2 = position-1;
            }
            final Content content = contentsInHome.get(pos2);
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
                                        intent.putExtra("from", "home");
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
                                                                contentsInHome.remove(pos2);
                                                                RecyclerViewAdapter.this.notifyDataSetChanged();
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

//            MultiTransformation multiTransformation = new MultiTransformation(new CenterCrop());

            final List<ContentImage> images = new ArrayList<>();
            if(!TextUtils.isEmpty(contentsInHome.get(pos2).getImage())) {
                ((NormalHolder) holder).imageView_contentImg.setVisibility(View.VISIBLE);
                Glide.with(context).load(URL_IMAGES + content.getImage())/*.fit()*//*.listener(requestListener)*//*.override(100)*//*.centerCrop()*//*.apply(RequestOptions.bitmapTransform(multiTransformation))*//*.thumbnail(0.1f)*/.into(((NormalHolder) holder).imageView_contentImg);
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
            if(!TextUtils.isEmpty(contentsInHome.get(pos2).getImage2())) {
                ((NormalHolder) holder).imageView_contentImg2.setVisibility(View.VISIBLE);
                Glide.with(context).load(URL_IMAGES + content.getImage2())/*.fit()*//*.override(100)*//*.centerCrop()*//*.apply(RequestOptions.bitmapTransform(multiTransformation))*//*.thumbnail(0.1f)*/.into(((NormalHolder) holder).imageView_contentImg2);
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
            if(!TextUtils.isEmpty(contentsInHome.get(pos2).getImage3())) {
                ((NormalHolder) holder).imageView_contentImg3.setVisibility(View.VISIBLE);
                Glide.with(context).load(URL_IMAGES + content.getImage3())/*.fit()*//*.override(100)*//*.centerCrop()*//*.apply(RequestOptions.bitmapTransform(multiTransformation))*//*.thumbnail(0.1f)*/.into(((NormalHolder) holder).imageView_contentImg3);
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

            // 동영상
            if(!TextUtils.isEmpty(contentsInHome.get(pos2).getYoutube())) {
                ((NormalHolder) holder).border.setVisibility(View.GONE);
                ((NormalHolder) holder).button.setVisibility(View.VISIBLE);
                final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }

                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailView.setVisibility(View.VISIBLE);
//                holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
                    }
                };

                ((NormalHolder) holder).youTubeThumbnailView.initialize("AIzaSyCVuF6-gx1ih3T6sxC_EpCwrI3XARn4FGI", new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                        youTubeThumbnailLoader.setVideo(contentsInHome.get(pos2).getYoutube());
                        youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                        //write something for failure
                    }
                });

                ((NormalHolder) holder).button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, "AIzaSyCVuF6-gx1ih3T6sxC_EpCwrI3XARn4FGI", contentsInHome.get(pos2).getYoutube(), 0, true, true);
                        context.startActivity(intent);
                    }
                });
                /*YouTubePlayer.OnInitializedListener listener = new YouTubePlayer.OnInitializedListener(){
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(contentsInHome.get(pos2).getYoutube());
                        youTubePlayer.setFullscreen(false);
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    }
                };*/
//                ((NormalHolder) holder).youtubeView.initialize("AIzaSyCVuF6-gx1ih3T6sxC_EpCwrI3XARn4FGI", listener);
                /*((NormalHolder) holder).youTubePlayerFragment.initialize("DEVELOPER_KEY", new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(contentsInHome.get(pos2).getYoutube());
                        youTubePlayer.setFullscreen(false);
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    }
                });*/
//                FragmentTransaction transaction = context.getChildFragmentManager().beginTransaction();
//                transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();
            } else {
                ((NormalHolder) holder).button.setVisibility(View.GONE);
            }

            ((NormalHolder) holder).textView_comments.setText("댓글 " + content.getComments());
            ((NormalHolder) holder).layout_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, CommentsActivity.class);
                    intent.putExtra("id", content.getId());
                    intent.putExtra("content", content.getContent());
                    intent.putExtra("from", "home");
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
            if(contentsInHome.get(pos2).isGood()) {
                ((NormalHolder) holder).imageView10_like.setImageResource(R.drawable.like4);
                ((NormalHolder) holder).textView11_like.setTextColor(Color.parseColor("#ffc107"));
            } else {
                ((NormalHolder) holder).imageView10_like.setImageResource(R.drawable.like3);
                ((NormalHolder) holder).textView11_like.setTextColor(Color.parseColor("#808080"));
            }
            if(contentsInHome.get(pos2).getGood_cnt().equals("0")) {
                ((NormalHolder) holder).textView11_like.setText("좋아요");
            } else {
                ((NormalHolder) holder).textView11_like.setText(contentsInHome.get(pos2).getGood_cnt());
            }
            ((NormalHolder) holder).layout_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(contentsInHome.get(pos2).isGood()) {
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
                                        contentsInHome.get(pos2).setGood(false);
                                        contentsInHome.get(pos2).setGood_cnt((Integer.valueOf(contentsInHome.get(pos2).getGood_cnt()) - 1) + "");
                                        if(contentsInHome.get(pos2).getGood_cnt().equals("0")) {
                                            ((NormalHolder) holder).textView11_like.setText("좋아요");
                                        } else {
                                            ((NormalHolder) holder).textView11_like.setText(contentsInHome.get(pos2).getGood_cnt());
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
                                        contentsInHome.get(pos2).setGood(true);
                                        contentsInHome.get(pos2).setGood_cnt((Integer.valueOf(contentsInHome.get(pos2).getGood_cnt()) + 1) + "");
                                        if(contentsInHome.get(pos2).getGood_cnt().equals("0")) {
                                            ((NormalHolder) holder).textView11_like.setText("좋아요");
                                        } else {
                                            ((NormalHolder) holder).textView11_like.setText(contentsInHome.get(pos2).getGood_cnt());
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

            if(contentsInHome.get(pos2).isFavorite()) {
                ((NormalHolder) holder).imageView1022.setImageResource(R.drawable.folder_closed);
                ((NormalHolder) holder).textView_save.setTextColor(Color.parseColor("#ffc107"));
            } else {
                ((NormalHolder) holder).imageView1022.setImageResource(R.drawable.folder_open);
                ((NormalHolder) holder).textView_save.setTextColor(Color.parseColor("#808080"));
            }
            ((NormalHolder) holder).layout_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(contentsInHome.get(pos2).isFavorite()) {
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
                                        contentsInHome.get(pos2).setFavorite(false);
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
                                        contentsInHome.get(pos2).setFavorite(true);
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
        return contentsInHome.size() + 1;
//        return 7;
    }

    /*@Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        Toast.makeText(context, "attach: " + String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        Toast.makeText(context, "detach: " + String.valueOf(holder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
    }*/

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
            recyclerView.addItemDecoration(new RecyclerViewDecorationHorizontal(context, 10));

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
//        private YouTubePlayerView youtubeView;
//        Fragment fragment;
//        YouTubePlayerSupportFragment youTubePlayerFragment;
        private YouTubeThumbnailView youTubeThumbnailView;
        private RelativeLayout button;
        private TextView textView_comments;
        private RelativeLayout layout_comment;;
        private RelativeLayout layout_like;
        private ImageView imageView10_like;
        private TextView textView11_like;
        private RelativeLayout layout_share;
        private RelativeLayout layout_save;
        private ImageView imageView1022;
        private TextView textView_save;
        private View border;
        public NormalHolder(View itemView, Fragment fragment) {
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
//            youtubeView = (YouTubePlayerView) itemView. findViewById(R.id.youtubeView);
//            this.fragment = fragment;
//            youTubePlayerFragment = (YouTubePlayerSupportFragment) fragment.getChildFragmentManager().findFragmentById(R.id.youtube_fragment);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtubeView);
            button = (RelativeLayout) itemView.findViewById(R.id.button);
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

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
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
