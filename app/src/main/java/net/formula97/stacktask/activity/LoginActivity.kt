package net.formula97.stacktask.activity

import android.os.Bundle
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.Auth
import net.formula97.stacktask.R

class LoginActivity : AbstractAppActivity() {

    private val loginRequestCode: Int = 0x8086

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)
    }
}
