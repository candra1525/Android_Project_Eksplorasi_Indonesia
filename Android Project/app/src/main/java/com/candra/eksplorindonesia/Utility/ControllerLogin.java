package com.candra.eksplorindonesia.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ControllerLogin
{
    private SharedPreferences sp;
    private SharedPreferences.Editor spEditor;
    private Context context;

    public String keySP_id = "sp_id";
    public String keySP_fullname = "sp_fullname";
    public String keySP_email = "sp_email";
    public String keySP_role = "sp_role";
    public String keySP_phone = "sp_phone";
    public String keySP_foto = "sp_foto";
    public String keySP_password = "sp_password";

    // Construktor yang menerima param context
    public ControllerLogin(Context ctx)
    {
        this.context = ctx;
    }

    // Melakukan Set Preferences
    public void setPreferences(Context context, String key, String value)
    {
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        spEditor = sp.edit();
        spEditor.putString(key, value);
        spEditor.commit();
    }

    // Melakukan Get Preferences
    public String getPreferences(Context ctx, String key)
    {
        sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        return sp.getString(key, null);
    }

    // Pengecekan apakah user login atau tidak
    public boolean isLogin(Context ctx, String key)
    {
        sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        String pref = sp.getString(key, null);
        if(pref != null)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


}
