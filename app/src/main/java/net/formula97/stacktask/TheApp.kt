package net.formula97.stacktask

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class TheApp : Application() {

//    private lateinit var appComponet: AppComponent

    override fun onCreate() {
        super.onCreate()

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
//        appComponet = DaggerAppComponent.builder()
//                .appModule(AppModule(this))
//                .logicModule(LogicModule())
//                .repositoryModule(RepositoryModule())
//                .build()
//        appComponet.inject(this)
    }

//    fun getComponent(): AppComponent {
//        return appComponet
//    }
}