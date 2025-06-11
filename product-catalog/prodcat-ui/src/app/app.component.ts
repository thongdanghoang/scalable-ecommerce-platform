import {Component} from '@angular/core';

@Component({
	selector: 'app-root',
	template: `
		<h1>Welcome to {{ title }}!</h1>

		<router-outlet />
	`,
	standalone: false,
	styles: []
})
export class AppComponent {
	title = 'prodcat-ui';
}
