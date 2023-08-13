import {Injectable} from '@angular/core';
import {Observable, Subject} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  public eventSource!: EventSource | null;
  private _notifications$: Subject<Notification> = new Subject<Notification>();
  public notifications$ = this._notifications$.asObservable();
  private previousErrorTime: number | null = null;
  private unsuccessfulRetriesNumber = 0;
  private defaultTimeout = 300000; // Adjust the default timeout value as needed
  private errorTimeEps = 100; // Adjust the error time epsilon value as needed
  private delays = [10000, 20000, 30000]; // Adjust the delay values as needed

  constructor() {
  }

  subscribeToNotifications(topic: string): Observable<any> {
    this.eventSource = new EventSource(`http://localhost:8080/api/v1/notifications/subscribe?topic=${topic}`);

    this.eventSource.addEventListener('message', (event: MessageEvent) => {
      const message = JSON.parse(event.data);
      this._notifications$.next(message);
    });

    this.eventSource.addEventListener('error', () => {
      this.handleError(topic);
    });

    return new Observable<any>(() => {
      this.resubscribeWithDelay(topic);
    });
  }

  unsubscribeFromNotifications() {
    if (this.eventSource) {
      this.eventSource.close();
      this.eventSource = null;
    }
  }

  private handleError(topic: string) {
    const currentErrorTime = Date.now();
    if (this.previousErrorTime !== null) {
      const timeSincePreviousError = currentErrorTime - this.previousErrorTime;
      if (Math.abs(timeSincePreviousError - this.defaultTimeout) < this.errorTimeEps) {
        this.unsuccessfulRetriesNumber = 0;
      } else {
        this.unsuccessfulRetriesNumber++;
      }
    }

    this.resubscribeWithDelay(topic);
    this.previousErrorTime = currentErrorTime;
  }

  private resubscribeWithDelay(topic: string) {
    if (this.eventSource?.readyState === EventSource.CLOSED) {
      setTimeout(() => this.subscribeToNotifications(topic).subscribe(), this.delays[Math.min(this.unsuccessfulRetriesNumber, this.delays.length - 1)]);
    }
  }
}

