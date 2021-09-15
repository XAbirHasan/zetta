package com.project.zetta;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.zetta.Prevalent.Prevalent;
import com.braintreepayments.cardform.utils.CardType;
import com.braintreepayments.cardform.view.CardEditText;
import com.braintreepayments.cardform.view.CardForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PaymentActivity extends AppCompatActivity implements
        CardEditText.OnCardTypeChangedListener {

    private com.braintreepayments.cardform.view.CardForm cardForm;
    private Button buy;
    private ProgressDialog loadingBar;
    String key;
    String randomId;
    String saveCurrentDate, saveCurrentTime;

    private String orderName, orderPhone, orderRegion, orderCity, orderRoad, orderZip, orderHomeAddress;


    private static final CardType[] SUPPORTED_CARD_TYPES = { CardType.VISA, CardType.MASTERCARD, CardType.DISCOVER,
            CardType.AMEX, CardType.DINERS_CLUB, CardType.JCB, CardType.UNIONPAY,
            CardType.HIPER, CardType.HIPERCARD };

    private com.braintreepayments.cardform.view.SupportedCardTypesView mSupportedCardTypesView;

    // hidden variable
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    private androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // this will be hidden
        recyclerView =  (androidx.recyclerview.widget.RecyclerView)findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        // here take the address info .
        orderName = getIntent().getStringExtra("name");
        orderCity = getIntent().getStringExtra("city");
        orderRoad = getIntent().getStringExtra("road");
        orderPhone = getIntent().getStringExtra("phone");
        orderHomeAddress = getIntent().getStringExtra("house");
        orderZip = getIntent().getStringExtra("zip");
        orderRegion = getIntent().getStringExtra("region");


        cardForm =(CardForm) findViewById(R.id.card_form);
        buy = (Button) findViewById(R.id.btnBuy);

        mSupportedCardTypesView =(com.braintreepayments.cardform.view.SupportedCardTypesView) findViewById(R.id.supported_card_types);
        mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);


        cardForm.cardRequired(true)
                .maskCardNumber(true)
                .maskCvv(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .mobileNumberRequired(true)
                .mobileNumberExplanation("SMS is required on this number")
                .actionLabel("Purchase")
                .setup(PaymentActivity.this);

        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        cardForm.setOnCardTypeChangedListener(this);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardForm.isValid()) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(PaymentActivity.this);
                    alertBuilder.setTitle("Confirm before purchase");
                    alertBuilder.setMessage("Card number: " + cardForm.getCardNumber() + "\n" +
                            "Card expiry date: " + cardForm.getExpirationDateEditText().getText().toString() + "\n" +
                            "Card CVV: " + cardForm.getCvv() + "\n" +
                            "Postal code: " + cardForm.getPostalCode() + "\n" +
                            "Phone number: " + cardForm.getMobileNumber());
                    alertBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            // make comfirmation
                            confirmOrder();
                        }
                    });
                    alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog alertDialog = alertBuilder.create();
                    alertDialog.show();
                }
                else {
                    Toast.makeText(PaymentActivity.this, "Please complete the form", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();


    }

    private void confirmOrder() {

        final Random myRandom = new Random();

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());
        randomId = myRandom.nextInt(9999) + saveCurrentDate + saveCurrentTime;


        moveGameRoom(FirebaseDatabase.getInstance().getReference().
                        child("Cart list")
                        .child("User View")
                        .child(Prevalent.currentOnlineUser.getPhone())
                        .child("Product"),
                FirebaseDatabase.getInstance().getReference().child("Orders"));

        // save address
        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Address")
                .child(Prevalent.currentOnlineUser.getPhone());
        final HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("name", orderName);
        orderMap.put("city", orderCity);
        orderMap.put("road", orderRoad);
        orderMap.put("phone", orderPhone);
        orderMap.put("house", orderHomeAddress);
        orderMap.put("zip", orderZip);
        orderMap.put("region", orderRegion);

        ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Cart list")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                // make a lodin bar placing your order.

                                loadingBar = new ProgressDialog(PaymentActivity.this);
                                // make a delay.
                                loadingBar.setTitle("Order Placed");
                                loadingBar.setMessage("Please wait...");
                                loadingBar.setCanceledOnTouchOutside(false);
                                loadingBar.show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadingBar.dismiss();
                                        Toast.makeText(PaymentActivity.this, "Order placed successfully", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }, 3000);
                            }
                        }
                    });
                }
            }
        });
    }

    private void moveGameRoom(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.updateChildren((Map<String, Object>) dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        if (firebaseError != null) {
                            Toast.makeText(PaymentActivity.this, "Copy failed", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(PaymentActivity.this, "Success", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /*
    private void copyRecord(final String saveCurrentDate, final String saveCurrentTime, final String randomId)
    {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().
                child("Cart list")
                .child("User View")
                .child(Prevalent.currentOnlineUser.getPhone())
                .child("Product");
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectPhoneNumbers((Map<String, Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
    }
    private void collectPhoneNumbers(Map<String,Object> users) {

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            image.add((String) singleUser.get("image"));
            pname.add((String) singleUser.get("pname"));
            price.add((String) singleUser.get("price"));
            quantity.add((String) singleUser.get("quantity"));

        }
    }

    /*
    private void copyRecord(final String saveCurrentDate, final String saveCurrentTime, final String randomId)
    {
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart list");
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                                .child("Product"), Cart.class).build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, int i, @NonNull final Cart cart) {

                addData(cart.getImage(), cart.getPid(), cart.getPname(), cart.getPrice(), cart.getQuantity(), saveCurrentDate, saveCurrentTime, randomId);

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);

                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    private void copyRecord(final String saveCurrentDate, final String saveCurrentTime, final String randomId)
    {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart list")
                .child("User View")
                .child(Prevalent.currentOnlineUser.getPhone())
                .child("Product");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for(DataSnapshot d : dataSnapshot.getChildren()) {
                        key = d.getKey();

                        final DatabaseReference srcData = FirebaseDatabase.getInstance().getReference().child("Cart list")
                                .child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone())
                                .child("Product");
                        srcData.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                image = snapshot.child("image").getValue().toString();
                                pid = snapshot.child("pid").getValue().toString();
                                pname = snapshot.child("pname").getValue().toString();
                                price = snapshot.child("price").getValue().toString();
                                quantity = snapshot.child("quantity").getValue().toString();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        //addData(productImage.getText().toString(), key, productName.getText().toString(),productPrice.getText().toString(), productQuantity.getText().toString(), saveCurrentDate, saveCurrentTime, randomId);
                    }

                }
            }//onDataChange

            @Override
            public void onCancelled(DatabaseError error) {

            }//onCancelled
        });
    }

     */

//    private void addData(String image,String pid, String pname,String price,String quantity) {
//        final DatabaseReference dstData = FirebaseDatabase.getInstance().getReference().child("Orders")
//                .child(Prevalent.currentOnlineUser.getPhone())
//                .child(randomId)
//                .child("Product");
//        final HashMap<String, Object> cartMap = new HashMap<>();
//
//
//        cartMap.put("pid", pid);
//        cartMap.put("pname", pname);
//        cartMap.put("price", price);
//        cartMap.put("image", image);
//        cartMap.put("date", saveCurrentDate);
//        cartMap.put("time", saveCurrentTime);
//        cartMap.put("quantity", quantity);
//
//        dstData.child(key).updateChildren(cartMap)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task)
//                    {
//                        if(task.isSuccessful())
//                        {
//                        }
//                    }
//                });
//    }

    @Override
    public void onCardTypeChanged(CardType cardType) {
        if (cardType == CardType.EMPTY) {
            mSupportedCardTypesView.setSupportedCardTypes(SUPPORTED_CARD_TYPES);
        } else {
            mSupportedCardTypesView.setSelected(cardType);
        }
    }
}