package net.formula97.stacktask

import android.app.Application

class TheApp : Application() {

//    private lateinit var appComponet: AppComponent

    override fun onCreate() {
        super.onCreate()

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