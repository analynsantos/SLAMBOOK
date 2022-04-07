package com.example.slambook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Activity1_LogIn extends AppCompatActivity {

    EditText username_xml, password_xml;
    Button login_xml, register_xml;
    Context context = this;
    ArrayList<ForLogIn_AccountInformation> account = new ArrayList<>();
    String inputUN, inputPW;
    String password1 = "13579abcdeA", password2 = "Th3Q41ckBr0wnF0x", password3 = "p@zzW0rd";
    SQLiteDBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        myDB = new SQLiteDBHelper(context);
        selectAllUser();
        init();
        regList();
    }

    private void selectAllUser() {
        Cursor result = myDB.selectAllUser();
        while (result.moveToNext()){
            account.add(new ForLogIn_AccountInformation(result.getString(0), result.getString(1),
                    result.getString(2), result.getString(3), result.getString(5)
                    + " " + result.getString(4) + " " + result.getString(6)));
        }

    }

    private void init() {
        username_xml = (EditText) findViewById(R.id.username_xml);
        password_xml = (EditText) findViewById(R.id.password_xml);
        login_xml = (Button) findViewById(R.id.login_xml);
        register_xml = (Button) findViewById(R.id.register_xml);

        //account.add(new ForLogIn_AccountInformation(R.drawable.anna_lisa,"Anna","13579abcdeA", "Anna Lisa"));
        //account.add(new ForLogIn_AccountInformation(R.drawable.lorna_dee,"Lorna","Th3Q41ckBr0wnF0x", "Lorna Dee"));
        //account.add(new ForLogIn_AccountInformation(R.drawable.fe_rari,"_Fe_","p@zzW0rd", "Fe Rari"));
    }

    private void regList() {
        login_xml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputUN = username_xml.getText().toString();
                inputPW = password_xml.getText().toString();

                //This is the part where confirmation happens.
                for (int i = 0; i < account.size(); i ++) {
                    if(inputUN.equals(account.get(i).getUsername()) && inputPW.equals(account.get(i).getPassword())){
                        Intent display = new Intent(context, Activity3_EntryList.class);
                        display.putExtra("ID", account.get(i).getUser_ID());
                        display.putExtra("Picture", account.get(i).getAccountImage());
                        display.putExtra("Name", account.get(i).getName());
                        display.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(display);
                        finish();
                        toast();
                        break;
                    }
                    else {
                        if (i+1 == account.size()){
                            alert();
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
        });

        register_xml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Activity2_Register.class);
                startActivity(i);
            }
        });

    }

    //Alert Dialog that displays when the account does not match from the record here.
    private void alert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("LOG IN")
                .setMessage("Username or Password is Incorrect.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        builder.show();
    }

    //Alert Dialog that displays when register is clicked.
    private void alertRegister() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("REGISTER")
                .setMessage("Redirecting to Registration...")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        builder.show();
    }

    //Toast when account is matched.
    private void toast() {
        Toast.makeText(context,"Login is Successful!",Toast.LENGTH_LONG).show();
    }
}