package bluetooth_demo.kct.com.socialdemobyfirebase.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

import bluetooth_demo.kct.com.socialdemobyfirebase.R;
import bluetooth_demo.kct.com.socialdemobyfirebase.adapter.PostAdapter;
import bluetooth_demo.kct.com.socialdemobyfirebase.bean.Post;
import bluetooth_demo.kct.com.socialdemobyfirebase.util.AVLoadingIndicatorView;
import bluetooth_demo.kct.com.socialdemobyfirebase.util.AppPrefernce;


public class HomePageFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;


    public HomePageFragment() {
        // Required empty public constructor
    }

    FirebaseDatabase mFirebaseDatabase;
    RecyclerView recyclerView;
    PostAdapter adapter;
    EditText postContent;
    TextView name;
    ImageView dp,postDone;
    AVLoadingIndicatorView loader;

    public static HomePageFragment newInstance(String param1)
    {

        HomePageFragment fragment = new HomePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mParam1 = getArguments().getString(ARG_PARAM1);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        dp=view.findViewById(R.id.dp);
        name=view.findViewById(R.id.name);
        postContent=view.findViewById(R.id.post_content);
        loader=view.findViewById(R.id.loader);
        postDone=view.findViewById(R.id.post_done);
        recyclerView=view.findViewById(R.id.recycler);

        setData();
    }

    ArrayList<Post> posts;
    private void setData()
    {
        loader.smoothToShow();
        AppPrefernce prefernce=new AppPrefernce(getActivity());
        Picasso.get().load(prefernce.getString(AppPrefernce.PROFILEDP)).into(dp);
        name.setText(prefernce.getString(AppPrefernce.USERNAME));
        postDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setPostOnFireBase();

            }
        });



        posts=new ArrayList<>();
        adapter=new PostAdapter(getActivity(),posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);


        addNewPostListener(prefernce);

    }

    private void addNewPostListener(AppPrefernce prefernce)
    {
        DatabaseReference reference=mFirebaseDatabase.getReference().child("Post");

        final String currentUserId=prefernce.getString(AppPrefernce.TOKENKEY);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                loader.smoothToHide();
                posts.clear();
                Iterator<DataSnapshot> postList= (Iterator<DataSnapshot>) dataSnapshot.getChildren().iterator();
                while (postList.hasNext())
                {
                    DataSnapshot snapshot=postList.next();
                    Post post=snapshot.getValue(Post.class);
                    post.setReference(snapshot.getKey());

                    if(mParam1.equalsIgnoreCase("myProfile") && !post.getEmailId().equalsIgnoreCase(currentUserId))
                        continue;
                    posts.add(0,post);
                }
                adapter.setList(posts);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

    }

    private void setPostOnFireBase()
    {
        String content=postContent.getText().toString();
        postContent.setText("");
        if(content.equalsIgnoreCase(""))
            Toast.makeText(getActivity(),"Please enter content to be posted",Toast.LENGTH_SHORT).show();
        else
            {
            AppPrefernce prefernce = new AppPrefernce(getActivity());
            Post post = new Post();
            post.setContent(content);
            post.setTimeStamp(System.currentTimeMillis());
            post.setNoOfLikes(0);
            post.setUserName(prefernce.getString(AppPrefernce.USERNAME));
            post.setEmailId(prefernce.getString(AppPrefernce.TOKENKEY));
            post.setProfileDp(prefernce.getString(AppPrefernce.PROFILEDP));

            mFirebaseDatabase.getReference()
                    .child("Post")
                    .child(mFirebaseDatabase.getReference().push().getKey())
                    .setValue(post);
        }
    }
}
