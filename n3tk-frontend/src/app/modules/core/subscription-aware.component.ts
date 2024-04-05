import {Subscription} from 'rxjs';

import {Directive, OnDestroy} from '@angular/core';

/**
 * A based component that contains logic to aware about the subscription in order to unsubscribe it when the component is destroy.
 */
@Directive()
export abstract class SubscriptionAwareComponent implements OnDestroy {
  private subscriptions: Subscription[] = [];

  ngOnDestroy(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
  }

  protected registerSubscription(subscription: Subscription): void {
    if (subscription) {
      this.subscriptions.push(subscription);
    }
  }

  protected registerSubscriptions(subscriptions: Subscription[]): void {
    if (subscriptions) {
      this.subscriptions = this.subscriptions.concat(subscriptions);
    }
  }

  protected removeAllSubscriptions(): void {
    this.subscriptions.forEach(s => s.unsubscribe());
    this.subscriptions.splice(0, this.subscriptions.length);
  }
}
