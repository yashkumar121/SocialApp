package bluetooth_demo.kct.com.socialdemobyfirebase.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bluetooth_demo.kct.com.socialdemobyfirebase.R;
import bluetooth_demo.kct.com.socialdemobyfirebase.bean.Post;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>
{
    Context context;
    ArrayList<Post> postArrayList;
    DatabaseReference reference;
    public PostAdapter(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
        this.reference= FirebaseDatabase.getInstance().getReference().child("Post");
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        View v=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.postview,viewGroup,false);
        return new PostViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder postViewHolder, final int i)
    {
        postViewHolder.name.setText(postArrayList.get(i).getUserName());
        postViewHolder.postContent.setText(postArrayList.get(i).getContent());
        if(postArrayList.get(i).getNoOfLikes()!=0)
         postViewHolder.noOfLikes.setText(""+postArrayList.get(i).getNoOfLikes());
        else
            postViewHolder.noOfLikes.setText("");

        postViewHolder.timeOfPost.setText(getDate(postArrayList.get(i).getTimeStamp()));
        Picasso.get().load(postArrayList.get(i).getProfileDp()).into(postViewHolder.dp);
        postViewHolder.like.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Post post=postArrayList.get(i);
                post.setNoOfLikes(post.getNoOfLikes()+1);
                reference.child(post.getReference()).setValue(post);

            }
        });

        postViewHolder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");

                share.putExtra(Intent.EXTRA_STREAM,postArrayList.get(i).getContent());

                context.startActivity(Intent.createChooser(share, "Share Image"));

            }
        });
    }
    private String getDate(long time)
    {
        String date = DateFormat.format("MMM dd, yyyy hh:mm a", time).toString();
        return date;
    }
    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public void setList(ArrayList<Post> posts)
    {
        postArrayList=posts;
        notifyDataSetChanged();
    }

    public class PostViewHolder extends RecyclerView.ViewHolder
    {
        TextView name,like,share,noOfLikes,timeOfPost,postContent;
        ImageView dp;

        public PostViewHolder(@NonNull View itemView)
        {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            postContent=itemView.findViewById(R.id.post_content);
            like=itemView.findViewById(R.id.like);
            share=itemView.findViewById(R.id.share);
            noOfLikes=itemView.findViewById(R.id.no_of_likes);
            timeOfPost=itemView.findViewById(R.id.time_of_post);
            dp=itemView.findViewById(R.id.dp);
        }
    }


}

