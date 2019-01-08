package tab;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.conchapp.Login;
import com.example.administrator.conchapp.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tab.homedetail.Todo;
import tab.minedetail.ModifyPwd;
import utils.AppUtils;
import utils.BaseFragment;
import utils.CheckVersion;
import utils.PermissionUtil;
import utils.PortIpAddress;
import utils.SDUtils;
import utils.SharedPrefsUtil;
import utils.ShowToast;

import static utils.PortIpAddress.CODE;
import static utils.PortIpAddress.MESSAGE;
import static utils.PortIpAddress.SUCCESS_CODE;
import static utils.PortIpAddress.TOKEN_KEY;
import static utils.PortIpAddress.USERID_KEY;

public class Mine extends BaseFragment implements View.OnClickListener {
    private View view;
    @BindView(R.id.user_photo)
    ImageView user_photo;
    @BindView(R.id.mine_todo)
    RelativeLayout mine_todo;
    @BindView(R.id.modify_pwd)
    RelativeLayout modify_pwd;
    @BindView(R.id.mine_version_check)
    RelativeLayout mine_version_check;
    @BindView(R.id.version_check_tv)
    TextView version_check_tv;

    @BindView(R.id.mine_about)
    RelativeLayout mine_about;

    @BindView(R.id.mine_cancellation_rl)
    RelativeLayout mine_cancellation_rl;
    @BindView(R.id.mineName)
    TextView mineName;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private Dialog mCameraDialog;
    private ArrayAdapter adapter;
    private AlertDialog alertDialog;
    private String[] adress_arr;

    private static final int PHOTO_REQUEST_CAMERA = 1;// 相机
    private static final int PHOTO_REQUEST_GALLERY = 2;// 相册
    private static final int PHOTO_REQUEST_CUT = 3;// 裁剪

    private File cameraFile;
    private Uri cameraUri;
    private Uri galleryUri;
    private File cropFile;
    private Uri cropUri;
    private String savePath = SDUtils.sdPath + "/DCIM/Crop/";
//    private String cameraPath = SDUtils.sdPath + "/DCIM/Camera/";

    private String token;

    public Mine() {
        // Required empty public constructor
    }

    @Override
    public View makeView() {
        view = View.inflate(getActivity(), R.layout.fragment_mine, null);
        //绑定fragment
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void loadData() {
        token = PortIpAddress.getToken(getActivity());
        String userName = SharedPrefsUtil.getValue(getActivity(), "userInfo", "username", null);
        mineName.setText(userName);
        version_check_tv.setText("当前版本："+AppUtils.getVersionName(getActivity()));
        sp = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        editor = sp.edit();
        //设置用户头像
        user_photo.setImageResource(R.drawable.head);
//        if (SharedPrefsUtil.getValue(getActivity(), "userInfo", "headurl", null).equals("") || SharedPrefsUtil.getValue(getActivity(), "userInfo", "headurl", null).equals("null")) {
//            user_photo.setImageResource(R.drawable.head);
//        } else {
//            String headurl = SharedPrefsUtil.getValue(getActivity(), "userInfo", "headurl", null);
////          headurl = PortIpAddress.host.replace("mobile/", "") + headurl.replace("\\", "/");
//            headurl = PortIpAddress.host.replace("mobile/", "") + headurl;
//            Log.e(TAG, headurl);
//            Glide
//                    .with(getActivity())
//                    .load(headurl)
//                    .placeholder(R.drawable.head)
//                    .error(R.drawable.head)
//                    .centerCrop()
//                    .crossFade()
//                    .into(user_photo);
//        }

        if (AppUtils.getSDKVersionNumber() >= 21) {
            mine_todo.setBackgroundResource(R.drawable.lrbtn_click);
            modify_pwd.setBackgroundResource(R.drawable.lrbtn_click);
            mine_version_check.setBackgroundResource(R.drawable.lrbtn_click);
            mine_about.setBackgroundResource(R.drawable.lrbtn_click);
        }
    }


    /**
     * 设置选择头像的dialog
     */
    @OnClick(R.id.user_photo)
    void ChooseHeadImage() {
        if (AppUtils.getSDKVersionNumber() >= 21) {
            mCameraDialog = new Dialog(getActivity(), R.style.my_dialog);
            LinearLayout root = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
                    R.layout.mine_camera_control, null);
            root.findViewById(R.id.btn_open_camera).setOnClickListener(this);
            root.findViewById(R.id.btn_choose_img).setOnClickListener(this);
            root.findViewById(R.id.btn_cancel).setOnClickListener(this);
            mCameraDialog.setContentView(root);
            mCameraDialog.setCanceledOnTouchOutside(true);//点击dialog外部消失
            Window dialogWindow = mCameraDialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            dialogWindow.setWindowAnimations(R.style.dialogstyle); // 添加动画
            WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            lp.x = 0; // 新位置X坐标
            lp.y = -20; // 新位置Y坐标
            lp.width = (int) (getResources().getDisplayMetrics().widthPixels - 100); // 宽度
            //      lp.height = WindowManager.LayoutParams.MATCH_PARENT; // 高度
            //      lp.alpha = 9f; // 透明度
            root.measure(0, 0);
            lp.height = root.getMeasuredHeight();
            lp.alpha = 9f; // 透明度
            dialogWindow.setAttributes(lp);
            mCameraDialog.show();
        } else {
            adress_arr = getResources().getStringArray(R.array.xcxj);
            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, adress_arr);
            alertDialog = new AlertDialog.Builder(getActivity())
                    .setIcon(R.drawable.industry)
                    .setTitle("请选择")
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    camera();
                                    break;
                                case 1:
                                    gallery();
                                    break;
                                case 2:
                                    alertDialog.dismiss();
                                    break;
                            }
                        }
                    }).create();
            alertDialog.show();
        }
    }

    /**
     * 待办事宜
     */
    @OnClick(R.id.mine_todo)
    void Todo() {
        startActivity(new Intent(getActivity(), Todo.class));
    }

    /**
     * 修改密码
     */
    @OnClick(R.id.modify_pwd)
    void ModifyPwd() {
        Intent intent = new Intent(getActivity(), ModifyPwd.class);
        startActivity(intent);
    }

    /**
     * 检查版本
     */
    @OnClick(R.id.mine_version_check)
    void Clean() {
        CheckVersion checkVersion = new CheckVersion();
        checkVersion.CheckVersions(getActivity(), TAG);
    }

    /**
     * 关于
     */
    @OnClick(R.id.mine_about)
    void About() {
//        ShowToast.showToastNowait(getActivity(), "关于");
    }


    @OnClick(R.id.mine_cancellation_rl)
    void Cancellation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.mine_cancellation_dialog_title);
        builder.setMessage(R.string.mine_cancellation_dialog_content);
        builder.setPositiveButton(R.string.mine_cancellation_dialog_btn2, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editor.remove("ISCHECK");
                editor.remove("USER_NAME");
                editor.commit();
                Intent logoutIntent = new Intent(getActivity(), Login.class);
                logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(logoutIntent);
            }
        });

        builder.setNegativeButton(R.string.mine_cancellation_dialog_btn1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }


    /**
     * 相机
     */
    private void camera() {
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        // 判断有无SD卡
//        if (SDUtils.hasSdcard()) {
//            createFile();
//            cameraFile = new File(cameraPath, System.currentTimeMillis() + ".jpg");
//            cameraUri = Uri.fromFile(cameraFile);
//            cropFile = new File(savePath, System.currentTimeMillis() + "_cropimage" + ".jpg");
//            cropUri = Uri.fromFile(cropFile);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
//        }
//        startActivityForResult(intent, PHOTO_REQUEST_CAMERA);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            createFile();
            cameraFile = new File(savePath, System.currentTimeMillis() + ".jpg");
            cameraUri = FileProvider.getUriForFile(getActivity(), "com.example.administrator.conchapp.fileprovider", cameraFile);
            cropFile = new File(savePath, System.currentTimeMillis() + "_cropimage" + ".jpg");
            cropUri = FileProvider.getUriForFile(getActivity(), "demo.yqh.wisdomapp.fileprovider", cropFile);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            startActivityForResult(takePictureIntent, PHOTO_REQUEST_CAMERA);
        } else {//判断是否有相机应用
            ShowToast.showShort(getActivity(), "无相机应用");
        }
    }

    /**
     * 相册选择
     */
    private void gallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        createFile();
        cropFile = new File(savePath, System.currentTimeMillis() + "_cropimage" + ".jpg");
        cropUri = Uri.fromFile(cropFile);
        intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
        SDUtils.refreshAlbum(getActivity(), cropFile);
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }


    /**
     * 创建保存图片的文件夹
     */
    private void createFile() {

        File f = new File(savePath);
        if (!f.exists()) {
            f.mkdir();
        }
    }

    /**
     * 裁剪
     *
     * @param uri
     */
    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 768);
        intent.putExtra("outputY", 1280);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PHOTO_REQUEST_GALLERY) {
                if (data != null) {
                    galleryUri = data.getData();
                    crop(galleryUri);
                }

            } else if (requestCode == PHOTO_REQUEST_CAMERA) {
                if (SDUtils.hasSdcard()) {
                    crop(cameraUri);
                } else {
                    ShowToast.showShort(getActivity(), "没有找到SD卡");
                }

            } else if (requestCode == PHOTO_REQUEST_CUT) {
                Glide
                        .with(getActivity())
                        .load(cropUri)
                        .placeholder(R.drawable.default_error)
                        .error(R.drawable.default_error)
                        .centerCrop()
                        .crossFade()
                        .into(user_photo);
                SDUtils.refreshAlbum(getActivity(), cropFile);

                File file = new File(cropUri.getPath());
                mConnect("file", file.getName(), file);

            }
        }

    }

    private void mConnect(String key, String filename, File file) {
        OkGo.<String>post(PortIpAddress.updateHeadImage())
                .tag(this)
                .params(TOKEN_KEY, token)
                .params(USERID_KEY, PortIpAddress.getUserId(getActivity()))
                .params("filename", filename)
                .params(key, file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e(TAG, response.body().toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().toString());
                            String message = jsonObject.getString(MESSAGE);
                            if (jsonObject.getString(CODE).equals(SUCCESS_CODE)) {
                                //拍照后原图删除留裁剪的照片
                                if (cropFile != null) {
                                    cropFile.delete();
                                }
                                ShowToast.showShort(getActivity(), message);
                            } else {
                                ShowToast.showShort(getActivity(), message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ShowToast.showShort(getActivity(), R.string.connect_err);
                    }
                });
    }


    @Override
    public void onClick(View v) {
        if (AppUtils.getSDKVersionNumber() >= 21) {
            switch (v.getId()) {
                case R.id.btn_open_camera:
                    AndPermission.with(getActivity())
                            .requestCode(200)
                            .permission(
                                    PermissionUtil.CameraPermission,
                                    PermissionUtil.WriteFilePermission
                            ).send();
                    break;
                case R.id.btn_choose_img:
                    AndPermission.with(getActivity())
                            .requestCode(201)
                            .permission(PermissionUtil.WriteFilePermission)
                            .send();

                    break;
                case R.id.btn_cancel:
                    mCameraDialog.dismiss();
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults, listener);
    }

    //权限回调
    private PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode) {
            if (requestCode == 200) {
                camera();
                mCameraDialog.dismiss();
            } else if (requestCode == 201) {
                gallery();
                mCameraDialog.dismiss();
            }
        }

        @Override
        public void onFailed(int requestCode) {
            if (requestCode == 200) {
                ShowToast.showShort(getActivity(), "调用相机失败,需开启相机权限");
                mCameraDialog.dismiss();
            } else if (requestCode == 201) {
                ShowToast.showShort(getActivity(), "调用相册失败,需开启文件读写权限");
                mCameraDialog.dismiss();
            }
        }
    };

//
//    /**
//     * 获取权限成功
//     *
//     * @param requestCode
//     * @param perms
//     */
//    @Override
//    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        if (requestCode == CAMERA_REQUESTCODE) {
//            ShowToast.showShort(getActivity(), "开启相机成功");
//        } else if (requestCode == STORAGE_REQUESTCODE) {
//            ShowToast.showShort(getActivity(), "开启相册成功");
//        }
//    }
//
//    /**
//     * 获取权限失败
//     *
//     * @param requestCode
//     * @param perms
//     */
//    @Override
//    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        if (requestCode == CAMERA_REQUESTCODE) {
//            ShowToast.showShort(getActivity(), "开启相机失败");
//        } else if (requestCode == STORAGE_REQUESTCODE) {
//            ShowToast.showShort(getActivity(), "开启相册失败");
//        }
//    }
}
