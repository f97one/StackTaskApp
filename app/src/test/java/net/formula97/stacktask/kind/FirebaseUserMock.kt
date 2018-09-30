package net.formula97.stacktask.kind

import android.net.Uri
import android.os.Parcel
import com.google.android.gms.internal.firebase_auth.zzap
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseUserMetadata
import com.google.firebase.auth.UserInfo

class FirebaseUserMock: FirebaseUser() {
    var _emailVerified = true
    var _userAnonymous = false
    var _email: String = "user1@example.com"

    override fun getEmail(): String? {
        return _email
    }

    override fun zza(p0: MutableList<out UserInfo>): FirebaseUser {
        return this
    }

    override fun zza(p0: zzap) {
        // nothing to do when test
    }

    override fun getProviderData(): MutableList<out UserInfo> {
        return ArrayList()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        // nothing to do when test
    }

    override fun zzs(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMetadata(): FirebaseUserMetadata? {
        return null
    }

    override fun zzq(): FirebaseApp {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun isAnonymous(): Boolean {
        return _userAnonymous
    }

    override fun getPhoneNumber(): String? {
        return "09012344567"
    }

    override fun getUid(): String {
        return _email
    }

    override fun isEmailVerified(): Boolean {
        return _emailVerified
    }

    override fun getDisplayName(): String? {
        return "TEST USER=SAN"
    }

    override fun zzp(): FirebaseUser {
        return this
    }

    override fun getPhotoUrl(): Uri? {
        return null
    }

    override fun getProviders(): MutableList<String> {
        return ArrayList()
    }

    override fun zzt(): String {
        return ""
    }

    override fun zzr(): zzap {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getProviderId(): String {
        return "MOCK_PROVIDER_ID"
    }
}