package saedc.example.com.View.Profile;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import saedc.example.com.Model.Entity.User;
import saedc.example.com.R;

public class Profile extends AppCompatActivity {

    @BindView(R.id.editButton)
    ImageButton editButton;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.editText3)
    EditText editText3;

    ProfileViewModel viewModel;

    User user;
    @BindView(R.id.cancelButton)
    ImageButton cancelButton;
//hello
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        viewModel.getUser().observe(this, user -> {
            this.user = user;
            editText2.setText(user.getUserName());
            editText3.setText(user.getSalary().toString());

        });

        disableEditText(editText2);
        disableEditText(editText3);
    }


    private void disableEditText(EditText editText) {
        editButton.setImageResource(R.drawable.edit_ico);
        editButton.setBackgroundResource(R.drawable.button_bordered);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
    }

    private void enableEditText(EditText editText) {
        editButton.setImageResource(R.drawable.icon_save);
        editButton.setBackgroundResource(R.drawable.button_bordered_save);
        editText.setEnabled(true);
        editText.setCursorVisible(true);
    }


    boolean editMoode = false;

    @OnClick(R.id.editButton)
    public void onViewClicked() {

        editMoode = !editMoode;

        if (editMoode) {
            cancelButton.setVisibility(View.VISIBLE);
            enableEditText(editText2);
            enableEditText(editText3);
        } else {
            cancelButton.setVisibility(View.GONE);
            disableEditText(editText2);
            disableEditText(editText3);

            user.setUserName(editText2.getText().toString());
            user.setSalary(Double.valueOf(editText3.getText().toString()));
            viewModel.addUser(user);
        }


    }

    @OnClick(R.id.cancelButton)
    public void onViewClickedCancelButton() {
        cancelButton.setVisibility(View.GONE);
        disableEditText(editText2);
        disableEditText(editText3);

        viewModel.getUser().observe(this, user -> {
            editText2.setText(user.getUserName());
            editText3.setText(user.getSalary().toString());
        });

    }
}
