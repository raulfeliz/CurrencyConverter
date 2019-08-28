package com.raul.androidapps.currencyconverter.debug

import android.app.Application
import com.raul.androidapps.currencyconverter.CurrencyConverterApplication
import com.raul.androidapps.currencyconverter.di.modules.*
import com.raul.androidapps.currencyconverter.di.modules.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import testclasses.AppProvidesModuleForTest
import javax.inject.Singleton

@Singleton
@Component(
    modules = [(AndroidSupportInjectionModule::class), (ActivityBuilder::class), (AppBindsModule::class),
        (AppProvidesModuleForTest::class), (FragmentsProvider::class), (FragmentsProvider::class), (ViewModelModule::class)]
)
interface AppComponentForTest : AndroidInjector<CurrencyConverterApplication> {

//    override fun inject(currencyConverterApp: CurrencyConverterApplication)

    @Component.Builder
    interface Builder {
        fun appProvidesModuleForTest(appProvidesModuleForTest: AppProvidesModuleForTest):Builder

        @BindsInstance
        fun application(application: CurrencyConverterApplication): Builder

        fun build(): AppComponentForTest
    }

}