package tab.minedetail;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.conchapp.Login;
import com.example.administrator.conchapp.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import utils.BaseActivity;
import utils.PortIpAddress;
import utils.SharedPrefsUtil;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;
import static utils.PortIpAddress.USERID_KEY;

public class ModifyPwd extends BaseActivity {
    @BindView(R.id.etv_oldpwd)
    EditText etv_oldpwd;
    @BindView(R.id.etv_newpwd)
    EditText etv_newpwd;
    @BindView(R.id.etv_newpwd_again)
    EditText etv_newpwd_again;
    private String userOldPwd = "";
    private String message;
//    private String modifyType = "";
    @BindView(R.id.displaypwd_btn)
    ImageButton displaypwd_btn;
    @BindView(R.id.display_hidden_tv)
    TextView display_hidden_tv;

    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void initData() {
        userOldPwd = SharedPrefsUtil.getValue(this, "userInfo", "USER_PWD", "");
        PwdNoSpace();
    }


    @OnClick(R.id.title_back)
    void Back() {
        finish();
    }

    /**
     * 禁止输入框输入空格
     */
    private void PwdNoSpace() {
        //旧密码
        etv_oldpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NoSpace(etv_oldpwd, s, start);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etv_newpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NoSpace(etv_newpwd, s, start);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etv_newpwd_again.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                NoSpace(etv_newpwd_again, s, start);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick(R.id.modify_submit)
    void Submit() {
        if (etv_oldpwd.getText().toString().equals("")) {
            ShowToast.showShort(this, R.string.oldpwdEmpty);
        } else {
            if (etv_newpwd.getText().toString().equals("")) {
                ShowToast.showShort(this, R.string.newpwdEmpty);
            } else {
                if (!etv_newpwd.getText().toString().equals(etv_newpwd_again.getText().toString())) {
                    ShowToast.showShort(this, R.string.samenewpwd);
                } else {
                    if (etv_oldpwd.getText().toString().equals(etv_newpwd.getText().toString())) {
                        ShowToast.showShort(this, R.string.sameOldNew);
                    } else {
                        if (etv_oldpwd.getText().toString().equals(userOldPwd)) {
                            mConnect(etv_oldpwd.getText().toString(), etv_newpwd.getText().toString());
                        } else {
                            ShowToast.showShort(this, R.string.oldpwd_err);
                        }
                    }
                }
            }
        }
    }

    /**
     * 新密码可视
     */
    @OnClick(R.id.displaypwd_btn)
    void Displaypwd() {
        if (isChecked) {
            etv_newpwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etv_newpwd_again.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            displaypwd_btn.setBackgroundResource(R.drawable.displaypwd);
            display_hidden_tv.setText(R.string.displayPwd);
            isChecked = false;
        } else {
            etv_newpwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            etv_newpwd_again.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            displaypwd_btn.setBackgroundResource(R.drawable.hiddenpwd);
            display_hidden_tv.setText(R.string.hiddenPwd);
            isChecked = true;
        }
    }


    private void mConnect(String oldPwd, String newPwd) {
        OkGo.<String>get(PortIpAddress.modifyPwd())
                .tag(this)
                .params(TOKEN_KEY, PortIpAddress.getToken(this))
                .params(USERID_KEY, oldPwd)
                .params("newLoginpwd", newPwd)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            message = jsonObject.getString("message");
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                ShowToast.showShort(ModifyPwd.this, message + ",请重新登陆");
                                //保存修改后的密码
                                SharedPrefsUtil.putValue(ModifyPwd.this, "userInfo", "USER_PWD", etv_newpwd.getText().toString());
                                userOldPwd = SharedPrefsUtil.getValue(ModifyPwd.this, "userInfo", "USER_PWD", "");
//                                finish();
                                Intent logoutIntent = new Intent(ModifyPwd.this, Login.class);
                                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(logoutIntent);
                            } else {
                                ShowToast.showShort(ModifyPwd.this, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(ModifyPwd.this, R.string.connect_err);
                    }
                });
    }


}
