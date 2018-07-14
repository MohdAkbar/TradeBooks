package com.example.akbar.tradebooks;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Akbar on 6/23/2018.
 */
public class HomePage extends AppCompatActivity{

    Toolbar homePageToolbar;
    DrawerLayout homePageDrawerLayout;
    NavigationView homePageNavigationView;
    ImageView genreEducation,genreKids,genreThriller,genreMystery,genreBiography,genreStory,genreOthers;
    ImageView recommendedImageA,recommendedImageB,recommendedImageC,recommendedImageD,recommendedImageE,recommendedImageF,recommendedOthers;
    AppCompatTextView recommendedTextA,recommendedTextB,recommendedTextC,recommendedTextD,recommendedTextE,recommendedTextF;
    ImageView trendingImageA,trendingImageB,trendingImageC,trendingImageD,trendingImageE,trendingImageF,trendingOthers;
    AppCompatTextView trendingTextA,trendingTextB,trendingTextC,trendingTextD,trendingTextE,trendingTextF;
    ImageView freshImageA,freshImageB,freshImageC,freshImageD,freshImageE,freshImageF,freshOthers;
    AppCompatTextView freshTextA,freshTextB,freshTextC,freshTextD,freshTextE,freshTextF;
    ImageView topImageA,topImageB,topImageC,topImageD,topImageE,topImageF,topOthers;
    AppCompatTextView topTextA,topTextB,topTextC,topTextD,topTextE,topTextF;
    ImageView tradeImageA,tradeImageB,tradeImageC,tradeImageD,tradeImageE,tradeImageF,tradeOthers;
    AppCompatTextView tradeTextA,tradeTextB,tradeTextC,tradeTextD,tradeTextE,tradeTextF;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,reference2,reference3;
    FirebaseAuth auth;
    AlertDialog resetDialog;
    AlertDialog.Builder resetDialogBuilder;
    final List<Book> bookList=new ArrayList<>();
    final List<Book> bookListTrending=new ArrayList<>();
    final List<Book> bookListTopDeals=new ArrayList<>();
    final List<Book> bookListFreshDeals=new ArrayList<>();
    final List<Book> bookListTrade=new ArrayList<>();
    List<Book> bookListRecycler=new ArrayList<>();
    RecyclerView homePageRecyclerView;//
    MoreBooksAdapter adapter;//
    AutoCompleteTextView searchTextView;
    ArrayList<String> bookNames;
    ArrayAdapter<String> bookNamesAdapter;
    int visible=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_navigation_drawer);
        firebaseDatabase=FirebaseDatabase.getInstance();
        auth=FirebaseAuth.getInstance();
        homePageToolbar=(Toolbar)findViewById(R.id.homePageToolbar);
        homePageToolbar.setTitle("Home Page");
        setSupportActionBar(homePageToolbar);
        searchTextView=(AutoCompleteTextView)findViewById(R.id.searchTextView);
        searchTextView.setVisibility(View.GONE);
        homePageDrawerLayout=(DrawerLayout)findViewById(R.id.homePageDrawerLayout);
        homePageNavigationView=(NavigationView)findViewById(R.id.homePageNavigationView);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(HomePage.this,homePageDrawerLayout,homePageToolbar,R.string.openDrawer,R.string.closeDrawer);
        homePageDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        homePageNavigationView.setNavigationItemSelectedListener(navigationItemListener);
        View header=homePageNavigationView.getHeaderView(0);
        setHeaderInformation(header);
        setRecommendedInformation();
        setTrendingInformation();
        setTopDealsInformation();
        setFreshDealsInformation();
        setTradeBooksInformation();
        setGenreInformation();
        //setEverything();
        homePageRecyclerView=(RecyclerView)findViewById(R.id.homePageRecyclerView);//
        LinearLayoutManager manager=new LinearLayoutManager(this);//
        homePageRecyclerView.setLayoutManager(manager);//
        adapter=new MoreBooksAdapter(bookListRecycler,HomePage.this);//
        homePageRecyclerView.setAdapter(adapter);//
        homePageRecyclerView.setNestedScrollingEnabled(false);//
        showHomePageRecyclerView();//
        adapter.notifyDataSetChanged();//

    }

    private void setEverything() {
        reference3=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByChild("name").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if(!ob.isSold())
                    {
                        bookNames.add(ob.getName());
                        if(bookNames.size()==dataSnapshot.getChildrenCount())
                        {
                            //bookNamesAdapter=new ArrayAdapter<String>(HomePage.this,android.R.layout.simple_list_item_1,bookNames);
                            //searchTextView.setAdapter(bookNamesAdapter);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        searchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i== EditorInfo.IME_ACTION_GO)
                {

                    return true;
                }
                return false;
            }
        });
    }


    private void showHomePageRecyclerView() {
        reference2=firebaseDatabase.getReference().child("Books");
        reference2.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if(!ob.isSold())
                    {
                        ob.setBookId(dataSnapshot.getKey());
                        bookListRecycler.add(ob);
                        adapter.notifyDataSetChanged();
                    }
                }

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void setGenreInformation() {

        genreEducation=(ImageView)findViewById(R.id.genreEducation);
        genreEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","GE");
                startActivity(intent);
            }
        });
        genreKids=(ImageView)findViewById(R.id.genreKids);
        genreKids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","GK");
                startActivity(intent);
            }
        });
        genreThriller=(ImageView)findViewById(R.id.genreThriller);
        genreThriller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","GT");
                startActivity(intent);
            }
        });
        genreMystery=(ImageView)findViewById(R.id.genreMystery);
        genreMystery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","GM");
                startActivity(intent);
            }
        });
        genreBiography=(ImageView)findViewById(R.id.genreBiography);
        genreBiography.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","GB");
                startActivity(intent);
            }
        });
        genreStory=(ImageView)findViewById(R.id.genreStory);
        genreStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","GS");
                startActivity(intent);
            }
        });
        genreOthers=(ImageView)findViewById(R.id.genreOthers);
        genreOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","GO");
                startActivity(intent);
            }
        });
    }

    private void setTradeBooksInformation() {
        tradeImageA=(ImageView)findViewById(R.id.tradeImageA);
        tradeImageB=(ImageView)findViewById(R.id.tradeImageB);
        tradeImageC=(ImageView)findViewById(R.id.tradeImageC);
        tradeImageD=(ImageView)findViewById(R.id.tradeImageD);
        tradeImageE=(ImageView)findViewById(R.id.tradeImageE);
        tradeImageF=(ImageView)findViewById(R.id.tradeImageF);
        tradeOthers=(ImageView)findViewById(R.id.tradeOthers);
        tradeTextA=(AppCompatTextView)findViewById(R.id.tradeTextA);
        tradeTextB=(AppCompatTextView)findViewById(R.id.tradeTextB);
        tradeTextC=(AppCompatTextView)findViewById(R.id.tradeTextC);
        tradeTextD=(AppCompatTextView)findViewById(R.id.tradeTextD);
        tradeTextE=(AppCompatTextView)findViewById(R.id.tradeTextE);
        tradeTextF=(AppCompatTextView)findViewById(R.id.tradeTextF);
        tradeOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","TH");
                startActivity(intent);
            }
        });
        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByChild("sellerId").equalTo("O2vEnUPeZtQzWCI6S8kmQT6ugWD2").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if((!ob.isSold())&&(bookListTrade.size()<=6)) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookListTrade.add(ob);
                        if(bookListTrade.size()==6)
                        {
                            tradeTextA.setText(bookListTrade.get(0).getName());
                            tradeTextB.setText(bookListTrade.get(1).getName());
                            tradeTextC.setText(bookListTrade.get(2).getName());
                            tradeTextD.setText(bookListTrade.get(3).getName());
                            tradeTextE.setText(bookListTrade.get(4).getName());
                            tradeTextF.setText(bookListTrade.get(5).getName());
                            Glide.with(HomePage.this).load(bookListTrade.get(0).getPhotoUrl())
                                    .into(tradeImageA);
                            Glide.with(HomePage.this).load(bookListTrade.get(1).getPhotoUrl())
                                    .into(tradeImageB);
                            Glide.with(HomePage.this).load(bookListTrade.get(2).getPhotoUrl())
                                    .into(tradeImageC);
                            Glide.with(HomePage.this).load(bookListTrade.get(3).getPhotoUrl())
                                    .into(tradeImageD);
                            Glide.with(HomePage.this).load(bookListTrade.get(4).getPhotoUrl())
                                    .into(tradeImageE);
                            Glide.with(HomePage.this).load(bookListTrade.get(5).getPhotoUrl())
                                    .into(tradeImageF);
                            tradeImageA.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTrade.get(0).getBookId());
                                    startActivity(intent);
                                }
                            });
                            tradeImageB.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTrade.get(1).getBookId());
                                    startActivity(intent);
                                }
                            });
                            tradeImageC.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTrade.get(2).getBookId());
                                    startActivity(intent);
                                }
                            });
                            tradeImageD.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTrade.get(3).getBookId());
                                    startActivity(intent);
                                }
                            });
                            tradeImageE.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTrade.get(4).getBookId());
                                    startActivity(intent);
                                }
                            });
                            tradeImageF.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTrade.get(5).getBookId());
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }


    private void setFreshDealsInformation() {

        freshImageA=(ImageView)findViewById(R.id.freshImageA);
        freshImageB=(ImageView)findViewById(R.id.freshImageB);
        freshImageC=(ImageView)findViewById(R.id.freshImageC);
        freshImageD=(ImageView)findViewById(R.id.freshImageD);
        freshImageE=(ImageView)findViewById(R.id.freshImageE);
        freshImageF=(ImageView)findViewById(R.id.freshImageF);
        freshOthers=(ImageView)findViewById(R.id.freshOthers);
        freshTextA=(AppCompatTextView)findViewById(R.id.freshTextA);
        freshTextB=(AppCompatTextView)findViewById(R.id.freshTextB);
        freshTextC=(AppCompatTextView)findViewById(R.id.freshTextC);
        freshTextD=(AppCompatTextView)findViewById(R.id.freshTextD);
        freshTextE=(AppCompatTextView)findViewById(R.id.freshTextE);
        freshTextF=(AppCompatTextView)findViewById(R.id.freshTextF);
        freshOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","F");
                startActivity(intent);
            }
        });
        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if((!ob.isSold())&&(bookListFreshDeals.size()<=6)) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookListFreshDeals.add(ob);
                        if (bookListFreshDeals.size() == 6) {
                            freshTextA.setText(bookListFreshDeals.get(0).getName());
                            freshTextB.setText(bookListFreshDeals.get(1).getName());
                            freshTextC.setText(bookListFreshDeals.get(2).getName());
                            freshTextD.setText(bookListFreshDeals.get(3).getName());
                            freshTextE.setText(bookListFreshDeals.get(4).getName());
                            freshTextF.setText(bookListFreshDeals.get(5).getName());
                            Glide.with(HomePage.this).load(bookListFreshDeals.get(0).getPhotoUrl())
                                    .into(freshImageA);
                            Glide.with(HomePage.this).load(bookListFreshDeals.get(1).getPhotoUrl())
                                    .into(freshImageB);
                            Glide.with(HomePage.this).load(bookListFreshDeals.get(2).getPhotoUrl())
                                    .into(freshImageC);
                            Glide.with(HomePage.this).load(bookListFreshDeals.get(3).getPhotoUrl())
                                    .into(freshImageD);
                            Glide.with(HomePage.this).load(bookListFreshDeals.get(4).getPhotoUrl())
                                    .into(freshImageE);
                            Glide.with(HomePage.this).load(bookListFreshDeals.get(5).getPhotoUrl())
                                    .into(freshImageF);
                            freshImageA.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListFreshDeals.get(0).getBookId());
                                    startActivity(intent);
                                }
                            });
                            freshImageB.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListFreshDeals.get(1).getBookId());
                                    startActivity(intent);
                                }
                            });
                            freshImageC.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListFreshDeals.get(2).getBookId());
                                    startActivity(intent);
                                }
                            });
                            freshImageD.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListFreshDeals.get(3).getBookId());
                                    startActivity(intent);
                                }
                            });
                            freshImageE.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListFreshDeals.get(4).getBookId());
                                    startActivity(intent);
                                }
                            });
                            freshImageF.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListFreshDeals.get(5).getBookId());
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }
                else
                    Toast.makeText(getApplicationContext(),"Ob null",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void setTopDealsInformation() {

        topImageA=(ImageView)findViewById(R.id.topImageA);
        topImageB=(ImageView)findViewById(R.id.topImageB);
        topImageC=(ImageView)findViewById(R.id.topImageC);
        topImageD=(ImageView)findViewById(R.id.topImageD);
        topImageE=(ImageView)findViewById(R.id.topImageE);
        topImageF=(ImageView)findViewById(R.id.topImageF);
        topOthers=(ImageView)findViewById(R.id.topOthers);
        topTextA=(AppCompatTextView)findViewById(R.id.topTextA);
        topTextB=(AppCompatTextView)findViewById(R.id.topTextB);
        topTextC=(AppCompatTextView)findViewById(R.id.topTextC);
        topTextD=(AppCompatTextView)findViewById(R.id.topTextD);
        topTextE=(AppCompatTextView)findViewById(R.id.topTextE);
        topTextF=(AppCompatTextView)findViewById(R.id.topTextF);
        topOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","D");
                startActivity(intent);
            }
        });
        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByChild("price").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if((!ob.isSold())&&(bookListTopDeals.size()<=6)) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookListTopDeals.add(ob);
                        if (bookListTopDeals.size() == 6) {
                            topTextA.setText(bookListTopDeals.get(0).getName());
                            topTextB.setText(bookListTopDeals.get(1).getName());
                            topTextC.setText(bookListTopDeals.get(2).getName());
                            topTextD.setText(bookListTopDeals.get(3).getName());
                            topTextE.setText(bookListTopDeals.get(4).getName());
                            topTextF.setText(bookListTopDeals.get(5).getName());
                            Glide.with(HomePage.this).load(bookListTopDeals.get(0).getPhotoUrl())
                                    .into(topImageA);
                            Glide.with(HomePage.this).load(bookListTopDeals.get(1).getPhotoUrl())
                                    .into(topImageB);
                            Glide.with(HomePage.this).load(bookListTopDeals.get(2).getPhotoUrl())
                                    .into(topImageC);
                            Glide.with(HomePage.this).load(bookListTopDeals.get(3).getPhotoUrl())
                                    .into(topImageD);
                            Glide.with(HomePage.this).load(bookListTopDeals.get(4).getPhotoUrl())
                                    .into(topImageE);
                            Glide.with(HomePage.this).load(bookListTopDeals.get(5).getPhotoUrl())
                                    .into(topImageF);
                            topImageA.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTopDeals.get(0).getBookId());
                                    startActivity(intent);
                                }
                            });
                            topImageB.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTopDeals.get(1).getBookId());
                                    startActivity(intent);
                                }
                            });
                            topImageC.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTopDeals.get(2).getBookId());
                                    startActivity(intent);
                                }
                            });
                            topImageD.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTopDeals.get(3).getBookId());
                                    startActivity(intent);
                                }
                            });
                            topImageE.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTopDeals.get(4).getBookId());
                                    startActivity(intent);
                                }
                            });
                            topImageF.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTopDeals.get(5).getBookId());
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }
                else
                    Toast.makeText(getApplicationContext(),"Ob null",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void setTrendingInformation() {

        trendingImageA=(ImageView)findViewById(R.id.trendingImageA);
        trendingImageB=(ImageView)findViewById(R.id.trendingImageB);
        trendingImageC=(ImageView)findViewById(R.id.trendingImageC);
        trendingImageD=(ImageView)findViewById(R.id.trendingImageD);
        trendingImageE=(ImageView)findViewById(R.id.trendingImageE);
        trendingImageF=(ImageView)findViewById(R.id.trendingImageF);
        trendingOthers=(ImageView)findViewById(R.id.trendingOthers);
        trendingTextA=(AppCompatTextView)findViewById(R.id.trendingTextA);
        trendingTextB=(AppCompatTextView)findViewById(R.id.trendingTextB);
        trendingTextC=(AppCompatTextView)findViewById(R.id.trendingTextC);
        trendingTextD=(AppCompatTextView)findViewById(R.id.trendingTextD);
        trendingTextE=(AppCompatTextView)findViewById(R.id.trendingTextE);
        trendingTextF=(AppCompatTextView)findViewById(R.id.trendingTextF);
        trendingOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","T");
                startActivity(intent);
            }
        });
        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByChild("views").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null)
                {
                    if((!ob.isSold())&&(bookListTrending.size()<=6)) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookListTrending.add(ob);
                        if (bookListTrending.size() == 6) {
                            trendingTextA.setText(bookListTrending.get(0).getName());
                            trendingTextB.setText(bookListTrending.get(1).getName());
                            trendingTextC.setText(bookListTrending.get(2).getName());
                            trendingTextD.setText(bookListTrending.get(3).getName());
                            trendingTextE.setText(bookListTrending.get(4).getName());
                            trendingTextF.setText(bookListTrending.get(5).getName());
                            Glide.with(HomePage.this).load(bookListTrending.get(0).getPhotoUrl())
                                    .into(trendingImageA);
                            Glide.with(HomePage.this).load(bookListTrending.get(1).getPhotoUrl())
                                    .into(trendingImageB);
                            Glide.with(HomePage.this).load(bookListTrending.get(2).getPhotoUrl())
                                    .into(trendingImageC);
                            Glide.with(HomePage.this).load(bookListTrending.get(3).getPhotoUrl())
                                    .into(trendingImageD);
                            Glide.with(HomePage.this).load(bookListTrending.get(4).getPhotoUrl())
                                    .into(trendingImageE);
                            Glide.with(HomePage.this).load(bookListTrending.get(5).getPhotoUrl())
                                    .into(trendingImageF);
                            trendingImageA.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTrending.get(0).getBookId());
                                    startActivity(intent);
                                }
                            });
                            trendingImageB.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTrending.get(1).getBookId());
                                    startActivity(intent);
                                }
                            });
                            trendingImageC.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTrending.get(2).getBookId());
                                    startActivity(intent);
                                }
                            });
                            trendingImageD.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTrending.get(3).getBookId());
                                    startActivity(intent);
                                }
                            });
                            trendingImageE.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTrending.get(4).getBookId());
                                    startActivity(intent);
                                }
                            });
                            trendingImageF.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookListTrending.get(5).getBookId());
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }
                else
                    Toast.makeText(getApplicationContext(),"Ob null",Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setRecommendedInformation() {

        recommendedImageA=(ImageView)findViewById(R.id.recommendedImageA);
        recommendedImageB=(ImageView)findViewById(R.id.recommendedImageB);
        recommendedImageC=(ImageView)findViewById(R.id.recommendedImageC);
        recommendedImageD=(ImageView)findViewById(R.id.recommendedImageD);
        recommendedImageE=(ImageView)findViewById(R.id.recommendedImageE);
        recommendedImageF=(ImageView)findViewById(R.id.recommendedImageF);
        recommendedOthers=(ImageView)findViewById(R.id.recommendedOthers);
        recommendedTextA=(AppCompatTextView)findViewById(R.id.recommendedTextA);
        recommendedTextB=(AppCompatTextView)findViewById(R.id.recommendedTextB);
        recommendedTextC=(AppCompatTextView)findViewById(R.id.recommendedTextC);
        recommendedTextD=(AppCompatTextView)findViewById(R.id.recommendedTextD);
        recommendedTextE=(AppCompatTextView)findViewById(R.id.recommendedTextE);
        recommendedTextF=(AppCompatTextView)findViewById(R.id.recommendedTextF);
        recommendedOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","R");
                startActivity(intent);
            }
        });
        databaseReference=firebaseDatabase.getReference().child("Books");
        databaseReference.orderByChild("genre").equalTo("Mystery").limitToFirst(6).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Book ob=dataSnapshot.getValue(Book.class);
                if(ob!=null) {
                    if((!ob.isSold())&&(bookList.size()<=6)) {
                        ob.setBookId(dataSnapshot.getKey());
                        bookList.add(ob);
                        if (bookList.size() == 6) {
                            recommendedTextA.setText(bookList.get(0).getName());
                            recommendedTextB.setText(bookList.get(1).getName());
                            recommendedTextC.setText(bookList.get(2).getName());
                            recommendedTextD.setText(bookList.get(3).getName());
                            recommendedTextE.setText(bookList.get(4).getName());
                            recommendedTextF.setText(bookList.get(5).getName());
                            Glide.with(HomePage.this).load(bookList.get(0).getPhotoUrl())
                                    .into(recommendedImageA);
                            Glide.with(HomePage.this).load(bookList.get(1).getPhotoUrl())
                                    .into(recommendedImageB);
                            Glide.with(HomePage.this).load(bookList.get(2).getPhotoUrl())
                                    .into(recommendedImageC);
                            Glide.with(HomePage.this).load(bookList.get(3).getPhotoUrl())
                                    .into(recommendedImageD);
                            Glide.with(HomePage.this).load(bookList.get(4).getPhotoUrl())
                                    .into(recommendedImageE);
                            Glide.with(HomePage.this).load(bookList.get(5).getPhotoUrl())
                                    .into(recommendedImageF);
                            recommendedImageA.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookList.get(0).getBookId());
                                    startActivity(intent);
                                }
                            });
                            recommendedImageB.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookList.get(1).getBookId());
                                    startActivity(intent);
                                }
                            });
                            recommendedImageC.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookList.get(2).getBookId());
                                    startActivity(intent);
                                }
                            });
                            recommendedImageD.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookList.get(3).getBookId());
                                    startActivity(intent);
                                }
                            });
                            recommendedImageE.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookList.get(4).getBookId());
                                    startActivity(intent);
                                }
                            });
                            recommendedImageF.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(HomePage.this,BookPreview.class);
                                    intent.putExtra("bookId",bookList.get(5).getBookId());
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                }
                else
                    Toast.makeText(getApplicationContext(),"Ob null",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    NavigationView.OnNavigationItemSelectedListener navigationItemListener=new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            int id=item.getItemId();
            if(id==R.id.logOutMenu)
            {
                auth.signOut();
                FirebaseUser user=auth.getCurrentUser();
                if(user==null)
                {
                    startActivity(new Intent(HomePage.this,LoginPage.class));
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Log Out Failed",Toast.LENGTH_SHORT).show();
                }
            }
            else if(id==R.id.resetPasswordMenu)
            {
                createResetPasswordDialog();
            }
            else if(id==R.id.sellOnTradeBooksMenu)
            {
                Intent intent=new Intent(HomePage.this,Selling.class);
                startActivity(intent);
            }
            else if(id==R.id.yourCartMenu)
            {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","CART");
                startActivity(intent);
            }
            else if(id==R.id.soldBooksMenu)
            {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","SOLD");
                startActivity(intent);
            }
            else if(id==R.id.boughtBooksMenu)
            {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","BOUGHT");
                startActivity(intent);
            }
            else if(id==R.id.yourSoldOrdersMenu)
            {
                Intent intent=new Intent(HomePage.this,MoreBooks.class);
                intent.putExtra("requestCode","ORDERS");
                startActivity(intent);
            }


            return true;
        }
    };




    public void setHeaderInformation(View v)
    {
        final AppCompatTextView name=v.findViewById(R.id.nameHomePageHeader);
        final AppCompatTextView phone=v.findViewById(R.id.phoneNumberHomePageHeader);
        final AppCompatTextView email=v.findViewById(R.id.emailHomePageHeader);
        databaseReference=firebaseDatabase.getReference().child("Users").child(auth.getCurrentUser().getUid());
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User ob=dataSnapshot.getValue(User.class);
                if (ob != null) {
                    name.setText(ob.getName());
                    phone.setText(ob.getPhoneNumber());
                    email.setText(ob.getEmail());
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No user found",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void createResetPasswordDialog()
    {
        resetDialogBuilder=new AlertDialog.Builder(HomePage.this);
        resetDialogBuilder.setTitle("Reset Password")
                .setView(R.layout.reset_dialog)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        resetDialogBuilder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppCompatEditText resetPasswordOld=(AppCompatEditText) resetDialog.findViewById(R.id.resetPasswordOld);
                AppCompatEditText resetPasswordNew=(AppCompatEditText) resetDialog.findViewById(R.id.resetPasswordNew);
                String oldPassword=resetPasswordOld.getText().toString();
                final String newPassword=resetPasswordNew.getText().toString();
                if(TextUtils.isEmpty(oldPassword)||(TextUtils.isEmpty(newPassword)))
                {
                    Toast.makeText(getApplicationContext(),"Fields are Empty",Toast.LENGTH_SHORT).show();
                }
                else if((oldPassword.length()<6)||(newPassword.length()<6))
                {
                    Toast.makeText(getApplicationContext(),"Password Length Less",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    FirebaseUser user=auth.getCurrentUser();
                    AuthCredential credential= EmailAuthProvider.getCredential(auth.getCurrentUser().getEmail(),oldPassword);
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                auth.getCurrentUser().updatePassword(newPassword);
                                Toast.makeText(getApplicationContext(),"Password Updated",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Updation Failed:"+task.getException(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        resetDialog=resetDialogBuilder.create();
        resetDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.home_page_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void searchButtonClick(MenuItem item) {
        if(visible==0)
        {
            searchTextView.setVisibility(View.VISIBLE);
            visible=1;
        }
        else
        {
            searchTextView.setVisibility(View.GONE);
            visible=0;
        }
    }
}

