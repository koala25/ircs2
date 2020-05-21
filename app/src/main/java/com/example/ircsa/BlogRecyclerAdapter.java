package com.example.ircsa;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<BlogPost> blog_list;
    public List<User> user_list;
    public Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    public BlogRecyclerAdapter(List<BlogPost> blog_list, List<User> user_list){

        this.blog_list = blog_list;
        this.user_list = user_list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_list_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.setIsRecyclable(false);

        final String blogPostId = blog_list.get(i).BlogPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        String desc_data = blog_list.get(i).getDesc();
        viewHolder.setDescText(desc_data);

        String image_url = blog_list.get(i).getImage_url();
        String thumbUri = blog_list.get(i).getImage_thumb();
        viewHolder.setBlogImage(image_url,thumbUri);

        String admin_user_id ="ObXcGonIhDRbNwrGQfHaJ6JyGYx2";



        String blog_user_id  = blog_list.get(i).getUser_id();

        if(blog_user_id.equals(currentUserId) || currentUserId.equals(admin_user_id)){

            viewHolder.blogDeleteBtn.setEnabled(true);
            viewHolder.blogDeleteBtn.setVisibility(View.VISIBLE);

        }
        //we read user information over here
/*
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){
*/

        String username = user_list.get(i).getName();
        String userImage = user_list.get(i).getImage();

        viewHolder.setUserData(username,userImage);
/*
                }else{

                }

            }
        });*/

        try{
            long millisecond = blog_list.get(i).getTimestamp().getTime();
            String dateString = DateFormat.format("dd/MM/yyyy", new Date(millisecond)).toString();
            viewHolder.setTime(dateString);


        }catch(Exception e) {
            Toast.makeText(context,"Time Exception: " + e.getMessage(),Toast.LENGTH_LONG).show();
        }

        //Get Likes Count
        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(!documentSnapshots.isEmpty()){

                    int count = documentSnapshots.size();

                    viewHolder.updateLikesCount(count);

                } else {

                    viewHolder.updateLikesCount(0);

                }

            }
        });


        //Get Likes
        firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if(documentSnapshot.exists()){

                    viewHolder.blogLikeBtn.setImageDrawable(context.getDrawable(R.drawable.action_like_accent));

                } else {

                    viewHolder.blogLikeBtn.setImageDrawable(context.getDrawable(R.drawable.action_like_gray));

                }

            }
        });

        //Likes Feature
        viewHolder.blogLikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){

                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).set(likesMap);

                        } else {

                            firebaseFirestore.collection("Posts/" + blogPostId + "/Likes").document(currentUserId).delete();

                        }

                    }
                });
            }
        });

        viewHolder.blogCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent commentIntent = new Intent(context, CommentsActivity.class);
                commentIntent.putExtra("blog_post_id", blogPostId);
                context.startActivity(commentIntent);

            }
        });


        viewHolder.blogDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseFirestore.collection("Posts").document(blogPostId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        blog_list.remove(i);
                        user_list.remove(i);
                    }
                });



            }
        });

        firebaseFirestore.collection("Posts/" + blogPostId + "/Comments").addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(!documentSnapshots.isEmpty()){

                    int count = documentSnapshots.size();

                    viewHolder.updateCommentsCount(count);
                } else {

                    viewHolder.updateCommentsCount(0);

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;

        private TextView descView;
        private ImageView blogImageView;
        private TextView blogDate;

        private TextView blogUsername;
        private CircleImageView blogUserImage;

        private ImageView blogLikeBtn;
        private TextView blogLikeCount;

        private ImageView blogCommentBtn;
        private TextView blogCommentCount;
        private Button blogDeleteBtn;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            blogLikeBtn = mView.findViewById(R.id.blog_like_btn);
            blogCommentBtn = mView.findViewById(R.id.blog_comment_icon);
            blogDeleteBtn = mView.findViewById(R.id.blog_delete_btn);
        }

        public void setDescText(String descText){

            descView = mView.findViewById(R.id.blog_desc);
            descView.setText(descText);
        }

        public void setBlogImage(String downloadUri, String thumbUri){

            blogImageView = mView.findViewById(R.id.blog_image);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.image_placeholder);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(blogImageView);

        }

        public void setTime(String date) {

            blogDate = mView.findViewById(R.id.blog_date);
            blogDate.setText(date);

        }




        public void setUserData(String name, String Image){

            blogUserImage = mView.findViewById(R.id.blog_user_image);
            blogUsername = mView.findViewById(R.id.blog_user_name);

            blogUsername.setText(name);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_placeholder);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(Image).into(blogUserImage);
        }

        public void updateLikesCount(int count){

            blogLikeCount = mView.findViewById(R.id.blog_like_count);
            blogLikeCount.setText(count + " Likes");

        }

        public void updateCommentsCount(int count){

            blogCommentCount = mView.findViewById(R.id.blog_comment_count);
            blogCommentCount.setText(count + " Comments");
        }

    }
}