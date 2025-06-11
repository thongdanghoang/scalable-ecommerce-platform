import {LOCALE_ID, NgModule, provideExperimentalZonelessChangeDetection} from '@angular/core';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {
	OB_BANNER,
	ObButtonModule,
	ObExternalLinkModule,
	ObHttpApiInterceptor,
	ObMasterLayoutConfig,
	ObMasterLayoutModule,
	provideObliqueConfiguration
} from '@oblique/oblique';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {registerLocaleData} from '@angular/common';
import localeZHCN from '@angular/common/locales/fr-CH';
import localeVIVN from '@angular/common/locales/it-CH';
import localeENUS from '@angular/common/locales/de-CH';
import {TranslateModule} from '@ngx-translate/core';
import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi} from '@angular/common/http';
import {environment} from '../environments/environment';
import {HomeComponent} from './home/home.component';
import {MatButtonModule} from '@angular/material/button';
import {MatCardModule} from '@angular/material/card';
import {MatIconModule} from '@angular/material/icon';

registerLocaleData(localeZHCN);
registerLocaleData(localeVIVN);
registerLocaleData(localeENUS);

@NgModule({
	declarations: [AppComponent, HomeComponent],
	imports: [
		AppRoutingModule,
		ObMasterLayoutModule,
		BrowserAnimationsModule,
		ObButtonModule,
		TranslateModule,
		MatButtonModule,
		MatCardModule,
		MatIconModule,
		ObExternalLinkModule
	],
	providers: [
		provideExperimentalZonelessChangeDetection(),
		{provide: LOCALE_ID, useValue: 'zh-CN'},
		provideObliqueConfiguration({
			accessibilityStatement: {
				applicationName: 'Product Catalog',
				applicationOperator: 'Replace me with the name and address of the federal office that exploit this application, HTML is permitted',
				contact: {/* at least 1 email or phone number has to be provided */ emails: [''], phones: ['']}
			}
		}),
		{provide: HTTP_INTERCEPTORS, useClass: ObHttpApiInterceptor, multi: true},
		provideHttpClient(withInterceptorsFromDi()),
		{provide: OB_BANNER, useValue: environment.banner}
	],
	bootstrap: [AppComponent]
})
export class AppModule {
	constructor(config: ObMasterLayoutConfig) {
		config.locale.locales = ['zh-CN', 'vi-VN', 'en-US'];
	}
}
