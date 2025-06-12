import {LOCALE_ID, NgModule} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {registerLocaleData} from '@angular/common';
import localeDECH from '@angular/common/locales/de-CH';
import localeFRCH from '@angular/common/locales/fr-CH';
import localeITCH from '@angular/common/locales/it-CH';
import {TranslateModule} from '@ngx-translate/core';
import {provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {HomeComponent} from './home/home.component';
import {UnknownRouteComponent} from './unknown-route/unknown-route.component';

registerLocaleData(localeDECH);
registerLocaleData(localeFRCH);
registerLocaleData(localeITCH);

@NgModule({
	declarations: [AppComponent, HomeComponent, UnknownRouteComponent],
	imports: [AppRoutingModule, BrowserAnimationsModule, TranslateModule],
	providers: [{provide: LOCALE_ID, useValue: 'de-CH'}, provideHttpClient(withInterceptorsFromDi())],
	bootstrap: [AppComponent]
})
export class AppModule {}
